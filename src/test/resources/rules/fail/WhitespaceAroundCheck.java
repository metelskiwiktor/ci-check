package rules.fail;

public class WhitespaceAroundCheck {
    public void method() {
        int x = 5;
        if(x > 0) {
            System.out.println("Positive");
        }
    }
}
