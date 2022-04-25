package Heatmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.scene.image.Image;

public class Map implements ChoiceBoxToStringInterface {

    private String name;
    private String imgFileName;

    private String defaultPathPrefix = "src/main/resources/Heatmap/";

    private Image image;

    private double width;
    private double height;

    ArrayList<PlayerDefencePoint> playerDefencePoints = new ArrayList<>();
    ArrayList<ObjectivePoint> objectivePoints = new ArrayList<>();

    public Map(String name, String imgFileName) throws FileNotFoundException {
        this.name = name;
        this.imgFileName = imgFileName;

        try {
            FileInputStream inputStream = new FileInputStream(defaultPathPrefix +
                    imgFileName);
            this.image = new Image(inputStream);
            this.width = image.getWidth();
            this.height = image.getHeight();

        } catch (FileNotFoundException e) {
            throw e;
        }

        // try {
        // Image fileImage = getFileImage(imgFileName);
        // this.image = fileImage;
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

    }

    public void removePlayerDefencePoint(PlayerDefencePoint playerDefencePoint) {
        // Find equal playerdefencepoint and delete from list
        playerDefencePoints.stream().filter(p -> p.equals(playerDefencePoint)).findFirst()
                .ifPresent(p -> playerDefencePoints.remove(p));
    }

    private File getFile(String filename) {
        return new File(Map.class.getResource("maps/").getFile() + filename);
    }

    private Image getFileImage(String filename) {
        return new Image(getFile(filename).toURI().toString());
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

    public ArrayList<PlayerDefencePoint> getPlayerDefencePoints(MatchType matchType, Team team, Player player,
            ObjectivePoint objectivePoint) {
        // Return all PlayerDefencePoints that match the given parameters, mathcType can
        // be null, player can be null
        return playerDefencePoints.stream()
                .filter(point -> point.getMatchType() == matchType || matchType == null)
                .filter(point -> point.getTeam() == team)
                .filter(point -> point.getPlayer() == player || player == null)
                .filter(point -> point.getObjectivePoint() == objectivePoint)
                .collect(ArrayList<PlayerDefencePoint>::new, ArrayList::add, ArrayList::addAll);
    }

    public ArrayList<ObjectivePoint> getObjectivePoints() {
        return objectivePoints;
    }

    public ArrayList<ObjectivePoint> getObjectivePoints(ObjectivePoint obj) {
        // Return objectivepoint that equals obj, or return list of all objectives
        if (obj == null) {
            return objectivePoints;
        } else {
            return objectivePoints.stream().filter(point -> point == obj).collect(ArrayList<ObjectivePoint>::new,
                    ArrayList::add, ArrayList::addAll);
        }
    }

    public void addObjectivePoint(ObjectivePoint objectivePoint) {
        this.objectivePoints.add(objectivePoint);
    }

    @Override
    public String toString() {
        return "Map [name=" + name + ", height=" + height + ", width=" + width + "]";
    }

    public static void main(String[] args) {
        Map bazaar;
        try {
            bazaar = new Map("Bazaar", "bazaar.jpg");
            System.out.println(bazaar);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Map arctic = new Map("Arctic", "arctic.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getChoiceBoxString() {
        return name;
    }

    public ObjectivePoint getObjectivePoint(String string) {
        return objectivePoints.stream().filter(point -> point.getName().equals(string)).findFirst().orElse(null);
    }

    public void addPlayerDefencePoint(PlayerDefencePoint playerDefencePoint) {
        this.playerDefencePoints.add(playerDefencePoint);
    }

}
