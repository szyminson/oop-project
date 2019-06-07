package oop.project;


import com.flowpowered.noise.module.source.Perlin;

public class Wind implements IForceField {
    private final double strengthVariation;
    private final Vector windForce;

    private final Perlin perlin;
    private final GravityField gravityObject;

    public Wind(double strengthVariation, Vector windForce, GravityField gravity) {
        this.strengthVariation = strengthVariation;
        this.windForce = windForce;
        this.gravityObject = gravity;

        this.perlin = new Perlin();
        this.perlin.setFrequency(0.001);
        this.perlin.setOctaveCount(4);
        this.perlin.setSeed((int) System.currentTimeMillis());
    }

    @Override
    public Vector getForce(Vector position, Vector rotation, Vector velocity, double mass, double time) {
        // TODO: take gravity into account
        // -0.5, * 2 == [0, 1] --> [-1, 1]
        double randomX = (this.perlin.getValue(position.getX(), position.getY(), time) - 0.5) * 2 * this.strengthVariation;
        double randomY = (this.perlin.getValue(position.getX(), position.getY(), -time) - 0.5) * 2 * this.strengthVariation;

        return new Vector(randomX, randomY).add(this.windForce);
    }
}
