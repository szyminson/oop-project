package oop.project;


import com.flowpowered.noise.module.source.Perlin;

public class Wind implements IForceField {
    private double strengthVariation;
    private Vector windForce;

    private final Perlin perlin;

    public Wind(double strengthVariation, Vector windForce) {
        this.strengthVariation = strengthVariation;
        this.windForce = windForce;

        this.perlin = new Perlin();
        this.perlin.setFrequency(0.001);
        this.perlin.setOctaveCount(4);
        this.perlin.setSeed((int) System.currentTimeMillis());
    }

    @Override
    public Vector getForce(Vector position, Vector rotation, Vector velocity, double mass, double time) {
        // -0.5, * 2 == [0, 1] --> [-1, 1]
        double randomX = (perlin.getValue(position.getX(), position.getY(), time) - 0.5) * 2 * strengthVariation;
        double randomY = (perlin.getValue(position.getX(), position.getY(), -time) - 0.5) * 2 * strengthVariation;

        return new Vector(randomX, randomY).add(windForce);
    }
}
