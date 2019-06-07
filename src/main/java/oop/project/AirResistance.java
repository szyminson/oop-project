package oop.project;

public class AirResistance implements IForceField {
    private double dragCoefficient;
    private GravityField gravityObject;

    public AirResistance(double dragCoefficient, GravityField gravity) {
        this.dragCoefficient = dragCoefficient;
        this.gravityObject = gravity;
    }

    @Override
    public Vector getForce(Vector position, Vector rotation, Vector velocity, double mass, double time) {
        // TODO: take gravity into account
        return velocity.mul(-this.dragCoefficient * velocity.lengthSquared());
    }
}
