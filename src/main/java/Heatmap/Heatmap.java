package Heatmap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Heatmap {

    // Variables
    private ArrayList<Map> maps = new ArrayList<>();
    private ArrayList<MatchType> matchTypes = new ArrayList<>();
    private ArrayList<Team> teams = new ArrayList<>();

    // Editor Variables
    private Map selectedMap;
    private MatchType selectedMatchType;
    private Team selectedTeam;
    private Player selectedPlayer;
    private ObjectivePoint selectedObjectivePoint;
    private PlayerDefencePoint selectedPlayerDefencePoint;

    public Heatmap() {

        createMaps();
        createMatchTypes();
        createTeams();
    }

    @Override
    public String toString() {
        return "{" +
                "}";
    }

    private void createMaps() {
        Map bazaar = new Map("Bazaar", "bazaar.jpg");
        maps.add(bazaar);
        bazaar.addObjectivePoint(new ObjectivePoint(bazaar, 1884.0, 552.0, "Kyot"));
        bazaar.addObjectivePoint(new ObjectivePoint(bazaar, 2798.0, 1218.0, "Tank"));
        bazaar.addObjectivePoint(new ObjectivePoint(bazaar, 2300.0, 1522.0, "South East Courtyard"));
        maps.add(new Map("Arctic", "arctic.jpg"));
        maps.add(new Map("Abandoned", "abandoned.png"));
        maps.add(new Map("Cargo", "cargo.jpg"));
        maps.add(new Map("Quarantine", "quarantine.jpg"));
        maps.add(new Map("Snowpeak", "snowpeak.jpg"));
        maps.add(new Map("Suburbia", "suburbia.jpg"));
        maps.add(new Map("Subway", "subway.jpg"));
        maps.add(new Map("Tanker", "tanker.jpg"));
    }

    private void createMatchTypes() {
        matchTypes.add(new MatchType("VRML"));
        matchTypes.add(new MatchType("IVRL"));
        matchTypes.add(new MatchType("Scrim"));

    }

    private void createTeams() {
        Team bossfight = new Team("Bossfight");
        teams.add(bossfight);
        bossfight.addPlayer(new Player("BF Player 1"));
        bossfight.addPlayer(new Player("BF Player 2"));
        bossfight.addPlayer(new Player("BF Player 3"));
        Team beginners = new Team("Beginners");
        teams.add(beginners);
        beginners.addPlayer(new Player("BG Player 1"));
        beginners.addPlayer(new Player("BG Player 2"));
        beginners.addPlayer(new Player("BG Player 3"));
        teams.add(new Team("Silent Purge"));
        teams.add(new Team("Dead Logic"));
    }

    public ArrayList<Map> getMaps() {
        return maps;
    }

    public ArrayList<MatchType> getMatchTypes() {
        return matchTypes;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    // Editor Methods

    public void setEditorMap(String mapName) {
        this.selectedMap = maps.stream().filter(matchType -> matchType.getName().equals(mapName)).findAny()
                .orElseThrow(() -> new IllegalArgumentException("MatchType not found: " + mapName));
    }

    public void setEditorMatchType(String matchTypeName) {
        if (matchTypeName == null || matchTypeName == "None") {
            this.selectedMatchType = null;
        } else {
            this.selectedMatchType = matchTypes.stream()
                    .filter(matchType -> matchType.getChoiceBoxString().equals(matchTypeName))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("MatchType not found: " + matchTypeName));
        }
    }

    public void setEditorSelectedPlayer(String playerName) {
        if (playerName == null || playerName == "None") {
            this.selectedPlayer = null;
        } else {
            this.selectedPlayer = selectedTeam.getPlayers().stream()
                    .filter(player -> player.getChoiceBoxString().equals(playerName))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("Player not found: " + playerName));
        }
    }

    public void setEditorTeam(String teamName) {
        if (teamName == null || teamName == "None") {
            this.selectedTeam = null;
        } else {
            this.selectedTeam = teams.stream()
                    .filter(team -> team.getChoiceBoxString().equals(teamName))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("Team not found: " + teamName));
        }
    }

    public void setEditorObjectivePoint(String objectivePointName) {
        if (objectivePointName == null || objectivePointName == "None") {
            this.selectedObjectivePoint = null;
        } else {
            this.selectedObjectivePoint = selectedMap.getObjectivePoints().stream()
                    .filter(objectivePoint -> objectivePoint.getChoiceBoxString().equals(objectivePointName))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("ObjectivePoint not found: " + objectivePointName));
        }
    }

    // Add Player point to the map selected
    public void addPointToCurrentMap(double x, double y) {
        selectedMap.getPlayerDefencePoints().add(new PlayerDefencePoint(selectedMatchType, selectedMap, selectedTeam,
                selectedPlayer, selectedObjectivePoint, x, y));
    }

    public Map getCurrentSelectedMap() {
        return selectedMap;
    }

    public Team getCurrentSelectedTeam() {
        return selectedTeam;
    }

    public ArrayList<PlayerDefencePoint> getPlayerDefencePointsFromSelection() {
        // Return arraylist of PLayerDefencePoint in selected map, with selected team
        // and matchType
        // player, objective point
        return selectedMap.getPlayerDefencePoints(selectedMatchType, selectedTeam, selectedPlayer,
                selectedObjectivePoint);
    }

    public ArrayList<ObjectivePoint> getObjectivePointsFromSelection() {
        return selectedMap.getObjectivePoints(selectedObjectivePoint);
    }

    // Return new array of playerDefencePoints with coordinates scaled to canvas
    // size
    public ArrayList<PlayerDefencePoint> getRelativePlayerDefencePoints(double canvasWidth,
            double canvasHeight) {
        return getPlayerDefencePointsFromSelection().stream()
                .map(p -> new PlayerDefencePoint(p.getMap(), p.getTeam(), p.getObjectivePoint(),
                        p.getX() / selectedMap.getWidth() * canvasWidth,
                        p.getY() / selectedMap.getHeight() * canvasHeight))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // Return new array of ObjectivePoint with coordinates scaled to canvas size
    public ArrayList<ObjectivePoint> getRelativeObjectivePoints(double canvasWidth, double canvasHeight) {
        return getObjectivePointsFromSelection().stream()
                .map(p -> new ObjectivePoint(p.getMap(), p.getX() / selectedMap.getWidth() * canvasWidth,
                        p.getY() / selectedMap.getHeight() * canvasHeight, p.getName()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public double calculateMaxAmountOfOverlappingLayers(double canvasWidth, double canvasHeight, double circleRadius) {
        // Calculate maximum amount of circles overlapping
        HashMap<PlayerDefencePoint, HashSet<PlayerDefencePoint>> overlappingPointsMap = new HashMap<PlayerDefencePoint, HashSet<PlayerDefencePoint>>();
        // For each playerDefencePoint, add set of overlapping points to
        // arrayOfOverlappingPoints
        for (PlayerDefencePoint p : getRelativePlayerDefencePoints(canvasWidth, canvasHeight)) {
            for (PlayerDefencePoint p2 : getRelativePlayerDefencePoints(canvasWidth, canvasHeight)) {
                if (!p.equals(p2)) {
                    if (isOverlapping(p, p2, circleRadius)) {
                        if (overlappingPointsMap.containsKey(p)) {
                            boolean allOverlapping = true;
                            for (PlayerDefencePoint p3 : overlappingPointsMap.get(p)) {
                                if (!isOverlapping(p2, p3, circleRadius)) {
                                    allOverlapping = false;
                                }
                            }
                            if (allOverlapping) {
                                overlappingPointsMap.get(p).add(p2);
                            }
                        } else {
                            HashSet<PlayerDefencePoint> set = new HashSet<PlayerDefencePoint>();
                            set.add(p2);
                            overlappingPointsMap.put(p, set);
                        }
                    }

                }
            }
        }

        // Get the maximum length HashSet from overlappingPointsMap
        double maxAmountOfOverlappingLayers = overlappingPointsMap.values().stream().mapToInt(HashSet::size).max()
                .orElse(0);
        // System.out.println(overlappingPointsMap);
        return maxAmountOfOverlappingLayers + 1;

    }

    public double calculateAlphaValuesFromMaxOverlap(double maxLayersOverlapping) {
        double alphaToApproach = 0.75;
        double alpha = 1 - Math.pow(1 - alphaToApproach, 1 / maxLayersOverlapping);
        return alpha;
    }

    private boolean isOverlapping(double x1, double y1, double x2, double y2, double pointRadius) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)) < pointRadius * 2;
    }

    private boolean isOverlapping(Point p1, Point p2, double pointRadius) {
        return isOverlapping(p1.getX(), p1.getY(), p2.getX(), p2.getY(), pointRadius);
    }

}
