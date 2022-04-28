package Heatmap;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PointTest {

    @Test
    @DisplayName("Test Point constructor")
    public void testPointConstructor() throws FileNotFoundException {

        Map bazaar = new Map("Bazaar", "bazaar.jpg");
        // Test map
        Point point = new Point(bazaar, 100.0, 100.0);
        assertEquals(bazaar, point.getMap(), "Map should be bazaar");
        // Test x
        assertEquals(100.0, point.getX(), "X should be 100.0");
        // Test y
        assertEquals(100.0, point.getY(), "Y should be 100.0");
        // Test dateCreated
        assertNotNull(point.getDateCreated(), "DateCreated should not be null");
        // Check date is same as today
        assertEquals(LocalDate.now(), point.getDateCreated().toLocalDate(), "DateCreated should be today");

        // Test invalid x
        assertThrows(IllegalArgumentException.class, () -> {
            new Point(bazaar, -1.0, 100.0);
        });
        // Test invalid y
        assertThrows(IllegalArgumentException.class, () -> {
            new Point(bazaar, 100.0, -1.0);
        });
        // Test Double.max x
        assertThrows(IllegalArgumentException.class, () -> {
            new Point(bazaar, Double.MAX_VALUE, 100.0);
        });
        // Test Double.max y
        assertThrows(IllegalArgumentException.class, () -> {
            new Point(bazaar, 100.0, Double.MAX_VALUE);
        });

        // Test null map
        assertThrows(IllegalArgumentException.class, () -> {
            new Point(null, 100.0, 100.0);
        });

        // Test null x
        assertThrows(IllegalArgumentException.class, () -> {
            new Point(bazaar, null, 100.0);
        });
        // Test null y
        assertThrows(IllegalArgumentException.class, () -> {
            new Point(bazaar, 100.0, null);
        });

    }

}
