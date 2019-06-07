package oop.project;


public class GravityField implements IForceField {
    /**
     *Parameters of this GravityField.
     */
    private final double strength;
    private final Vector sourcePosition;
    /**
     * @param sourcePosition of this GravityField.
     * @param strength of this Gravity Field.
     */
    public GravityField(Vector sourcePosition, double strength) {
        this.sourcePosition = sourcePosition;
        this.strength = strength;
    }
    /**
     * Calculates force of this GravityField using given parameters.
     * @param position Given position.
     * @param rotation Given rotation.
     * @param velocity Given velocity.
     * @param mass Given mass.
     * @param time Given time.
     * @return Calculated force.
     */
    @Override
    public Vector getForce(Vector position, Vector rotation, Vector velocity, double mass, double time) {
        Vector diff = this.sourcePosition.sub(position);
        return diff.normalize().mul(mass * this.strength / diff.lengthSquared());
    }
}
