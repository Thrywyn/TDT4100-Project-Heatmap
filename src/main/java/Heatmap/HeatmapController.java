package Heatmap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
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
    @FXML
    private Button buttonNewTeam;
    @FXML
    private Button buttonNewObjective;
    @FXML
    private Button buttonNewMatchType;
    @FXML
    private Button buttonNewPlayer;
    @FXML
    private Slider sliderPlayerRadius;
    @FXML
    private Slider sliderObjectiveRadius;
    @FXML
    private Slider sliderAlphaValue;

    // Text Point Information
    @FXML
    private Text textPointInfoPosition;
    @FXML
    private Text textPointInfoTeam;
    @FXML
    private Text textPointInfoObjective;
    @FXML
    private Text textPointInfoMatchType;
    @FXML
    private Text textPointInfoPlayer;

    // Project Objects
    private Heatmap heatmap = new Heatmap();
    private ImportExporter importExporter = new ImportExporter();

    // Mouse Related Variables

    // Absolute values
    private Double absoluteMouseX;
    private Double absoluteMouseY;
    // Relative values
    private Double relativeMouseX;
    private Double relativeMouseY;

    private Double actualImageCoordinateX;
    private Double actualImageCoordinateY;

    // Image values
    private Double imageBoundWidth = 0.0;
    private Double imageBoundHeight = 0.0;

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
    private Stage stage;

    // Misc
    private double alphaToApproach = 0.75;

    @FXML
    public void initialize() {

        // StackPane
        setStackPaneChildren();

        // Image
        setDefaultImage();
        // addImageEvents();
        updateImageInformation();

        // Canvas
        updateCanvasPaneSize();
        setCanvasContext();
        addCanvasEvents();

        // Editor Settings
        fillChoiceBoxes();
        addChoiceBoxEvents();
        setDefaultChoiceBoxOptions();

        // List View
        setListViewCellFactory();
        fillListView();
        addListViewListener();
        addDeleteButtonListener();

        //
        addSliderEvents();

        // Nav Menu
        setNavMenuOnAction();

        addNewButtonListeners();

        System.out.println("Setup finished");
    }

    private void updatePointInformationText() {
        if (heatmap.getSelectedPlayerDefencePoint() != null) {
            textPointInfoPosition
                    .setText("Position: " + Math.round(heatmap.getSelectedPlayerDefencePoint().getX()) + ","
                            + Math.round(heatmap.getSelectedPlayerDefencePoint().getY()));
            textPointInfoTeam.setText("Team: " + heatmap.getSelectedPlayerDefencePoint().getTeam().getName());
            textPointInfoObjective
                    .setText("Objective: " + heatmap.getSelectedPlayerDefencePoint().getObjectivePoint().getName());

            if (heatmap.getSelectedPlayerDefencePoint().getMatchType() != null) {
                textPointInfoMatchType
                        .setText("Match Type: " + heatmap.getSelectedPlayerDefencePoint().getMatchType().getName());
            } else {
                textPointInfoMatchType.setText("Match Type: None");
            }

            if (heatmap.getSelectedPlayerDefencePoint().getPlayer() != null) {
                textPointInfoPlayer.setText("Player: " + heatmap.getSelectedPlayerDefencePoint().getPlayer().getName());
            } else {
                textPointInfoPlayer.setText("Player: None");
            }
        } else {
            textPointInfoPosition.setText("Position: ");
            textPointInfoTeam.setText("Team: ");
            textPointInfoObjective.setText("Objective: ");
            textPointInfoMatchType.setText("Match Type: ");
            textPointInfoPlayer.setText("Player: ");
        }

    }

    private void addSliderEvents() {
        sliderPlayerRadius.valueProperty().addListener((observable, oldValue, newValue) -> {
            playerPointRadius = newValue.doubleValue();
            refreshCanvasDrawings();
        });
        sliderObjectiveRadius.valueProperty().addListener((observable, oldValue, newValue) -> {
            objectivePointRadius = newValue.doubleValue();
            refreshCanvasDrawings();
        });
        sliderAlphaValue.valueProperty().addListener((observable, oldValue, newValue) -> {
            alphaToApproach = newValue.doubleValue();
            refreshCanvasDrawings();
        });
    }

    private void addNewButtonListeners() {
        buttonNewTeam.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("Team Name");
            dialog.setTitle("New Team");
            dialog.setHeaderText("Enter a name for the new team");
            dialog.setContentText("Team Name:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                try {
                    heatmap.addTeam(name);
                    refillTeamChoiceBox();
                    teamChoiceBox.getSelectionModel().select(name);
                } catch (Exception e1) {
                    displayException(e1);
                }
            });
        });

        buttonNewObjective.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("Objective Name");
            dialog.setTitle("New Objective");
            dialog.setHeaderText("Enter a name for the new objective");
            dialog.setContentText("Objective Name:");

            Optional<String> nameObjective = dialog.showAndWait();

            TextInputDialog dialog2 = new TextInputDialog("Objective Position");
            dialog2.setTitle("New Objective");
            dialog2.setHeaderText("Enter a raw whole number position for the new objective in format x,y");
            dialog2.setContentText("Objective Position:");

            Optional<String> positionObjective = dialog2.showAndWait();

            if (nameObjective.isPresent() && positionObjective.isPresent()) {
                try {
                    heatmap.addObjective(nameObjective.get(), positionObjective.get());
                    refillObjectiveChoiceBox();
                    refreshCanvasDrawings();
                    objectiveChoiceBox.getSelectionModel().select(nameObjective.get());
                } catch (IllegalArgumentException ex) {
                    displayException(ex);
                }
            }
        });

        buttonNewMatchType.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("Match Type Name");
            dialog.setTitle("New Match Type");
            dialog.setHeaderText("Enter a name for the new match type");
            dialog.setContentText("Match Type Name:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                try {
                    heatmap.addMatchType(name);
                    refillMatchTypeChoiceBox();
                    matchChoiceBox.getSelectionModel().select(name);
                } catch (IllegalArgumentException ex) {
                    displayException(ex);
                }
            });
        });

        buttonNewPlayer.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("Player Name");
            dialog.setTitle("New Player");
            dialog.setHeaderText("Enter a name for the new player");
            dialog.setContentText("Player Name:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                try {
                    heatmap.addPlayerToSelectedTeam(name);
                    refillPlayerChoiceBox();
                    playerChoiceBox.getSelectionModel().select(name);
                } catch (IllegalStateException ex) {
                    displayException(ex);
                } catch (IllegalArgumentException ex) {
                    displayException(ex);
                }
            });
        });
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

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            try {
                heatmap = importExporter.read(result.get());
                heatmap.setSelectedMap("Bazaar");
                refillMapChoiceBox();
                refillTeamChoiceBox();
                refillObjectiveChoiceBox();
                refillMatchTypeChoiceBox();
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
    }

    private void refillMatchTypeChoiceBox() {
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

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            try {
                String filename = result.get();
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
                heatmap.setSelectedPlayerDefencePoint(newSelection);
                editDeleteButton.setDisable(false);
            } else if (newSelection == null) {
                System.out.println("Selected: null");
                heatmap.setSelectedPlayerDefencePoint(null);
                editDeleteButton.setDisable(true);
            }
            updatePointInformationText();
            refreshCanvasDrawings();
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
                    setText("(" + Math.round(item.getX()) + "," + Math.round(item.getY()) + ")");
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
        //e.printStackTrace();

    }

    private void displayException(Exception e, String explanation) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.contentTextProperty().set(explanation + "\n" + e.getMessage());
        alert.show();
        //e.printStackTrace();

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
        // Get bounds to calculate relative coords
        Bounds boundsInScene = imageDisplay.localToScene(imageDisplay.getBoundsInLocal());
        Double imageX = boundsInScene.getMinX();
        Double imageY = boundsInScene.getMinY();
        // Set relative coordinates, relative to ImageNode/Pane
        relativeMouseX = absoluteMouseX - imageX;
        relativeMouseY = absoluteMouseY - imageY;

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
                    updateListViewSelection();
                    if (heatmap.getSelectedPlayerDefencePoint() == null) {
                        editDeleteButton.setDisable(true);
                    } else {
                        editDeleteButton.setDisable(false);
                    }
                    updatePointInformationText();

                    System.out.println(heatmap.getSelectedPlayerDefencePoint());
                }

            }

            private void updateListViewSelection() {
                if (heatmap.getSelectedPlayerDefencePoint() != null) {
                    pointListView.getSelectionModel().select(heatmap.getSelectedPlayerDefencePoint());
                    pointListView.scrollTo(heatmap.getSelectedPlayerDefencePoint());
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
        // System.out.println("Max overlap amount:" + overLapAmount);
        double calculatedAlpha = heatmap.calculateAlphaValuesFromMaxOverlap(overLapAmount, alphaToApproach);
        // System.out.println("Alpha value: " + calculatedAlpha);
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
                // Do nothing
            } else {
                drawPlayerDefenceCircle(value.getX(), value.getY());
            }
        });

        heatmap.getPlayerDefencePointMapAbsToRel(imageBoundWidth, imageBoundHeight).entrySet().stream().filter(
                entry -> entry.getKey().equals(heatmap.getSelectedPlayerDefencePoint()))
                .forEach(entry -> drawPlayerDefenceCircle(entry.getValue().getX(), entry.getValue().getY(),
                        Color.ORANGE));

        // Draw Objectives
        for (ObjectivePoint o : heatmap.getRelativeObjectivePoints(imageBoundWidth, imageBoundHeight)) {
            drawObjectiveCircle(o.getX(), o.getY());
        }

    }

    private void setDefaultImage() {
        imageDisplay.setImage(imageNode);
        heatmap.setSelectedMap("Bazaar");
        reSetImageNode();
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
        teamChoiceBox.getItems().add("None");
        fillChoiceBoxWithInterface(teamChoiceBox, heatmap.getTeams());
    }

    private void fillPlayerChoiceBox() {
        playerChoiceBox.getItems().add("None");
        if (heatmap.getSelectedTeam() != null) {
            fillChoiceBoxWithInterface(playerChoiceBox, heatmap.getSelectedTeam().getPlayers());
        }
    }

    private void fillObjectiveChoiceBox() {
        objectiveChoiceBox.getItems().add("None");
        fillChoiceBoxWithInterface(objectiveChoiceBox, heatmap.getSelectedMap().getObjectivePoints());
    }

    private void addChoiceBoxEvents() {
        mapChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Editor Map: " + newVal);
            if (newVal == null) {
                return;
            }
            heatmap.setSelectedMap(newVal);
            reSetImageNode();
            updateImageInformationAndCanvas();
            refreshCanvasDrawings();
            refreshListView();
            refillObjectiveChoiceBox();
        });

        matchChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Editor Match: " + newVal);
            heatmap.setSelectedMatchType(newVal);
            refreshCanvasDrawings();
            refreshListView();
        });

        teamChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Editor Team: " + newVal);
            heatmap.setSelectedTeam(newVal);
            refillPlayerChoiceBox();
            refreshCanvasDrawings();
            refreshListView();
        });
        objectiveChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Editor Objective: " + newVal);
            heatmap.setSelectedObjectivePoint(newVal);
            refreshCanvasDrawings();
            refreshListView();
        });
        playerChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Editor Player: " + newVal);
            heatmap.setSelectedPlayer(newVal);
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
