package oop.project;

import java.util.Map;

/**
 * Shared math and physics functions.
 */
public class MathUtils {
    /**
     * Given a map with different set values for some keys (time), calculates an interpolated
     * value between 2 closest points, one with higher and one with lower key (time).
     * Returns 0 if there are no values.
     * Returns the single value from given map if only one value exists.
     * For key values lower/higher than the lowest/highest key in the map, returns
     * exactly value for the corresponding lowest/highest key.
     *
     * @param map points to interpolate
     * @param key the key to interpolate for
     * @return interpolated value
     */
    public static double interpolate(Map<Double, Double> map, double key) {
        if (map.isEmpty()) {
            return 0;
        }
        double lowKey = -Double.MAX_VALUE;
        Double lowValue = null;

        double highKey = Double.MAX_VALUE;
        Double highValue = null;

        // find 2 time values, one higher and one lower than key, closest to key
        for (Map.Entry<Double, Double> entry : map.entrySet()) {
            if (entry.getKey() == key) {
                return entry.getValue();
            }
            if (entry.getKey() < key && entry.getKey() > lowKey) {
                lowKey = entry.getKey();
                lowValue = entry.getValue();
            }
            if (entry.getKey() > key && entry.getKey() < highKey) {
                highKey = entry.getKey();
                highValue = entry.getValue();
            }
        }
        if (lowValue == null) {
            return highValue;
        }
        if (highValue == null) {
            return lowValue;
        }
        double keyFraction = (key - lowKey) / (highKey - lowKey);
        double valueDifference = highValue - lowValue;
        return keyFraction * valueDifference + lowValue;
    }

    /**
     * Calculates approximate air density for given gravity source, position and surface pressure
     *
     * @param gravity            Given gravity source
     * @param position           Given position to calculate air density
     * @param surfaceAirPressure Air pressure at the surface
     * @return Air density
     */
    public static double getAirDensity(GravityField gravity, Vector position, double surfaceAirPressure) {
        // Approximation of air pressure
        // this assumes temperature about 270K, and a relatively big planet
        // so that strength of gravity isn't significantly different at distance where air density becomes negligible
        double gSurface = gravity.getForce(gravity.getSourcePosition().add(new Vector(0, gravity.getRadius())),
                new Vector(1, 0), new Vector(0, 0), 1, 0).length();
        double h = position.sub(gravity.getSourcePosition()).length() - gravity.getRadius();
        double M = 0.03;
        double R0 = 8.3;
        double T0 = 270;
        double airPressure = surfaceAirPressure * Math.exp(-(gSurface * h * M) / (T0 * R0));

        double Rspecific = 287;
        double density = airPressure / (Rspecific * T0);
        return density;
    }
}
