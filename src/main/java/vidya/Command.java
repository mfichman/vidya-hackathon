package vidya;

import org.joml.Vector2f;

public class Command {
    public enum Code {
        ATTACK,
        SELECT,
        DESELECT,
        BUILD,
        OPTIONS,
        MENU,
        UPGRADE,
        REPAIR,
        TECH,
        CANCEL,
        INTERACT,
        MOVE,
        AIM,
        FIRE,
        RADIAL,
        ALTFIRE,
        SPECIAL
    }

    public final Code code;
    public final Vector2f value;
   // public final int playerId;

    public Command(Code code) {
        this.code = code;
        this.value = null;
        System.out.println("Key Pressed: "+code);
    }

    public Command(Code code, Vector2f value) {
        this.code = code;
        this.value = value;
        System.out.println("Key Pressed: "+code+" Value: "+value);
    }
}