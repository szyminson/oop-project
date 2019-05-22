package oop.project;

import java.util.Vector;

public class Engine implements IRocketPart {
    private double mass;
    private FuelContainer fuelSource;

    @Override
    public double getMass() {
        return 0;
    }
    @Override
    public Vector createThrust(Vector rotation) {
        return null;
    }
}
