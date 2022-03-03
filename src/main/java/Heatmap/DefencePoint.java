package Heatmap;

public class DefencePoint extends Point {

    MatchType matchType;
    Team team;
    Player player;
    ObjectivePoint obj;

    public DefencePoint(Map map, Team team, ObjectivePoint obj, Double x, Double y) {
        super(map, team, obj, x, y);
    }

    public DefencePoint(Map map, Team team, ObjectivePoint obj, Double x, Double y, MatchType matchType) {
        this(map, team, obj, x, y);
        this.matchType = matchType;
    }

    public Team getTeam() {
        return this.team;
    }

    public Player getPlayer() {
        return this.player;
    }

    public ObjectivePoint getObj() {
        return this.obj;
    }

}
