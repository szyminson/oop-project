package oop.project;


import java.util.Map;

/**
 * An implementation of rocket's engine in the project.
 */
public class Engine implements IRocketPart {
    /**
     * Mass of this Engine.
     */
    private final double mass;
    /**
     * Fuel source of this Engine.
     */
    private final FuelContainer fuelSource;
    /**
     * Thrust data of this Engine.
     */
    private final Map<Double, Double> thrustData;

    /**
     * Sets parameters of this Engine.
     *
     * @param mass       Given mass.
     * @param fuelSource Given source of fuel.
     * @param thrustData Given thrust data.
     */
    public Engine(double mass, FuelContainer fuelSource, Map<Double, Double> thrustData) {
        this.mass = mass;
        this.fuelSource = fuelSource;
        this.thrustData = thrustData;
    }

    /**
     * Gets mass of this Engine.
     *
     * @return Mass of this Engine.
     */
    @Override
    public double getMass() {
        return this.mass;
    }

    /**
     * Calculates a thrust force of this Engine using given parameters.
     *
     * @param direction Given direction.
     * @param time      Given time.
     * @return Calculated thrust force.
     */
    @Override
    public Vector createThrust(Vector direction, double time) {
        if (this.fuelSource.getRemainingFuel() <= 0) {
            return new Vector(0, 0);
        }
        double lowerTime = -Double.MAX_VALUE;
        double lowerTimeThrust = 0;

        double higherTime = Double.MAX_VALUE;
        double higherTimeThrust = 0;

        // find 2 time values, one higher and one lower than time, closest to time
        for (Map.Entry<Double, Double> entry : this.thrustData.entrySet()) {
            if (entry.getKey() == time) {
                return direction.mul(entry.getValue());
            }
            if (entry.getKey() < time && entry.getKey() > lowerTime) {
                lowerTime = entry.getKey();
                lowerTimeThrust = entry.getValue();
            }
            if (entry.getKey() > time && entry.getKey() < higherTime) {
                higherTime = entry.getKey();
                higherTimeThrust = entry.getValue();
            }
        }
        double timeFraction = (time - lowerTime) / (higherTime - lowerTime);
        double thrustDifference = higherTimeThrust - lowerTimeThrust;
        double thrust = timeFraction * thrustDifference + lowerTimeThrust;

        this.fuelSource.takeFuel(thrust); // TODO: scale by calculated factor

        return direction.mul(thrust);
    }
}
