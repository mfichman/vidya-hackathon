package vidya.graphics;

import org.joml.*;

public class Camera {
    public final Vector2f viewport = new Vector2f();

    public float left = 0;
    public float right = 1000;

    public float top = 1000;
    public float bottom = 0;

    public float near = -100;
    public float far = 100;

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
