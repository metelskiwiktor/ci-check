package pl.wiktor.checkstyle;

import java.util.List;

/**
 * Configuration class containing all Checkstyle rules to be tested.
 */
public class CheckstyleTestConfig {
    private static final String FAIL_PATH = "src/test/resources/rules/fail/";
    private static final String SUCCESS_PATH = "src/test/resources/rules/success/";

    public static List<CheckstyleRule> getAllRules() {
        return List.of(
                createRule("UnusedImport", "UnusedImports", "Unused import"),
                createRule("UnusedLocalVariable", "UnusedLocalVariable", "Unused named local variable"),
                createRule("IndentationCheck", "IndentationCheck", "incorrect indentation level"),
                createRule("WhitespaceAround", "WhitespaceAround", "is not preceded with whitespace."),
                createRule("WhitespaceAroundCheck", "WhitespaceAroundCheck", "is not followed by whitespace"),
                createRule("NoWhitespaceBefore", "NoWhitespaceBefore", "is preceded with whitespace."),
                createRule("GenericWhitespace", "GenericWhitespace", "GenericWhitespace"),
                createRule("UnnecessaryBlankLine", "RegexpMultilineCheck", "Unnecessary blank line"),
                createRule("EmptyLineSeparatorCheck", "EmptyLineSeparatorCheck", "line"),
                createRule("TypeName", "TypeName", "must match pattern"),
                createRule("MethodName", "MethodName", "must match pattern"),
                createRule("LocalVariableName", "LocalVariableName", "must match pattern"),
                createRule("ConstantName", "ConstantName", "must match pattern"),
                createRule("SystemOutUsage", "RegexpSinglelineJava", "is forbidden"),
                createRule("SingleLineCommentCheck", "RegexpSinglelineJava", "Single-line comments are forbidden"),
                createRule("LineLengthCheck", "LineLength", "Line is longer than 120 characters"),
                createRule("NeedBracesCheck", "NeedBraces", "construct must use '{}'"),
                createRule("SystemErrUsage", "RegexpSinglelineJava", "is forbidden")
        );
    }

    public static List<String> getExpectedViolationMessages() {
        return getAllRules().stream()
                .map(CheckstyleRule::messageFragment)
                .distinct()
                .toList();
    }

    private static CheckstyleRule createRule(String fileName, String checkNameFragment, String messageFragment) {
        return new CheckstyleRule(
                FAIL_PATH + fileName + ".java",
                SUCCESS_PATH + fileName + ".java",
                checkNameFragment,
                messageFragment
        );
    }
}