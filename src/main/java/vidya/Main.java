package vidya;

import vidya.graphics.Model;
import vidya.graphics.Shader;

public class Main {
    public static void main(String[] args) {
        Client client = new Client();

        client.load();
        client.run();
    }
}
