package Heatmap;

import java.util.ArrayList;

import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HeatmapController {

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
    private Canvas imageDispleyCanvas;
    @FXML
    private Text imageHeightText;
    @FXML
    private Text imageWidthText;

    private Heatmap heatmap = new Heatmap();

    private Double mouseX;
    private Double mouseY;
    private String mouseXString;
    private String mouseYString;

    private Double relativeMouseX;
    private Double relativeMouseY;

    private String relativeMouseXString;
    private String relativeMouseYString;

    private Double imageBoundWidth;
    private Double imageBoundHeight;

    private Image imageNode = new Image(getClass().getResource("bazaar.jpg").toExternalForm());

    Scene scene;
    Stage stage;

    @FXML
    public void initialize() {

        setDefaultImage();
        setImageEvents();
        fillChoiceBoxes();

        System.out.println("Setup finished");
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
    }

    private void setImageEvents() {
        // On Mouse clicked
        imageDisplay.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                mouseXString = Double.toString(mouseX);
                mouseYString = Double.toString(mouseY);

                Bounds boundsInScene = imageDisplay.localToScene(imageDisplay.getBoundsInLocal());
                Double imageX = boundsInScene.getMinX();
                Double imageY = boundsInScene.getMinY();

                relativeMouseX = mouseX - imageX;
                relativeMouseY = mouseY - imageY;

                relativeMouseXString = Double.toString(relativeMouseX);
                relativeMouseYString = Double.toString(relativeMouseY);

                clickXYText.setText("Click x,y :    (" + relativeMouseXString + "," + relativeMouseYString + ")");

                System.out.println(event.getSource().toString());
                System.out.println("SceneX:" + Double.toString(event.getSceneX()));
                System.out.println("ScreenX:" + Double.toString(event.getScreenX()));

                System.out.println("MinX:" + Double.toString(boundsInScene.getMinX()) + "\t Min Y: "
                        + Double.toString(boundsInScene.getMinY()));

            }
        });

        // On mouse moved
        imageDisplay.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                mouseXString = Double.toString(mouseX);
                mouseYString = Double.toString(mouseY);

                Bounds boundsInScene = imageDisplay.localToScene(imageDisplay.getBoundsInLocal());
                Double imageX = boundsInScene.getMinX();
                Double imageY = boundsInScene.getMinY();

                relativeMouseX = mouseX - imageX;
                relativeMouseY = mouseY - imageY;

                relativeMouseXString = Double.toString(relativeMouseX);
                relativeMouseYString = Double.toString(relativeMouseY);

                mouseXYText.setText("Mouse x,y : (" + relativeMouseXString + "," + relativeMouseYString + ")");

            }
        });

    }

    public void setStage(Stage stage) {
        this.stage = stage;
        System.out.println("Stage set");
    }

    public void setScene() {
        scene = clickXYText.getScene();
    }

    public void setStageEvent() {

        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            // System.out.println("Window Width:" + stage.getWidth());
            updateImageInformation();
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            // System.out.println("Window Height:" + stage.getHeight());
            updateImageInformation();
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
