package Heatmap;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MapTest {

    @Test
    @DisplayName("Test Map constructor")
    public void testMapConstructor() throws FileNotFoundException {
        // Create map
        Map bazaar = new Map("Bazaar", "bazaar.jpg");
        // Test name
        assertEquals("Bazaar", bazaar.getName());
        // Test name with invalid characters
        assertThrows(IllegalArgumentException.class, () -> {
            new Map("[Bazaar]", "bazaar.jpg");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Map(";Bazaar", "bazaar.jpg");
        });
        // Test null
        assertThrows(IllegalArgumentException.class, () -> {
            new Map(null, "bazaar.jpg");
        });
        // Test empty name
        assertThrows(IllegalArgumentException.class, () -> {
            new Map("", "bazaar.jpg");
        });
        // Test empty image
        assertThrows(IllegalArgumentException.class, () -> {
            new Map("Bazaar", "");
        });
        // Test invalid image
        assertThrows(FileNotFoundException.class, () -> {
            new Map("Bazaar", "invalid.jpg");
        });
    }

    @Test
    @DisplayName("Test Map getters")
    public void testMapGetters() throws FileNotFoundException {
        // Create map
        Map bazaar = new Map("Bazaar", "bazaar.jpg");
        // Test name
        assertEquals("Bazaar", bazaar.getName());
    }

    @Test
    @DisplayName("Test Map setters")
    public void testMapSetters() throws FileNotFoundException {
        // Create map
        Map bazaar = new Map("Bazaar", "bazaar.jpg");
        // Test name
        bazaar.setName("Bazaar");
        assertEquals("Bazaar", bazaar.getName());
        // Test name with invalid characters
        assertThrows(IllegalArgumentException.class, () -> {
            bazaar.setName("[Bazaar]");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            bazaar.setName(";Bazaar");
        });
        // Test null
        assertThrows(IllegalArgumentException.class, () -> {
            bazaar.setName(null);
        });
        // Test empty name
        assertThrows(IllegalArgumentException.class, () -> {
            bazaar.setName("");
        });
    }

    Map map;
    Team team;
    ObjectivePoint objectivePoint;
    PlayerDefencePoint playerDefencePoint;

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        // Create map
        map = new Map("Bazaar", "bazaar.jpg");
        // Create Team
        team = new Team("Team");
        // Create Objective Point
        objectivePoint = new ObjectivePoint(map, 200.0, 200.0, "ObjectivePoint");
        // Create player point
        playerDefencePoint = new PlayerDefencePoint(map, team, objectivePoint, 100.0, 100.0);
    }

    @Test
    @DisplayName("Test Map PlayerDefencePoint Functionality")
    public void testObjectivePointFunctions() throws FileNotFoundException {
        // Test add objective point
        map.addObjectivePoint(objectivePoint);
        assertTrue(map.getObjectivePoints().contains(objectivePoint));
        // Test add null objective point
        assertThrows(IllegalArgumentException.class, () -> {
            map.addObjectivePoint(null);
        });
        // Test add duplicate point
        assertThrows(IllegalArgumentException.class, () -> {
            map.addObjectivePoint(objectivePoint);
        });
        // Test add objective point with same name
        ObjectivePoint objectivePoint2 = new ObjectivePoint(map, 300.0, 300.0, "ObjectivePoint");
        assertThrows(IllegalArgumentException.class, () -> {
            map.addObjectivePoint(objectivePoint2);
        });
    }

    @Test
    @DisplayName("Test Map PlayerDefencePoint Functionality")
    public void testPlayerDefencePointAdd() throws FileNotFoundException {
        // Test add playerdefencepoint
        map.addObjectivePoint(objectivePoint);
        map.addPlayerDefencePoint(playerDefencePoint);
        assertTrue(map.getPlayerDefencePoints().contains(playerDefencePoint));
        // Test add null playerdefencepoint
        assertThrows(IllegalArgumentException.class, () -> {
            map.addPlayerDefencePoint(null);
        });

        // Test add playerdefencepoint with objectivePoint that belongs to annother map
        Map map2 = new Map("Arctic", "arctic.jpg");
        ObjectivePoint objectivePoint3 = new ObjectivePoint(map2, 300.0, 300.0, "ObjectivePoint");
        assertThrows(IllegalArgumentException.class, () -> {
            map.addPlayerDefencePoint(new PlayerDefencePoint(map, team, objectivePoint3, 100.0, 100.0));
        });
    }

    @Test
    @DisplayName("Test Map GetPlayerDefencePoints")
    public void testGetPlayerDefencePoints() throws FileNotFoundException {
        // Test getPlayerDefencePoints
        ObjectivePoint objectivePoint2 = new ObjectivePoint(map, 300.0, 300.0, "ObjectivePoint2");
        map.addObjectivePoint(objectivePoint);
        map.addObjectivePoint(objectivePoint2);
        MatchType matchType1 = new MatchType("MatchType");
        MatchType matchType2 = new MatchType("MatchType2");
        Player player1 = new Player("Player");
        Player player2 = new Player("Player2");
        Team team1 = new Team("Team1");
        Team team2 = new Team("Team2");
        team1.addPlayer(player1);
        team2.addPlayer(player2);

        PlayerDefencePoint p1 = new PlayerDefencePoint(matchType1, map, team1, player1, objectivePoint, 100.0, 100.0);
        PlayerDefencePoint p2 = new PlayerDefencePoint(matchType1, map, team2, player2, objectivePoint2, 200.0, 200.0);
        PlayerDefencePoint p3 = new PlayerDefencePoint(matchType1, map, team1, player1, objectivePoint, 300.0, 300.0);
        PlayerDefencePoint p4 = new PlayerDefencePoint(matchType2, map, team2, player2, objectivePoint, 400.0, 400.0);
        PlayerDefencePoint p5 = new PlayerDefencePoint(matchType2, map, team1, player1, objectivePoint2, 500.0, 500.0);
        PlayerDefencePoint p6 = new PlayerDefencePoint(matchType2, map, team2, player2, objectivePoint, 600.0, 600.0);
        map.addPlayerDefencePoint(p1);
        map.addPlayerDefencePoint(p2);
        map.addPlayerDefencePoint(p3);
        map.addPlayerDefencePoint(p4);
        map.addPlayerDefencePoint(p5);
        map.addPlayerDefencePoint(p6);

        // Test get all with same matchtype
        assertEquals(new ArrayList<>(Arrays.asList(p1, p2, p3)),
                map.getPlayerDefencePoints(matchType1, null, null, null));
        // Test get all with same team
        assertEquals(new ArrayList<>(Arrays.asList(p1, p3, p5)), map.getPlayerDefencePoints(null, team1, null, null));
        // Test get all with same player
        assertEquals(new ArrayList<>(Arrays.asList(p1, p3, p5)), map.getPlayerDefencePoints(null, null, player1, null));
        // Test get all with same objective point
        assertEquals(new ArrayList<>(Arrays.asList(p2, p5)),
                map.getPlayerDefencePoints(null, null, null, objectivePoint2));

        // test get all with same matchtype and team
        assertEquals(new ArrayList<>(Arrays.asList(p1, p3)),
                map.getPlayerDefencePoints(matchType1, team1, null, null));
    }

    @Test
    @DisplayName("Test Map get obj by string")
    public void testGetObjectivePointByString() {
        // Test get objective point by name
        map.addObjectivePoint(objectivePoint);
        assertEquals(objectivePoint, map.getObjectivePoint("ObjectivePoint"));
    }

    @Test
    @DisplayName("Test removeObjectivePoint")
    public void testRemoveObjectivePoint() {
        // Test remove objective point
        map.addObjectivePoint(objectivePoint);
        assertTrue(map.getObjectivePoints().contains(objectivePoint));
        map.removeObjectivePoint("ObjectivePoint");
        assertFalse(map.getObjectivePoints().contains(objectivePoint));
    }

    @Test
    @DisplayName("Test getObjectivePoints")
    public void testGetObjectivePoints() {
        // Test get objective points
        map.addObjectivePoint(objectivePoint);
        assertEquals(new ArrayList<>(Arrays.asList(objectivePoint)), map.getObjectivePoints());
    }

    @Test
    @DisplayName("Test getObjectivePointsFromSelection")
    public void testGetObjectivePointsFromSelection() {
        // Test get objective points
        map.addObjectivePoint(objectivePoint);
        ObjectivePoint objectivePoint2 = new ObjectivePoint(map, 300.0, 300.0, "ObjectivePoint2");
        map.addObjectivePoint(objectivePoint2);
        assertEquals(new ArrayList<>(Arrays.asList(objectivePoint, objectivePoint2)), map.getObjectivePoints(null));
        assertEquals(new ArrayList<>(Arrays.asList(objectivePoint2)), map.getObjectivePoints(objectivePoint2));
    }

}
