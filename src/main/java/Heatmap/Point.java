package Heatmap;

import java.time.LocalDate;

public class Point {

    Map map;
    LocalDate dateCreated;

    Double x;
    Double y;

    public Point(Map map, Double x, Double y) {
        if (map == null) {
            throw new IllegalArgumentException("Map cannot be null");
        }
        if (x > map.getWidth() || x < 0) {
            throw new IllegalArgumentException("X must be between 0 and " + map.getWidth());
        }
        if (y > map.getHeight() || y < 0) {
            throw new IllegalArgumentException("Y must be between 0 and " + map.getHeight());
        }
        this.map = map;
        this.x = x;
        this.y = y;
        this.dateCreated = LocalDate.now();
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
