package Heatmap;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PlayerDefencePointTest {
	private Map map;
	private Team team;
	private ObjectivePoint obj;
	private Double x;
	private Double y;
	private Player player;
	private MatchType matchType;

	private PlayerDefencePoint playerDefencePoint;

	@BeforeEach
	public void setup() throws FileNotFoundException {
		team = new Team("team");
		x = 100.0;
		y = 100.0;
		player = new Player("player");
		team.addPlayer(player);
		matchType = new MatchType("SCRIM");
		map = new Map("Bazaar", "bazaar.jpg");
		obj = new ObjectivePoint(map, x, y, "obj");
		map.addObjectivePoint(obj);
		this.playerDefencePoint = new PlayerDefencePoint(matchType, map, team, player, obj, x, y);
	}

	@Test
	public void shouldGetTeam() {
		Team actualValue = playerDefencePoint.getTeam();
		assertEquals(team, actualValue);
	}

	@Test
	public void shouldGetPlayer() {
		Player actualValue = playerDefencePoint.getPlayer();
		assertEquals(player, actualValue);
	}

	@Test
	public void shouldGetObjectivePoint() {
		ObjectivePoint actualValue = playerDefencePoint.getObjectivePoint();
		assertEquals(obj, actualValue);
	}

	@Test
	public void shouldGetMatchType() {
		MatchType actualValue = playerDefencePoint.getMatchType();
		assertEquals(matchType, actualValue);
	}

	@Test
	@DisplayName("Test PlayerDefencePoint constructor")
	public void testPlayerDefencePointConstructor() throws FileNotFoundException {
		// Test create playerDefencePoint with wrong team
		Team team2 = new Team("Team2");
		MatchType matchType = new MatchType("MatchType");
		Player player2 = new Player("Player");
		team2.addPlayer(player2);
		assertThrows(IllegalArgumentException.class, () -> {
			PlayerDefencePoint pp = new PlayerDefencePoint(matchType, map, team, player2, obj, 100.0, 100.0);
		});

		// Test create playerDefencePoint with player on annother team
		Team team3 = new Team("Team3");
		team3.addPlayer(player);
		assertThrows(IllegalArgumentException.class, () -> {
			PlayerDefencePoint pp = new PlayerDefencePoint(matchType, map, team3, player2, obj, 100.0, 100.0);
		});

		// Test create playerDefencePoint with map without objective point
		Map map2;
		map2 = new Map("Bazaar2", "bazaar.jpg");
		assertThrows(IllegalArgumentException.class, () -> {
			PlayerDefencePoint pp = new PlayerDefencePoint(matchType, map2, team, player, obj, 100.0, 100.0);
		});

		// Test create playerDefencePoint with team and null player
		assertDoesNotThrow(() -> {
			PlayerDefencePoint pp = new PlayerDefencePoint(matchType, map, team, null, obj, 100.0, 100.0);
		});

	}
}
