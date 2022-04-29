package Heatmap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ContainsTest {

	@Test
	public void shouldOnlyOneorMoreAlphaNumericSpace() {
		assertTrue(Contains.onlyOneorMoreAlphaNumericSpaceAndNoWhitespaceAtStartAndEnd("abc"));
		assertTrue(Contains.onlyOneorMoreAlphaNumericSpaceAndNoWhitespaceAtStartAndEnd("a"));
		assertTrue(Contains.onlyOneorMoreAlphaNumericSpaceAndNoWhitespaceAtStartAndEnd("1"));
		assertTrue(Contains.onlyOneorMoreAlphaNumericSpaceAndNoWhitespaceAtStartAndEnd("abc123"));
		assertTrue(Contains.onlyOneorMoreAlphaNumericSpaceAndNoWhitespaceAtStartAndEnd("abc 123"));

		assertFalse(Contains.onlyOneorMoreAlphaNumericSpaceAndNoWhitespaceAtStartAndEnd(""));
		assertFalse(Contains.onlyOneorMoreAlphaNumericSpaceAndNoWhitespaceAtStartAndEnd("abc123_"));
		assertFalse(Contains.onlyOneorMoreAlphaNumericSpaceAndNoWhitespaceAtStartAndEnd("abc123-"));
		assertFalse(Contains.onlyOneorMoreAlphaNumericSpaceAndNoWhitespaceAtStartAndEnd("abc123;"));
		assertFalse(Contains.onlyOneorMoreAlphaNumericSpaceAndNoWhitespaceAtStartAndEnd("abc123[]"));

	}
}
