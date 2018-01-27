package vidya.controls;

import vidya.State;
import vidya.Command.Code;
import vidya.Command;
import static org.lwjgl.glfw.GLFW.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import org.joml.Vector2f;


public class Input {
    public final int attackKey    = GLFW_KEY_A;
    public final int menuKey      = GLFW_KEY_ESCAPE;
    public final int upgradeKey   = GLFW_KEY_U;
    public final int buildKey     = GLFW_KEY_B;
    public final int moveUpKey    = GLFW_KEY_UP;
    public final int moveDownKey  = GLFW_KEY_DOWN;
    public final int moveLeftKey  = GLFW_KEY_LEFT;
    public final int moveRightKey = GLFW_KEY_DOWN;
    
    public Float last_left_x = new Float(0);
    public Float last_left_y = new Float(0);
    public Float last_right_x = new Float(0);
    public Float last_right_y = new Float(0);

    public void init() {

    }

    public void step(State state) {
        final long window = glfwGetCurrentContext();

        if (glfwGetKey(window, menuKey) == GLFW_PRESS) {
            state.commands.add(new Command(Command.Code.MENU));
        }

        // TODO ADD CONTROLLER SUPPORT
        for (int i = 0; i < 4; ++i) {
            if (!glfwJoystickPresent(i)) { continue; }
        
            ByteBuffer buttons = glfwGetJoystickButtons(i);
            if(glfwJoystickIsGamepad(i)){
                if(buttons.get(GLFW_GAMEPAD_BUTTON_START) == 1){
                    state.commands.add(new Command(Command.Code.MENU));
                }
                if(buttons.get(GLFW_GAMEPAD_BUTTON_B) == 1){
                    state.commands.add(new Command(Command.Code.CANCEL));
                }
                if(buttons.get(GLFW_GAMEPAD_BUTTON_A) == 1){
                    state.commands.add(new Command(Command.Code.INTERACT));
                }
                if(buttons.get(GLFW_GAMEPAD_BUTTON_Y) == 1){
                    state.commands.add(new Command(Command.Code.UPGRADE));
                }
                if(buttons.get(GLFW_GAMEPAD_BUTTON_X) == 1){
                    state.commands.add(new Command(Command.Code.BUILD));
                }

            } else {
                if (buttons.get(GLFW_JOYSTICK_1) == 1) {
                }
                if (buttons.get(GLFW_JOYSTICK_2) == 1) {
                }
            }

            FloatBuffer axes = glfwGetJoystickAxes(i);
            if(axes != null){
                if( !last_left_x.equals(axes.get(0)) || !last_left_y.equals(axes.get(1)) ){ //LEFT STICK X+Y Axis
                    state.commands.add(new Command(Command.Code.MOVE, new Vector2f(axes.get(0),axes.get(1))));
                    last_left_x = axes.get(0);
                    last_left_y = axes.get(1);
                }
                if( !last_right_x.equals(axes.get(3)) || !last_right_y.equals(axes.get(2)) ){ //RIGHT STICK X+Y Axis
                    state.commands.add(new Command(Command.Code.AIM, new Vector2f(axes.get(3),axes.get(2))));
                    last_right_x = axes.get(3);
                    last_right_y = axes.get(2);
                }
            }
        }
    }
}