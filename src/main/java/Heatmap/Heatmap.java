package Heatmap;

import java.util.ArrayList;

public class Heatmap {

    // Variables
    private ArrayList<Map> maps = new ArrayList<>();
    private ArrayList<MatchType> matchTypes = new ArrayList<>();
    private ArrayList<Team> teams = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();

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
            this.selectedPlayer = players.stream()
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

}
