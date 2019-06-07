package oop.project;


import com.flowpowered.noise.module.source.Perlin;

/**
 * An implementation of wind as an external factor in the project.
 */
public class Wind implements IForceField {
    /**
     * Strength variation of this Wind.
     */
    private double strengthVariation;
    /**
     * Force of this Wind.
     */
    private Vector windForce;

    /**
     * Perlin noise of this Wind.
     */
    private final Perlin perlin;

    /**
     * Sets strengthVariation and windForce using given parameters and generates perlin noise of this Wind using Perlin class.
     *
     * @param strengthVariation Given variation of strength.
     * @param windForce         Given force of wind.
     */
    public Wind(double strengthVariation, Vector windForce) {
        this.strengthVariation = strengthVariation;
        this.windForce = windForce;

        this.perlin = new Perlin();
        this.perlin.setFrequency(0.001);
        this.perlin.setOctaveCount(4);
        this.perlin.setSeed((int) System.currentTimeMillis());
    }

    /**
     * Calculates force of this Wind using given parameters.
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
        // -0.5, * 2 == [0, 1] --> [-1, 1]
        double randomX = (this.perlin.getValue(position.getX(), position.getY(), time) - 0.5) * 2 * this.strengthVariation;
        double randomY = (this.perlin.getValue(position.getX(), position.getY(), -time) - 0.5) * 2 * this.strengthVariation;

        return new Vector(randomX, randomY).add(this.windForce);
    }
}
