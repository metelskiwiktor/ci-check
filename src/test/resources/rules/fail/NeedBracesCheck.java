package rules.fail;

public class NeedBracesCheck {
    public void method() {
        int x = 5;

        if (x > 0)
            x++;

        if (x < 10)
            x = x * 2;
        else
            x = x / 2;

        while (x > 0)
            x--;

        for (int i = 0; i < 5; i++)
            x += i;
    }
}
