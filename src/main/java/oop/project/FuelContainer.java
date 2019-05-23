package oop.project;


public class FuelContainer implements IRocketPart {
    private double containerMass;
    private double fuelMass;

    public double getRemainingFuel() {

        return this.fuelMass;
    }

    public void takeFuel(double amount) {
        this.fuelMass -= amount;
    }

    @Override
    public double getMass() {
        return this.containerMass + this.fuelMass;
    }

    @Override
    public Vector createThrust(Vector rotation) {
        return null;
    }
}
