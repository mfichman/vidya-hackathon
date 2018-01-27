package vidya.graphics;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL15.*;

public class Buffer {
    public final int id;
    public final int target;
    public final int usage;
    private int size = 0;

    public Buffer(int target, int usage) {
        this.id = glGenBuffers();
        this.target = target;
        this.usage = usage;
    }

    public void update(ByteBuffer data) {
        glBindBuffer(this.target, this.id);
        glBufferData(this.target, data, this.usage);
        this.size = data.limit();
    }

    public void bind() {
        glBindBuffer(this.target, this.id);
    }

    public int size() {
        return this.size;
    }
}
