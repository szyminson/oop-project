package oop.project;

import java.util.List;
import java.util.function.Predicate;

public class World {
    private Rocket rocket;
    private List<IForceField> forceFields;
    private Predicate<Rocket> endCondition;

    private Runnable uiUpdateHandler;

    private double timeStep;
    private double time = 0;

    public World(Rocket rocket, List<IForceField> forceFields, Predicate<Rocket> endCondition, double timeStep, Runnable uiUpdateHandler) {
        this.rocket = rocket;
        this.forceFields = forceFields;
        this.endCondition = endCondition;
        this.timeStep = timeStep;
        this.uiUpdateHandler = uiUpdateHandler;
    }

    public void runSimulation() {
        while (!endCondition.test(rocket)) {
            Vector directionVector = new Vector(Math.cos(rocket.getDirection()), Math.sin(rocket.getDirection()));
            Vector totalForce = forceFields.stream()
                    .map(f -> f.getForce(rocket.getPosition(), directionVector, rocket.getVelocity(), rocket.getMass(), time))
                    .reduce(Vector::add).orElseGet(() -> new Vector(0, 0));
            rocket.updateRocket(totalForce, time, timeStep);
            uiUpdateHandler.run();
            time += timeStep;
        }
    }
}
