package oop.project.gui;

import com.google.common.collect.ImmutableMap;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;

public class SimulatorGui extends JFrame {

    // Data based on  Saturn V rocket https://en.wikipedia.org/wiki/Saturn_V, no staging
    private static final double DEFAULT_ENGINE_MASS = 8_400 + 1_788.1 * 2;
    private static final double DEFAULT_FUEL_CONTAINER_MASS = 130_000 + 40_100 + 13_500 - DEFAULT_ENGINE_MASS;
    private static final double DEFAULT_FUEL_MASS = 2_290_000 + 496_200 + 123_000 - DEFAULT_ENGINE_MASS - DEFAULT_FUEL_CONTAINER_MASS;

    // recalculated thrust to get the same acceleration, without discarding mass of previous stages
    private static final Map<Double, Double> DEFAULT_THRUST_TABLE = ImmutableMap.<Double, Double>builder()
            .put(-0.1, 0.0)
            .put(0.0, 35_100_000.0 * 5)
            .put(137.0, 35_100_000.0 * 5)// +137
            .put(137 + 0.1, 0.0)
            .put(260000 + 137.0, 0.0)
            .put(260000 + 137 + 0.1, 35_100_000.0 * 5)
            .put(260000 + 137.0 + 3, 35_100_000.0 * 5) // +10
            .put(260000 + 137 + 3 + 0.1, 0.0)
            .put(332820 + 137.0 + 3, 0.0)
            .put(332820 + 137 + 3 + 0.1, 35_100_000.0 * 5)
            .put(332820 + 168.0, 35_100_000.0 * 5)
            .put(332820 + 168.01, 6_220_344.0 * 5)
            .put(332820 + 168.0 + 360, 6_220_344.0 * 5)
            .put(332820 + 168.01 + 360, 2_461_801.0)
            .put(332820 + 168.0 + 360 + 165 + 335, 2_461_801.0)
            .put(332820 + 168.01 + 360 + 165 + 335, 0.0)
            .build();

    private static final Map<Double, Double> DEFAULT_ANGLES = ImmutableMap.<Double, Double>builder()
            .put(0.0, 90.7)
            .put(260000.0 - 10000, 90.7)
            .put(260000.0, 180.0)
            .put(332820.0 - 10000, 180.0)
            .put(332820.0, 0.0)
            .build();

    private static final double DEFAULT_GRAVITY_STRENGTH = 6.67430e-11 * 5.972e24;

    private static final double DEFAULT_WIND_STRENGTH = 0;
    private static final double DEFAULT_WIND_VARIATION = 0.001;
    private static final double DEFAULT_SURFACE_AIR_PRESSURE = 100_000;
    private static final double DEFAULT_TIME_STEP = 0.03;
    private static final double DEFAULT_MAX_TIME = 1000000;
    private static final double DEFAULT_TARGET_ALTITUDE = 1000000000;


    private JTextField engineMass;
    private JTextField fuelMass;
    private JTextField fuelContainerMass;
    private JTable thrustTable;

    private JTable pathTable;

    private JTextField windForceX;
    private JTextField windForceY;
    private JTextField windForceVariation;
    private JTextField dragConstant;
    private JTable gravitySources;

    private JLabel timeLabel;
    private JLabel externalForcesLabel;
    private JLabel positionLabel;
    private JLabel directionLabel;
    private JLabel airResistanceLabel;
    private JLabel gravityLabel;
    private JLabel windLabel;
    private JLabel velocityLabel;

    private JTextField timeStep;
    private JTextField maxTime;
    private JTextField maxDistance;
    private JTextField loggingInterval;

    private JCheckBox createLog;

    private volatile boolean simulationRunning = false;
    private SimulationWorker simulationnWorker;


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
                new Insets(1, 0, 0, 0), 0, 0);

        JPanel viewContainer = new JPanel();

        timeLabel = new JLabel("Time:");
        constraints.gridx = 0;
        constraints.weightx = 1;
        panel.add(timeLabel, constraints);

        externalForcesLabel = new JLabel("External forces:");
        constraints.gridy++;
        constraints.gridx = 0;
        panel.add(externalForcesLabel, constraints);

        gravityLabel = new JLabel("  Gravity:");
        constraints.gridy++;
        constraints.gridx = 0;
        panel.add(gravityLabel, constraints);

        windLabel = new JLabel("  Wind:");
        constraints.gridy++;
        constraints.gridx = 0;
        panel.add(windLabel, constraints);

        airResistanceLabel = new JLabel("  Air resistance:");
        constraints.gridy++;
        constraints.gridx = 0;
        panel.add(airResistanceLabel, constraints);

        velocityLabel = new JLabel("Velocity:");
        constraints.gridy++;
        constraints.gridx = 0;
        panel.add(velocityLabel, constraints);

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

        constraints.gridx = 2;
        constraints.gridheight = constraints.gridy + 1;
        constraints.gridy = 0;
        constraints.weightx = 0;
        constraints.weighty = 1;
        JPanel sidePanel = new JPanel();
        panel.add(sidePanel, constraints);
        sidePanel.setPreferredSize(new Dimension(250, 200));
        sidePanel.setLayout(new GridBagLayout());
        {

            JButton start = new JButton("Start");
            start.addActionListener(e -> startSimulation(viewContainer));
            constraints.weightx = 1;
            constraints.weighty = 0;
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.gridwidth = 2;
            constraints.gridheight = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            sidePanel.add(start, constraints);

            constraints.gridx = 0;
            constraints.gridy++;
            JButton stop = new JButton("Stop");
            stop.addActionListener(e -> stopSimulation());
            sidePanel.add(stop, constraints);

            constraints.gridwidth = 1;

            constraints.gridx = 0;
            constraints.gridy++;
            sidePanel.add(new JLabel("Time step"), constraints);
            timeStep = new JTextField(String.valueOf(DEFAULT_TIME_STEP));
            constraints.gridx = 1;
            sidePanel.add(timeStep, constraints);

            constraints.gridx = 0;
            constraints.gridy++;
            sidePanel.add(new JLabel("Stop time"), constraints);
            maxTime = new JTextField(String.valueOf(DEFAULT_MAX_TIME));
            constraints.gridx = 1;
            sidePanel.add(maxTime, constraints);

            constraints.gridx = 0;
            constraints.gridy++;
            sidePanel.add(new JLabel("Target altitude"), constraints);
            maxDistance = new JTextField(String.valueOf(DEFAULT_TARGET_ALTITUDE));
            constraints.gridx = 1;
            sidePanel.add(maxDistance, constraints);

            constraints.gridx = 0;
            constraints.gridy++;
            sidePanel.add(new JLabel("Log interval"), constraints);
            loggingInterval = new JTextField("10");
            constraints.gridx = 1;
            sidePanel.add(loggingInterval, constraints);

            constraints.gridy++;
            createLog = new JCheckBox("Create log");
            constraints.gridx = 0;
            constraints.gridwidth = 2;
            sidePanel.add(createLog, constraints);

            constraints.gridwidth = 1;

            constraints.gridx = 0;
            constraints.gridy++;
            constraints.gridwidth = 2;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.BOTH;
            sidePanel.add(new JPanel(), constraints);
        }

        return panel;
    }

    private void stopSimulation() {
        if (simulationnWorker != null) {
            simulationnWorker.stopSimulation();
        }
    }

    private void startSimulation(JPanel viewContainer) {
        if (simulationnWorker != null && simulationnWorker.isSimulationRunning()) {
            return;
        }
        simulationnWorker = new SimulationWorker.Builder()
                .gravitySources(gravitySources.getModel())
                .path(pathTable.getModel())
                .thrust(thrustTable.getModel())
                .airSurfacePressure(DEFAULT_SURFACE_AIR_PRESSURE + "")
                .dragConstant(dragConstant.getText())
                .engineMass(engineMass.getText())
                .fuelContainerMass(fuelContainerMass.getText())
                .fuelMass(fuelMass.getText())
                .maxAltitude(maxDistance.getText())
                .maxTime(maxTime.getText())
                .timeStep(timeStep.getText())
                .windForce(windForceX.getText(), windForceY.getText())
                .windVariation(windForceVariation.getText())
                .viewPanelContainer(viewContainer)
                .describeAirResistance(airResistanceLabel::setText)
                .describeExternalForces(externalForcesLabel::setText)
                .describeDirection(directionLabel::setText)
                .describeGravity(gravityLabel::setText)
                .describePosition(positionLabel::setText)
                .describeTime(timeLabel::setText)
                .describeWind(windLabel::setText)
                .describeVelocity(velocityLabel::setText)
                .loggingInverval(loggingInterval.getText())
                .createLogFile(createLog.isSelected())
                .build();
        simulationnWorker.execute();
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
        Object[][] table = new Object[DEFAULT_ANGLES.size()][3];
        {
            int i = 0;
            for (Map.Entry<Double, Double> entry : DEFAULT_ANGLES.entrySet()) {
                table[i][0] = bc.newButton();
                table[i][1] = entry.getKey();
                table[i][2] = entry.getValue();
                i++;
            }
            table[table.length - 1][0] = add;
        }
        TableModel model = new DefaultTableModel(table, new String[]{"Add/remove", "Time", "Direction angle (degrees)"});
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
        dragConstant = new JTextField("0.06");
        constraints.gridx = 1;
        panel.add(dragConstant, constraints);

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
                {bc.newButton(), 0.0, -6_371_000 + 384_399_000, 1_737_000, 7.347_673e22 * 6.67430e-11},
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
