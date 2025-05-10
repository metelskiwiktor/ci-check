package rules.success;

public class LocalVariableName {
    public void method() {
        String goodVariable = "Good World";
        goodVariable.isEmpty();
    }
}