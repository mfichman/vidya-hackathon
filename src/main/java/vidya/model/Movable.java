package vidya.model;

public class Movable {
    public static final float MIN_X = 0;
    public static final float MAX_X = 1000;
    public static final float MIN_Y = 0;
    public static final float MAX_Y = 1000;

    public double xPos; // X position (meters)
    public double yPos; // Y position (meters)
    public double xVel; // X velocity (meters/second)
    public double yVel; // Y velocity (meters/second)

    public Movable() {
        xPos = Movable.MIN_X;
        yPos = Movable.MIN_Y;
        xVel = 0;
        yVel = 0;
    }

    public String toString() {
        return "X:" + Double.toString(xPos) + ", Y:" + Double.toString(yPos);
    }

    public Movable(double xPos, double yPos, double xVel, double yVel) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.xVel = xVel;
        this.yVel = yVel;
    }

    public Movable copy() {
        return new Movable(xPos, yPos, xVel, yVel);
    }

    public void move() {
        // Convert from per second to per frame
        double xVelFrame = xVel/60;
        double yVelFrame = yVel/60;

        // Update X
        xPos += xVelFrame;
        if(xPos < MIN_X) {
            xPos = MIN_X;
        }
        if(xPos > MAX_X) {
            xPos = MAX_X;
        }

        // Update Y
        yPos += yVelFrame;
        if(yPos < MIN_Y) {
            yPos = MIN_Y;
        }
        if(yPos > MAX_Y) {
            yPos = MAX_Y;
        }
    }
}
