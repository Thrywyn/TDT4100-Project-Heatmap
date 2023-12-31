package Heatmap;

public class Point {

    private Map map;

    private Double x;
    private Double y;

    public Point(Map map, Double x, Double y) {
        if (map == null) {
            throw new IllegalArgumentException("Map cannot be null");
        }
        if (x == null) {
            throw new IllegalArgumentException("X cannot be null");
        }
        if (y == null) {
            throw new IllegalArgumentException("Y cannot be null");
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((map == null) ? 0 : map.hashCode());
        result = prime * result + ((x == null) ? 0 : x.hashCode());
        result = prime * result + ((y == null) ? 0 : y.hashCode());
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
        Point other = (Point) obj;
        if (map == null) {
            if (other.map != null)
                return false;
        } else if (!map.equals(other.map))
            return false;
        if (x == null) {
            if (other.x != null)
                return false;
        } else if (!x.equals(other.x))
            return false;
        if (y == null) {
            if (other.y != null)
                return false;
        } else if (!y.equals(other.y))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Point [map=" + map + ", x=" + x + ", y=" + y + "]";
    }

}
