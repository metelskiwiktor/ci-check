package rules.success;

import java.util.Date;
import java.util.List;

public class UnnecessaryBlankLine {
    public void printDate() {

        Date date = new Date();
        System.out.println(date);

        List<String> list = null;
        System.out.println(list);

    }

    public void anotherMethod() {
        System.out.println("This is another method");
    }

}
