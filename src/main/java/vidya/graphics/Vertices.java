package vidya.graphics;

import jdk.jshell.spi.ExecutionControl;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class Vertices {
    public final Buffer vertexBuffer = new Buffer(GL_ARRAY_BUFFER, GL_STATIC_DRAW);
    public final Buffer indexBuffer = new Buffer(GL_ELEMENT_ARRAY_BUFFER, GL_STATIC_DRAW);
    public final int id = glGenVertexArrays();

    public static class FormatAttr {
        public final int type;
        public final int count;

        public FormatAttr(int type, int count) {
            this.type = type;
            this.count = count;
        }

        public int size() {
            switch (type) {
                case GL_FLOAT: return Float.BYTES * count;
                default: throw new RuntimeException("invalid format attr type");
            }
        }
    }

    public static class Format {
        public final List<FormatAttr> attr;

        public Format(FormatAttr... attr) {
            this.attr = Arrays.asList(attr);
        }

        public int size() {
           return attr.stream().mapToInt((attr) -> { return attr.size(); }).sum();
        }
    }

    public void update(Format format, ByteBuffer vertices, ByteBuffer indices) {
        glBindVertexArray(this.id);

        final int stride = format.size();
        int index = 0;
        int offset = 0;

        vertexBuffer.update(vertices);
        vertexBuffer.bind();

        indexBuffer.update(indices);
        indexBuffer.bind();

        for (FormatAttr attr : format.attr) {
            final boolean normalized = true;
            glEnableVertexAttribArray(index);
            glVertexAttribPointer(index, attr.count, attr.type, normalized, stride, offset);
            offset += attr.size();
        }

        glBindVertexArray(0);
    }

    public void bind() {
        glBindVertexArray(this.id);
    }

    public int indices() {
        return indexBuffer.size() / Integer.BYTES;
    }
}
