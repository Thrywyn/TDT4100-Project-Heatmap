package Heatmap;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Team implements ChoiceBoxToStringInterface {

    private String name;

    private ArrayList<Player> players = new ArrayList<>();

    public Team(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (!Contains.onlyOneorMoreAlphaNumericSpaceAndNoWhitespaceAtStartAndEnd(name)) {
            throw new IllegalArgumentException("Name must be only one or more alphanumeric characters");
        }
        this.name = name;
    }

    public void addPlayer(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        if (players.contains(player)) {
            throw new IllegalArgumentException("Player already exists");
        }
        // Check if player with same name exists
        for (Player p : players) {
            if (p.getName().equals(player.getName())) {
                throw new IllegalArgumentException("Player already exists");
            }
        }
        players.add(player);
    }

    @Override
    public String getChoiceBoxString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((players == null) ? 0 : players.hashCode());
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
        Team other = (Team) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (players == null) {
            if (other.players != null)
                return false;
        } else if (!players.equals(other.players))
            return false;
        return true;
    }

    public Player getPlayer(String string) {
        if (string == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        // Stream to find player with same name
        for (Player p : players) {
            if (p.getName().equals(string)) {
                return p;
            }
        }

        throw new NoSuchElementException("Player does not exist");
    }

    @Override
    public String toString() {
        return "Team [name=" + name + ", players=" + players + "]";
    }

}
