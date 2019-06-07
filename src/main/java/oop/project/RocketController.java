package oop.project;

import java.util.Map;

/**
 * Part that controls the direction of a rocket.
 */
public class RocketController implements IRocketPart {
    private final Map<Double, Double> path;

    /**
     * Constructs a rocket controller with given path data.
     * A path consists of a mapping from time to rotation in radians.
     * The rocket controller will interpolate between the specified values.
     *
     * @param path Path data
     */
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
