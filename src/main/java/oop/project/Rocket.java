package oop.project;

import java.util.List;

public class Rocket {
    private List<IRocketPart> parts;
    private Vector position;
    private Vector direction;
    private Vector velocity;

    public double getMass() {
        return this.parts.stream().mapToDouble(IRocketPart::getMass).sum();
    }

    public Vector getVelocity() {
        return this.velocity;
    }

    public Vector getPosition() {
        return this.position;
    }

    public Vector getDirection() {
        return this.direction;
    }

    public Iterable<IRocketPart> getParts() {
        return this.parts;
    }

    public void updateRocket(Vector externalForce, double deltaTime) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
