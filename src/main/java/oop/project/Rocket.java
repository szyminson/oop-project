package oop.project;

import java.util.List;
import java.util.Vector;

public class Rocket {
    private List<IRocketPart> parts;
    private Vector position;
    private Vector direction;
    private Vector velocity;

    public double getMass() {
        return 0;
    }
    public Vector getVelocity() {
        return null;
    }
    public Vector getPosition() {
        return null;
    }
    public Vector getDirection() {
        return null;
    }
    public Iterable <IRocketPart> getParts() {
        return null;
    }
    public void updateRocket (Vector externalForce, double deltaTime) {

    }
}
