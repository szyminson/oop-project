package oop.project;

import java.util.List;

/**
 * A rocket. Has a lit of rocket parts, position, velocity and direction.
 */
public class Rocket {

    /**
     * List of parts in this rocket
     */
    private List<IRocketPart> parts;

    /**
     * Position of this rocket
     */
    private Vector position;

    /**
     * Rotation angle of this rocket (radians)
     */
    private double direction;

    /**
     * Velocity of this rocket
     */
    private Vector velocity;

    /**
     * Creates a rocket with given part list, position and direction.
     * @param parts List of parts for this rocket
     * @param position Position of this rocket
     * @param direction Rotation angle (radians)
     */
    public Rocket(List<IRocketPart> parts, Vector position, double direction) {
        this.parts = parts;
        this.position = position;
        this.direction = direction;
        this.velocity = new Vector(0, 0);
    }

    /**
     * Calculates mass of the rocket
     * @return mass of this rocket
     */
    public double getMass() {
        return this.parts.stream().mapToDouble(IRocketPart::getMass).sum();
    }

    /**
     * Gets velocity of this rocket.
     * @return velocity vector
     */
    public Vector getVelocity() {
        return this.velocity;
    }

    /**
     * Gets position of this rocket
     * @return position vector
     */
    public Vector getPosition() {
        return this.position;
    }

    /**
     * Gets rotation angle of this rocket
     * @return rotation angle in radians
     */
    public double getDirection() {
        return this.direction;
    }

    /**
     * Gets list of parts in this rocket
     * @return list of rocket parts
     */
    public Iterable<IRocketPart> getParts() {
        return this.parts;
    }

    /**
     * Computes change in the rocket's state over specified time delta, with an external force acting on it.
     * @param externalForce external force vector
     * @param time current time
     * @param deltaTime time delta to calculate state change
     */
    public void updateRocket(Vector externalForce, double time, double deltaTime) {
        Vector directionVector = new Vector(Math.cos(direction), Math.sin(direction));
        Vector totalThrust = parts.stream()
                .map(p -> p.createThrust(directionVector, time, deltaTime))
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
