package vidya.model;

import vidya.*;

public class Drone {

    public final Movable movable;
    public int hp = 100;
    public int hpArmor = 0;
    public int shield = 0;
    public int shieldArmor = 0;
    public double shieldRegen = 0; // rate of shield regen (per second)
    public int damage = 50;
    public int rof = 60; // frames per shot
    public double range = 500;
    public double targetXPos;
    public double targetYPos;
    public boolean alive = true;
    private int frameCount = 0;

    public Drone(Movable movable) {
        this.movable = movable;
    }

    public void fire(vidya.State state) {
        state.droneLasers.add(new DroneLaser(new Movable(
                movable.xPos, movable.yPos, movable.xVel*3, movable.yVel*3), range, damage, Color.GREEN));
    }

    public void randomizeRof() {
        // Applies +/- 10% randomization to rof.
        double rofD = rof;
        double modifier = (Math.random()*2.0*rofD/10.0) - (rofD/10.0);
        rof += (int)modifier;
    }

    public void update(vidya.State state) {
        // Compute liveness.
        if(hp <= 0) {
            alive = false;
        }

        // Fire when ready.
        if((frameCount % rof) == 0) {
            fire(state);
            randomizeRof();
        }

        // Move.
        movable.move();

        frameCount++;
    }

    public void takeDamage(int dmg) {
        // Apply damage to shield.
        int shieldDmg = dmg;
        if(shield > 0) {
            shieldDmg -= shieldArmor;
            shieldDmg = (shieldDmg <= 0) ? (1) : shieldDmg;
        }
        shield -= shieldDmg;

        // Apply damage to hp.
        int hpDmg = (shield >= 0) ? (0) : (-shield);
        hp -= hpDmg;

        // Apply minimums.
        if(shield < 0) {
            shield = 0;
        }
        if(hp < 0) {
            hp = 0;
        }
    }
}
