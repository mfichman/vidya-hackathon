package vidya.math;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.nio.FloatBuffer;

public class Transform {
    public final Quaternionf rotation = new Quaternionf().identity();
    public final Vector3f position = new Vector3f();

    public void put(FloatBuffer buffer) {
        buffer.put(rotation.x);
        buffer.put(rotation.y);
        buffer.put(rotation.z);
        buffer.put(position.x);
        buffer.put(position.y);
        buffer.put(position.z);
    }
}
