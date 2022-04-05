package Heatmap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.scene.image.Image;

public class Map implements ChoiceBoxToStringInterface {

    private String name;
    private String imgFileName;

    private String defaultPathPrefix = "src/main/resources/Heatmap/";

    private FileInputStream inputStream;
    private Image image;

    private double width;
    private double height;

    ArrayList<PlayerDefencePoint> playerDefencePoints = new ArrayList<>();
    ArrayList<ObjectivePoint> objectivePoints = new ArrayList<>();

    public Map(String name, String imgFileName) {
        this.name = name;
        this.imgFileName = imgFileName;
        try {
            this.inputStream = new FileInputStream(defaultPathPrefix + imgFileName);
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

    public String getImgFileName() {
        return imgFileName;
    }

    public void setImgFileName(String imgName) {
        this.imgFileName = imgName;
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

    public ArrayList<PlayerDefencePoint> getPlayerDefencePoints() {
        return playerDefencePoints;
    }

    public ArrayList<ObjectivePoint> getObjectivePoints() {
        return objectivePoints;
    }

    public void addObjectivePoint(ObjectivePoint objectivePoint) {
        this.objectivePoints.add(objectivePoint);
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
