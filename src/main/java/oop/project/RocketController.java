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
        double newDirection = MathUtils.interpolate(path, time);
        return newDirection - currentDirection;
    }
}
