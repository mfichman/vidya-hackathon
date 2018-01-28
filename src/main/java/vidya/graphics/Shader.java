package vidya.graphics;

import com.google.common.base.Charsets;
import vidya.Log;

import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL31.GL_INVALID_INDEX;
import static org.lwjgl.opengl.GL31.glGetUniformBlockIndex;
import static org.lwjgl.opengl.GL31.glUniformBlockBinding;

public class Shader {

    public final int id = glCreateProgram();

    public static class Stage {
        public final String name;
        public final String source;
        public final int kind;
        public final int id;

        public Stage(String name, int kind) {
            this.kind = kind;
            this.name = name;
            this.source = read();
            this.id = glCreateShader(kind);

            glShaderSource(this.id, this.source);
            glCompileShader(this.id);

            if (glGetShaderi(this.id, GL_COMPILE_STATUS) == 0) {
                Log.error("shader compile failed: %s", this.name);
                Log.error(glGetShaderInfoLog(this.id));
                throw new RuntimeException("shader compile failed: "+this.name);
            }
        }

        private String read() {
            try {
                URL url = Resources.getResource(this.name);
                return Resources.toString(url, Charsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException("shader not found: "+this.name, e);
            }
        }
    }

    public Shader(Stage... stages) {
        for (Stage stage : stages) {
            glAttachShader(this.id, stage.id);
        }

        glLinkProgram(this.id);

        if (glGetProgrami(this.id, GL_LINK_STATUS) == 0) {
            Log.error("shader link failed");
            Log.error(glGetProgramInfoLog(this.id));
            throw new RuntimeException("shader link failed");
        }

        final int transform = glGetUniformBlockIndex(this.id, "transformBlock");
        final int material = glGetUniformBlockIndex(this.id, "materialBlock");
        final int light = glGetUniformBlockIndex(this.id, "lightBlock");

        if (transform != GL_INVALID_INDEX) {
            glUniformBlockBinding(this.id, transform, Uniforms.Binding.TRANSFORM.ordinal());
        }
        if (material != GL_INVALID_INDEX) {
            glUniformBlockBinding(this.id, material, Uniforms.Binding.MATERIAL.ordinal());
        }
        if (light != GL_INVALID_INDEX) {
            glUniformBlockBinding(this.id, light, Uniforms.Binding.LIGHT.ordinal());
        }
    }

    public void bind() {
        glUseProgram(this.id);
    }
}
