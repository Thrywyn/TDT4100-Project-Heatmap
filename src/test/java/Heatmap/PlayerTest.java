package Heatmap;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PlayerTest {

    @Test
    @DisplayName("Test Player constructor")
    public void testPlayerConstructor() {
        // Test name
        Player player = new Player("abc");
        assertEquals("abc", player.getName());
        // Test name with invalid characters
        assertThrows(IllegalArgumentException.class, () -> {
            new Player("[abc]");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Player(";abc");
        });
        // Test null
        assertThrows(IllegalArgumentException.class, () -> {
            new Player(null);
        });
        // Test empty
        assertThrows(IllegalArgumentException.class, () -> {
            new Player("");
        });
        // Test alphanumeric
        assertDoesNotThrow(() -> {
            new Player("abc123");
        });
    }
}
