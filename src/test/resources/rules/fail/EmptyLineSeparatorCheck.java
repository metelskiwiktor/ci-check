package rules.fail;

public class EmptyLineSeparatorCheck {
    public void method() {
        String hw = "Hello World";

        hw = "Hello World2";


        hw.lines();
    }
}
