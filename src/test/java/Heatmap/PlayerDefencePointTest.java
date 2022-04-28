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

	@Override
	public String toString() {
		return "PlayerDefencePointTest [map=" + map + ", matchType=" + matchType + ", obj=" + obj + ", player=" + player
				+ ", playerDefencePoint=" + playerDefencePoint + ", team=" + team + ", x=" + x + ", y=" + y + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlayerDefencePointTest other = (PlayerDefencePointTest) obj;
		if (map == null) {
			if (other.map != null)
				return false;
		} else if (!map.equals(other.map))
			return false;
		if (matchType == null) {
			if (other.matchType != null)
				return false;
		} else if (!matchType.equals(other.matchType))
			return false;
		if (this.obj == null) {
			if (other.obj != null)
				return false;
		} else if (!this.obj.equals(other.obj))
			return false;
		if (player == null) {
			if (other.player != null)
				return false;
		} else if (!player.equals(other.player))
			return false;
		if (playerDefencePoint == null) {
			if (other.playerDefencePoint != null)
				return false;
		} else if (!playerDefencePoint.equals(other.playerDefencePoint))
			return false;
		if (team == null) {
			if (other.team != null)
				return false;
		} else if (!team.equals(other.team))
			return false;
		if (x == null) {
			if (other.x != null)
				return false;
		} else if (!x.equals(other.x))
			return false;
		if (y == null) {
			if (other.y != null)
				return false;
		} else if (!y.equals(other.y))
			return false;
		return true;
	}

}
