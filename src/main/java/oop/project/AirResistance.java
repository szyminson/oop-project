package oop.project;

public class AirResistance implements IForceField {
    /**
     * Constant for the simulated rocket that specifies air drag.
     * Physically equal to {@code 0.5 * crossSectionalArea * dragCoefficient}
     */
    private final double dragConstant;

    private final double surfaceAirPressure;
    private final GravityField gravityObject;

    /**
     * Sets dragConstant, surfaceAirPressure and gravity of this AirResistance.
     *
     * @param dragConstant Given drag constant. See {@link #dragConstant}
     * @param surfaceAirPressure Air pressure at the surface
     * @param gravity Gravity source
     */
    public AirResistance(double dragConstant, double surfaceAirPressure, GravityField gravity) {
        this.dragConstant = dragConstant;
        this.surfaceAirPressure = surfaceAirPressure;
        this.gravityObject = gravity;
    }

    /**
     * Calculates force of this AirResistance using given parameters.
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
        double density = MathUtils.getAirDensity(gravityObject, position, surfaceAirPressure);
        return velocity.mul(-this.dragConstant * velocity.length() * density);
    }
}
