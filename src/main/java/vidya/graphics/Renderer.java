package vidya.graphics;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;
import vidya.Asset;
import vidya.math.Transform;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.glDrawElementsBaseVertex;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Renderer {
    private Uniforms transformBuffer;
    private Uniforms lightBuffer;

    public void render(Scene scene) {
        if (transformBuffer == null) {
            transformBuffer = new Uniforms(Uniforms.Binding.TRANSFORM);
        }
        if (lightBuffer == null) {
            lightBuffer = new Uniforms(Uniforms.Binding.LIGHT);
        }

        glEnable(GL_FRAMEBUFFER_SRGB);
        glEnable(GL_DEPTH_TEST);
        //glEnable(GL_CULL_FACE);
        glDepthFunc(GL_LEQUAL);
        glClearColor(0, 0, 0, 0);
        glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);

        updateLight(scene.light);

        for (Draw draw : scene.draw) {
            draw.vertices.bind();
            draw.shader.bind();

            updateTransform(scene, draw.transform);

            if (draw.uniforms != null) {
                draw.uniforms.bind();
            }

            final int count = draw.vertices.indices();
            glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_INT, 0L);
        }
        assert(glGetError()==GL_NO_ERROR);
        scene.draw.clear();
    }

    private void updateLight(Light light) {
        try (MemoryStack stack = stackPush()) {
            ByteBuffer buffer = stack.malloc(2048);
            light.diffuse.get(buffer);
            light.specular.get(buffer);
            light.ambient.get(buffer);
            light.direction.get(buffer);
            lightBuffer.update(buffer);
            lightBuffer.bind();
        }
    }

    private void updateTransform(Scene scene, Transform transform) {
        try (MemoryStack stack = stackPush()) {
            Matrix4f worldMatrix = new Matrix4f().translate(transform.position);
            //worldMatrix.rotate(transform.rotation)

            Matrix4f worldViewProjMatrix = scene.camera.viewProjMatrix().mul(worldMatrix);

            ByteBuffer buffer = stack.malloc(16 * 3 * Float.BYTES);
            worldViewProjMatrix.get(buffer);

            transformBuffer.update(buffer);
            transformBuffer.bind();
        }
    }

}
