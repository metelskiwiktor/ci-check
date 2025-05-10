package rules.fail;

public class LocalVariableName {
    public void method() {
        String BadVariable = "Bad World";
        BadVariable.getBytes();
    }
}