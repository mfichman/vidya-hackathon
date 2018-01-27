package vidya.graphics;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;
import vidya.math.Transform;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.glDrawElementsBaseVertex;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Renderer {
    public final Uniforms transformBuffer = new Uniforms(Uniforms.Binding.TRANSFORM);
    public final Uniforms lightBuffer = new Uniforms(Uniforms.Binding.LIGHT);

    public Renderer() {
        glEnable(GL_FRAMEBUFFER_SRGB);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
    }

    public void render(Scene scene) {
        updateLight(scene.light);

        final int mode = GL_TRIANGLES;
        final int type = GL_UNSIGNED_INT;
        final int base = 0;
        final int indices = 0;

        for (Draw draw : scene.draw) {
            updateTransform(draw.transform);
            draw.uniforms.bind();
            draw.vertices.bind();
            draw.shader.bind();
            glDrawElementsBaseVertex(mode, draw.vertices.indices(), type, indices, base);
        }

    }

    private void updateLight(Light light) {
        try (MemoryStack stack = stackPush()) {
            ByteBuffer buffer = stack.malloc(1024);
            light.diffuse.get(buffer);
            light.specular.get(buffer);
            light.ambient.get(buffer);
            light.direction.get(buffer);

        }
    }

    private void updateTransform(Transform transform) {
        Matrix4f worldMatrix = new Matrix4f();
        try (MemoryStack stack = stackPush()) {
            ByteBuffer buffer = stack.malloc(1024);
            worldMatrix.rotate(transform.rotation);
            worldMatrix.translate(transform.position);
            worldMatrix.get(buffer);
        }
    }

}
