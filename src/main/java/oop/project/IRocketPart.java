package oop.project;


public interface IRocketPart {
    double getMass();

    default Vector createThrust(Vector direction, double time) {
        return new Vector(0, 0);
    }

    default double changeDirection(Vector currentDirection, double time) {
        return 0;
    }
}
