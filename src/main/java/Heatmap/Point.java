package Heatmap;

import java.time.LocalDate;

public class Point {

    Map map;
    LocalDate dateCreated;

    Double x;
    Double y;

    public Point(Map map, ObjectivePoint obj, Double x, Double y) {
        this.map = map;
        this.x = x;
        this.y = y;
        this.dateCreated = LocalDate.now();
    }

    public Point(Map map, Team team, ObjectivePoint obj, Double x, Double y) {
        this(map, obj, x, y);
    }

    public Map getMap() {
        return this.map;
    }

    public Double getX() {
        return this.x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return this.y;
    }

    public void setY(Double y) {
        this.y = y;
    }

}
