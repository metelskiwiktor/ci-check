package rules.success;

public class UnusedLocalVariable {
    public void method() {
        String used = "used";
        used.isEmpty();
    }
}
