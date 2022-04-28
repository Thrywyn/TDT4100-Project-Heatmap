package Heatmap;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ObjectivePointTest {

    @Test
    @DisplayName("Test ObjectivePoint constructor")
    public void testObjectivePointConstructor() throws FileNotFoundException {
        // Create map
        Map bazaar = new Map("Bazaar", "bazaar.jpg");
        // Test name
        ObjectivePoint objectivePoint = new ObjectivePoint(bazaar, 100.0, 100.0, "abc");
        assertEquals("abc", objectivePoint.getName(), "Name should be abc");
        // Test name with invalid characters
        assertThrows(IllegalArgumentException.class, () -> {
            new ObjectivePoint(bazaar, 100.0, 100.0, "[abc]");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new ObjectivePoint(bazaar, 100.0, 100.0, ";abc");
        });
        // Test null
        assertThrows(IllegalArgumentException.class, () -> {
            new ObjectivePoint(bazaar, 100.0, 100.0, null);
        });
        // Test empty name
        assertThrows(IllegalArgumentException.class, () -> {
            new ObjectivePoint(bazaar, 100.0, 100.0, "");
        });
    }

}
