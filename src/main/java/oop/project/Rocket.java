package oop.project;

import java.util.List;

public class Rocket {
    private List<IRocketPart> parts;
    private Vector position;
    private double direction;
    private Vector velocity;

    public Rocket(List<IRocketPart> parts, Vector position, double direction) {
        this.parts = parts;
        this.position = position;
        this.direction = direction;
        this.velocity = new Vector(0, 0);
    }

    public double getMass() {
        return this.parts.stream().mapToDouble(IRocketPart::getMass).sum();
    }

    public Vector getVelocity() {
        return this.velocity;
    }

    public Vector getPosition() {
        return this.position;
    }

    public double getDirection() {
        return this.direction;
    }

    public Iterable<IRocketPart> getParts() {
        return this.parts;
    }

    public void updateRocket(Vector externalForce, double time, double deltaTime) {
        Vector directionVector = new Vector(Math.cos(direction), Math.sin(direction));
        Vector totalThrust = parts.stream()
                .map(p -> p.createThrust(directionVector, time))
                .reduce(Vector::add).orElse(new Vector(0, 0))
                .add(externalForce);
        double directionChange = parts.stream().mapToDouble(p -> p.changeDirection(direction, time)).sum();
        // F = m*a -> a = F/m
        Vector acceleration = totalThrust.div(getMass());
        // a = dv/dt -> dv = a*dt
        Vector dv = acceleration.mul(deltaTime);

        this.velocity = this.velocity.add(dv);
        // v = dx/dt -> dx = v*dt
        Vector dx = this.velocity.mul(deltaTime);
        this.position = this.position.add(dx);
        this.direction += directionChange;
    }
}
