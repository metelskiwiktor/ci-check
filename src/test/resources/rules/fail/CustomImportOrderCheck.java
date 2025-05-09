package rules.fail;

import java.util.Date;

import java.util.List;

public class CustomImportOrderCheck {
    public void printDate() {
        Date date = new Date();
        System.out.println(date);
        List<String> list = null;
        System.out.println(list);
    }
}
