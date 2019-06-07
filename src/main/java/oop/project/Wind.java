package oop.project;


import com.flowpowered.noise.module.source.Perlin;

public class Wind implements IForceField {
    /**
     *Parameters of this Wind.
     */
    private double strengthVariation;
    private Vector windForce;

    private final Perlin perlin;

    /**
     * @param strengthVariation Specifies changes of this Wing strength.
     * @param windForce of this Wind.
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
     * @param position Given position.
     * @param rotation Given rotation.
     * @param velocity Given velocity.
     * @param mass Given mass.
     * @param time Given time.
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
