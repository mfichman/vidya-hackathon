package vidya;

import vidya.graphics.Shader;

public class Main {
    public static void main(String[] args) {
        Client client = new Client();

        client.load();
        Shader shader = Asset.meshShader;

        client.run();
    }
}
