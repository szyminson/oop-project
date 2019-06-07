package oop.project;


/**
 * An implementation of gravitational field in the project.
 */
public class GravityField implements IForceField {

    /**
     * Strength of this GravityField.
     */
    private final double strength;
    /**
     * Source position of this GravityField.
     */
    private final Vector sourcePosition;

    /**
     * Sets sourcePosition and strength of this GravityField.
     *
     * @param sourcePosition Given source position.
     * @param strength       Given strength.
     */
    public GravityField(Vector sourcePosition, double strength) {
        this.sourcePosition = sourcePosition;
        this.strength = strength;
    }

    /**
     * Calculates this GravityField's force using given parameters.
     *
     * @param position Given position.
     * @param rotation Given rotation.
     * @param velocity Given velocity.
     * @param mass     Given mass.
     * @param time     Given time.
     * @return Calculated force.
     */
    @Override
    public Vector getForce(Vector position, Vector rotation, Vector velocity, double mass, double time) {
        Vector diff = this.sourcePosition.sub(position);
        return diff.normalize().mul(mass * this.strength / diff.lengthSquared());
    }
}
