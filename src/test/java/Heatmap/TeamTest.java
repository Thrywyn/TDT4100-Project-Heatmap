package Heatmap;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TeamTest {

    @BeforeEach
    public void setup() {
    }

    @Test
    @DisplayName("Test team constructor")
    public void testTeamConstructor() {
        // Test name
        Team team = new Team("abc");
        assertEquals("abc", team.getName());
        // Test name with invalid characters
        assertThrows(IllegalArgumentException.class, () -> {
            new Team("[abc]");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Team(";abc");
        });
        // Test null
        assertThrows(IllegalArgumentException.class, () -> {
            new Team(null);
        });
        // Test empty
        assertThrows(IllegalArgumentException.class, () -> {
            new Team("");
        });
    }

    @Test
    @DisplayName("Test team add player function")
    public void testTeamAddPlayer() {
        // Test add player
        Team team = new Team("Test Team");
        Player player = new Player("Test Player");
        team.addPlayer(player);
        assertTrue(team.getPlayers().contains(player));
        // Test add player with null
        assertThrows(IllegalArgumentException.class, () -> {
            team.addPlayer(null);
        });
        // Test add player with existing player
        assertThrows(IllegalArgumentException.class, () -> {
            team.addPlayer(player);
        });
    }
}
