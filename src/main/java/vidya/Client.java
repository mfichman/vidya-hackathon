package vidya;

import vidya.controls.Input;

import static org.lwjgl.glfw.GLFW.*;

/* Runs the game client. Loads a config, window and intitial game state,
 * processes input from controllers/mouse/keywobard, and then updates the game
 * state in realtime.
 */
public class Client {
    /* Configs, like window mode, vsync, etc */
    private Config config = new Config();
   
    private Input input = new Input();
    private State state = new State();

    /* Sets up window-related stuff, like centering it on the screen and
     * selecting fullscreen/windowed mode */
    public Window window = new Window();

    /* Time the current frame began */
    private double time;

    /* Time accumulated since the last physics update, plus any remainder.
     * Physics updates happen in discrete steps, and it's possible for the
     * delta between frames to be a bit more or a bit less than a full physics
     * timestep. When this happens, the remainder is stored here. This prevents
     * animation hitching */
    private double accumulator;

    /* Amount of time that passes before the game is paused when the window is
     * inactive */
    private double pauseDelay;

    /* Total time the window has been inactive */
    private double inactiveTime;

    /* Loads config, creates a window, and initializes game state */
    public void load() {
        config.load();
        window.load(config);
    }

    /* Closes the game and game window */
    public void unload() {
        window.unload();
    }

    /* Run the game client until exit */
    public void run() {
        time = glfwGetTime();

        while (!window.shouldClose()) {
            update();
        }
    }

    /* Called once per frame to execute physics updates, then render */
    private void update() {
        if (active()) {
            updateActive();
        } else {
            updatePaused();
        }
        render();
    }

    /* Step the client forward by one logic frame, and consume any input */
    private void step() {
        input.step(state);
    }

    /* Render one frame */
    private void render() {
        window.render();
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
