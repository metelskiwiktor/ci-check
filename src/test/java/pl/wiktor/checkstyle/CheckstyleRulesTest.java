package pl.wiktor.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CheckstyleRulesTest {

    @ParameterizedTest(name = "Violation for {0}: {1}")
    @CsvSource({
            "'src/test/resources/rules/fail/UnusedImport.java',          UnusedImports,             Unused import",
            "'src/test/resources/rules/fail/CustomImportOrderCheck.java', CustomImportOrderCheck, Extra separation in import",
            "'src/test/resources/rules/fail/UnusedLocalVariable.java',  UnusedLocalVariable,       Unused named local variable",
            "'src/test/resources/rules/fail/IndentationCheck.java',     IndentationCheck,          incorrect indentation level",
            "'src/test/resources/rules/fail/WhitespaceAround.java',      WhitespaceAround,          is not preceded with whitespace.",
            "'src/test/resources/rules/fail/NoWhitespaceBefore.java',    NoWhitespaceBefore,        is preceded with whitespace.",
            "'src/test/resources/rules/fail/GenericWhitespace.java',     GenericWhitespace,         is preceded with whitespace"
    })
    void shouldDetectOnlyOneKindOfViolation(String filePath,
                                            String checkNameFragment,
                                            String messageFragment) throws Exception {
        List<AuditEvent> events = runCheckstyle(filePath);

        // 1) Co najmniej jeden błąd
        assertFalse(events.isEmpty(),
                () -> "Expected at least one violation for " + filePath + ", but got none.");

        // 2) Wszystkie błędy są z tego samego checku
        List<String> distinctSources = events.stream()
                .map(AuditEvent::getSourceName)
                .distinct()
                .toList();

        assertEquals(1, distinctSources.size(),
                () -> "Expected only one kind of violation, but got multiple sources: " + distinctSources);

        assertTrue(distinctSources.getFirst().contains(checkNameFragment),
                () -> String.format("Expected check '%s' but got '%s'",
                        checkNameFragment, distinctSources.getFirst()));

        // 3) Każdy komunikat zawiera oczekiwany fragment
        events.forEach(e ->
                assertTrue(e.getMessage().toLowerCase().contains(messageFragment.toLowerCase()),
                        () -> String.format("Violation message should contain '%s', but got '%s'",
                                messageFragment, e.getMessage()))
        );
    }

    @ParameterizedTest(name = "Clean code: {0}")
    @CsvSource({
            "'src/test/resources/rules/success/UnusedImport.java'",
            "'src/test/resources/rules/success/CustomImportOrderCheck.java'",
            "'src/test/resources/rules/success/UnusedLocalVariable.java'",
            "'src/test/resources/rules/success/IndentationCheck.java'",
            "'src/test/resources/rules/success/WhitespaceAround.java'",
            "'src/test/resources/rules/success/NoWhitespaceBefore.java'",
            "'src/test/resources/rules/success/GenericWhitespace.java'",
            "'src/test/resources/rules/success/UnnecessaryBlankLine.java'"
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
