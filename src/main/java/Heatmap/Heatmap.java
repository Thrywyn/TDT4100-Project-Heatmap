package Heatmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;
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
        this.selectedMap = maps.get(0);
        this.selectedMatchType = matchTypes.get(0);

    }

    @Override
    public String toString() {
        return "Heatmap [maps=" + maps + ", matchTypes=" + matchTypes + ", selectedMap=" + selectedMap
                + ", selectedMatchType=" + selectedMatchType + ", selectedObjectivePoint=" + selectedObjectivePoint
                + ", selectedPlayer=" + selectedPlayer + ", selectedPlayerDefencePoint=" + selectedPlayerDefencePoint
                + ", selectedTeam=" + selectedTeam + ", teams=" + teams + "]";
    }

    public void deleteTeam(Team team) {
        if (team == null) {
            throw new IllegalArgumentException("Team cannot be null");
        }
        if (teams.contains(team)) {
            teams.remove(team);
        } else {
            throw new NoSuchElementException("Team not found");
        }
    }

    public void deleteTeam(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Team name cannot be null");
        }
        if (teams.stream().filter(t -> t.getName().equals(name)).findFirst().isPresent()) {
            deleteTeam(teams.stream().filter(t -> t.getName().equals(name)).findFirst().get());
        } else {
            throw new NoSuchElementException("Team " + name + " does not exist");
        }
    }

    public PlayerDefencePoint getSelectedPlayerDefencePoint() {
        return selectedPlayerDefencePoint;
    }

    public void setSelectedPlayerDefencePoint(PlayerDefencePoint playerDefencePoint) {
        this.selectedPlayerDefencePoint = playerDefencePoint;
    }

    public void deleteSelectedPlayerDefencePoint() {
        if (selectedPlayerDefencePoint != null) {
            selectedMap.removePlayerDefencePoint(selectedPlayerDefencePoint);
        }
    }

    public void setSelectedPlayer(Player player) {
        this.selectedPlayer = player;
    }

    public void selectClosestPlayerPointInRadius(double x, double y, double radius, double canvasWidth,
            double canvasHeight) {

        double relativeRadius = radius / canvasWidth * selectedMap.getWidth();

        double relativeX = x / canvasWidth * selectedMap.getWidth();
        double relativeY = y / canvasHeight * selectedMap.getHeight();

        PlayerDefencePoint closestPlayerPoint = getClosestPlayerPointInRadius(relativeX, relativeY, relativeRadius);

        this.selectedPlayerDefencePoint = closestPlayerPoint;
    }

    private PlayerDefencePoint getClosestPlayerPoint(double x, double y) {

        PlayerDefencePoint closestPlayer = null;
        double closestDistance = Double.MAX_VALUE;
        ArrayList<PlayerDefencePoint> selectedPoints = getPlayerDefencePointsFromSelection();
        for (PlayerDefencePoint playerPoint : selectedPoints) {
            double distance = Math.sqrt(Math.pow(playerPoint.getX() - x, 2) + Math.pow(playerPoint.getY() - y, 2));
            if (distance < closestDistance) {
                closestDistance = distance;
                closestPlayer = playerPoint;
            }
        }
        return closestPlayer;
    }

    private PlayerDefencePoint getClosestPlayerPointInRadius(double x, double y, double radius) {
        PlayerDefencePoint closestPlayerPoint = getClosestPlayerPoint(x, y);
        if (closestPlayerPoint == null) {
            return null;
        }
        if (isOverlapping(x, y, closestPlayerPoint.getX(), closestPlayerPoint.getY(), radius)) {
            return closestPlayerPoint;
        }
        return null;
    }

    private void createMaps() {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void setSelectedMap(String mapName) {
        try {
            this.selectedMap = maps.stream().filter(map -> map.getName().equals(mapName)).collect(Collectors.toList())
                    .get(0);
        } catch (NoSuchElementException e) {
            System.out.println("Map not found");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Map not found");
        }
    }

    public void setEditorMatchType(String matchTypeName) {
        if (matchTypeName == null || matchTypeName == "None") {
            this.selectedMatchType = null;
        } else {
            this.selectedMatchType = matchTypes.stream()
                    .filter(matchType -> matchType.getChoiceBoxString().equals(matchTypeName))
                    .findAny()
                    .orElseThrow(() -> new NoSuchElementException("MatchType not found: " + matchTypeName));
        }
    }

    public void setEditorSelectedPlayer(String playerName) {
        if (playerName == null || playerName == "None") {
            this.selectedPlayer = null;
        } else {
            this.selectedPlayer = selectedTeam.getPlayers().stream()
                    .filter(player -> player.getChoiceBoxString().equals(playerName))
                    .findAny()
                    .orElseThrow(() -> new NoSuchElementException("Player not found: " + playerName));
        }
    }

    public void setEditorTeam(String teamName) {
        if (teamName == null || teamName == "None") {
            this.selectedTeam = null;
        } else {
            this.selectedTeam = teams.stream()
                    .filter(team -> team.getChoiceBoxString().equals(teamName))
                    .findAny()
                    .orElseThrow(() -> new NoSuchElementException("Team not found: " + teamName));
        }
    }

    public void setEditorObjectivePoint(String objectivePointName) {
        if (objectivePointName == null || objectivePointName == "None") {
            this.selectedObjectivePoint = null;
        } else {
            this.selectedObjectivePoint = selectedMap.getObjectivePoints().stream()
                    .filter(objectivePoint -> objectivePoint.getChoiceBoxString().equals(objectivePointName))
                    .findAny()
                    .orElseThrow(() -> new NoSuchElementException("ObjectivePoint not found: " + objectivePointName));
        }
    }

    // Add Player point to the map selected
    public void addPointToSelectedMap(double x, double y) {
        selectedMap.addPlayerDefencePoint(new PlayerDefencePoint(selectedMatchType, selectedMap, selectedTeam,
                selectedPlayer, selectedObjectivePoint, x, y));
    }

    public Map getSelectedMap() {
        return selectedMap;
    }

    public Team getSelectedTeam() {
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
    private ArrayList<PlayerDefencePoint> getRelativePlayerDefencePoints(double canvasWidth,
            double canvasHeight) {
        return getPlayerDefencePointsFromSelection().stream()
                .map(p -> translatePointToRelative(p, canvasWidth, canvasHeight))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public HashMap<PlayerDefencePoint, PlayerDefencePoint> getPlayerDefencePointMapAbsToRel(double canvasWidth,
            double canvasHeight) {
        if (canvasWidth == 0 || canvasHeight == 0) {
            throw new IllegalArgumentException("Canvas width or height cannot be 0");
        }
        if (canvasWidth < 0 || canvasHeight < 0) {
            throw new IllegalArgumentException("Canvas width or height cannot be negative");
        }
        HashMap<PlayerDefencePoint, PlayerDefencePoint> map = new HashMap<>();
        for (PlayerDefencePoint p : getPlayerDefencePointsFromSelection()) {
            map.put(p, translatePointToRelative(p, canvasWidth, canvasHeight));
        }
        return map;
    }

    private PlayerDefencePoint translatePointToRelative(PlayerDefencePoint p, double canvasWidth,
            double canvasHeight) {
        if (p == null) {
            throw new IllegalArgumentException("PlayerDefencePoint cannot be null");
        }
        if (canvasWidth == 0 || canvasHeight == 0) {
            throw new IllegalArgumentException("Canvas width or height cannot be 0");
        }
        if (canvasWidth < 0 || canvasHeight < 0) {
            throw new IllegalArgumentException("Canvas width or height cannot be negative");
        }
        return new PlayerDefencePoint(p.getMap(), p.getTeam(), p.getObjectivePoint(),
                p.getX() / p.getMap().getWidth() * canvasWidth,
                p.getY() / p.getMap().getHeight() * canvasHeight);
    }

    // Return new array of ObjectivePoint with coordinates scaled to canvas size
    public ArrayList<ObjectivePoint> getRelativeObjectivePoints(double canvasWidth, double canvasHeight) {
        if (canvasWidth == 0 || canvasHeight == 0) {
            throw new IllegalArgumentException("Canvas width or height cannot be 0");
        }
        if (canvasWidth < 0 || canvasHeight < 0) {
            throw new IllegalArgumentException("Canvas width or height cannot be negative");
        }
        return getObjectivePointsFromSelection().stream()
                .map(p -> new ObjectivePoint(p.getMap(), p.getX() / p.getMap().getWidth() * canvasWidth,
                        p.getY() / p.getMap().getHeight() * canvasHeight, p.getName()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public double calculateMaxAmountOfOverlappingLayers(double canvasWidth, double canvasHeight, double circleRadius) {
        // Calculate maximum amount of circles overlapping
        HashMap<PlayerDefencePoint, HashSet<PlayerDefencePoint>> overlappingPointsMap = new HashMap<PlayerDefencePoint, HashSet<PlayerDefencePoint>>();
        // For each playerDefencePoint, add set of overlapping points to
        // arrayOfOverlappingPoints
        for (PlayerDefencePoint p : getRelativePlayerDefencePoints(canvasWidth, canvasHeight)) {
            for (PlayerDefencePoint p2 : getRelativePlayerDefencePoints(canvasWidth, canvasHeight)) {
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
        // System.out.println("Overlapping points: " + overlappingPointsMap);
        // Get the maximum length HashSet from overlappingPointsMap
        double maxAmountOfOverlappingLayers = overlappingPointsMap.values().stream().mapToInt(HashSet::size).max()
                .orElse(0);
        // System.out.println(overlappingPointsMap);
        return maxAmountOfOverlappingLayers;

    }

    public double calculateAlphaValuesFromMaxOverlap(double maxLayersOverlapping, double alphaToApproach) {
        // Calculate alpha values from max amount of overlapping layers
        if (alphaToApproach < 0 || alphaToApproach > 1) {
            throw new IllegalArgumentException("Alpha to approach must be between 0 and 1");
        }
        if (maxLayersOverlapping < 0) {
            throw new IllegalArgumentException("Max layers overlapping must be positive");
        }
        if (maxLayersOverlapping == 0) {
            return alphaToApproach;
        }
        double alpha = 1 - Math.pow(1 - alphaToApproach, 1 / maxLayersOverlapping);
        return alpha;
    }

    private boolean isOverlapping(double x1, double y1, double x2, double y2, double pointRadius) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)) < pointRadius * 2;
    }

    public boolean isOverlapping(Point p1, Point p2, double pointRadius) {
        return isOverlapping(p1.getX(), p1.getY(), p2.getX(), p2.getY(), pointRadius);
    }

    public void addMap(Map map) {
        if (map == null) {
            throw new IllegalArgumentException("Map cannot be null");
        }
        maps.add(map);
    }

    public void addTeam(Team team) {
        if (team == null) {
            throw new IllegalArgumentException("Team cannot be null");
        }
        if (teams.contains(team)) {
            throw new IllegalArgumentException("Team already exists");
        }
        if (teams.stream().map(Team::getName).anyMatch(name -> name.equals(team.getName()))) {
            throw new IllegalArgumentException("Team name already exists");
        }
        teams.add(team);
    }

    public void addMatchType(MatchType matchType) {
        if (matchType == null) {
            throw new IllegalArgumentException("MatchType cannot be null");
        }
        if (matchTypes.contains(matchType)) {
            throw new IllegalArgumentException("MatchType already exists");
        }
        // Check if matchType name already exists
        if (matchTypes.stream().map(MatchType::getName).anyMatch(name -> name.equals(matchType.getName()))) {
            throw new IllegalArgumentException("MatchType name already exists");
        }
        matchTypes.add(matchType);
    }

    public Map getMap(String string) {
        if (string == null) {
            throw new IllegalArgumentException("String cannot be null");
        }
        if (!maps.stream().map(Map::getName).anyMatch(name -> name.equals(string))) {
            throw new NoSuchElementException("Map does not exist: " + string);
        }
        return maps.stream().filter(m -> m.getName().equals(string)).findFirst().get();
    }

    public Team getTeam(String name) {
        // System.out.println("Searching for team:" + name);
        if (name == null) {
            throw new IllegalArgumentException("String cannot be null");
        }
        return teams.stream().filter(t -> t.getName().equals(name)).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Team does not exist: " + name));
    }

    public Player getPlayer(String playerName, String teamName) {
        return teams.stream().filter(t -> t.getName().equals(teamName)).findFirst().orElseThrow(
                () -> new NoSuchElementException("Team does not exist: " + teamName))
                .getPlayers().stream()
                .filter(p -> p.getName().equals(playerName)).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Player does not exist: " + playerName));
    }

    public MatchType getMatchType(String string) {
        if (string == null) {
            throw new IllegalArgumentException("String cannot be null");
        }
        if (matchTypes.stream().filter(m -> m.getName().equals(string)).findFirst().isPresent()) {
            return matchTypes.stream().filter(m -> m.getName().equals(string)).findFirst().get();
        } else {
            throw new NoSuchElementException("MatchType was not found");
        }
    }

    public void addTeam(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Team cannot be null");
        }
        // Check if team with same name exists
        if (teams.stream().map(Team::getName).anyMatch(name1 -> name1.equals(name))) {
            throw new IllegalArgumentException("Team name already exists");
        }
        addTeam(new Team(name));
    }

    public void addObjective(String name, String pos) {
        if (pos.matches("[0-9]*,[0-9]*")) {
            String[] xy = pos.split(",");
            addObjective(new ObjectivePoint(selectedMap, Double.parseDouble(xy[0]), Double.parseDouble(xy[1]), name));
        } else {
            throw new IllegalArgumentException("Objective point position must be in format x,y");
        }
    }

    private void addObjective(ObjectivePoint objectivePoint) {
        if (objectivePoint == null) {
            throw new IllegalArgumentException("Objective point cannot be null");
        }
        selectedMap.addObjectivePoint(objectivePoint);
    }

    public void addMatchType(String name) {
        if (name == null) {
            throw new IllegalArgumentException("MatchType cannot be null");
        }
        // Check if matchType with same name exists
        if (matchTypes.stream().map(MatchType::getName).anyMatch(name1 -> name1.equals(name))) {
            throw new IllegalArgumentException("MatchType name already exists");
        }
        addMatchType(new MatchType(name));
    }

    public void addPlayerToSelectedTeam(String name) throws IllegalStateException {
        if (selectedTeam == null) {
            throw new IllegalStateException("No team selected");
        }
        selectedTeam.addPlayer(new Player(name));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((maps == null) ? 0 : maps.hashCode());
        result = prime * result + ((matchTypes == null) ? 0 : matchTypes.hashCode());
        result = prime * result + ((selectedMap == null) ? 0 : selectedMap.hashCode());
        result = prime * result + ((selectedMatchType == null) ? 0 : selectedMatchType.hashCode());
        result = prime * result + ((selectedObjectivePoint == null) ? 0 : selectedObjectivePoint.hashCode());
        result = prime * result + ((selectedPlayer == null) ? 0 : selectedPlayer.hashCode());
        result = prime * result + ((selectedPlayerDefencePoint == null) ? 0 : selectedPlayerDefencePoint.hashCode());
        result = prime * result + ((selectedTeam == null) ? 0 : selectedTeam.hashCode());
        result = prime * result + ((teams == null) ? 0 : teams.hashCode());
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
        Heatmap other = (Heatmap) obj;
        if (maps == null) {
            if (other.maps != null)
                return false;
        } else if (!maps.equals(other.maps))
            return false;
        if (matchTypes == null) {
            if (other.matchTypes != null)
                return false;
        } else if (!matchTypes.equals(other.matchTypes))
            return false;
        if (selectedMap == null) {
            if (other.selectedMap != null)
                return false;
        } else if (!selectedMap.equals(other.selectedMap))
            return false;
        if (selectedMatchType == null) {
            if (other.selectedMatchType != null)
                return false;
        } else if (!selectedMatchType.equals(other.selectedMatchType))
            return false;
        if (selectedObjectivePoint == null) {
            if (other.selectedObjectivePoint != null)
                return false;
        } else if (!selectedObjectivePoint.equals(other.selectedObjectivePoint))
            return false;
        if (selectedPlayer == null) {
            if (other.selectedPlayer != null)
                return false;
        } else if (!selectedPlayer.equals(other.selectedPlayer))
            return false;
        if (selectedPlayerDefencePoint == null) {
            if (other.selectedPlayerDefencePoint != null)
                return false;
        } else if (!selectedPlayerDefencePoint.equals(other.selectedPlayerDefencePoint))
            return false;
        if (selectedTeam == null) {
            if (other.selectedTeam != null)
                return false;
        } else if (!selectedTeam.equals(other.selectedTeam))
            return false;
        if (teams == null) {
            if (other.teams != null)
                return false;
        } else if (!teams.equals(other.teams))
            return false;
        return true;
    }

}
