package vidya;

public class Config {
    public int windowMonitor;
    public int windowSamples;
    public int windowWidth;
    public int windowHeight;
    public int windowSwapInterval;

    public Window.Mode windowMode = Window.Mode.WINDOWED;

    public boolean debugIde;

    public void load() {
        // Load this from a file or something
    }
}
