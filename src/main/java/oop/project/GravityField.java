package oop.project;


public class GravityField implements IForceField {
    private final double strength;
    private final Vector sourcePosition;
    private final double radius;

    public GravityField(Vector sourcePosition, double strength, double radius) {
        this.sourcePosition = sourcePosition;
        this.strength = strength;
        this.radius = radius;
    }
    @Override
    public Vector getForce(Vector position, Vector rotation, Vector velocity, double mass, double time) {
        Vector diff = this.sourcePosition.sub(position);
        return diff.normalize().mul(mass * this.strength / diff.lengthSquared());
    }

    public double getRadius() {
        return radius;
    }

    public Vector getSourcePosition() {
        return sourcePosition;
    }
}
