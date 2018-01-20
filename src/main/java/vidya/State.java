package vidya;

import java.util.ArrayList;

/* Contains the top-level state of the game world. Lists of enemies, gameplay
 * objects, obstacles, etc. This class is updated once per logic step. */
public class State {
    public final ArrayList<Object> objects = new ArrayList<Object>();

    public void step() {
        objects.removeIf((object) -> {

            return true;
        });

    }
}
