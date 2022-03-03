package Heatmap;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.css.Match;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    private Heatmap heatmap = new Heatmap();

    @FXML
    public void initialize() {
        setDefaultImage();
        fillChoiceBoxes();
    }

    private void setDefaultImage() {
        imageDisplay.setImage(new Image(getClass().getResource("bazaar.jpg").toExternalForm()));
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
