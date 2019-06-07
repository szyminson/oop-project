package oop.project;


import java.util.ArrayList;
import java.util.List;
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
     * Specifies fuel usage per unit of impulse.
     */
    private final double fuelUsage;

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
        this.fuelUsage = fuelSource.getRemainingFuel() / integrate(thrustData);
    }

    private double integrate(Map<Double, Double> function) {
        // special case for empty and size 1
        if (function.isEmpty()) {
            return 0;
        }
        if (function.size() == 1) {
            // can be either 0, -infnity or +infinity
            double value = function.values().iterator().next();
            return Math.copySign(Double.POSITIVE_INFINITY, value);
        }
        List<Double> sortedList = new ArrayList<>(function.keySet());
        sortedList.sort(Double::compare);
        {
            double v1 = function.get(sortedList.get(0));
            double v2 = function.get(sortedList.get(sortedList.size() - 1));
            if (v1 != 0 || v2 != 0) {
                // undefined (nan) is both ends are nonzero and have different signs, otherwise infinite with that sign
                if ((v1 < 0 && v2 > 0) || (v1 > 1 && v2 < 0)) {
                    return Double.NaN;
                }
                return Math.copySign(Double.POSITIVE_INFINITY, v1);
            }
        }

        double sum = 0;
        for (int i = 0; i < sortedList.size() - 1; i++) {
            double t1 = sortedList.get(i);
            double t2 = sortedList.get(i + 1);
            double v1 = function.get(t1);
            double v2 = function.get(t2);

            /*
                   <^|  + --> v2
                 <^  |  |-> triangle part
               <^    |  +
              +------+  + --> v1
              |      |  |
              |      |  |-> rectangle part
              |      |  |
              +------+  +
              t1     t2
            */
            double rectanglePart = v1 * (t2 - t1);
            double trianglePart = (v2 - v1) * (t2 - t1) * 0.5; // this can be negative when v2 < v1
            sum += rectanglePart + trianglePart;
        }
        return sum;
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
    public Vector createThrust(Vector direction, double time, double deltaTime) {
        if (this.fuelSource.getRemainingFuel() <= 0) {
            return new Vector(0, 0);
        }
        double thrust = MathUtils.interpolate(thrustData, time);
        this.fuelSource.takeFuel(thrust * deltaTime * fuelUsage); // TODO: scale by calculated factor
        return direction.mul(thrust);
    }
}
