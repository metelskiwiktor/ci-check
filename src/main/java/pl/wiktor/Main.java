package pl.wiktor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class Main {
    public static void main(String[] args) {
        String a = "as";

        if (a.equals("as")) {
        } else {
        }

        try (BufferedReader reader = new BufferedReader(new StringReader("line1\nline2"));
             StringReader stringReader = new StringReader("ignored")) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Read: " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void test() {
        String a = "as";

        //comment
        if (a.equals("as")) {
            System.out.println("sout");
        } else {
        }

        if (a.equals("ase")) return;
    }
}
