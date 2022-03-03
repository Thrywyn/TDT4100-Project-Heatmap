package Heatmap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.scene.image.Image;

public class Map implements ChoiceBoxToStringInterface {

    private String name;
    private String imgName;
    private String defaultPathPrefix = "src/main/resources/Heatmap/";

    private FileInputStream inputStream;
    private Image image;

    private double width;
    private double height;

    ArrayList<DefencePoint> defencePoints = new ArrayList<>();
    ArrayList<ObjectivePoint> objectivePoints = new ArrayList<>();

    public Map(String name, String imgName) {
        this.name = name;
        this.imgName = imgName;
        try {
            this.inputStream = new FileInputStream(defaultPathPrefix + imgName);
            this.image = new Image(inputStream);
            this.width = image.getWidth();
            this.height = image.getHeight();

        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File not found");
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public ArrayList<DefencePoint> getDefencePoints() {
        return defencePoints;
    }

    public ArrayList<ObjectivePoint> getObjectivePoints() {
        return objectivePoints;
    }

    @Override
    public String toString() {
        return "Map [name=" + name + ", height=" + height + ", width=" + width + "]";
    }

    public static void main(String[] args) {
        Map bazaar = new Map("Bazaar", "bazaar.jpg");
        System.out.println(bazaar);
    }

    @Override
    public String getChoiceBoxString() {
        return name;
    }

}
