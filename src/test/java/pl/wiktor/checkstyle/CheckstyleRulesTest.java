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
    @ParameterizedTest(name = "Violation: {0}")
    @CsvSource({
            "'src/test/resources/rules/fail/UnusedImport.java', UnusedImports, Unused import",
            "'src/test/resources/rules/fail/CustomImportOrderCheck.java', CustomImportOrderCheck, Extra separation in import",
            "'src/test/resources/rules/fail/UnusedLocalVariable.java', UnusedLocalVariable, Unused named local variable",
            "'src/test/resources/rules/fail/IndentationCheck.java', IndentationCheck, incorrect indentation level",
            "'src/test/resources/rules/fail/WhitespaceAround.java', WhitespaceAround, is not preceded with whitespace.",
            "'src/test/resources/rules/fail/NoWhitespaceBefore.java', NoWhitespaceBefore, is preceded with whitespace.",
            "'src/test/resources/rules/fail/GenericWhitespace.java', GenericWhitespace, is preceded with whitespace",
    })
    void shouldFailForViolations(String filePath, String checkNameFragment, String messageFragment) throws Exception {
        List<AuditEvent> events = runCheckstyle(filePath);
        assertEquals(1, events.size(),
                () -> "Expected exactly one violation for " + filePath + ", but got:\n" +
                        events.stream()
                                .map(e -> String.format("Line %d:%d - %s [%s]",
                                        e.getLine(),
                                        e.getColumn(),
                                        e.getMessage(),
                                        e.getSourceName()))
                                .collect(Collectors.joining("\n")));

        AuditEvent event = events.getFirst();
        assertTrue(event.getSourceName().contains(checkNameFragment),
                () -> String.format("Expected check '%s' but got: '%s'", checkNameFragment, event.getSourceName()));
        assertTrue(event.getMessage().toLowerCase().contains(messageFragment.toLowerCase()), () ->
                String.format("Violation message should contain '%s', but got '%s'",
                        messageFragment, event.getMessage()));
    }


    @ParameterizedTest(name = "Clean code: {0}")
    @CsvSource({
            "'src/test/resources/rules/success/UnusedImport.java'",
            "'src/test/resources/rules/success/CustomImportOrderCheck.java'",
            "'src/test/resources/rules/success/UnusedLocalVariable.java'",
            "'src/test/resources/rules/success/IndentationCheck.java'",
            "'src/test/resources/rules/success/WhitespaceAround.java'",
            "'src/test/resources/rules/success/NoWhitespaceBefore.java'",
            "'src/test/resources/rules/success/GenericWhitespace.java'"
    })
    void shouldPassForCleanCode(String filePath) throws Exception {
        List<AuditEvent> events = runCheckstyle(filePath);

        assertTrue(events.isEmpty(), () ->
                "Code should pass without errors for " + filePath + ", but got:\n" +
                        events.stream()
                                .map(e -> String.format("Line %d:%d - %s [%s]",
                                        e.getLine(),
                                        e.getColumn(),
                                        e.getMessage(),
                                        e.getSourceName()))
                                .collect(Collectors.joining("\n"))
        );
    }

    private List<AuditEvent> runCheckstyle(String filePath) throws Exception {
        File configFile = new File("checkstyle.xml");
        try (FileInputStream configStream = new FileInputStream(configFile)) {
            Configuration config = ConfigurationLoader.loadConfiguration(
                    new InputSource(configStream),
                    new PropertiesExpander(new Properties()),
                    ConfigurationLoader.IgnoredModulesOptions.EXECUTE
            );

            Checker checker = new Checker();
            checker.setModuleClassLoader(Checker.class.getClassLoader());

            List<AuditEvent> events = new LinkedList<>();
            checker.addListener(new com.puppycrawl.tools.checkstyle.api.AuditListener() {
                public void auditStarted(AuditEvent e) {
                }

                public void auditFinished(AuditEvent e) {
                }

                public void fileStarted(AuditEvent e) {
                }

                public void fileFinished(AuditEvent e) {
                }

                public void addError(AuditEvent e) {
                    events.add(e);
                }

                public void addException(AuditEvent e, Throwable throwable) {
                }
            });

            checker.configure(config);
            checker.process(Collections.singletonList(new File(filePath)));
            checker.destroy();
            return events;
        }
    }
}
