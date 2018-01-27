package vidya.model;

public class Movable {

    public double xPos; // X position (meters)
    public double yPos; // Y position (meters)
    public double xVel; // X velocity (meters/second)
    public double yVel; // Y velocity (meters/second)

    public Movable() {
        xPos = 0;
        yPos = 0;
        xVel = 0;
        yVel = 0;
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
        xPos += xVel;
        yPos += yVel;
    }
}
