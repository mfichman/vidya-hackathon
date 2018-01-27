package vidya.graphics;

import org.joml.*;

public class Camera {
    public final Vector2f viewport = new Vector2f();
    public float left;
    public float right;
    public float top;
    public float bottom;
    public float near = -1;
    public float far = 1;

    public Matrix4f viewMatrix() {
        return new Matrix4f().identity();
    }

    public Matrix4f projMatrix() {
        return new Matrix4f().ortho(left, right, bottom, top, near, far);
    }

    public Matrix4f viewProjMatrix() {
        return viewMatrix().mul(projMatrix());
    }
}
