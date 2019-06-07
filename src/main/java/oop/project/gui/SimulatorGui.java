package oop.project.gui;

import com.google.common.collect.ImmutableMap;
import oop.project.*;
import oop.project.Vector;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class SimulatorGui extends JFrame {

    // Data based on  Saturn V rocket https://en.wikipedia.org/wiki/Saturn_V, no staging
    private static final double DEFAULT_ENGINE_MASS = 8_400 + 1_788.1 * 2;
    private static final double DEFAULT_FUEL_CONTAINER_MASS = 130_000 + 40_100 + 13_500 - DEFAULT_ENGINE_MASS;
    private static final double DEFAULT_FUEL_MASS = 2_290_000 + 496_200 + 123_000 - DEFAULT_ENGINE_MASS - DEFAULT_FUEL_CONTAINER_MASS;

    // recalculated thrust to get the same acceleration, without discarding mass of previous stages
    private static final Map<Double, Double> DEFAULT_THRUST_TABLE = ImmutableMap.<Double, Double>builder()
            .put(-0.1, 0.0)
            .put(0.0, 35_100_000.0)
            .put(168.0, 35_100_000.0)
            .put(168.01, 6_220_344.0)
            .put(168.0 + 360, 6_220_344.0)
            .put(168.01 + 360, 2_461_801.0)
            .put(168.0 + 360 + 165 + 335, 2_461_801.0)
            .put(168.01 + 360 + 165 + 335, 0.0)
            .build();

    private static final double DEFAULT_GRAVITY_STRENGTH = 6.67430e-11 * 5.972e24;

    private static final double DEFAULT_WIND_STRENGTH = 0;
    private static final double DEFAULT_WIND_VARIATION = 0.001;
    private static final double DEFAULT_SURFACE_AIR_PRESSURE = 100_000;

    private JTextField engineMass;
    private JTextField fuelMass;
    private JTextField fuelContainerMass;
    private JTable thrustTable;

    private JTable pathTable;

    private JTextField windForceX;
    private JTextField windForceY;
    private JTextField windForceVariation;
    private JTextField dragCoefficient;
    private JTable gravitySources;

    private JLabel timeLabel;
    private JLabel externalForcesLabel;
    private JLabel positionLabel;
    private JLabel directionLabel;

    public SimulatorGui() throws HeadlessException {
        super("Rocket Simulator");

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(640, 480);

        JTabbedPane mainPane = new JTabbedPane(SwingConstants.TOP);

        mainPane.addTab("Simulation", getSimulationPane());
        mainPane.addTab("Rocket", getRocketConfigPane());
        mainPane.addTab("Rocket path", getRocketPathConfigPane());
        mainPane.addTab("Force fields", getForceFieldConfigPane());

        this.add(mainPane);
    }

    private Component getSimulationPane() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints(0, 0, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0);

        JPanel viewContainer = new JPanel();

        JButton start = new JButton("Start");
        start.addActionListener(e -> startSimulation(viewContainer));
        constraints.gridx = 0;
        panel.add(start, constraints);

        timeLabel = new JLabel("Time:");
        constraints.gridy++;
        constraints.gridx = 0;
        panel.add(timeLabel, constraints);

        externalForcesLabel = new JLabel("External forces:");
        constraints.gridy++;
        constraints.gridx = 0;
        panel.add(externalForcesLabel, constraints);

        positionLabel = new JLabel("Position:");
        constraints.gridy++;
        constraints.gridx = 0;
        panel.add(positionLabel, constraints);

        directionLabel = new JLabel("Direction:");
        constraints.gridy++;
        constraints.gridx = 0;
        panel.add(directionLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1;
        panel.add(viewContainer, constraints);
        return panel;
    }

    private void startSimulation(JPanel viewContainer) {
        List<GravityField> gravityFields = new ArrayList<>();
        for (int i = 0; i < gravitySources.getRowCount(); i++) {
            String x = gravitySources.getModel().getValueAt(i, 1).toString();
            String y = gravitySources.getModel().getValueAt(i, 2).toString();
            String r = gravitySources.getModel().getValueAt(i, 3).toString();
            String strength = gravitySources.getModel().getValueAt(i, 4).toString();

            GravityField field = new GravityField(new Vector(Double.parseDouble(x), Double.parseDouble(y)), Double.parseDouble(strength), Double.parseDouble(r));
            gravityFields.add(field);
        }

        List<IForceField> forces = new ArrayList<>();
        for (GravityField field : gravityFields) {
            String dragCoefficient = this.dragCoefficient.getText();
            AirResistance airResistance = new AirResistance(Double.parseDouble(dragCoefficient), DEFAULT_SURFACE_AIR_PRESSURE, field);

            String strengthVariation = this.windForceVariation.getText();
            String windX = this.windForceX.getText();
            String windY = this.windForceY.getText();
            Wind wind = new Wind(Double.parseDouble(strengthVariation),
                    new Vector(Double.parseDouble(windX), Double.parseDouble(windY)), DEFAULT_SURFACE_AIR_PRESSURE, field);

            forces.add(field);
            forces.add(airResistance);
            forces.add(wind);
        }

        String fuelContainerMass = this.fuelContainerMass.getText();
        String fuelMass = this.fuelMass.getText();
        FuelContainer fuelContainer = new FuelContainer(Double.parseDouble(fuelContainerMass), Double.parseDouble(fuelMass));

        Map<Double, Double> thrustData = new HashMap<>();
        for (int i = 0; i < thrustTable.getRowCount(); i++) {
            String time = thrustTable.getModel().getValueAt(i, 1).toString();
            String value = thrustTable.getModel().getValueAt(i, 2).toString();
            thrustData.put(Double.parseDouble(time), Double.parseDouble(value));
        }

        String engineMass = this.engineMass.getText();
        Engine engine = new Engine(Double.parseDouble(engineMass), fuelContainer, thrustData);

        Map<Double, Double> path = new HashMap<>();
        for (int i = 0; i < pathTable.getRowCount(); i++) {
            String time = pathTable.getModel().getValueAt(i, 1).toString();
            String value = pathTable.getModel().getValueAt(i, 2).toString();
            path.put(Double.parseDouble(time), Math.toRadians(Double.parseDouble(value)));
        }
        RocketController controller = new RocketController(path);

        Rocket rocket = new Rocket(Arrays.asList(fuelContainer, engine, controller), new Vector(0, 0), Math.PI / 2);

        viewContainer.removeAll();
        viewContainer.setLayout(new CardLayout());
        viewContainer.add(new SimulationViewPanel(rocket, gravityFields));
        viewContainer.repaint();

        SwingWorker<?, ?> swingWorker = new SwingWorker<Object, Object>() {
            private long lastUpdate = System.currentTimeMillis();
            private World world;

            @Override
            protected Object doInBackground() {
                world = new World(rocket, forces, w -> w.getTime() > 100000, 0.0001, () -> {
                    if (System.currentTimeMillis() - lastUpdate > 100) {
                        viewContainer.revalidate();
                        viewContainer.repaint();
                        timeLabel.setText(String.format("Time: %.2f", world.getTime()));
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

                        @SuppressWarnings("SuspiciousMethodCalls")
                        Vector otherForces = forces.stream().filter(f -> !gravityFields.contains(f))
                                .map(f -> f.getForce(position, directionVector, velocity, world.getRocket().getMass(), world.getTime()))
                                .reduce(Vector::add).orElse(new Vector(0, 0));

                        externalForcesLabel.setText(String.format("External forces: %8.1f, %8.1f (gravity: %8.1f, %8.1f, other: %8.1f, %8.1f)",
                                extForces.getX(), extForces.getY(), gravityForces.getX(), gravityForces.getY(), otherForces.getX(), otherForces.getY()));
                        positionLabel.setText(String.format("Position: %8.1f, %8.1f", position.getX(), position.getY()));
                        directionLabel.setText(String.format("Direction: %3.3fpi", world.getRocket().getDirection() / Math.PI));
                        lastUpdate = System.currentTimeMillis();
                    }
                });
                world.runSimulation();
                return null;
            }
        };
        swingWorker.execute();
    }

    private Component getRocketConfigPane() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints(0, 0, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0);

        panel.add(new JLabel("Engine mass"), constraints);
        engineMass = new JTextField(String.valueOf(DEFAULT_ENGINE_MASS));
        constraints.gridx = 1;
        panel.add(engineMass, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        panel.add(new JLabel("Fuel mass"), constraints);
        fuelMass = new JTextField(String.valueOf(DEFAULT_FUEL_MASS));
        constraints.gridx = 1;
        panel.add(fuelMass, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        panel.add(new JLabel("Fuel container mass"), constraints);
        fuelContainerMass = new JTextField(String.valueOf(DEFAULT_FUEL_CONTAINER_MASS));
        constraints.gridx = 1;
        panel.add(fuelContainerMass, constraints);

        // thrust configuration
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2;
        panel.add(new JLabel("Thrust settings:"), constraints);
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2;

        thrustTable = new JTable();

        JTableButtonColumn bc = new JTableButtonColumn(thrustTable);
        JButton add = new JButton("Add");
        add.addActionListener(bc);
        Object[][] table = new Object[DEFAULT_THRUST_TABLE.size()][3];
        {
            int i = 0;
            for (Map.Entry<Double, Double> entry : DEFAULT_THRUST_TABLE.entrySet()) {
                table[i][0] = bc.newButton();
                table[i][1] = entry.getKey();
                table[i][2] = entry.getValue();
                System.out.println(entry.getValue());
                i++;
            }
            table[table.length - 1][0] = add;
        }
        TableModel model = new DefaultTableModel(table, new String[]{"Add/remove", "Time", "Thrust"});
        thrustTable.setModel(model);
        bc.addToColumn(0);

        // empty jpanel to take all remaining vertical space
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1;
        JScrollPane scrollPane = new JScrollPane(thrustTable);
        panel.add(scrollPane, constraints);
        return panel;
    }

    private Component getRocketPathConfigPane() {
        JPanel panel = new JPanel();
        panel.setLayout(new CardLayout());

        pathTable = new JTable();

        JTableButtonColumn bc = new JTableButtonColumn(pathTable);
        JButton add = new JButton("Add");
        add.addActionListener(bc);
        TableModel model = new DefaultTableModel(new Object[][]{
                {add, 0.0, 90.0}
        }, new String[]{"Add/remove", "Time", "Direction angle (degrees)"});
        pathTable.setModel(model);
        bc.addToColumn(0);

        JScrollPane scrollPane = new JScrollPane(pathTable);
        panel.add(scrollPane);
        return panel;
    }

    private Component getForceFieldConfigPane() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints(0, 0, 1, 1, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0);

        panel.add(new JLabel("Wind force x"), constraints);
        windForceX = new JTextField(String.valueOf(DEFAULT_WIND_STRENGTH));
        constraints.gridx = 1;
        panel.add(windForceX, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        panel.add(new JLabel("Wind force y"), constraints);
        windForceY = new JTextField(String.valueOf(DEFAULT_WIND_STRENGTH));
        constraints.gridx = 1;
        panel.add(windForceY, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        panel.add(new JLabel("Wind force variation"), constraints);
        windForceVariation = new JTextField(String.valueOf(DEFAULT_WIND_VARIATION));
        constraints.gridx = 1;
        panel.add(windForceVariation, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        panel.add(new JLabel("Drag coefficient"), constraints);
        dragCoefficient = new JTextField("0.06");
        constraints.gridx = 1;
        panel.add(dragCoefficient, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2;
        panel.add(new JLabel("Gravity sources:"), constraints);
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2;

        gravitySources = new JTable();

        JTableButtonColumn bc = new JTableButtonColumn(gravitySources);
        JButton add = new JButton("Add");
        add.addActionListener(bc);
        TableModel model = new DefaultTableModel(new Object[][]{
                {add, 0.0, -6_371_000, 6_371_000, DEFAULT_GRAVITY_STRENGTH}
        }, new String[]{"Add/remove", "Position x", "Position y", "Radius", "Strength (G*M)"});
        gravitySources.setModel(model);
        bc.addToColumn(0);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1;
        JScrollPane scrollPane = new JScrollPane(gravitySources);
        panel.add(scrollPane, constraints);
        return panel;
    }
}
