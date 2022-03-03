package Heatmap;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.css.Match;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

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

    private Heatmap heatmap = new Heatmap();

    private Double mouseX;
    private Double mouseY;
    private String mouseXString;
    private String mouseYString;

    @FXML
    public void initialize() {
        setDefaultImage();
        setImageEvents();
        fillChoiceBoxes();
        System.out.println("Setup finished");
    }

    private void setDefaultImage() {
        imageDisplay.setImage(new Image(getClass().getResource("bazaar.jpg").toExternalForm()));
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

                clickXYText.setText("Click x,y :    (" + mouseXString + "," + mouseYString + ")");
                System.out.println(event.getSource().toString());
                System.out.println("SceneX:" + Double.toString(event.getSceneX()));
                System.out.println("ScreenX:" + Double.toString(event.getScreenX()));

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

                mouseXYText.setText("Mouse x,y : (" + mouseXString + "," + mouseYString + ")");
            }
        });
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
