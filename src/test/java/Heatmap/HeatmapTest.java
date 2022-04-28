package Heatmap;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HeatmapTest {

	private Heatmap heatmap;

	@BeforeEach
	public void setup() {
		this.heatmap = new Heatmap();
	}

	@Test
	public void shouldDeleteTeam() {
		Team team = heatmap.getTeams().get(0);
		// Test delete team
		heatmap.deleteTeam(team);
		assertFalse(heatmap.getTeams().contains(team));
		// Test delete team with by string name
		Team team2 = heatmap.getTeams().get(0);
		heatmap.deleteTeam(team2.getName());
		assertFalse(heatmap.getTeams().contains(team2));

		assertThrows(NoSuchElementException.class, () -> {
			heatmap.deleteTeam(team);
		});

		assertThrows(NoSuchElementException.class, () -> {
			heatmap.deleteTeam(team.getName());
		});

		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.deleteTeam((String) null);
		});

		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.deleteTeam((Team) null);
		});

	}

	@Test
	public void shouldDeleteSelectedPlayerDefencePoint() throws FileNotFoundException {
		Team team = new Team("team");
		Double x = 100.0;
		Double y = 100.0;
		Player player = new Player("player");
		team.addPlayer(player);
		MatchType matchType = new MatchType("SCRIM");
		Map map = new Map("TEST", "bazaar.jpg");
		ObjectivePoint obj = new ObjectivePoint(map, x, y, "obj");
		map.addObjectivePoint(obj);
		PlayerDefencePoint playerDefencePoint = new PlayerDefencePoint(matchType, map, team, player, obj, x, y);
		heatmap.addMap(map);

		heatmap.setSelectedMap(map.getName());
		heatmap.setSelectedPlayerDefencePoint(playerDefencePoint);
		heatmap.getSelectedMap().addPlayerDefencePoint(playerDefencePoint);

		assertTrue(heatmap.getSelectedPlayerDefencePoint().equals(playerDefencePoint));
		heatmap.deleteSelectedPlayerDefencePoint();
		assertFalse(heatmap.getSelectedMap().getPlayerDefencePoints().contains(playerDefencePoint));
	}

	@Test
	public void shouldSelectClosestPlayerPointInRadius() throws FileNotFoundException {
		Team team = new Team("team");
		Double x = 100.0;
		Double y = 100.0;
		Player player = new Player("player");
		team.addPlayer(player);
		Map map = new Map("TEST", "bazaar.jpg");
		ObjectivePoint obj = new ObjectivePoint(map, x, y, "obj");
		map.addObjectivePoint(obj);
		MatchType matchType = heatmap.getMatchType("VRML");
		PlayerDefencePoint pp1 = new PlayerDefencePoint(matchType, map, team, player, obj, x, y);
		heatmap.addMap(map);
		heatmap.setSelectedMap(map.getName());
		map.addPlayerDefencePoint(pp1);
		PlayerDefencePoint pp2 = new PlayerDefencePoint(matchType, map, team, player, obj, 205.0, 205.0);
		map.addPlayerDefencePoint(pp2);
		PlayerDefencePoint pp3 = new PlayerDefencePoint(matchType, map, team, player, obj, 300.0, 300.0);
		map.addPlayerDefencePoint(pp3);
		// Check for inside radius
		assertDoesNotThrow(() -> {
			heatmap.selectClosestPlayerPointInRadius(200.0, 200.0, 5.0, map.getWidth(), map.getHeight());
		});
		assertTrue(heatmap.getSelectedPlayerDefencePoint().equals(pp2));

		// Check for outside radius
		heatmap.selectClosestPlayerPointInRadius(200.0, 200.0, 2.0, map.getWidth(), map.getHeight());
		assertNull(heatmap.getSelectedPlayerDefencePoint());

	}

	@Test
	public void shouldGetPlayerDefencePointMapAbsToRel() {
		double canvasWidth = heatmap.getMap("Bazaar").getWidth();
		double canvasHeight = heatmap.getMap("Bazaar").getHeight();
		Team team = new Team("Test team");
		team.addPlayer(new Player("player"));
		Map map = heatmap.getMap("Bazaar");
		heatmap.addTeam(team);
		heatmap.setEditorTeam(team.getName());
		heatmap.setEditorMatchType(null);

		Double x = 200.0;
		Double y = 200.0;
		ObjectivePoint obj = map.getObjectivePoints().get(0);
		PlayerDefencePoint pp = new PlayerDefencePoint(map, team, obj, x, y);
		map.addPlayerDefencePoint(pp);
		heatmap.setSelectedMap(map.getName());
		heatmap.setSelectedPlayerDefencePoint(pp);
		heatmap.setEditorObjectivePoint(obj.getName());

		HashMap<PlayerDefencePoint, PlayerDefencePoint> actualValue = heatmap
				.getPlayerDefencePointMapAbsToRel(canvasWidth / 2, canvasHeight / 2);
		System.out.println(actualValue);

		// Test functionality
		assertTrue(actualValue.get(pp).getY().equals(y / 2));
		assertTrue(actualValue.get(pp).getX().equals(x / 2));

		// Test illegal values
		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.getPlayerDefencePointMapAbsToRel(0.0, 0.0);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.getPlayerDefencePointMapAbsToRel(-100.0, 0);
		});

	}

	@Test
	public void shouldGetRelativeObjectivePoints() throws FileNotFoundException {
		double canvasWidth = heatmap.getMap("Bazaar").getWidth() / 2;
		double canvasHeight = heatmap.getMap("Bazaar").getWidth() / 2;

		Map map = new Map("TEST", "bazaar.jpg");

		ObjectivePoint obj1 = new ObjectivePoint(map, 100.0, 100.0, "obj1");
		ObjectivePoint obj2 = new ObjectivePoint(map, 200.0, 200.0, "obj2");
		ObjectivePoint obj3 = new ObjectivePoint(map, 300.0, 300.0, "obj3");

		map.addObjectivePoint(obj1);
		map.addObjectivePoint(obj2);
		map.addObjectivePoint(obj3);
		heatmap.addMap(map);
		heatmap.setSelectedMap("TEST");

		ArrayList<ObjectivePoint> actualValue = heatmap.getRelativeObjectivePoints(canvasWidth, canvasHeight);

		// Test translation
		assertTrue(actualValue.stream().filter(obj -> obj.getName().equals("obj1")).findAny().get()
				.getX() == obj1.getX() / 2);
		assertTrue(actualValue.stream().filter(obj -> obj.getName().equals("obj2")).findAny().get()
				.getX() == obj2.getX() / 2);
		assertTrue(actualValue.stream().filter(obj -> obj.getName().equals("obj3")).findAny().get()
				.getX() == obj3.getX() / 2);

		// Test that it returns all objective points
		heatmap.setEditorObjectivePoint(null);
		assertTrue(heatmap.getRelativeObjectivePoints(canvasWidth, canvasHeight).size() == map.getObjectivePoints()
				.size());

		// Test illegal values
		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.getRelativeObjectivePoints(0.0, 0.0);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.getRelativeObjectivePoints(-100.0, 0);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.getRelativeObjectivePoints(0.0, -100.0);
		});

	}

	@Test
	public void shouldCalculateMaxAmountOfOverlappingLayers() throws FileNotFoundException {
		Team team = new Team("team");
		Player player = new Player("player");
		team.addPlayer(player);
		Map map = new Map("TEST", "bazaar.jpg");
		ObjectivePoint obj = new ObjectivePoint(map, 50.0, 50.0, "obj");
		map.addObjectivePoint(obj);
		MatchType matchType = heatmap.getMatchType("VRML");
		PlayerDefencePoint pp1 = new PlayerDefencePoint(matchType, map, team, player, obj, 100.0, 100.0);
		heatmap.addMap(map);
		heatmap.setSelectedMap(map.getName());

		// Test empty map
		double actualValue = heatmap.calculateMaxAmountOfOverlappingLayers(map.getWidth(), map.getHeight(), 5.0);
		assertEquals(0, actualValue);

		map.addPlayerDefencePoint(pp1);
		// Test one point
		actualValue = heatmap.calculateMaxAmountOfOverlappingLayers(map.getWidth(), map.getHeight(), 5.0);
		assertEquals(1, actualValue);

		PlayerDefencePoint pp2 = new PlayerDefencePoint(matchType, map, team, player, obj, 209.0, 200.0);
		map.addPlayerDefencePoint(pp2);
		PlayerDefencePoint pp3 = new PlayerDefencePoint(matchType, map, team, player, obj, 200.0, 200.0);
		map.addPlayerDefencePoint(pp3);
		// Test two points
		actualValue = heatmap.calculateMaxAmountOfOverlappingLayers(map.getWidth(), map.getHeight(), 5.0);
		assertEquals(2.0, actualValue);

		// Test three points
		PlayerDefencePoint pp4 = new PlayerDefencePoint(matchType, map, team, player, obj, 205.0, 200.0);
		map.addPlayerDefencePoint(pp4);
		actualValue = heatmap.calculateMaxAmountOfOverlappingLayers(map.getWidth(), map.getHeight(), 5.0);
		assertEquals(3.0, actualValue);

		PlayerDefencePoint pp5 = new PlayerDefencePoint(matchType, map, team, player, obj, 195.0, 200.0);

		map.addPlayerDefencePoint(pp5);
		actualValue = heatmap.calculateMaxAmountOfOverlappingLayers(map.getWidth(), map.getHeight(), 5.0);
		assertEquals(3.0, actualValue);

	}

	@Test
	public void shouldCalculateAlphaValuesFromMaxOverlap() {
		// Test empty map
		double actualValue = heatmap.calculateAlphaValuesFromMaxOverlap(0, 0.75);
		assertEquals(0.75, actualValue);

		// Test one point
		actualValue = heatmap.calculateAlphaValuesFromMaxOverlap(1, 0.75);
		assertEquals(0.75, actualValue);

		// Test two points
		actualValue = heatmap.calculateAlphaValuesFromMaxOverlap(2, 0.75);
		assertEquals(0.5, actualValue);
	}

	@Test
	public void shouldAddTeam() {
		Team team = new Team("team");
		// Add team
		assertDoesNotThrow(() -> {
			heatmap.addTeam(team);
		});
		assertTrue(heatmap.getTeams().contains(team));

		// Add same team twice
		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.addTeam(team);
		});
		// Add null
		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.addTeam((Team) null);
		});
		// Add team with same name
		Team team2 = new Team("team");
		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.addTeam(team2);
		});
	}

	@Test
	public void shouldAddMatchType() {
		MatchType matchType = new MatchType("TEST");

		// Add match type
		assertDoesNotThrow(() -> {
			heatmap.addMatchType(matchType);
		});
		assertTrue(heatmap.getMatchTypes().contains(matchType));

		// Null
		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.addMatchType((MatchType) null);
		});
		// Add same match type twice
		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.addMatchType(matchType);
		});

	}

	@Test
	public void shouldGetMap() throws FileNotFoundException {
		Map map = new Map("TEST", "bazaar.jpg");
		heatmap.addMap(map);

		// Test that it returns the correct map
		Map actualValue = heatmap.getMap("TEST");
		assertEquals(map, actualValue);

		// Test with map that does not exist
		assertThrows(NoSuchElementException.class, () -> {
			heatmap.getMap("TEST2");
		});

		// Test null
		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.getMap(null);
		});
	}

	@Test
	public void shouldGetTeam() {
		String name = "TEST";
		Team team = new Team(name);
		heatmap.addTeam(team);

		Team actualValue = heatmap.getTeam(name);
		assertTrue(actualValue.equals(team));

		// Test with team that does not exist
		assertThrows(NoSuchElementException.class, () -> {
			heatmap.getTeam("TEST2");
		});
		// Test null
		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.getTeam(null);
		});
	}

	@Test
	public void shouldGetPlayer() {
		String name = "TEST";
		Player player = new Player(name);
		Team team = new Team("team");
		team.addPlayer(player);
		heatmap.addTeam(team);

		Player actualValue = heatmap.getPlayer(name, team.getName());
		// Test that it returns the correct player
		assertTrue(actualValue.equals(player));

		// Test with player that does not exist
		assertThrows(NoSuchElementException.class, () -> {
			heatmap.getPlayer("TEST2", team.getName());
		});

		// Test with team that does not exist
		assertThrows(NoSuchElementException.class, () -> {
			heatmap.getPlayer(name, "teamthatdoesnotexist");
		});
	}

	@Test
	public void shouldGetMatchType() {
		String name = "TEST";
		MatchType matchType = new MatchType(name);
		heatmap.addMatchType(matchType);

		MatchType actualValue = heatmap.getMatchType(name);
		assertEquals(matchType, actualValue);

		// Test with match type that does not exist
		assertThrows(NoSuchElementException.class, () -> {
			heatmap.getMatchType("TEST2");
		});
		// Test null
		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.getMatchType(null);
		});
		// Test lowercase, should seperate between them
		assertThrows(NoSuchElementException.class, () -> {
			heatmap.getMatchType(name.toLowerCase());
		});
	}

	@Test
	public void shouldAddTeamString() {
		String name = "test team";
		// Add team
		heatmap.addTeam(name);
		// Check if contains team with name
		assertTrue(heatmap.getTeams().stream().anyMatch(team -> team.getName().equals(name)));
		// Add team with same name
		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.addTeam(name);
		});
		// Add null
		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.addTeam((String) null);
		});
	}

	@Test
	public void shouldAddObjective() {
		String name = "TEST";
		String pos = "10,10";

		assertDoesNotThrow(() -> {
			heatmap.addObjective(name, pos);
		});
		// Test add obj with same name
		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.addObjective(name, pos);
		});
		// Test add obj with same position but different name
		assertDoesNotThrow(() -> {
			heatmap.addObjective(name + "2", pos);
		});
		// Test add obj with wrong position format
		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.addObjective(name, "10,10,10");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.addObjective(name, "10");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.addObjective(name, "10.10");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.addObjective(name, "asd,sd");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.addObjective(name, "10,a");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.addObjective(name, "10,10,a");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.addObjective(name, "10,10a");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.addObjective(name, "asd");
		});
		// Test null
		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.addObjective((String) null, pos);
		});
	}

	@Test
	public void shouldAddMatchTypeString() {

		String name = "TEST";
		assertDoesNotThrow(() -> {
			heatmap.addMatchType(name);
		});
		// Test add match type with same name
		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.addMatchType(name);
		});
		// Test add match type with same name but different case
		assertDoesNotThrow(() -> {
			heatmap.addMatchType(name.toLowerCase());
		});
		// Test add null
		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.addMatchType((String) null);
		});
	}

	@Test
	public void shouldAddPlayerToSelectedTeam() {
		String name = "TEST";
		Team team = new Team("team");
		heatmap.addTeam(team);
		heatmap.setEditorTeam(team.getName());
		heatmap.addPlayerToSelectedTeam(name);
		// Check if contains player with name
		assertTrue(team.getPlayers().stream().anyMatch(player -> player.getName().equals(name)));
		// Add player with same name
		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.addPlayerToSelectedTeam(name);
		});
		// Add null
		assertThrows(IllegalArgumentException.class, () -> {
			heatmap.addPlayerToSelectedTeam((String) null);
		});
	}
}
