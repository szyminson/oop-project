package oop.project;


import java.util.Map;

public class Engine implements IRocketPart {
    private final double mass;
    private final FuelContainer fuelSource;
    private final Map<Double, Double> thrustData;

    public Engine(double mass, FuelContainer fuelSource, Map<Double, Double> thrustData) {
        this.mass = mass;
        this.fuelSource = fuelSource;
        this.thrustData = thrustData;
    }

    @Override
    public double getMass() {
        return this.mass;
    }

    @Override
    public Vector createThrust(Vector direction, double time) {
        if (fuelSource.getRemainingFuel() <= 0) {
            return new Vector(0, 0);
        }
        double lowerTime = -Double.MAX_VALUE;
        double lowerTimeThrust = 0;

        double higherTime = Double.MAX_VALUE;
        double higherTimeThrust = 0;

        // find 2 time values, one higher and one lower than time, closest to time
        for (Map.Entry<Double, Double> entry : thrustData.entrySet()) {
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

        fuelSource.takeFuel(thrust); // TODO: scale by calculated factor

        return direction.mul(thrust);
    }
}
