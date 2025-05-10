package rules.fail;

public class SingleLineCommentCheck {
    public void method() {
        // This is a single-line comment that should be forbidden
        String code = "some code";
        code.isEmpty();
        
        // Another forbidden comment
        Integer value = 42;
        value.byteValue();
    }
}