package rules.fail;

public class WhitespaceAroundCheck {
    public void method() {
        Integer x = 5;
        if(x > 0) {
            x.byteValue();
        }
    }
}
