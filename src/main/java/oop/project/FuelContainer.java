package oop.project;

import java.util.Vector;

public class FuelContainer implements IRocketPart {
    private double containerMass;
    private double fuelMass;

    public double getRemainingFuel() {
        return 0;
    }

    public void takeFuel(double amount) {

    }

    @Override
    public double getMass() {
        return 0;
    }

    @Override
    public Vector createThrust(Vector rotation) {
        return null;
    }
}
