package pl.wiktor.checkstyle;

import java.util.*;
import java.util.List;

import java.io.File;

public class MultiErrorTest {
    public static final int not_correct_name = 42;

    public static void main(String[] args) {
        String Used = "used";
        List <String> unused = new ArrayList<>();

        if(Used .contains("")){
        System.out.println("test");
            System.err.println("error message");
        } else {
            Used = "test";
        }


        if (Used.equals("test"))
            Used = "tes2";

        // This is a forbidden single-line comment
        BadMethodName();

        String veryLongString = "This is a very long string that exceeds the 120 character limit and should trigger a checkstyle violation for line length test";
    }
    public static void BadMethodName() {
        System.out.println("Bad method name");
    }

    class bad_class_name {
        public void method() {
        }
    }

}