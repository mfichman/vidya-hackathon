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
        double speed = 10;
        double xMax = 1000;
        double yMax = 1000;

        // Spawn left side drones
        for(int i=0;i<100;++i) {
            double x1 = Math.random()*xMax/10;
            double y1 = Math.random()*yMax;
            double x2 = xMax;
            double y2 = yMax/2;
            double theta = Math.atan((y2-y1)/(x2-x1));
            double xVel = speed*Math.cos(theta);
            double yVel = speed*Math.sin(theta);
            drones.add(new Drone(new Movable(x1,x2,xVel,yVel)));
        }

        // Spawn right side drones
        for(int i=0;i<100;++i) {
            double x1 = Math.random()*xMax/10+xMax/9;
            double y1 = Math.random()*yMax;
            double x2 = 0;
            double y2 = yMax/2;
            double theta = Math.atan((y2-y1)/(x2-x1));
            double xVel = speed*Math.cos(theta);
            double yVel = speed*Math.sin(theta);
            drones.add(new Drone(new Movable(x1,x2,xVel,yVel)));
        }
    }

    public void step() {
        drones.forEach((d) -> {d.update(this);});
        droneLasers.forEach((l) -> {l.update(this);});
        drones.removeIf((d) -> {return d.alive;});
        droneLasers.removeIf((l) -> {return l.alive;});
    }
}
