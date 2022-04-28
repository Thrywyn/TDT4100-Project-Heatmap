package Heatmap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ImportExporterTest {

	private ImportExporter importExporter;

	private static final String valid_file_content = """
			[Maps]
			Bazaar;bazaar.jpg
			Arctic;arctic.jpg
			Abandoned;abandoned.png
			Cargo;cargo.jpg
			Quarantine;quarantine.jpg
			Snowpeak;snowpeak.jpg
			Suburbia;suburbia.jpg
			Subway;subway.jpg
			Tanker;tanker.jpg
			[Teams]
			Bossfight;BF Player 1;BF Player 2;BF Player 3
			Beginners;BG Player 1;BG Player 2;BG Player 3
			Silent Purge
			Dead Logic
			Test team;Test Player 1;Test Player 2;Test Player 3
			[MatchTypes]
			VRML
			IVRL
			Scrim
			Test Match Type
			[ObjectivePoints]
			Bazaar;1884.0;552.0;Kyot
			Bazaar;2798.0;1218.0;Tank
			Bazaar;2300.0;1522.0;South East Courtyard
			Bazaar;2300.0;500.0;Test Objective Point
			[PlayerDefencePoints]
			Bazaar;Bossfight;Kyot;400.0;400.0;null;Test Match Type
			Bazaar;Test team;Kyot;1200.0;700.0;Test Player 1;VRML
			Bazaar;Beginners;Test Objective Point;2400.0;700.0;null;IVRL
			Bazaar;Beginners;South East Courtyard;1400.0;1000.0;BG Player 3;IVRL
			          """
			.replaceAll("\\R", System.getProperty("line.separator"));

	private static final String invalid_file_content = """
			[Maps]
			Bazaar;bazaar.jpg
			Arctic;arctic.jpg
			[Teams]
			Bossfight;BF_Player 1;BF Player 2;BF Player 3
			Beginners;;
			[MatchTypes]
			VRML

			IVRL
			null
			[ObjectivePoints]
			Bazaar;1884.0;552.0;Kyot
			Bazaar;2798.0;1218.0;Tank
			Bazaar;2300.0;1522.0;South East Courtyard
			[PlayerDefencePoints]
			Bazaar;Bossfight;;400;400;;
			""".replaceAll("\\R", System.getProperty("line.separator"));

	private Heatmap getStandardHeatmap() {
		return new Heatmap();
	}

	private Heatmap getFilledHeatmap() {
		Heatmap heatmap = new Heatmap();
		Team team = new Team("Test team");
		team.addPlayer(new Player("Test Player 1"));
		team.addPlayer(new Player("Test Player 2"));
		team.addPlayer(new Player("Test Player 3"));
		heatmap.addTeam(team);
		MatchType matchType = new MatchType("Test Match Type");
		heatmap.addMatchType(matchType);
		ObjectivePoint objectivePoint = new ObjectivePoint(heatmap.getMap("Bazaar"), 2300.0, 500.0,
				"Test Objective Point");
		heatmap.getMap("Bazaar").addObjectivePoint(objectivePoint);

		// Create and add points
		PlayerDefencePoint playerDefencePoint = new PlayerDefencePoint(heatmap.getMatchType("Test Match Type"),
				heatmap.getMap("Bazaar"),
				heatmap.getTeam("Bossfight"), (Player) null, heatmap.getMap("Bazaar").getObjectivePoint("Kyot"), 400.0,
				400.0);
		heatmap.getMap("Bazaar").addPlayerDefencePoint(playerDefencePoint);

		PlayerDefencePoint playerDefencePoint2 = new PlayerDefencePoint(heatmap.getMatchType("VRML"),
				heatmap.getMap("Bazaar"),
				heatmap.getTeam("Test team"), heatmap.getTeam("Test team").getPlayer("Test Player 1"),
				heatmap.getMap("Bazaar").getObjectivePoint("Kyot"), 1200.0, 700.0);
		heatmap.getMap("Bazaar").addPlayerDefencePoint(playerDefencePoint2);

		PlayerDefencePoint playerDefencePoint3 = new PlayerDefencePoint(heatmap.getMatchType("IVRL"),
				heatmap.getMap("Bazaar"),
				heatmap.getTeam("Beginners"), (Player) null,
				heatmap.getMap("Bazaar").getObjectivePoint("Test Objective Point"), 2400.0, 700.0);
		heatmap.getMap("Bazaar").addPlayerDefencePoint(playerDefencePoint3);

		// Bazaar;Beginners;South East
		// Courtyard;1400;1000;2022-04-28T22:21:39.138272900;BG Player 3;IVRL
		Team t = heatmap.getTeam("Beginners");
		PlayerDefencePoint playerDefencePoint4 = new PlayerDefencePoint(heatmap.getMatchType("IVRL"),
				heatmap.getMap("Bazaar"),
				heatmap.getTeam("Beginners"), t.getPlayer("BG Player 3"),
				heatmap.getMap("Bazaar").getObjectivePoint("South East Courtyard"), 1400.0, 1000.0);
		heatmap.getMap("Bazaar").addPlayerDefencePoint(playerDefencePoint4);

		return heatmap;

	}

	@BeforeAll
	public static void setup() throws IOException {

		Files.write(ImportExporter.getFile("test_file").toPath(), valid_file_content.getBytes());
		Files.write(ImportExporter.getFile("invalid_file").toPath(), invalid_file_content.getBytes());
	}

	@Test
	public void shouldWrite() throws IOException {
		ImportExporter importExporter = new ImportExporter();
		Heatmap expectedHeatmap = getFilledHeatmap();
		importExporter.write("test_file_written", expectedHeatmap);

		File written_file = ImportExporter.getFile("test_file_written");

		// Check that written_file content is equal to test_file_content
		assertEquals(valid_file_content, new String(Files.readAllBytes(written_file.toPath())));

	}

	@Test
	public void shouldRead() throws FileNotFoundException {
		ImportExporter importExporter = new ImportExporter();
		Heatmap expectedHeatmap = getFilledHeatmap();
		Heatmap actualHeatmap = importExporter.read("test_file");

		assertEquals(expectedHeatmap, actualHeatmap);
	}

	@Test
	@DisplayName("Should throw exception when reading invalid file")
	public void shouldThrowExceptionWhenReadingInvalidFile() throws FileNotFoundException {
		ImportExporter importExporter = new ImportExporter();
		assertThrows(IllegalArgumentException.class, () -> {
			Heatmap heatmap = importExporter.read("invalid_file");
		});
	}

	@Test
	@DisplayName("Test non existant file")
	public void shouldThrowExceptionWhenReadingNonExistantFile() {
		ImportExporter importExporter = new ImportExporter();
		assertThrows(FileNotFoundException.class, () -> {
			Heatmap heatmap = importExporter.read("non_existant_file");
		});
	}

}
