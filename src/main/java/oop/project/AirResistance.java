package oop.project;

public class AirResistance implements IForceField {
    /**
     * Drag coefficient of this AirResistance.
     */
    private double dragCoefficient;

    /**
     * Sets dragCoefficient od this AirResistance.
     *
     * @param dragCoefficient Given drag coefficient.
     */
    public AirResistance(double dragCoefficient) {
        this.dragCoefficient = dragCoefficient;
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
        return velocity.mul(-this.dragCoefficient * velocity.lengthSquared());
    }
}
