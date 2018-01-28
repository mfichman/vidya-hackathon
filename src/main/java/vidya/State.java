package vidya;

import java.util.ArrayList;

import vidya.graphics.Model;
import vidya.graphics.Scene;
import vidya.math.Transform;
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
        double speed = 300;
        double numDronesLeft = 100;
        double numDronesRight = 100;

        // Spawn left side drones
        for(int i=0;i<numDronesLeft;++i) {
            double x1 = Math.random()*Movable.MAX_X/10;
            double y1 = Math.random()*Movable.MAX_Y;
            //double x2 = Movable.MAX_X;
            //double y2 = Movable.MAX_Y/2;
            double x2 = Math.random()*Movable.MAX_X/10+Movable.MAX_X*9/10;
            double y2 = Math.random()*Movable.MAX_Y;
            double theta = Math.atan2(y2-y1, x2-x1);
            double xVel = speed*Math.cos(theta);
            double yVel = speed*Math.sin(theta);
            Drone d = new Drone(new Movable(x1,y1,xVel,yVel),0);
            d.targetXPos = x2;
            d.targetYPos = y2;
            drones.add(d);
        }

        // Spawn right side drones
        for(int i=0;i<numDronesRight;++i) {
            double x1 = Math.random()*Movable.MAX_X/10+Movable.MAX_X*9/10;
            double y1 = Math.random()*Movable.MAX_Y;
            //double x2 = Movable.MIN_X;
            //double y2 = Movable.MAX_Y/2;
            double x2 = Math.random()*Movable.MAX_X/10;
            double y2 = Math.random()*Movable.MAX_Y;
            double theta = Math.atan2(y2-y1, x2-x1);
            double xVel = speed*Math.cos(theta);
            double yVel = speed*Math.sin(theta);
            Drone d = new Drone(new Movable(x1,y1,xVel,yVel),1);
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

    public void render(Scene scene) {
        Model model = Asset.droneFighterModel;
        drones.forEach((d) -> {
            Transform transform = new Transform();
            transform.position.x = (float)d.movable.xPos;
            transform.position.y = (float)d.movable.yPos;
            transform.position.z = 0;

            Model.Material material = (d.team == 0) ? Asset.blue : Asset.red;

            model.render(scene, transform, material);
        });
    }

}
