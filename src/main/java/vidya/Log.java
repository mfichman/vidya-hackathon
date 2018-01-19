package vidya;

public class Log {
    public static void error(String msg, Object... args) {
        System.out.printf("ERROR: "+msg+"\n", args);
        System.out.flush();
    }

    public static void info(String msg, Object... args) {
        System.out.printf("INFO: "+msg+"\n", args);
        System.out.flush();
    }
}
