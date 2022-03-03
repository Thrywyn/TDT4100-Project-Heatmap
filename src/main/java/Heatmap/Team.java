package Heatmap;

import java.util.ArrayList;

public class Team implements ChoiceBoxToStringInterface {

    private String name;

    private ArrayList<Player> players = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }

    public Team(String name, ArrayList<Player> players) {
        this(name);
        this.players = players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    @Override
    public String getChoiceBoxString() {
        return name;
    }

}
