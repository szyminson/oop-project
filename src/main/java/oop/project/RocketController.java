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
        throw new UnsupportedOperationException("Not implemented");
    }
}
