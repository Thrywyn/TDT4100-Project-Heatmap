package Heatmap;

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
        checkIfValidName(name);
        if (imgFileName == null) {
            throw new IllegalArgumentException("Image file name cannot be null");
        }
        if (imgFileName.isEmpty()) {
            throw new IllegalArgumentException("Image file name cannot be empty");
        }
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }

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

    }

    private void checkIfValidName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (!Contains.onlyOneorMoreAlphaNumericSpace(name)) {
            throw new IllegalArgumentException("Name must be only one or more alphanumeric characters");
        }
    }

    public void removePlayerDefencePoint(PlayerDefencePoint playerDefencePoint) {
        // Find equal playerdefencepoint and delete from list
        playerDefencePoints.stream().filter(p -> p.equals(playerDefencePoint)).findFirst()
                .ifPresent(p -> playerDefencePoints.remove(p));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        checkIfValidName(name);
        this.name = name;
    }

    public String getImgFileName() {
        return imgFileName;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
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
                .filter(point -> point.getTeam() == team || team == null)
                .filter(point -> point.getPlayer() == player || player == null)
                .filter(point -> point.getObjectivePoint() == objectivePoint || objectivePoint == null)
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
        if (objectivePoint == null) {
            throw new IllegalArgumentException("ObjectivePoint cannot be null");
        }
        if (getObjectivePoints().contains(objectivePoint)) {
            throw new IllegalArgumentException("ObjectivePoint already exists");
        }
        if (getObjectivePoints().stream().map(ObjectivePoint::getName)
                .anyMatch(name -> name.equals(objectivePoint.getName()))) {
            throw new IllegalArgumentException("ObjectivePoint name already exists");
        }
        if (objectivePoint.getX() < 0 || objectivePoint.getX() > width) {
            throw new IllegalArgumentException("ObjectivePoint x coordinate is out of bounds");
        }
        if (objectivePoint.getY() < 0 || objectivePoint.getY() > height) {
            throw new IllegalArgumentException("ObjectivePoint y coordinate is out of bounds");
        }
        this.objectivePoints.add(objectivePoint);
    }

    public static void main(String[] args) {
        Map bazaar;
        try {
            bazaar = new Map("Bazaar", "bazaar.jpg");
            System.out.println(bazaar);
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
        if (playerDefencePoint == null) {
            throw new IllegalArgumentException("PlayerDefencePoint cannot be null");
        }
        if (!getObjectivePoints().contains(playerDefencePoint.getObjectivePoint())) {
            throw new IllegalArgumentException("ObjectivePoint does not exist in this map");
        }
        this.playerDefencePoints.add(playerDefencePoint);
    }

    public void removeObjectivePoint(String name2) {
        if (name2 == null) {
            throw new IllegalArgumentException("ObjectivePoint name cannot be null");
        }
        if (!getObjectivePoints().stream().map(ObjectivePoint::getName).anyMatch(name -> name.equals(name2))) {
            throw new IllegalArgumentException("ObjectivePoint does not exist");
        }
        this.objectivePoints.removeIf(point -> point.getName().equals(name2));
    }

    @Override
    public String toString() {
        return "Map [defaultPathPrefix=" + defaultPathPrefix + ", height=" + height + ", image=" + image
                + ", imgFileName=" + imgFileName + ", name=" + name + ", objectivePoints=" + objectivePoints
                + ", playerDefencePoints=" + playerDefencePoints + ", width=" + width + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((defaultPathPrefix == null) ? 0 : defaultPathPrefix.hashCode());
        long temp;
        temp = Double.doubleToLongBits(height);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((imgFileName == null) ? 0 : imgFileName.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((objectivePoints == null) ? 0 : objectivePoints.hashCode());
        result = prime * result + ((playerDefencePoints == null) ? 0 : playerDefencePoints.hashCode());
        temp = Double.doubleToLongBits(width);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Map other = (Map) obj;
        if (defaultPathPrefix == null) {
            if (other.defaultPathPrefix != null)
                return false;
        } else if (!defaultPathPrefix.equals(other.defaultPathPrefix))
            return false;
        if (Double.doubleToLongBits(height) != Double.doubleToLongBits(other.height))
            return false;
        if (imgFileName == null) {
            if (other.imgFileName != null)
                return false;
        } else if (!imgFileName.equals(other.imgFileName))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (objectivePoints == null) {
            if (other.objectivePoints != null)
                return false;
        } else if (!objectivePoints.equals(other.objectivePoints))
            return false;
        if (playerDefencePoints == null) {
            if (other.playerDefencePoints != null)
                return false;
        } else if (!playerDefencePoints.equals(other.playerDefencePoints))
            return false;
        if (Double.doubleToLongBits(width) != Double.doubleToLongBits(other.width))
            return false;
        return true;
    }

}
