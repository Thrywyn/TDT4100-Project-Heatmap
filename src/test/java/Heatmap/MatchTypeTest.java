package Heatmap;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MatchTypeTest {

	private MatchType matchType;

	@BeforeEach
	public void setup() {
		this.matchType = new MatchType("TEST");
	}

	@Test
	public void shouldGetName() {
		String actualValue = matchType.getName();
		assertEquals("TEST", actualValue);
	}
}
