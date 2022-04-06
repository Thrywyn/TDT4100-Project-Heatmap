package Heatmap;

public class PlayerDefencePoint extends Point {

    MatchType matchType;
    Team team;
    Player player;
    ObjectivePoint objectivePoint;

    public PlayerDefencePoint(Map map, Team team, ObjectivePoint obj, Double x, Double y) {
        super(map, x, y);
        if (obj == null) {
            throw new IllegalArgumentException("Objective cannot be null");
        }
        if (team == null) {
            throw new IllegalArgumentException("Team cannot be null");
        }
        this.team = team;
        this.objectivePoint = obj;
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

    public ObjectivePoint getObjectivePoint() {
        return this.objectivePoint;
    }

    public MatchType getMatchType() {
        return this.matchType;
    }

    @Override
    public String toString() {
        // return "{" +
        // " matchType='" + getMatchType() + "'" +
        // ", team='" + getTeam() + "'" +
        // ", player='" + getPlayer() + "'" +
        // ", obj='" + getObjectivePoint() + "'" +
        // "}";
        return "(" +
                getX() +
                ", " +
                getY() +
                ")";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((matchType == null) ? 0 : matchType.hashCode());
        result = prime * result + ((objectivePoint == null) ? 0 : objectivePoint.hashCode());
        result = prime * result + ((player == null) ? 0 : player.hashCode());
        result = prime * result + ((team == null) ? 0 : team.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        PlayerDefencePoint other = (PlayerDefencePoint) obj;
        if (matchType == null) {
            if (other.matchType != null)
                return false;
        } else if (!matchType.equals(other.matchType))
            return false;
        if (objectivePoint == null) {
            if (other.objectivePoint != null)
                return false;
        } else if (!objectivePoint.equals(other.objectivePoint))
            return false;
        if (player == null) {
            if (other.player != null)
                return false;
        } else if (!player.equals(other.player))
            return false;
        if (team == null) {
            if (other.team != null)
                return false;
        } else if (!team.equals(other.team))
            return false;
        return true;
    }

}
