package rules.fail;

public class LineLengthCheck {
    public void method() {
        String veryLongString = "This is a very long string that exceeds the 120 character limit and should trigger a checkstyle violation for line length";
        veryLongString.isEmpty();
    }
}