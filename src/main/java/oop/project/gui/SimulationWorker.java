package oop.project.gui;

import oop.project.*;
import oop.project.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

class SimulationWorker extends SwingWorker<Object, Object> {

    private final List<GravityField> gravityFields;
    private final List<Wind> windFields = new ArrayList<>();
    private final List<AirResistance> airResistanceFields = new ArrayList<>();

    private final List<IForceField> allForces = new ArrayList<>();
    private final Rocket rocket;

    private final double maxTime;
    private final double maxAltitude;
    private final double timeStep;

    private final Consumer<String> describeTime;
    private final Consumer<String> describePosition;
    private final Consumer<String> describeAirResistance;
    private final Consumer<String> describeVelocity;
    private final Consumer<String> describeExternalForces;
    private final Consumer<String> describeGravity;
    private final Consumer<String> describeWind;
    private final Consumer<String> describeDirection;
    private final FuelContainer fuelContainer;

    private long lastUpdate = System.currentTimeMillis();
    private World world;
    private boolean hasStartedOffPlanet = false;
    private boolean simulationRunning = true;
    private double lastLogTime = Double.NEGATIVE_INFINITY;

    private SimulationViewPanel viewPanel;

    private double loggingInterval;

    private SimulationLogger logger;

    SimulationWorker(Builder builder) {
        this.gravityFields = builder.gravityFields;
        for (GravityField field : gravityFields) {
            AirResistance airResistance = new AirResistance(builder.dragConstant, builder.airSurfacePressure, field);
            Wind wind = new Wind(builder.windVariation, builder.windForce, builder.airSurfacePressure, field);

            windFields.add(wind);
            airResistanceFields.add(airResistance);
        }

        this.allForces.addAll(gravityFields);
        this.allForces.addAll(windFields);
        this.allForces.addAll(airResistanceFields);

        fuelContainer = new FuelContainer(builder.fuelContainerMass, builder.fuelMass);
        Engine engine = new Engine(builder.engineMass, fuelContainer, builder.thrust);
        RocketController controller = new RocketController(builder.path);
        this.rocket = new Rocket(Arrays.asList(fuelContainer, engine, controller), new Vector(0, 0), Math.PI / 2);


        this.maxTime = builder.maxTime;
        this.maxAltitude = builder.maxAltitude;
        this.timeStep = builder.timeStep;

        this.world = new World(rocket, allForces, this::testEndSimulation, timeStep, () -> {
            if (world.getTime() - lastLogTime >= loggingInterval) {
                logStatus(world);
                lastLogTime = world.getTime();
            }
            if (System.currentTimeMillis() - lastUpdate > 100) {
                updateHandler();
            }
        });

        this.viewPanel = new SimulationViewPanel(rocket, gravityFields);
        builder.viewPanelContainer.removeAll();
        builder.viewPanelContainer.setLayout(new CardLayout());
        builder.viewPanelContainer.add(this.viewPanel);

        this.loggingInterval = builder.logInterval;

        this.describeTime = builder.describeTime;
        this.describePosition = builder.describePosition;
        this.describeAirResistance = builder.describeAirResistance;
        this.describeVelocity = builder.describeVelocity;
        this.describeExternalForces = builder.describeExternalForces;
        this.describeGravity = builder.describeGravity;
        this.describeWind = builder.describeWind;
        this.describeDirection = builder.describeDirection;

        if (builder.createLogFile) {
            String time = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss").format(new Date());
            try {
                this.logger = new SimulationLogger("simulation-log" + time + ".csv");
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Error writing log file " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void logStatus(World world) {
        if (logger != null) {
            Vector position = world.getRocket().getPosition();
            double direction = world.getRocket().getDirection();
            Vector directionVector = new Vector(Math.cos(direction), Math.sin(direction));
            Vector velocity = world.getRocket().getVelocity();

            Vector gravityForces = gravityFields.stream()
                    .map(f -> f.getForce(position, directionVector, velocity, world.getRocket().getMass(), world.getTime()))
                    .reduce(Vector::add).orElse(new Vector(0, 0));

            Vector windForces = windFields.stream().filter(f -> !gravityFields.contains(f))
                    .map(f -> f.getForce(position, directionVector, velocity, world.getRocket().getMass(), world.getTime()))
                    .reduce(Vector::add).orElse(new Vector(0, 0));

            Vector airResistanceForces = airResistanceFields.stream().filter(f -> !gravityFields.contains(f))
                    .map(f -> f.getForce(position, directionVector, velocity, world.getRocket().getMass(), world.getTime()))
                    .reduce(Vector::add).orElse(new Vector(0, 0));


            logger.log(world.getTime(), rocket.getMass(), fuelContainer.getRemainingFuel(), position,
                    directionVector, velocity,
                    windForces.normalize(), windForces, gravityForces.normalize(), gravityForces,
                    airResistanceForces.normalize(), airResistanceForces);
        }
    }

    private boolean testEndSimulation(World world) {
        GravityField mainGravityField = gravityFields.get(0);

        double distanceFromSurface = rocket.getPosition().sub(mainGravityField.getSourcePosition()).length() - mainGravityField.getRadius();
        if (distanceFromSurface > 0) {
            hasStartedOffPlanet = true;
        }
        if (!simulationRunning) {
            System.out.println("Simulation stopped by user");
            onClose();
            return true;
        }
        if (world.getTime() > maxTime) {
            System.out.println("Simulation stopped: ran out of time");
            onClose();
            return true;
        }
        if (hasStartedOffPlanet) if (distanceFromSurface < 0) {
            System.out.println("Simulation stopped: touched ground");
            onClose();
            return true;
        }
        if (distanceFromSurface > maxAltitude) {
            System.out.println("Simulation stopped: exceeded max altitude");
            onClose();
            return true;
        }
        return false;
    }

    private void onClose() {
        updateHandler();
        logStatus(world);
        stopSimulation();
        if (logger != null) {
            logger.close();
        }
    }

    private void updateHandler() {
        viewPanel.revalidate();
        viewPanel.repaint();
        Vector position = world.getRocket().getPosition();
        double direction = world.getRocket().getDirection();
        Vector directionVector = new Vector(Math.cos(direction), Math.sin(direction));
        Vector velocity = world.getRocket().getVelocity();
        Vector extForces = world.getForceFields().stream()
                .map(f -> f.getForce(position, directionVector, velocity, world.getRocket().getMass(), world.getTime()))
                .reduce(Vector::add).orElse(new Vector(0, 0));

        Vector gravityForces = gravityFields.stream()
                .map(f -> f.getForce(position, directionVector, velocity, world.getRocket().getMass(), world.getTime()))
                .reduce(Vector::add).orElse(new Vector(0, 0));

        Vector windForces = windFields.stream().filter(f -> !gravityFields.contains(f))
                .map(f -> f.getForce(position, directionVector, velocity, world.getRocket().getMass(), world.getTime()))
                .reduce(Vector::add).orElse(new Vector(0, 0));

        Vector airResistanceForces = airResistanceFields.stream().filter(f -> !gravityFields.contains(f))
                .map(f -> f.getForce(position, directionVector, velocity, world.getRocket().getMass(), world.getTime()))
                .reduce(Vector::add).orElse(new Vector(0, 0));

        describeTime.accept(String.format("Time: %.2f", world.getTime()));
        describeExternalForces.accept(String.format("External forces: %8.1f, %8.1f", extForces.getX(), extForces.getY()));
        describeWind.accept(String.format("Wind: %8.1f, %8.1f", windForces.getX(), windForces.getY()));
        describeAirResistance.accept(String.format("Air resistance: %8.1f, %8.1f", airResistanceForces.getX(), airResistanceForces.getY()));
        describeGravity.accept(String.format("Gravity: %8.1f, %8.1f", gravityForces.getX(), gravityForces.getY()));
        describePosition.accept(String.format("Position: %8.1f, %8.1f", position.getX(), position.getY()));
        describeVelocity.accept(String.format("Velocity:  %8.1f, %8.1f", rocket.getVelocity().getX(), rocket.getVelocity().getY()));
        describeDirection.accept(String.format("Direction: %3.3fpi", world.getRocket().getDirection() / Math.PI));
        lastUpdate = System.currentTimeMillis();
    }

    void stopSimulation() {
        this.simulationRunning = false;
    }

    boolean isSimulationRunning() {
        return simulationRunning;
    }

    @Override
    protected Object doInBackground() {
        world.runSimulation();
        return null;
    }

    static class Builder {
        private List<GravityField> gravityFields = new ArrayList<>();
        private double dragConstant;
        private double airSurfacePressure;
        private Vector windForce;
        private double windVariation;

        private Map<Double, Double> thrust;
        private Map<Double, Double> path;
        private double fuelMass;
        private double fuelContainerMass;
        private double engineMass;

        private JPanel viewPanelContainer;

        private double maxTime;
        private double maxAltitude;
        private double timeStep;

        private Consumer<String> describePosition;
        private Consumer<String> describeVelocity;
        private Consumer<String> describeExternalForces;
        private Consumer<String> describeGravity;
        private Consumer<String> describeWind;
        private Consumer<String> describeAirResistance;
        private Consumer<String> describeTime;
        private Consumer<String> describeDirection;
        private double logInterval;
        private boolean createLogFile;


        Builder gravitySources(TableModel gravitySources) {
            for (int i = 0; i < gravitySources.getRowCount(); i++) {
                String x = gravitySources.getValueAt(i, 1).toString();
                String y = gravitySources.getValueAt(i, 2).toString();
                String r = gravitySources.getValueAt(i, 3).toString();
                String strength = gravitySources.getValueAt(i, 4).toString();

                GravityField field = new GravityField(new Vector(Double.parseDouble(x), Double.parseDouble(y)), Double.parseDouble(strength), Double.parseDouble(r));
                gravityFields.add(field);
            }
            return this;
        }

        // simulation

        Builder maxTime(String maxTime) {
            this.maxTime = Double.parseDouble(maxTime);
            return this;
        }

        Builder maxAltitude(String maxAltitude) {
            this.maxAltitude = Double.parseDouble(maxAltitude);
            return this;
        }

        Builder timeStep(String timeStep) {
            this.timeStep = Double.parseDouble(timeStep);
            return this;
        }

        // forces

        Builder dragConstant(String dragConstant) {
            this.dragConstant = Double.parseDouble(dragConstant);
            return this;
        }

        Builder airSurfacePressure(String pressure) {
            this.airSurfacePressure = Double.parseDouble(pressure);
            return this;
        }

        Builder windForce(String forceX, String forceY) {
            this.windForce = new Vector(Double.parseDouble(forceX), Double.parseDouble(forceY));
            return this;
        }

        Builder windVariation(String variation) {
            this.windVariation = Double.parseDouble(variation);
            return this;
        }

        // rocket

        Builder fuelMass(String fuelMass) {
            this.fuelMass = Double.parseDouble(fuelMass);
            return this;
        }

        Builder fuelContainerMass(String fuelContainerMass) {
            this.fuelContainerMass = Double.parseDouble(fuelContainerMass);
            return this;
        }

        Builder engineMass(String mass) {
            this.engineMass = Double.parseDouble(mass);
            return this;
        }

        Builder thrust(TableModel model) {
            Map<Double, Double> thrustData = new HashMap<>();
            for (int i = 0; i < model.getRowCount(); i++) {
                String time = model.getValueAt(i, 1).toString();
                String value = model.getValueAt(i, 2).toString();
                thrustData.put(Double.parseDouble(time), Double.parseDouble(value));
            }
            this.thrust = thrustData;
            return this;
        }

        Builder path(TableModel model) {
            Map<Double, Double> pathData = new HashMap<>();
            for (int i = 0; i < model.getRowCount(); i++) {
                String time = model.getValueAt(i, 1).toString();
                String value = model.getValueAt(i, 2).toString();
                pathData.put(Double.parseDouble(time), Math.toRadians(Double.parseDouble(value)));
            }
            this.path = pathData;
            return this;
        }

        Builder viewPanelContainer(JPanel panel) {
            this.viewPanelContainer = panel;
            return this;
        }

        Builder describePosition(Consumer<String> consumer) {
            this.describePosition = consumer;
            return this;
        }

        Builder describeVelocity(Consumer<String> consumer) {
            this.describeVelocity = consumer;
            return this;
        }

        Builder describeExternalForces(Consumer<String> consumer) {
            this.describeExternalForces = consumer;
            return this;
        }

        Builder describeGravity(Consumer<String> consumer) {
            this.describeGravity = consumer;
            return this;
        }

        Builder describeWind(Consumer<String> consumer) {
            this.describeWind = consumer;
            return this;
        }

        Builder describeAirResistance(Consumer<String> consumer) {
            this.describeAirResistance = consumer;
            return this;
        }

        Builder describeTime(Consumer<String> consumer) {
            this.describeTime = consumer;
            return this;
        }

        Builder describeDirection(Consumer<String> consumer) {
            this.describeDirection = consumer;
            return this;
        }

        Builder loggingInverval(String text) {
            this.logInterval = Double.parseDouble(text);
            return this;
        }

        Builder createLogFile(boolean createLog) {
            this.createLogFile = createLog;
            return this;
        }

        SimulationWorker build() {
            return new SimulationWorker(this);
        }
    }
}
