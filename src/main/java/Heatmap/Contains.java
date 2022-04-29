package Heatmap;

public class Contains {

    public static boolean onlyOneorMoreAlphaNumericSpaceAndNoWhitespaceAtStartAndEnd(String s) {
        if (s.matches("[a-zA-Z0-9\\s]+")) {
            // Check if trailing whitespace
            if (s.endsWith(" ")) {
                return false;
            }
            // Check if name starts with whitespace
            if (s.startsWith(" ")) {
                return false;
            }
            return true;
        }
        return false;
    }

}
