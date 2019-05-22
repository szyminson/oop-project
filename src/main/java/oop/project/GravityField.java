package oop.project;


public class GravityField implements IForceField {
    private double strength;
    private Vector sourcePosition;

    @Override
    public Vector getForce(Vector position, Vector rotation, Vector velocity, double mass, double time) {
        return null;
    }
}
