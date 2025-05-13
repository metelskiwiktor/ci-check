package rules.success;

public class EmptyLineSeparatorCheck {
    public void method() {
        String hw = "Hello World";

        hw.lines();

        hw.isEmpty();
    }

    public void anotherMethod() {
        String hw = "Hello World";
        hw.lines();
    }
}
