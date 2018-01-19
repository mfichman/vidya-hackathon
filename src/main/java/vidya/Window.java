package vidya;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import org.joml.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    private static final int GL_VERSION_MINOR = 3;
    private static final int GL_VERSION_MAJOR = 3;

    private long window = 0;
    private long monitor = 0;

    public enum Mode {
        WINDOWED,
        BORDERLESS,
        FULLSCREEN
    }

    /* Loads the client window */
    public void load(Config config) {
        if (window != 0) { return; }

        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            Log.error("failed to initialize glfw");
            System.exit(1);
        }

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, GL_VERSION_MAJOR);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, GL_VERSION_MINOR);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, 1);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_SAMPLES, config.windowSamples);
        glfwWindowHint(GLFW_DEPTH_BITS, 32);
        glfwWindowHint(GLFW_SRGB_CAPABLE, 1);
        glfwWindowHint(GLFW_RESIZABLE, 0);

        monitor = monitor(config);

        switch (config.windowMode) {
            case WINDOWED:
                loadWindowed(config);
                break;
            case FULLSCREEN:
                loadFullscreen(config);
                break;
            case BORDERLESS:
            default:
                loadBorderless(config);
                break;
        }

        glfwMakeContextCurrent(window);
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
        glfwSwapInterval(config.windowSwapInterval);
        //glfwSetWindowUserPointer(window, this);

        loadGl();
    }

    /* Swap the window back buffers */
    public void render() {
        glfwSwapBuffers(window);
    }

    /* Unload the window */
    public void unload() {
        if (window == 0) { return; }

        glfwDestroyWindow(window);
        window = 0;
    }

    public boolean focused() {
        return glfwGetWindowAttrib(window, GLFW_FOCUSED) != 0;
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public Vector2f viewport() {
        try (MemoryStack stack = stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            glfwGetFramebufferSize(window, width, height);
            return new Vector2f(width.get(0), height.get(0));
        }
    }

    /* Get the monitor from the user's config settings */
    private long monitor(Config config) {
        int i = config.windowMonitor;

        PointerBuffer monitors = glfwGetMonitors();
        return (i < monitors.limit() && i >= 0) ? monitors.get(i) : monitors.get(0);
    }

    /* Load OpenGL and check the version */
    private void loadGl() {
        GL.createCapabilities();

        if (!GL.getCapabilities().OpenGL33) {
            Log.error("this program requires OpenGL 3.3");
            System.exit(1);
        }

        Log.info("opengl vendor: %s", glGetString(GL_VENDOR));
        Log.info("opengl version: %s", glGetString(GL_VERSION));
        Log.info("opengl renderer: %s", glGetString(GL_RENDERER));
    }

    /* Center the window within its monitor */
    private void center() {
        GLFWVidMode vidmode = glfwGetVideoMode(monitor);

        try (MemoryStack stack = stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer x = stack.mallocInt(1);
            IntBuffer y = stack.mallocInt(1);

            glfwGetWindowSize(window, width, height);
            glfwGetMonitorPos(monitor, x, y);

            int centerX = x.get(0) + (vidmode.width() - width.get(0))/2;
            int centerY = y.get(0) + (vidmode.height() - height.get(0))/2;

            glfwSetWindowPos(window, centerX, centerY);
        }
    }

    /* Load a windowed window */
    private void loadWindowed(Config config) {
        GLFWVidMode vidmode = glfwGetVideoMode(monitor);

        int width = config.windowWidth;
        int height = config.windowHeight;

        if (width <= 0) { width = 2*vidmode.width()/3; }
        if (height <= 0) { height = 2*vidmode.height()/3; }

        glfwWindowHint(GLFW_VISIBLE, 0);

        window = glfwCreateWindow(width, height, "", 0, 0);

        if (window == 0) {
            Log.error("failed to create game window");
            System.exit(1);
        }

        center();

        glfwShowWindow(window);
    }

    /* Load a fullscreen window */
    private void loadFullscreen(Config config) {
        GLFWVidMode vidmode = glfwGetVideoMode(monitor);

        Log.info("Here");

        int width = config.windowWidth;
        int height = config.windowHeight;

        if (width <= 0) { width = vidmode.width(); }
        if (height <= 0) { height = vidmode.height(); }

        window = glfwCreateWindow(width, height, "", monitor, 0);

        if (window == 0) {
            Log.error("failed to create game window");
            System.exit(1);
        }
    }

    /* Load a borderless window */
    private void loadBorderless(Config config) {
        GLFWVidMode vidmode = glfwGetVideoMode(monitor);

        int width = config.windowWidth;
        int height = config.windowHeight;

        if (width <= 0) { width = vidmode.width(); }
        if (height <= 0) { height = vidmode.height(); }

        glfwWindowHint(GLFW_VISIBLE, 0);
        glfwWindowHint(GLFW_DECORATED, 0); /* Disable window border */

        window = glfwCreateWindow(width, height, "", 0, 0);

        if (window == 0) {
            Log.error("failed to create game window");
            System.exit(1);
        }

        center();

        glfwShowWindow(window);
    }
}
