package Heatmap;

public class ObjectivePoint extends Point {

    String name;

    public ObjectivePoint(Map map, Team team, ObjectivePoint obj, Double x, Double y, String name) {
        super(map, team, obj, x, y);
        this.name = name;
    }

}
