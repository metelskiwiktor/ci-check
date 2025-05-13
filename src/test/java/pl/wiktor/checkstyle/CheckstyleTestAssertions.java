package pl.wiktor.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Helper class for Checkstyle test assertions.
 */
public class CheckstyleTestAssertions {
    private static final Logger LOGGER = Logger.getLogger(CheckstyleTestAssertions.class.getName());

    /**
     * Asserts that only one kind of violation is detected and it matches the expected pattern.
     */
    public static void assertSingleViolationType(List<AuditEvent> events, String filePath,
                                                 String expectedCheckName, String expectedMessage) {
        assertFalse(events.isEmpty(),
                () -> "Expected at least one violation for " + filePath + ", but got none.");

        List<String> distinctSources = getDistinctSources(events);

        if (distinctSources.size() != 1) {
            printViolationDetails(events, distinctSources);
        }

        assertEquals(1, distinctSources.size(),
                () -> "Expected only one kind of violation, but got multiple sources: " + distinctSources);

        assertContains(distinctSources.getFirst(), expectedCheckName,
                "check name", distinctSources.getFirst());

        assertAllMessagesContain(events, expectedMessage);
    }

    /**
     * Asserts that no violations are found for clean code.
     */
    public static void assertNoViolations(List<AuditEvent> events, String filePath) {
        assertTrue(events.isEmpty(),
                () -> "Expected no violations for " + filePath + ", but got:\n" +
                        formatViolations(events));
    }

    /**
     * Asserts that all expected violations are found and no unexpected ones are present.
     */
    public static void assertExpectedViolations(List<AuditEvent> events, List<String> expectedFragments) {
        assertFalse(events.isEmpty(), "Expected violations but found none.");

        List<String> messages = events.stream()
                .map(AuditEvent::getMessage)
                .toList();

        List<String> missing = findMissingFragments(messages, expectedFragments);
        assertTrue(missing.isEmpty(),
                () -> "Missing expected violations: " + missing);

        List<String> unexpected = findUnexpectedViolations(events, expectedFragments);
        assertTrue(unexpected.isEmpty(),
                () -> "Unexpected violations: " + unexpected);
    }

    private static List<String> getDistinctSources(List<AuditEvent> events) {
        return events.stream()
                .map(AuditEvent::getSourceName)
                .distinct()
                .toList();
    }

    private static void printViolationDetails(List<AuditEvent> events, List<String> distinctSources) {
        LOGGER.warning("Detected multiple violation sources:");
        distinctSources.forEach(s -> LOGGER.warning(" - " + s));

        LOGGER.warning("Full violation messages:");
        events.forEach(e -> LOGGER.warning(String.format("[%s] %s", e.getSourceName(), e.getMessage())));
    }

    private static void assertContains(String actual, String expected, String type, String fullValue) {
        assertTrue(actual.contains(expected),
                () -> String.format("Expected %s '%s' but got '%s'", type, expected, fullValue));
    }

    private static void assertAllMessagesContain(List<AuditEvent> events, String expectedFragment) {
        events.forEach(e ->
                assertTrue(e.getMessage().toLowerCase().contains(expectedFragment.toLowerCase()),
                        () -> String.format("Violation message should contain '%s', but got '%s'",
                                expectedFragment, e.getMessage()))
        );
    }

    private static String formatViolations(List<AuditEvent> events) {
        return events.stream()
                .map(e -> String.format("Line %d:%d - %s [%s]",
                        e.getLine(), e.getColumn(), e.getMessage(), e.getSourceName()))
                .collect(Collectors.joining("\n"));
    }

    private static List<String> findMissingFragments(List<String> messages, List<String> expectedFragments) {
        return expectedFragments.stream()
                .filter(fragment -> messages.stream().noneMatch(msg -> msg.contains(fragment)))
                .toList();
    }

    private static List<String> findUnexpectedViolations(List<AuditEvent> events, List<String> expectedFragments) {
        List<AuditEvent> unexpectedEvents = events.stream()
                .filter(event -> expectedFragments.stream()
                        .noneMatch(fragment -> event.getMessage().contains(fragment)))
                .toList();

        return unexpectedEvents.stream()
                .map(event -> event.getSourceName() + ": " + event.getMessage())
                .toList();
    }
}