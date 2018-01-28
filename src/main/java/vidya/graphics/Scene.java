package vidya.graphics;

import java.util.ArrayList;

public class Scene {
    public final ArrayList<Draw> draw = new ArrayList<Draw>();
    public final Camera camera = new Camera();
    public final Light light = new Light();

    public void submit(Draw draw) {
        this.draw.add(draw);
    }
}
