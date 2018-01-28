package vidya;

import org.joml.Vector3f;
import org.lwjgl.assimp.AIScene;
import vidya.graphics.Model;
import vidya.graphics.Shader;

import static org.lwjgl.assimp.Assimp.aiImportFile;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class Asset {

    public static Shader meshShader = new Shader(
            new Shader.Stage("vidya/shaders/Mesh.vert.glsl", GL_VERTEX_SHADER),
            new Shader.Stage("vidya/shaders/Mesh.frag.glsl", GL_FRAGMENT_SHADER)
    );

    public static Model droneFighterModel = new Model("vidya/models/DroneFighter.obj");

    public static Model.Material blue = new Model.Material(new Vector3f(1, 0, 0));
    public static Model.Material red = new Model.Material(new Vector3f(0, 0, 1));
}
