package oop.project;


public class GravityField implements IForceField {
    private final double strength;
    private final Vector sourcePosition;

    public GravityField(Vector sourcePosition, double strength) {
        this.sourcePosition = sourcePosition;
        this.strength = strength;
    }
    @Override
    public Vector getForce(Vector position, Vector rotation, Vector velocity, double mass, double time) {
        Vector diff = sourcePosition.sub(position);
        return diff.normalize().mul(mass * strength / diff.lengthSquared());
    }
}
