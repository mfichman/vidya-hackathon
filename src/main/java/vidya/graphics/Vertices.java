package vidya.graphics;

import jdk.jshell.spi.ExecutionControl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

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
                case GL_FLOAT: return 4;
                default: throw new RuntimeException("invalid format attr type");
            }
        }
    }

    public static class Format {
        public final ArrayList<FormatAttr> attr = new ArrayList();

        public int size() {
           return attr.stream().mapToInt((attr) -> { return attr.count; }).sum();
        }
    }

    public static class DefaultFormat extends Format {
        public DefaultFormat() {
            attr.add(new FormatAttr(GL_FLOAT, 3));
            attr.add(new FormatAttr(GL_FLOAT, 2));
            attr.add(new FormatAttr(GL_FLOAT, 3));
        }
    }

    public Vertices(Format format) {
        glBindVertexArray(this.id);

        final int stride = format.size();
        int index = 0;
        int offset = 0;

        for (FormatAttr attr : format.attr) {
            final boolean normalized = true;
            glEnableVertexAttribArray(index);
            glVertexAttribPointer(index, attr.count, attr.type, normalized, stride, offset);
            offset += attr.size();
        }

        vertexBuffer.bind();
        indexBuffer.bind();

        glBindVertexArray(0);
    }

    public void update(ByteBuffer vertices, ByteBuffer indices) {
        vertexBuffer.update(vertices);
        indexBuffer.update(indices);
    }

    public void bind() {
        glBindVertexArray(this.id);
    }

    public int indices() {
        final int sizeOfInt = 4;
        return indexBuffer.size() / sizeOfInt;
    }
}
