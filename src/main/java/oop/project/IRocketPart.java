package oop.project;

import java.util.Vector;

public interface IRocketPart {
    public double getMass();

    public Vector createThrust(Vector rotation);
}
