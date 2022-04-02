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
    private Text mouseXYText;
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
    private double pointRadius = 5;

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
        addImageEventHandler();

        // Canvas
        updateCanvasPaneSize();
        setCanvasContext();
        addCanvasEvents();

        // Editor Settings
        fillChoiceBoxes();
        setRadioButtonEvents();

        //

        System.out.println("Setup finished");
    }

    public void addStageListener() {
        stage.maximizedProperty().addListener((ov, oldVal, newVal) -> {
            updateImageInformation();
            updateCanvasPaneSize();
            System.out.println("Maximized property changed");
        });
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
        ctx.setFill(Color.RED);
        ctx.setStroke(Color.RED);
    }

    private void setMouseValues(MouseEvent mouseEvent) {
        // Set absolute coordinates
        absoluteMouseX = mouseEvent.getSceneX();
        absoluteMouseY = mouseEvent.getSceneY();
        absoluteMouseXString = Double.toString(absoluteMouseX);
        absoluteMouseYString = Double.toString(absoluteMouseY);
        // Get bounds to calculate relative coords
        Bounds boundsInScene = imageDisplay.localToScene(imageDisplay.getBoundsInLocal());
        Double imageX = boundsInScene.getMinX();
        Double imageY = boundsInScene.getMinY();
        // Set relative coordinates
        relativeMouseX = absoluteMouseX - imageX;
        relativeMouseY = absoluteMouseY - imageY;
        relativeMouseXString = Double.toString(relativeMouseX);
        relativeMouseYString = Double.toString(relativeMouseY);

        // Set click text
        clickXYText.setText("Click x,y :    (" + relativeMouseXString + "," + relativeMouseYString + ")");

        // Sysout values
        System.out.println("---Mouse Click Event---");
        System.out.println("Source: " + mouseEvent.getSource().toString());
        System.out.println("RelativeX: " + Double.toString(relativeMouseX));
        System.out.println("RelativeY: " + Double.toString(relativeMouseY));

        // System.out.println("MinX:" + Double.toString(boundsInScene.getMinX()) + "\t
        // Min Y: "
        // + Double.toString(boundsInScene.getMinY()));
    }

    private void drawCircle(GraphicsContext ctx, double centerX, double centerY, double radius) {
        ctx.strokeOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        ctx.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        System.out.println("Circle drawn");
    }

    private void addCanvasEvents() {
        // On Mouse clicked
        imageDisplayCanvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setMouseValues(mouseEvent);
                updateSelectedToggleButton();
                drawCircle(ctx, relativeMouseX, relativeMouseY, 5);
            }

        });

        imageDisplayCanvas.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // setMouseValues(mouseEvent);
                updateImageInformation();
                updateCanvasPaneSize();
            }
        });
    }

    private void setRadioButtonEvents() {
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
    }

    private void updateImageInformation() {

        imageBoundWidth = imageDisplay.boundsInParentProperty().getValue().getWidth();
        imageBoundHeight = imageDisplay.boundsInParentProperty().getValue().getHeight();
        System.out.println("Image Width: " + imageBoundWidth + " Height: " + imageBoundHeight);

        imageWidthText.setText("Width:" + Double.toString(imageBoundWidth));
        imageHeightText.setText("Hegight: " + Double.toString(imageBoundHeight));

        imageWidthTextRaw.setText("Width:" + Double.toString(imageNode.getWidth()));
        imageHeightTextRaw.setText("Hegight: " + Double.toString(imageNode.getHeight()));
    }

    private void addImageEventHandler() {
        // On Mouse clicked
        imageDisplay.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setMouseValues(mouseEvent);
            }
        });

        // On mouse moved
        imageDisplay.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // setMouseValues(mouseEvent);
                updateImageInformation();
                updateCanvasPaneSize();
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
            updateImageInformation();
            updateCanvasPaneSize();
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            // System.out.println("Window Height:" + stage.getHeight());
            updateImageInformation();
            updateCanvasPaneSize();
        });

        System.out.println("Stage EventListener Set");
    }

    private void fillChoiceBox(ChoiceBox<String> cb, ArrayList<? extends ChoiceBoxToStringInterface> objectArrayList) {
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
        fillChoiceBox(mapChoiceBox, heatmap.getMaps());
    }

    private void fillMatchTypeChoiceBox() {
        fillChoiceBox(matchChoiceBox, heatmap.getMatchTypes());
    }

    private void fillTeamChoiceBox() {
        fillChoiceBox(teamChoiceBox, heatmap.getTeams());
    }

}
