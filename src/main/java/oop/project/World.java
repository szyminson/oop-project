package oop.project;

import java.util.List;
import java.util.function.Predicate;


public class World {
    /**
     * Rocket object.
     */
    private Rocket rocket;
    /**
     * A list of force fields present in this simulated World.
     */
    private List<IForceField> forceFields;

    /**
     * The simulation will stop when this predicate returns true
     */
    private Predicate<World> endCondition;

    /**
     * A user interface update handler. Invoked every loop iteration.
     */
    private Runnable uiUpdateHandler;

    /**
     * Time step for the simulation. Lower values are more accurate, but slower to simulate.
     */
    private double timeStep;

    /**
     * Current time
     */
    private double time = 0;

    public World(Rocket rocket, List<IForceField> forceFields, Predicate<World> endCondition, double timeStep, Runnable uiUpdateHandler) {
        this.rocket = rocket;
        this.forceFields = forceFields;
        this.endCondition = endCondition;
        this.timeStep = timeStep;
        this.uiUpdateHandler = uiUpdateHandler;
    }

    public Rocket getRocket() {
        return rocket;
    }

    public List<IForceField> getForceFields() {
        return forceFields;
    }

    public double getTime() {
        return time;
    }

    /**
     * Runs the simulation.
     */
    public void runSimulation() {
        while (!endCondition.test(this)) {
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
