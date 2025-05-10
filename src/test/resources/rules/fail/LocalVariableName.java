package rules.fail;

public class LocalVariableName {
    public void method() {
        int BadVariable = 42;
        System.out.println(BadVariable);
    }
}