package oop.project;

public class AirResistance implements IForceField {
    private double dragCoefficient;

    public AirResistance(double dragCoefficient) {
        this.dragCoefficient = dragCoefficient;
    }

    @Override
    public Vector getForce(Vector position, Vector rotation, Vector velocity, double mass, double time) {
        return velocity.mul(-this.dragCoefficient * velocity.lengthSquared());
    }
}
