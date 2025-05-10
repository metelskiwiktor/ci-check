package rules.fail;

import java.util.List;

public class GenericWhitespace {
    public void method() {
        List <String> list = null;
        list.stream();
    }
}
