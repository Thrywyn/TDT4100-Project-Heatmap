package Heatmap;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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

		// TODO: assert scenario
	}

	@Test
	public void shouldGetObjectivePointsFromSelection() {
		ArrayList<ObjectivePoint> actualValue = heatmap.getObjectivePointsFromSelection();

		// TODO: assert scenario
	}

	@Test
	public void shouldGetPlayerDefencePointMapAbsToRel() {
		// TODO: initialize args
		double canvasWidth;
		double canvasHeight;

		HashMap<PlayerDefencePoint, PlayerDefencePoint> actualValue = heatmap
				.getPlayerDefencePointMapAbsToRel(canvasWidth, canvasHeight);

		// TODO: assert scenario
	}

	@Test
	public void shouldTranslatePointToRelative() {
		// TODO: initialize args
		PlayerDefencePoint p;
		double canvasWidth;
		double canvasHeight;

		PlayerDefencePoint actualValue = heatmap.translatePointToRelative(p, canvasWidth, canvasHeight);

		// TODO: assert scenario
	}

	@Test
	public void shouldGetRelativeObjectivePoints() {
		// TODO: initialize args
		double canvasWidth;
		double canvasHeight;

		ArrayList<ObjectivePoint> actualValue = heatmap.getRelativeObjectivePoints(canvasWidth, canvasHeight);

		// TODO: assert scenario
	}

	@Test
	public void shouldCalculateMaxAmountOfOverlappingLayers() {
		// TODO: initialize args
		double canvasWidth;
		double canvasHeight;
		double circleRadius;

		double actualValue = heatmap.calculateMaxAmountOfOverlappingLayers(canvasWidth, canvasHeight, circleRadius);

		// TODO: assert scenario
	}

	@Test
	public void shouldCalculateAlphaValuesFromMaxOverlap() {
		// TODO: initialize args
		double maxLayersOverlapping;

		double actualValue = heatmap.calculateAlphaValuesFromMaxOverlap(maxLayersOverlapping);

		// TODO: assert scenario
	}

	@Test
	public void shouldAddMap() {
		// TODO: initialize args
		Map map;

		heatmap.addMap(map);

		// TODO: assert scenario
	}

	@Test
	public void shouldAddTeam() {
		// TODO: initialize args
		Team team;

		heatmap.addTeam(team);

		// TODO: assert scenario
	}

	@Test
	public void shouldAddMatchType() {
		// TODO: initialize args
		MatchType matchType;

		heatmap.addMatchType(matchType);

		// TODO: assert scenario
	}

	@Test
	public void shouldGetMap() {
		// TODO: initialize args
		String string;

		Map actualValue = heatmap.getMap(string);

		// TODO: assert scenario
	}

	@Test
	public void shouldGetTeam() {
		// TODO: initialize args
		String name;

		Team actualValue = heatmap.getTeam(name);

		// TODO: assert scenario
	}

	@Test
	public void shouldGetPlayer() {
		// TODO: initialize args
		String string;

		Player actualValue = heatmap.getPlayer(string);

		// TODO: assert scenario
	}

	@Test
	public void shouldGetMatchType() {
		// TODO: initialize args
		String string;

		MatchType actualValue = heatmap.getMatchType(string);

		// TODO: assert scenario
	}

	@Test
	public void shouldAddMap() {
		// TODO: initialize args
		String mapName;
		String fileName;

		heatmap.addMap(mapName, fileName);

		// TODO: assert scenario
	}

	@Test
	public void shouldAddTeam() {
		// TODO: initialize args
		String name;

		heatmap.addTeam(name);

		// TODO: assert scenario
	}

	@Test
	public void shouldAddObjective() {
		// TODO: initialize args
		String name;
		String pos;

		heatmap.addObjective(name, pos);

		// TODO: assert scenario
	}

	@Test
	public void shouldAddMatchType() {
		// TODO: initialize args
		String name;

		heatmap.addMatchType(name);

		// TODO: assert scenario
	}

	@Test
	public void shouldAddPlayerToSelectedTeam() {
		// TODO: initialize args
		String name;

		heatmap.addPlayerToSelectedTeam(name);

		// TODO: assert scenario
	}

	@Test
	public void shouldDeleteMap() {
		// TODO: initialize args
		String name;

		heatmap.deleteMap(name);

		// TODO: assert scenario
	}
}
