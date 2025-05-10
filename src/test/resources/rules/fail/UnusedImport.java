package rules.fail;

import java.util.Date;

public class UnusedImport {
    public void print() {
        String hw = "Hello World";
        hw.getBytes();
    }
}
