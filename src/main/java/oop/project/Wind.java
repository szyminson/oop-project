package oop.project;

import java.util.Vector;

public class Wind implements IForceField {
    private double strengthVariation;
    private Vector windForce;

    @Override
    public Vector getForce(Vector position, Vector rotation, Vector velocity, double mass, double time) {
        return null;
    }
}
