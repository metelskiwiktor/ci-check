package rules.fail;

import java.util.stream.Stream;

public class NoWhitespaceBefore {
    public void method() {
        Stream .builder().build();
    }
}
