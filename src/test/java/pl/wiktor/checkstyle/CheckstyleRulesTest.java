package pl.wiktor.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CheckstyleRulesTest {

    @ParameterizedTest(name = "Violation for {0}: {1}")
    @CsvSource({
            "'src/test/resources/rules/fail/UnusedImport.java',           UnusedImports,           Unused import",
            "'src/test/resources/rules/fail/UnusedLocalVariable.java',    UnusedLocalVariable,     Unused named local variable",
            "'src/test/resources/rules/fail/IndentationCheck.java',       IndentationCheck,        incorrect indentation level",
            "'src/test/resources/rules/fail/WhitespaceAround.java',       WhitespaceAround,        is not preceded with whitespace.",
            "'src/test/resources/rules/fail/WhitespaceAroundCheck.java',  WhitespaceAroundCheck,   is not followed by whitespace",
            "'src/test/resources/rules/fail/NoWhitespaceBefore.java',     NoWhitespaceBefore,      is preceded with whitespace.",
            "'src/test/resources/rules/fail/GenericWhitespace.java',      GenericWhitespace,       GenericWhitespace",
            "'src/test/resources/rules/fail/UnnecessaryBlankLine.java',   RegexpMultilineCheck,    Unnecessary blank line",
            "'src/test/resources/rules/fail/EmptyLineSeparatorCheck.java',EmptyLineSeparatorCheck, more than 1 empty line",
            "'src/test/resources/rules/fail/TypeName.java',               TypeName,                must match pattern",
            "'src/test/resources/rules/fail/MethodName.java',             MethodName,              must match pattern",
            "'src/test/resources/rules/fail/LocalVariableName.java',      LocalVariableName,       must match pattern",
            "'src/test/resources/rules/fail/ConstantName.java',           ConstantName,            must match pattern",
            "'src/test/resources/rules/fail/SystemOutUsage.java',         RegexpSinglelineJava,    System.out is forbidden",
            "'src/test/resources/rules/fail/SingleLineCommentCheck.java', RegexpSinglelineJava,    Single-line comments are forbidden",
            "'src/test/resources/rules/fail/LineLengthCheck.java',        LineLength,              Line is longer than 120 characters",
            "'src/test/resources/rules/fail/NeedBracesCheck.java',        NeedBraces,              construct must use '{}'",
            "'src/test/resources/rules/fail/SystemErrUsage.java',         RegexpSinglelineJava,    System.err is forbidden",
    })
    void shouldDetectOnlyOneKindOfViolation(String filePath,
                                            String checkNameFragment,
                                            String messageFragment) throws Exception {
        List<AuditEvent> events = runCheckstyle(filePath);

        assertFalse(events.isEmpty(),
                () -> "Expected at least one violation for " + filePath + ", but got none.");

        List<String> distinctSources = events.stream()
                .map(AuditEvent::getSourceName)
                .distinct()
                .toList();

        if (distinctSources.size() != 1) {
            System.err.println("Detected multiple violation sources:");
            distinctSources.forEach(s -> System.err.println(" - " + s));

            System.err.println("Full violation messages:");
            events.forEach(e -> System.err.printf("[%s] %s%n", e.getSourceName(), e.getMessage()));
        }

        assertEquals(1, distinctSources.size(),
                () -> "Expected only one kind of violation, but got multiple sources: " + distinctSources);

        assertTrue(distinctSources.getFirst().contains(checkNameFragment),
                () -> String.format("Expected check '%s' but got '%s'",
                        checkNameFragment, distinctSources.getFirst()));

        events.forEach(e ->
                assertTrue(e.getMessage().toLowerCase().contains(messageFragment.toLowerCase()),
                        () -> String.format("Violation message should contain '%s', but got '%s'",
                                messageFragment, e.getMessage()))
        );
    }

    @ParameterizedTest(name = "Clean code: {0}")
    @CsvSource({
            "'src/test/resources/rules/success/UnusedImport.java'",
            "'src/test/resources/rules/success/UnusedLocalVariable.java'",
            "'src/test/resources/rules/success/IndentationCheck.java'",
            "'src/test/resources/rules/success/WhitespaceAround.java'",
            "'src/test/resources/rules/success/NoWhitespaceBefore.java'",
            "'src/test/resources/rules/success/GenericWhitespace.java'",
            "'src/test/resources/rules/success/UnnecessaryBlankLine.java'",
            "'src/test/resources/rules/success/EmptyLineSeparatorCheck.java'",
            "'src/test/resources/rules/success/TypeName.java'",
            "'src/test/resources/rules/success/MethodName.java'",
            "'src/test/resources/rules/success/LocalVariableName.java'",
            "'src/test/resources/rules/success/ConstantName.java'",
            "'src/test/resources/rules/success/SystemOutUsage.java'",
            "'src/test/resources/rules/success/SingleLineCommentCheck.java'",
            "'src/test/resources/rules/success/LineLengthCheck.java'",
            "'src/test/resources/rules/success/NeedBracesCheck.java'",
            "'src/test/resources/rules/success/SystemErrUsage.java'",
    })
    void shouldPassForCleanCode(String filePath) throws Exception {
        List<AuditEvent> events = runCheckstyle(filePath);
        assertTrue(events.isEmpty(),
                () -> "Expected no violations for " + filePath + ", but got:\n" +
                        events.stream()
                                .map(e -> String.format("Line %d:%d - %s [%s]",
                                        e.getLine(), e.getColumn(), e.getMessage(), e.getSourceName()))
                                .collect(Collectors.joining("\n"))
        );
    }

    @Test
    void shouldReportAllViolationsInSingleFile() throws Exception {
        String filePath = "src/test/resources/rules/fail/MultiErrorTest.java";
        List<AuditEvent> events = runCheckstyle(filePath);

        assertFalse(events.isEmpty(),
                () -> "Expected violations in " + filePath + " but found none.");

        List<String> messages = events.stream()
                .map(AuditEvent::getMessage)
                .toList();

        List<String> expectedFragments = List.of(
                "Unused import",
                "Unused named local variable",
                "incorrect indentation level",
                "is not preceded with whitespace.",
                "is not followed by whitespace",
                "is preceded with whitespace.",
                "GenericWhitespace",
                "Unnecessary blank line",
                "more than 1 empty line",
                "must match pattern",
                "System.out is forbidden",
                "System.err is forbidden",
                "Single-line comments are forbidden",
                "Line is longer than 120 characters",
                "construct must use '{}'"
        );

        List<String> missing = expectedFragments.stream()
                .filter(fragment -> messages.stream().noneMatch(msg -> msg.contains(fragment)))
                .toList();

        assertTrue(missing.isEmpty(),
                () -> "Missing expected violations: " + missing);

        List<AuditEvent> unexpectedEvents = events.stream()
                .filter(event -> expectedFragments.stream()
                        .noneMatch(fragment -> event.getMessage().contains(fragment)))
                .toList();

        List<String> unexpected = unexpectedEvents.stream()
                .map(event -> event.getSourceName() + ": " + event.getMessage())
                .toList();

        assertTrue(unexpected.isEmpty(),
                () -> "Unexpected violations: " + unexpected);
    }

    private List<AuditEvent> runCheckstyle(String filePath) throws Exception {
        Configuration config;
        try (FileInputStream configStream = new FileInputStream("checkstyle.xml")) {
            config = ConfigurationLoader.loadConfiguration(
                    new InputSource(configStream),
                    new PropertiesExpander(new Properties()),
                    ConfigurationLoader.IgnoredModulesOptions.EXECUTE
            );
        }

        Checker checker = new Checker();
        checker.setModuleClassLoader(Checker.class.getClassLoader());

        List<AuditEvent> events = new LinkedList<>();
        checker.addListener(new com.puppycrawl.tools.checkstyle.api.AuditListener() {
            @Override
            public void auditStarted(AuditEvent e) {
            }

            @Override
            public void auditFinished(AuditEvent e) {
            }

            @Override
            public void fileStarted(AuditEvent e) {
            }

            public void fileFinished(AuditEvent e) {
            }

            @Override
            public void addError(AuditEvent e) {
                events.add(e);
            }

            @Override
            public void addException(AuditEvent e, Throwable t) {
            }
        });

        checker.configure(config);
        checker.process(Collections.singletonList(new File(filePath)));
        checker.destroy();

        return events;
    }
}
