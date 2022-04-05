package Heatmap;

public class PlayerDefencePoint extends Point {

    MatchType matchType;
    Team team;
    Player player;
    ObjectivePoint obj;

    public PlayerDefencePoint(Map map, Team team, ObjectivePoint obj, Double x, Double y) {
        super(map, x, y);
        if (obj == null) {
            throw new IllegalArgumentException("Objective cannot be null");
        }
        if (team == null) {
            throw new IllegalArgumentException("Team cannot be null");
        }
        this.team = team;
        this.obj = obj;
    }

    public PlayerDefencePoint(MatchType matchType, Map map, Team team, ObjectivePoint obj, Double x, Double y) {
        this(map, team, obj, x, y);
        this.matchType = matchType;
    }

    public PlayerDefencePoint(MatchType matchType, Map map, Team team, Player player, ObjectivePoint obj, Double x,
            Double y) {
        this(matchType, map, team, obj, x, y);
        this.player = player;
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
