package vidya;

import static org.lwjgl.glfw.GLFW.*;

public class Client {
    public Config config = new Config();
    public Window window = new Window();

    double time;
    double accumulator;
    double pauseDelay;
    double inactiveTime;

    public void load() {
        window.load(config);
    }

    public void unload() {
        window.unload();
    }

    public void update() {
        if (active()) {
            updateActive();
        } else {
            updatePaused();
        }
        render();
    }

    /* Step the client forward by one logic frame, and consume any input */
    public void step() {
        // input.step()
        // state.step()
    }

    public void render() {
        window.render();
    }

    public void run() {
        time = glfwGetTime();

        while (!window.shouldClose()) {
            update();
        }
    }

    /* Step the client while it's in the paused state */
    private void updatePaused() {
        glfwWaitEvents(); /* Wait for an event before continuing */

        time = glfwGetTime();
        inactiveTime = 0;
    }

    /* Step the client while it's in the active state */
    private void updateActive() {
        glfwPollEvents();

        double t = glfwGetTime();
        double dt = t - time;

        time = t;

        if (!window.focused()) {
            inactiveTime += dt;
        }

        double step = 1/60.f;

        accumulator += dt;
        while (accumulator > step) {
            step();
            accumulator -= step;
        }
    }

    /* Return true if the client is active and should run the game */
    private boolean active() {
        return window.focused() || inactiveTime < pauseDelay;
    }
}
