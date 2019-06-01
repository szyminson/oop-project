package oop.project;


public class FuelContainer implements IRocketPart {
    private final double containerMass;
    private double fuelMass;

    public FuelContainer(double containerMass, double fuelMass) {
        this.containerMass = containerMass;
        this.fuelMass = fuelMass;
    }

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
}
