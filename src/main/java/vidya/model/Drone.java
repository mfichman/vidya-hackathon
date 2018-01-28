package vidya.model;

import vidya.*;

public class Drone {
    private static int idCounter = 0;

    public final int team;
    public final int id = idCounter++;
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

    private int idx;

    public Drone(Movable movable,int team) {
        this.movable = movable;
        this.team = team;
    }

    public void fire(vidya.State state) {
        state.droneLasers.add(new DroneLaser(new Movable(
                movable.xPos, movable.yPos, movable.xVel*3, movable.yVel*3), range, damage, Colors.GREEN));
    }

    public void randomizeRof() {
        // Applies +/- 10% randomization to rof.
        double rofD = rof;
        double modifier = (Math.random()*2.0*rofD/10.0) - (rofD/10.0);
        rof += (int)modifier;
    }

    public boolean closeToTarget() {
        double deltaX = movable.xPos - targetXPos;
        double deltaY = movable.yPos - targetYPos;
        return Math.hypot(deltaX, deltaY) < 10;
    }

    public void computeVector() {
        double deltaX = targetXPos - movable.xPos;
        double deltaY = targetYPos - movable.yPos;
        double norm = Math.hypot(deltaX,deltaY);
        movable.xVel = 0.05*(deltaX/norm)*300 + 0.95*movable.xVel;
        movable.yVel = 0.05*(deltaY/norm)*300 + 0.95*movable.yVel;
        // FixMe: make speed ctor arg
    }

    public String toString() {
        return "Id:" + Integer.toString(id) + ", " + movable.toString();
    }

    public void update(vidya.State state) {
        // Debug
        if(frameCount%60==0) {
            //System.out.println(toString());
        }

        // Target a random bruh
        if(frameCount%300==0||closeToTarget()) {
            Drone target = null;
            this.idx = 0;
            while(target == this || target == null || target.team == team) {
                this.idx = (int)(Math.random()*state.drones.size());
                target = state.drones.get(idx);
            }
        }
        Drone target = state.drones.get(idx);
        targetXPos = target.movable.xPos;
        targetYPos = target.movable.yPos;
        computeVector();

        // You may fire when ready.
        if((frameCount % rof) == 0) {
            fire(state);
            randomizeRof();
        }

        // Move.
        //if(!closeToTarget()) {
            movable.move();
        //}

        // Compute liveness.
        if(hp <= 0) {
            kill();
            return;
        }

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

    public void kill() {
        hp = 0;
        alive = false;
    }
}