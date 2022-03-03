package Heatmap;

import java.util.ArrayList;

public class Heatmap {

    private ArrayList<Map> maps = new ArrayList<>();
    private ArrayList<MatchType> matchTypes = new ArrayList<>();
    private ArrayList<Team> teams = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();

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
        maps.add(new Map("Bazaar", "bazaar.jpg"));
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
        teams.add(new Team("Bossfight"));
        teams.add(new Team("Beginners"));
        teams.add(new Team("Silent Purge"));
        teams.add(new Team("Dead Logic"));
    }

    private void createObjectives() {
        
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

}
