package vidya.model;

public class Collisions {
    static void collide(Drone d, DroneLaser l) {
        d.takeDamage(l.damage);
        l.kill();
    }
}
