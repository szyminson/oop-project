package oop.project;


public class Engine implements IRocketPart {
    private double mass;
    private FuelContainer fuelSource;

    @Override
    public double getMass() {
        return this.mass;
    }

    @Override
    public Vector createThrust(Vector rotation) {
        return null;
    }
}
