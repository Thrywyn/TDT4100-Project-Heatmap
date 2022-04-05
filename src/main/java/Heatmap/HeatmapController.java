package Heatmap;

import java.util.ArrayList;

import javafx.beans.binding.Bindings;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private Toggle editingModeEdit;
    @FXML
    private Toggle editingModeDelete;
    @FXML
    private StackPane stackPaneImageCanvas;
    @FXML
    private Text clickXYTextRaw;

    // Project Objects
    private Heatmap heatmap = new Heatmap();

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
    private double playerPointRadius = 5;
    private Color playerPointColor = Color.rgb(255, 0, 0, 0.5);
    // Objective Points
    private double objectivePointRadius = 10;
    private Color objectivePointColor = Color.rgb(0, 0, 255, 0.5);

    // FXML Objects
    private Image imageNode = new Image(getClass().getResource("bazaar.jpg").toExternalForm());
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

        addRadioButtonEvents();

        //

        System.out.println("Setup finished");
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

    private void drawPlayerDefenceCircle(GraphicsContext ctx, double centerX, double centerY, double radius) {
        drawCircle(ctx, centerX, centerY, radius, playerPointColor);
        // System.out.println("Player Circle drawn");
    }

    private void addCanvasEvents() {
        // On Mouse clicked
        imageDisplayCanvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setMouseValuesAndUpdateText(mouseEvent);
                if (mouseEvent.isPrimaryButtonDown()) {
                    addPointToHeatmap();
                }
                if (mouseEvent.isSecondaryButtonDown()) {

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
            heatmap.addPointToCurrentMap(actualImageCoordinateX, actualImageCoordinateY);
            System.out.println("Added new point to selected map object");
            refreshCanvasDrawings();
        } catch (Exception e) {
            displayException(e);
        }
    }

    private void refreshCanvasDrawings() {
        // Clear canvas
        ctx.clearRect(0, 0, imageDisplayCanvas.getWidth(), imageDisplayCanvas.getHeight());
        // For all playerDefencePoints in selected map, draw a playerCircle on canvas
        for (PlayerDefencePoint p : heatmap.getCurrentSelectedMap().getPlayerDefencePoints()) {
            drawPlayerDefenceCircle(ctx, p.getX() / imageNode.getWidth() * imageBoundWidth,
                    p.getY() / imageNode.getHeight() * imageBoundHeight, playerPointRadius);
        }

        if (objectiveChoiceBox.getValue() == null || objectiveChoiceBox.getValue() == "None") {
            // Draw All Objectives
            for (ObjectivePoint o : heatmap.getCurrentSelectedMap().getObjectivePoints()) {
                drawCircle(ctx, o.getX() / imageNode.getWidth() * imageBoundWidth,
                        o.getY() / imageNode.getHeight() * imageBoundHeight, objectivePointRadius, objectivePointColor);
            }
        } else {
            // Draw only currently selected objective
            for (ObjectivePoint o : heatmap.getCurrentSelectedMap().getObjectivePoints()) {
                if (objectiveChoiceBox.getValue() == o.getName()) {
                    drawCircle(ctx, o.getX() / imageNode.getWidth() * imageBoundWidth,
                            o.getY() / imageNode.getHeight() * imageBoundHeight, objectivePointRadius,
                            objectivePointColor);
                }
            }
        }

    }

    private void addRadioButtonEvents() {
        editingMode.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            selectedToggleButton = newVal;

            if (selectedToggleButton.equals(editingModeNew)) {
                editorModeString = "new";
            } else if (selectedToggleButton.equals(editingModeEdit)) {
                editorModeString = "edit";
            } else if (selectedToggleButton.equals(editingModeDelete)) {
                editorModeString = "delete";
            }

            System.out.println("Editor Mode: " + editorModeString);
        });
    }

    private void updateSelectedToggleButton() {
        selectedToggleButton = editingMode.getSelectedToggle();
        if (selectedToggleButton.equals(editingModeNew)) {
            editorModeString = "new";
        } else if (selectedToggleButton.equals(editingModeEdit)) {
            editorModeString = "edit";
        } else if (selectedToggleButton.equals(editingModeDelete)) {
            editorModeString = "delete";
        }
    }

    private void setDefaultImage() {
        imageDisplay.setImage(imageNode);
        heatmap.setEditorMap("Bazaar");
    }

    private void updateImageInformation() {

        imageBoundWidth = imageDisplay.boundsInParentProperty().getValue().getWidth();
        imageBoundHeight = imageDisplay.boundsInParentProperty().getValue().getHeight();
        // System.out.println("Image Width: " + imageBoundWidth + " Height: " +
        // imageBoundHeight);

        imageWidthText.setText("Width:" + Double.toString(imageBoundWidth));
        imageHeightText.setText("Hegight: " + Double.toString(imageBoundHeight));

        imageWidthTextRaw.setText("Width:" + Double.toString(imageNode.getWidth()));
        imageHeightTextRaw.setText("Hegight: " + Double.toString(imageNode.getHeight()));
    }

    private void addImageEvents() {
        // On Mouse clicked
        imageDisplay.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                updateImageInformationAndCanvas();
                setMouseValuesAndUpdateText(mouseEvent);
            }
        });

        // On mouse moved
        imageDisplay.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // setMouseValues(mouseEvent);
                // updateImageInformationAndCanvas();
                // refreshCanvasDrawings();
            }
        });

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

        System.out.println("Stage EventListener Set");
    }

    private void fillChoiceBoxWithInterface(ChoiceBox<String> cb, ArrayList<? extends ChoiceBoxToStringInterface> objectArrayList) {
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
        fillChoiceBoxWithInterface(playerChoiceBox, heatmap.getCurrentSelectedTeam().getPlayers());
    }

    private void fillObjectiveChoiceBox() {
        fillChoiceBoxWithInterface(objectiveChoiceBox, heatmap.getCurrentSelectedMap().getObjectivePoints());
    }

    private void addChoiceBoxEvents() {
        mapChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Editor Map: " + newVal);
            heatmap.setEditorMap(newVal);
            imageNode = new Image(
                    getClass().getResource(heatmap.getCurrentSelectedMap().getImgFileName()).toExternalForm());
            imageDisplay.setImage(imageNode);
            heatmap.setEditorMap(newVal);
            updateImageInformationAndCanvas();
            refreshCanvasDrawings();
            objectiveChoiceBox.getItems().clear();
            fillObjectiveChoiceBox();
        });

        matchChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Editor Match: " + newVal);
            heatmap.setEditorMatchType(newVal);
            refreshCanvasDrawings();
        });

        teamChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Editor Team: " + newVal);
            heatmap.setEditorTeam(newVal);
            refreshPlayerChoiceBox();
            refreshCanvasDrawings();
        });
        objectiveChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Editor Objective: " + newVal);
            heatmap.setEditorObjectivePoint(newVal);
            refreshCanvasDrawings();
        });
    }

    private void refreshPlayerChoiceBox() {
        // Empty playerChoiceBox, then fill it with players from selected team
        playerChoiceBox.getItems().clear();
        fillPlayerChoiceBox();
    }

    private void updateImageInformationAndCanvas() {
        updateImageInformation();
        updateCanvasPaneSize();
    }

}
