package vidya.graphics;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL30.glBindBufferBase;
import static org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER;

public class Uniforms {
    public final Buffer buffer = new Buffer(GL_UNIFORM_BUFFER, GL_DYNAMIC_DRAW);
    public final Binding binding;

    public enum Binding {
        LIGHT,
        TRANSFORM,
        MATERIAL,
    }

    public Uniforms(Binding binding) {
        this.binding = binding;
    }

    public void update(ByteBuffer data) {
        this.buffer.update(data);
    }

    public void bind() {
        glBindBufferBase(GL_UNIFORM_BUFFER, this.binding.ordinal(), this.buffer.id);
        this.buffer.bind();
    }
}
