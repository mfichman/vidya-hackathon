package vidya;

import java.util.ArrayList;
import vidya.model.*;

/* Contains the top-level state of the game world. Lists of enemies, gameplay
 * objects, obstacles, etc. This class is updated once per logic step. */

public class State {

    public final ArrayList<Drone> drones = new ArrayList<Drone>();
    public final ArrayList<DroneLaser> droneLasers = new ArrayList<DroneLaser>();
    public final ArrayList<Command> commands = new ArrayList<Command>();

    public State() {
        populate();
    }

    public void populate() {
        double speed = 100;
        double numDronesLeft = 1;
        double numDronesRight = 0;

        // Spawn left side drones
        for(int i=0;i<numDronesLeft;++i) {
            double x1 = Math.random()*Movable.MAX_X/10;
            double y1 = Math.random()*Movable.MAX_Y;
            double x2 = Movable.MAX_X;
            double y2 = Movable.MAX_Y/2;
            double theta = Math.atan((y2-y1)/(x2-x1));
            double xVel = speed*Math.cos(theta);
            double yVel = speed*Math.sin(theta);
            Drone d = new Drone(new Movable(x1,y1,xVel,yVel));
            d.targetXPos = x2;
            d.targetYPos = y2;
            drones.add(d);
        }

        // Spawn right side drones
        for(int i=0;i<numDronesRight;++i) {
            double x1 = Math.random()*Movable.MAX_X/10+Movable.MAX_X/9;
            double y1 = Math.random()*Movable.MAX_Y;
            double x2 = Movable.MIN_X;
            double y2 = Movable.MAX_Y/2;
            double theta = Math.atan((y2-y1)/(x2-x1));
            double xVel = speed*Math.cos(theta);
            double yVel = speed*Math.sin(theta);
            Drone d = new Drone(new Movable(x1,y1,xVel,yVel));
            d.targetXPos = x2;
            d.targetYPos = y2;
            drones.add(d);
        }
    }

    public void step() {
        drones.forEach((d) -> {d.update(this);});
        droneLasers.forEach((l) -> {l.update(this);});
        drones.removeIf((d) -> {return !d.alive;});
        droneLasers.removeIf((l) -> {return !l.alive;});
    }
}
