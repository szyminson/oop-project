package oop.project;

import java.util.Map;

public class RocketController implements IRocketPart {
    private final Map<Double, Double> path;

    public RocketController(Map<Double, Double> path) {
        this.path = path;
    }

    @Override
    public double getMass() {
        return 0;
    }

    @Override
    public double changeDirection(double currentDirection, double time) {
        if (path.isEmpty()) {
            return 0;
        }
        double lowerTime = -Double.MAX_VALUE;
        double lowerTimeDirection = 0;

        double higherTime = Double.MAX_VALUE;
        double higherTimeDirection = 0;

        // find 2 time values, one higher and one lower than time, closest to time
        for (Map.Entry<Double, Double> entry : this.path.entrySet()) {
            if (entry.getKey() == time) {
                return entry.getValue() - currentDirection;
            }
            if (entry.getKey() < time && entry.getKey() > lowerTime) {
                lowerTime = entry.getKey();
                lowerTimeDirection = entry.getValue();
            }
            if (entry.getKey() > time && entry.getKey() < higherTime) {
                higherTime = entry.getKey();
                higherTimeDirection = entry.getValue();
            }
        }
        double timeFraction = (time - lowerTime) / (higherTime - lowerTime);
        double previousAngle = lowerTimeDirection;
        double angleDiff = higherTimeDirection - previousAngle;
        return timeFraction * angleDiff + previousAngle - currentDirection;
    }
}
