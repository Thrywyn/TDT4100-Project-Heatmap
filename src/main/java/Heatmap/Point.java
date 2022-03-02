package Heatmap;

import java.time.LocalDate;

public class Point {

    String matchType;
    Map map;
    Team team;
    Player player;
    Objective obj;
    LocalDate dateCreated;

    Double x;
    Double y;

    public Point(Map map, Team team, Objective obj, Double x, Double y) {
        this.map = map;
        this.team = team;
        this.obj = obj;
        this.x = x;
        this.y = y;
        this.dateCreated = LocalDate.now();
    }

    public Point(String matchType, Map map, Team team, Objective obj, Double x, Double y) {
        this(map, team, obj, x, y);
        this.matchType = matchType;
    }

    public Point(String matchType, Map map, Team team, Player player, Objective obj, Double x, Double y) {
        this(matchType, map, team, obj, x, y);
        this.player = player;
    }

    public String getMatchType() {
        return this.matchType;
    }

    public Map getMap() {
        return this.map;
    }

    public Team getTeam() {
        return this.team;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Objective getObj() {
        return this.obj;
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
