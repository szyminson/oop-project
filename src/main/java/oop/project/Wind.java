package oop.project;


import com.flowpowered.noise.module.source.Perlin;

public class Wind implements IForceField {
    private final double strengthVariation;
    private final Vector windForce;

    private final Perlin perlin;
    private final double surfaceAirPressure;
    private final GravityField gravityObject;

    public Wind(double strengthVariation, Vector windForce, double surfaceAirPressure, GravityField gravity) {
        this.strengthVariation = strengthVariation;
        this.windForce = windForce;
        this.surfaceAirPressure = surfaceAirPressure;
        this.gravityObject = gravity;

        this.perlin = new Perlin();
        this.perlin.setFrequency(0.0001);
        this.perlin.setOctaveCount(4);
        this.perlin.setSeed((int) System.currentTimeMillis());
    }

    @Override
    public Vector getForce(Vector position, Vector rotation, Vector velocity, double mass, double time) {
        // this is not a physically accurate computation of wind,
        // but it's good enough for the purpose of this simulation

        final double delta = 0.0001;
        // calculate random fluctuations in density
        // and use their gradient to calculate a "force"
        double r0 = 1 + ((this.perlin.getValue(position.getX(), position.getY(), time) - 0.5) * 2);
        double rx = 1 + ((this.perlin.getValue(position.getX() + delta, position.getY(), time) - 0.5) * 2);
        double ry = 1 + ((this.perlin.getValue(position.getX(), position.getY() + delta, time) - 0.5) * 2);

        double density =  MathUtils.getAirDensity(gravityObject, position, surfaceAirPressure);

        double fx = this.strengthVariation * density * (rx - r0) / delta;
        double fy = this.strengthVariation * density * (ry - r0) / delta;

        return new Vector(fx, fy).add(this.windForce);
    }
}
