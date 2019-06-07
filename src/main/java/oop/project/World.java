package oop.project;

import java.util.List;
import java.util.function.Predicate;

public class World {
    private Rocket rocket;
    private List<IForceField> forceFields;
    private Predicate<Rocket> endCondition;

    private double timeStep;
    private double time = 0;

    public World(Rocket rocket, List<IForceField> forceFields, Predicate<Rocket> endCondition, double timeStep) {
        this.rocket = rocket;
        this.forceFields = forceFields;
        this.endCondition = endCondition;
        this.timeStep = timeStep;
    }

    public void runSimulation() {
        while (!endCondition.test(rocket)) {
            Vector totalForce = forceFields.stream()
                    .map(f -> f.getForce(rocket.getPosition(), rocket.getDirection(), rocket.getVelocity(), rocket.getMass(), time))
                    .reduce(Vector::add).orElseGet(() -> new Vector(0, 0));
            rocket.updateRocket(totalForce, time, timeStep);
            time += timeStep;
        }
    }
}
