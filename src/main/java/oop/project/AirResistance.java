package oop.project;

import java.util.Vector;

public class AirResistance implements IForceField {
    private double dragCoefficient;

    @Override
    public Vector getForce(Vector position, Vector rotation, Vector velocity, double mass, double time) {
        return null;
    }
}