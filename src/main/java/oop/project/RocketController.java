package oop.project;

import java.util.Map;

public class RocketController implements IRocketPart {
    private final Map<Double, Vector> path;

    public RocketController(Map<Double, Vector> path) {
        this.path = path;
    }

    @Override
    public double getMass() {
        return 0;
    }

    @Override
    public double changeDirection(Vector currentDirection, double time) {
        if (path.isEmpty()) {
            return 0;
        }
        double lowerTime = -Double.MAX_VALUE;
        Vector lowerTimeVector = null;

        double higherTime = Double.MAX_VALUE;
        Vector higherTimeVector = null;

        // find 2 time values, one higher and one lower than time, closest to time
        for (Map.Entry<Double, Vector> entry : this.path.entrySet()) {
            if (entry.getKey() == time) {
                Vector newV = entry.getValue();
                return newV.getAngle() - currentDirection.getAngle();
            }
            if (entry.getKey() < time && entry.getKey() > lowerTime) {
                lowerTime = entry.getKey();
                lowerTimeVector = entry.getValue();
            }
            if (entry.getKey() > time && entry.getKey() < higherTime) {
                higherTime = entry.getKey();
                higherTimeVector = entry.getValue();
            }
        }
        assert lowerTimeVector != null || higherTimeVector != null;
        if (lowerTimeVector == null) {
            return higherTimeVector.getAngle() - currentDirection.getAngle();
        }
        if (higherTimeVector == null) {
            return lowerTimeVector.getAngle() - currentDirection.getAngle();
        }
        double timeFraction = (time - lowerTime) / (higherTime - lowerTime);
        double previousAngle = lowerTimeVector.getAngle();
        double angleDiff = higherTimeVector.getAngle() - previousAngle;
        return timeFraction * angleDiff + previousAngle - currentDirection.getAngle();
    }
}
