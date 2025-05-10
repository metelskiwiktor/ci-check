package rules.success;

import java.util.Date;
import java.util.List;

public class UnnecessaryBlankLine {
    public void printDate() {

        Date date = new Date();
        date.hashCode();

        List<String> list = null;
        list.stream();

    }

    public void anotherMethod() {
    }

}
