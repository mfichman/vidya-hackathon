package vidya.model;

import java.lang.Math;

public class DroneLaser {
    public final Movable movable;
    public final double initXPos;
    public final double initYPos;
    public final double range;
    public final int damage;
    public final Color color;
    public boolean alive = true;

    public DroneLaser(Movable movable, double range, int damage, Color color) {
        this.movable = movable;
        this.initXPos = movable.xPos;
        this.initYPos = movable.yPos;
        this.range = range;
        this.damage = damage;
        this.color = color;
    }

    public double distanceTraveled() {
        double deltaX = movable.xPos - initXPos;
        double deltaY = movable.yPos - initYPos;
        return Math.sqrt(deltaX*deltaX + deltaY*deltaY);
    }

    public void update(vidya.State state) {
        if(distanceTraveled() >= range) {
            kill();
        }
        movable.move();
    }

    public void kill() {
        alive = false;
    }
}
