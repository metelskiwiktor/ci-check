package pl.wiktor.checkstyle;

/**
 * Represents a single Checkstyle rule configuration for testing.
 */
public record CheckstyleRule(
        String failTestFile,
        String successTestFile,
        String checkNameFragment,
        String messageFragment
) {
    @Override
    public String toString() {
        return String.format("Rule: %s (message: %s)", checkNameFragment, messageFragment);
    }
}