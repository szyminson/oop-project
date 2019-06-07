package oop.project;

public class AirResistance implements IForceField {
    /**
     * Constant for the simulated rocket that specifies air drag.
     * Physically equal to {@code 0.5 * crossSectionalArea * dragCoefficient}
     */
    private final double dragConstant;

    private final double surfaceAirPressure;
    private final GravityField gravityObject;

    public AirResistance(double dragConstant, double surfaceAirPressure, GravityField gravity) {
        this.dragConstant = dragConstant;
        this.surfaceAirPressure = surfaceAirPressure;
        this.gravityObject = gravity;
    }

    @Override
    public Vector getForce(Vector position, Vector rotation, Vector velocity, double mass, double time) {
        double density = MathUtils.getAirDensity(gravityObject, position, surfaceAirPressure);
        return velocity.mul(-this.dragConstant * velocity.length() * density);
    }
}
