package rules.fail;

public class BadVariableName {
    public void method() {
        int BadVariable = 42;
        System.out.println(BadVariable);
    }
}
