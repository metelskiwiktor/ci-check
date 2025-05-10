package pl.wiktor.checkstyle;

import java.util.*;
import java.util.List;

import java.io.File;

public class MultiErrorTest {
    public static void main(String[] args) {
        String used = "used";
        List <String> unused = 0;

        if(used .contains("")){
        System.out.println("test");
        } else {
            used = "test";
        }


        if (used)
            used = "tes2";
    }

}
