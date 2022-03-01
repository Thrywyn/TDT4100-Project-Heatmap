package Heatmap;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HeatmapController {

    @FXML
    private ImageView imageDisplay;

    @FXML
    public void initialize() {
        setDefaultImage();
    }

    private void setDefaultImage() {
        imageDisplay.setImage(new Image(getClass().getResource("bazaar-l-cropped.jpg").toExternalForm()));
    }
}
