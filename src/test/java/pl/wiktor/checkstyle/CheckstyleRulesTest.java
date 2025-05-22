package pl.wiktor.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

/**
 * Refactored test class for Checkstyle rules verification.
 */
class CheckstyleRulesTest {
    @ParameterizedTest(name = "Violation for {0}: {2}")
    @MethodSource("provideFailRules")
    void shouldDetectOnlyOneKindOfViolation_whenSingleRuleFails (String filePath, String checkNameFragment,
                                            String messageFragment) throws Exception {
        List<AuditEvent> events = CheckstyleTestRunner.runCheckstyle(filePath);
        CheckstyleTestAssertions.assertSingleViolationType(events, filePath,
                checkNameFragment, messageFragment);
    }

    @ParameterizedTest(name = "Clean code: {0}")
    @MethodSource("provideSuccessRules")
    void shouldPassForCleanCode(String filePath) throws Exception {
        List<AuditEvent> events = CheckstyleTestRunner.runCheckstyle(filePath);
        CheckstyleTestAssertions.assertNoViolations(events, filePath);
    }

    @Test
    void shouldReportAllViolationsInSingleFile() throws Exception {
        String filePath = "src/test/resources/rules/fail/MultiErrorTest.java";
        List<AuditEvent> events = CheckstyleTestRunner.runCheckstyle(filePath);
        List<String> expectedFragments = CheckstyleTestConfig.getExpectedViolationMessages();

        CheckstyleTestAssertions.assertExpectedViolations(events, expectedFragments);
    }

    private static Stream<Arguments> provideFailRules() {
        return CheckstyleTestConfig.getAllRules().stream()
                .map(rule -> Arguments.of(
                        rule.failTestFile(),
                        rule.checkNameFragment(),
                        rule.messageFragment()
                ));
    }

    private static Stream<String> provideSuccessRules() {
        return CheckstyleTestConfig.getAllRules().stream()
                .map(CheckstyleRule::successTestFile);
    }
}
