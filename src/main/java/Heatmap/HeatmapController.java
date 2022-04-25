package Heatmap;

import java.io.IOException;
import java.nio.channels.OverlappingFileLockException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HeatmapController {

    // FXML Objects
    @FXML
    private ImageView imageDisplay;
    @FXML
    private ChoiceBox<String> matchChoiceBox;
    @FXML
    private ChoiceBox<String> mapChoiceBox;
    @FXML
    private ChoiceBox<String> teamChoiceBox;
    @FXML
    private ChoiceBox<String> objectiveChoiceBox;
    @FXML
    private ChoiceBox<String> playerChoiceBox;
    @FXML
    private Pane imageDisplayPane;
    @FXML
    private Text clickXYText;
    @FXML
    private Text imageHeightText;
    @FXML
    private Text imageWidthText;
    @FXML
    private Text imageHeightTextRaw;
    @FXML
    private Text imageWidthTextRaw;
    @FXML
    private ToggleGroup editingMode;
    @FXML
    private Toggle editingModeNew;
    @FXML
    private Button editDeleteButton;
    @FXML
    private StackPane stackPaneImageCanvas;
    @FXML
    private Text clickXYTextRaw;
    @FXML
    private ListView<PlayerDefencePoint> pointListView;
    @FXML
    private MenuItem MenuFileExport;
    @FXML
    private MenuItem MenuFileImport;

    // Project Objects
    private Heatmap heatmap = new Heatmap();
    private ImportExporter importExporter = new ImportExporter();

    // Mouse Related Variables

    // Absolute values
    private Double absoluteMouseX;
    private Double absoluteMouseY;
    private String absoluteMouseXString;
    private String absoluteMouseYString;
    // Relative values
    private Double relativeMouseX;
    private Double relativeMouseY;
    private String relativeMouseXString;
    private String relativeMouseYString;

    private Double actualImageCoordinateX;
    private Double actualImageCoordinateY;

    // Image values
    private Double imageBoundWidth = 0.0;
    private Double imageBoundHeight = 0.0;

    // Editor State
    private Toggle selectedToggleButton;
    private String editorModeString;

    // Canvas Values
    private CanvasPane imageDisplayCanvasPane;
    private Canvas imageDisplayCanvas;
    private GraphicsContext ctx;
    // Player Points
    private double playerPointRadius = 10;
    private Color playerPointColor = Color.rgb(255, 0, 0, 0.5);
    // Objective Points
    private double objectivePointRadius = 20;
    private Color objectivePointColor = Color.rgb(0, 0, 255, 0.5);

    // FXML Objects
    private Image imageNode;
    private Scene scene;
    private Stage stage;

    @FXML
    public void initialize() {

        // StackPane
        setStackPaneChildren();

        // Image
        setDefaultImage();
        // addImageEvents();

        // Canvas
        updateCanvasPaneSize();
        setCanvasContext();
        addCanvasEvents();

        // Editor Settings
        fillChoiceBoxes();
        addChoiceBoxEvents();

        setDefaultChoiceBoxOptions();

        setListViewCellFactory();
        fillListView();
        addListViewListener();

        addDeleteButtonListener();
        setNavMenuOnAction();

        System.out.println("Setup finished");
    }

    private void setNavMenuOnAction() {
        MenuFileExport.setOnAction(event -> {
            handleExport();
        });
        MenuFileImport.setOnAction(event -> {
            handleImport();
        });
    }

    private void handleImport() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Import Heatmap");
        dialog.setHeaderText("What is the filename?");
        dialog.setContentText("Name:");

        try {
            heatmap = importExporter.read(dialog.showAndWait().get());
            heatmap.setEditorSelectedMap("Bazaar");
            refillMapChoiceBox();
            refillTeamChoiceBox();
            refillObjectiveChoiceBox();
            refillhmatchChoiceBox();
            refillPlayerChoiceBox();
            updateImageInformationAndCanvas();
            updateCanvasPaneSize();
            reSetImageNode();
            if (heatmap.getSelectedMap() != null) {
                mapChoiceBox.getSelectionModel().select(heatmap.getSelectedMap().getChoiceBoxString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refillhmatchChoiceBox() {
        matchChoiceBox.getItems().clear();
        fillMatchTypeChoiceBox();
    }

    private void refillMapChoiceBox() {
        mapChoiceBox.getItems().clear();
        fillMapChoiceBox();
    }

    private void handleExport() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Export Heatmap");
        dialog.setHeaderText("Set filename");
        dialog.setContentText("Name:");

        try {
            String filename = dialog.showAndWait().get();
            importExporter.write(filename, heatmap);
            System.out.println("Exported to " + filename);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export Successful");
            alert.setHeaderText("Export Successful");
            alert.setContentText("Exported to " + filename);
            alert.showAndWait();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            displayException(e, "IOException error, Could not write to file");
            try {
                throw e;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void refreshListView() {
        pointListView.getItems().clear();
        fillListView();
    }

    private void fillListView() {
        pointListView.setItems(FXCollections.observableArrayList(heatmap.getPlayerDefencePointsFromSelection()));
        pointListView.scrollTo(pointListView.getItems().size() - 1);
    }

    private void addListViewListener() {
        pointListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println("Selected: " + newSelection);
                heatmap.setEditorSelectedPlayerDefencePoint(newSelection);
                editDeleteButton.setDisable(false);
                refreshCanvasDrawings();
            } else if (newSelection == null) {
                System.out.println("Selected: null");
                heatmap.setEditorSelectedPlayerDefencePoint(null);
                editDeleteButton.setDisable(true);
                refreshCanvasDrawings();
            }
        });
    }

    private void addDeleteButtonListener() {
        editDeleteButton.setOnAction(e -> {
            heatmap.deleteSelectedPlayerDefencePoint();

            refreshListView();
            refreshCanvasDrawings();
            updateImageInformationAndCanvas();

            editDeleteButton.setDisable(true);

        });
    }

    private void reInitialize() {

        // Image
        heatmap.setEditorSelectedMap("Bazaar");

        // Canvas
        updateCanvasPaneSize();

        // Editor Settings
        clearChoiceBoxes();

        fillChoiceBoxes();
        playerChoiceBox.getItems().clear();
        objectiveChoiceBox.getItems().clear();

        setDefaultChoiceBoxOptions();

        clearListView();
        fillListView();
    }

    private void clearListView() {
        pointListView.getItems().clear();
    }

    private void clearChoiceBoxes() {
        teamChoiceBox.getItems().clear();
        objectiveChoiceBox.getItems().clear();
        mapChoiceBox.getItems().clear();
        matchChoiceBox.getItems().clear();
        playerChoiceBox.getItems().clear();
    }

    private void refillTeamChoiceBox() {
        teamChoiceBox.getItems().clear();
        fillTeamChoiceBox();
    }

    private void setListViewCellFactory() {
        pointListView.setCellFactory(param -> new ListCell<PlayerDefencePoint>() {
            @Override
            protected void updateItem(PlayerDefencePoint item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    if (item.getPlayer() == null) {
                        setText("(" + Math.round(item.getX()) + "," + Math.round(item.getY()) + ")");
                    } else {
                        setText(item.getPlayer().getName() + "(" + Math.round(item.getX()) + ","
                                + Math.round(item.getY()) + ")");
                    }
                }
            }
        });
    }

    private void setDefaultChoiceBoxOptions() {
        matchChoiceBox.setValue("VRML");
        mapChoiceBox.setValue("Bazaar");
    }

    private void displayException(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.contentTextProperty().set(e.getMessage());
        alert.show();

    }

    private void displayException(Exception e, String explanation) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.contentTextProperty().set(explanation + "\n" + e.getMessage());
        alert.show();

    }

    private void updateCanvasPaneSize() {

        double ibw = imageBoundWidth;
        double ibh = imageBoundHeight;

        imageDisplayCanvasPane.setMinSize(ibw, ibh);
        imageDisplayCanvasPane.setPrefSize(ibw, ibh);
        imageDisplayCanvasPane.setMaxSize(ibw, ibh);
    }

    private void setStackPaneChildren() {
        imageDisplayCanvasPane = new CanvasPane(100, 100);

        // Debug Code
        imageDisplayCanvasPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("CanvasPane Clicked!");
            }
        });

        stackPaneImageCanvas.getChildren().add(imageDisplayCanvasPane);
        System.out.print("Canvas Pane: ");
        System.out.println(imageDisplayCanvasPane);
        imageDisplayCanvas = (Canvas) imageDisplayCanvasPane.getChildren().get(0);
        System.out.print("Canvas: ");
        System.out.println(imageDisplayCanvas);
    }

    private static class CanvasPane extends Pane {

        final Canvas canvas;

        CanvasPane(double width, double height) {
            setWidth(width);
            setHeight(height);
            canvas = new Canvas(width, height);
            getChildren().add(canvas);

            canvas.widthProperty().bind(this.widthProperty());
            canvas.heightProperty().bind(this.heightProperty());
        }
    }

    private void setCanvasContext() {
        ctx = imageDisplayCanvas.getGraphicsContext2D();
    }

    private void setMouseValuesAndUpdateText(MouseEvent mouseEvent) {
        // Set absolute coordinates
        absoluteMouseX = mouseEvent.getSceneX();
        absoluteMouseY = mouseEvent.getSceneY();
        absoluteMouseXString = Double.toString(absoluteMouseX);
        absoluteMouseYString = Double.toString(absoluteMouseY);
        // Get bounds to calculate relative coords
        Bounds boundsInScene = imageDisplay.localToScene(imageDisplay.getBoundsInLocal());
        Double imageX = boundsInScene.getMinX();
        Double imageY = boundsInScene.getMinY();
        // Set relative coordinates, relative to ImageNode/Pane
        relativeMouseX = absoluteMouseX - imageX;
        relativeMouseY = absoluteMouseY - imageY;
        relativeMouseXString = Double.toString(relativeMouseX);
        relativeMouseYString = Double.toString(relativeMouseY);

        // Set Image Relative values, relative to Raw Image
        actualImageCoordinateX = relativeMouseX
                / imageDisplay.boundsInParentProperty().getValue().getWidth() * imageNode.getWidth();
        actualImageCoordinateY = relativeMouseY
                / imageDisplay.boundsInParentProperty().getValue().getHeight() * imageNode.getHeight();

        // Set click text
        clickXYText.setText("Relative Click x,y :    (" + Math.round(relativeMouseX) + ","
                + Math.round(relativeMouseY) + ")");
        clickXYTextRaw.setText("Raw Click x,y :    (" + Math.round(actualImageCoordinateX) + ","
                + Math.round(actualImageCoordinateY) + ")");

        // Sysout values
        System.out.println("---Mouse Click Event---");
        System.out.println("Source: " + mouseEvent.getSource().toString());
        System.out.println("RelativeX: " + Double.toString(relativeMouseX));
        System.out.println("RelativeY: " + Double.toString(relativeMouseY));

        // System.out.println("MinX:" + Double.toString(boundsInScene.getMinX()) + "\t
        // Min Y: "
        // + Double.toString(boundsInScene.getMinY()));
    }

    private void drawCircle(GraphicsContext ctx, double centerX, double centerY, double radius, Color color) {
        ctx.setFill(color);
        ctx.setStroke(color);
        ctx.strokeOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        ctx.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        // System.out.println("Circle drawn");
    }

    private void drawPlayerDefenceCircle(double centerX, double centerY, Color color) {
        drawCircle(ctx, centerX, centerY, playerPointRadius, color);
        // System.out.println("Player Circle drawn");
    }

    private void drawPlayerDefenceCircle(double centerX, double centerY) {
        drawCircle(ctx, centerX, centerY, playerPointRadius, playerPointColor);
        // System.out.println("Player Circle drawn");
    }

    private void drawObjectiveCircle(double centerX, double centerY) {
        drawCircle(ctx, centerX, centerY, objectivePointRadius, objectivePointColor);
        // System.out.println("Objective Circle drawn");
    }

    private void addCanvasEvents() {
        // On Mouse clicked
        imageDisplayCanvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setMouseValuesAndUpdateText(mouseEvent);
                if (mouseEvent.isPrimaryButtonDown()) {
                    addPointToHeatmap();
                    refreshListView();
                }
                if (mouseEvent.isSecondaryButtonDown()) {
                    heatmap.selectClosestPlayerPointInRadius(relativeMouseX, relativeMouseY, playerPointRadius,
                            imageBoundWidth, imageBoundHeight);
                    refreshCanvasDrawings();
                    if (heatmap.getSelectedPlayerDefencePoint() == null) {
                        editDeleteButton.setDisable(true);
                    } else {
                        editDeleteButton.setDisable(false);
                    }

                    System.out.println(heatmap.getSelectedPlayerDefencePoint());
                }

            }

        });

        imageDisplayCanvas.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // setMouseValues(mouseEvent);
                // updateImageInformationAndCanvas();
                // refreshCanvasDrawings();
            }
        });
    }

    protected void addPointToHeatmap() {
        try {
            heatmap.addPointToSelectedMap(actualImageCoordinateX, actualImageCoordinateY);
            System.out.println("Added new point to selected map object");
            refreshCanvasDrawings();
        } catch (Exception e) {
            displayException(e);
        }
    }

    private void updatePlayerPointAlphaValue() {
        double overLapAmount = heatmap.calculateMaxAmountOfOverlappingLayers(imageBoundWidth, imageBoundHeight,
                playerPointRadius);
        System.out.println("Max overlap amount:" + overLapAmount);
        double calculatedAlpha = heatmap.calculateAlphaValuesFromMaxOverlap(overLapAmount);
        System.out.println("Alpha value: " + calculatedAlpha);
        playerPointColor = Color.rgb(255, 0, 0, calculatedAlpha);
    }

    private void refreshCanvasDrawings() {
        // Clear canvas
        ctx.clearRect(0, 0, imageDisplayCanvas.getWidth(), imageDisplayCanvas.getHeight());
        updatePlayerPointAlphaValue();

        // Draw Points from heatmap with checkbox selection

        // Draw all player points from hashmap mapping absolute to relative points
        heatmap.getPlayerDefencePointMapAbsToRel(imageBoundWidth, imageBoundHeight).forEach((key, value) -> {
            if (key.equals(heatmap.getSelectedPlayerDefencePoint())) {
                drawPlayerDefenceCircle(value.getX(), value.getY(), Color.ORANGE);
            } else {
                drawPlayerDefenceCircle(value.getX(), value.getY());
            }
        });

        // Draw Objectives
        for (ObjectivePoint o : heatmap.getRelativeObjectivePoints(imageBoundWidth, imageBoundHeight)) {
            drawObjectiveCircle(o.getX(), o.getY());
        }

    }

    private void setDefaultImage() {
        imageDisplay.setImage(imageNode);
        heatmap.setEditorSelectedMap("Bazaar");
    }

    private void updateImageInformation() {

        imageBoundWidth = imageDisplay.boundsInParentProperty().getValue().getWidth();
        imageBoundHeight = imageDisplay.boundsInParentProperty().getValue().getHeight();
        // System.out.println("Image Width: " + imageBoundWidth + " Height: " +
        // imageBoundHeight);

        imageWidthText.setText("Width:" + Double.toString(imageBoundWidth));
        imageHeightText.setText("Height: " + Double.toString(imageBoundHeight));

        imageWidthTextRaw.setText("Width:" + Double.toString(imageNode.getWidth()));
        imageHeightTextRaw.setText("Height: " + Double.toString(imageNode.getHeight()));
    }

    // Sets stage, ran from App
    public void setStage(Stage stage) {
        this.stage = stage;
        System.out.println("Stage set");
    }

    // Sets scene
    public void setScene() {
        scene = clickXYText.getScene();
    }

    // Sets Stage Event listener to listen for changes in width/height on window
    public void addStageSizeEventListeners() {

        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            // System.out.println("Window Width:" + stage.getWidth());
            updateImageInformationAndCanvas();
            refreshCanvasDrawings();
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            // System.out.println("Window Height:" + stage.getHeight());
            updateImageInformationAndCanvas();
            refreshCanvasDrawings();
        });

        stage.maximizedProperty().addListener((ov, oldVal, newVal) -> {
            // System.out.println("Maximized property changed");
            Runnable myRunnable = new Runnable() {
                public void run() {
                    // System.out.println("Runnable running");
                    for (int i = 0; i <= 1; i++) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        updateImageInformationAndCanvas();
                        refreshCanvasDrawings();
                    }
                }
            };
            Thread thread = new Thread(myRunnable);
            thread.start();
            // System.out.println("---End of property change---");
        });

        stage.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if (key.getCode() == KeyCode.DELETE) {
                System.out.println("You pressed delete");

                heatmap.deleteSelectedPlayerDefencePoint();
                refreshListView();
                refreshCanvasDrawings();
                updateImageInformationAndCanvas();

                editDeleteButton.setDisable(true);
            }
        });

        System.out.println("Stage EventListener Set");
    }

    private void fillChoiceBoxWithInterface(ChoiceBox<String> cb,
            ArrayList<? extends ChoiceBoxToStringInterface> objectArrayList) {
        for (ChoiceBoxToStringInterface object : objectArrayList) {
            cb.getItems().add(object.getChoiceBoxString());
        }
    }

    private void fillChoiceBoxes() {
        fillMapChoiceBox();
        fillMatchTypeChoiceBox();
        fillTeamChoiceBox();
    }

    private void fillMapChoiceBox() {
        fillChoiceBoxWithInterface(mapChoiceBox, heatmap.getMaps());
    }

    private void fillMatchTypeChoiceBox() {
        matchChoiceBox.getItems().add("None");
        fillChoiceBoxWithInterface(matchChoiceBox, heatmap.getMatchTypes());
    }

    private void fillTeamChoiceBox() {
        fillChoiceBoxWithInterface(teamChoiceBox, heatmap.getTeams());
    }

    private void fillPlayerChoiceBox() {
        playerChoiceBox.getItems().add("None");
        if (heatmap.getSelectedTeam() != null) {
            fillChoiceBoxWithInterface(playerChoiceBox, heatmap.getSelectedTeam().getPlayers());
        }
    }

    private void fillObjectiveChoiceBox() {
        fillChoiceBoxWithInterface(objectiveChoiceBox, heatmap.getSelectedMap().getObjectivePoints());
    }

    private void addChoiceBoxEvents() {
        mapChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Editor Map: " + newVal);
            if (newVal == null) {
                return;
            }
            heatmap.setEditorSelectedMap(newVal);
            reSetImageNode();
            updateImageInformationAndCanvas();
            refreshCanvasDrawings();
            refreshListView();
            refillObjectiveChoiceBox();
        });

        matchChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Editor Match: " + newVal);
            heatmap.setEditorMatchType(newVal);
            refreshCanvasDrawings();
            refreshListView();
        });

        teamChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Editor Team: " + newVal);
            heatmap.setEditorTeam(newVal);
            refillPlayerChoiceBox();
            refreshCanvasDrawings();
            refreshListView();
        });
        objectiveChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Editor Objective: " + newVal);
            heatmap.setEditorObjectivePoint(newVal);
            refreshCanvasDrawings();
            refreshListView();
        });
        playerChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Editor Player: " + newVal);
            heatmap.setEditorSelectedPlayer(newVal);
            refreshCanvasDrawings();
            refreshListView();
        });
    }

    private void reSetImageNode() {
        imageNode = new Image(
                getClass().getResource(heatmap.getSelectedMap().getImgFileName()).toExternalForm());
        imageDisplay.setImage(imageNode);
    }

    private void refillPlayerChoiceBox() {
        // Empty playerChoiceBox, then fill it with players from selected team
        playerChoiceBox.getItems().clear();
        fillPlayerChoiceBox();
    }

    private void refillObjectiveChoiceBox() {
        objectiveChoiceBox.getItems().clear();
        fillObjectiveChoiceBox();
    }

    private void updateImageInformationAndCanvas() {
        updateImageInformation();
        updateCanvasPaneSize();
    }

}
