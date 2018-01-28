package vidya.model;

public class Collisions {
    static void collide(Drone d, DroneLaser l) {
        d.takeDamage(l.damage);
        l.kill();
    }

    static void collide(Drone d1, Drone d2) {
        d1.kill();
        d2.kill();
    }
}
