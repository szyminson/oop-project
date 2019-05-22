package oop.project;

import java.util.Vector;

public interface IForceField {
    public Vector getForce(Vector position, Vector rotation, Vector velocity, double mass, double time);
}
