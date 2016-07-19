package example.perf.util;

import java.io.PrintStream;

/**
 * Created by jackeylv on 2016/6/9.
 */
public class Printer {
    private PrintStream out;
    private String head;
    public Printer(PrintStream out, String className) {
        head = "<"+ Thread.currentThread().getName()+ "> [" + className+"]";
        this.out = out;
    }


    public void println(String msg) {
        out.println(head + ":"+System.currentTimeMillis() + ":" + msg);
    }
}
