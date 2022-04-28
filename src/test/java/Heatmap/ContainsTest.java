package Heatmap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ContainsTest {

	@Test
	public void shouldOnlyOneorMoreAlphaNumericSpace() {
		assertTrue(Contains.onlyOneorMoreAlphaNumericSpace("abc"));
		assertTrue(Contains.onlyOneorMoreAlphaNumericSpace("a"));
		assertTrue(Contains.onlyOneorMoreAlphaNumericSpace("1"));
		assertTrue(Contains.onlyOneorMoreAlphaNumericSpace("abc123"));
		assertTrue(Contains.onlyOneorMoreAlphaNumericSpace("abc 123"));

		assertFalse(Contains.onlyOneorMoreAlphaNumericSpace(""));
		assertFalse(Contains.onlyOneorMoreAlphaNumericSpace("abc123_"));
		assertFalse(Contains.onlyOneorMoreAlphaNumericSpace("abc123-"));
		assertFalse(Contains.onlyOneorMoreAlphaNumericSpace("abc123;"));
		assertFalse(Contains.onlyOneorMoreAlphaNumericSpace("abc123[]"));

	}
}
