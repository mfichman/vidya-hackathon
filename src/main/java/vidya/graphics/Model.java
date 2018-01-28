package vidya.graphics;


import com.google.common.io.Resources;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.system.MemoryStack;
import vidya.Asset;
import vidya.math.Transform;

import java.io.File;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.assimp.Assimp.aiImportFile;
import static org.lwjgl.assimp.Assimp.aiProcess_JoinIdenticalVertices;
import static org.lwjgl.assimp.Assimp.aiProcess_Triangulate;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.system.MemoryStack.stackPush;
import static vidya.graphics.Model.Mesh.FORMAT;

public class Model {

    public final String name;
    public final AIScene data;
    public final ArrayList<Mesh> mesh = new ArrayList<Mesh>();

    public static class Material {
        public final Vector3f color;
        public final Uniforms uniforms = new Uniforms(Uniforms.Binding.MATERIAL);

        public Material(Vector3f color) {
           this.color = color;

           try (MemoryStack stack = stackPush()) {
               ByteBuffer buffer = stack.malloc(3 * Float.BYTES);
               color.get(buffer);
               uniforms.update(buffer);
           }
        }
    }

    public static class Mesh {

        public static Vertices.Format FORMAT = new Vertices.Format(
                new Vertices.FormatAttr(GL_FLOAT, 3)
        );

        public final Vertices vertices = new Vertices();
    }

    public Model(String name) {
        this.name = name;

        URL url = Resources.getResource(this.name);
        File file = new File(url.getPath());
        final int flags = aiProcess_JoinIdenticalVertices | aiProcess_Triangulate;

        this.data = aiImportFile(file.getAbsolutePath(), flags);

        for (int i = 0; i < this.data.mNumMeshes(); ++i) {
            AIMesh aiMesh = AIMesh.create(this.data.mMeshes().get(i));
            Mesh mesh = new Mesh();


            Vector3f sum = new Vector3f();
            aiMesh.mVertices().forEach((vector) -> {
                sum.x += vector.x();
                sum.y += vector.y();
                sum.z += vector.z();

            });

            Vector3f average = sum.mul(1.0f/aiMesh.mNumVertices());

            ByteBuffer vertex = BufferUtils.createByteBuffer(aiMesh.mNumVertices() * FORMAT.size());
            aiMesh.mVertices().forEach((vector) -> {
                vertex.putFloat(vector.x() - average.x);

                // Flip Y and Z to match export from Cinema4D
                vertex.putFloat(vector.z() - average.z);
                vertex.putFloat(vector.y() - average.y);
            });
            vertex.flip();

            ByteBuffer index = BufferUtils.createByteBuffer(aiMesh.mNumFaces() * 3 * Integer.BYTES);
            aiMesh.mFaces().forEach((face) -> {
                if (face.mNumIndices() != 3) {
                    throw new RuntimeException("invalid number of indices per face");
                }
                index.putInt(face.mIndices().get(0));
                index.putInt(face.mIndices().get(1));
                index.putInt(face.mIndices().get(2));
            });
            index.flip();

            mesh.vertices.update(FORMAT, vertex, index);
            this.mesh.add(mesh);
        }
    }

    public void render(Scene scene, Transform transform, Material material) {
        for (Mesh mesh : this.mesh) {
            Draw draw = new Draw();
            draw.vertices = mesh.vertices;
            draw.shader = Asset.meshShader;
            draw.transform = transform;
            draw.uniforms = material.uniforms;
            scene.submit(draw);
        }

    }

}
