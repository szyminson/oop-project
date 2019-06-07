package oop.project;


public interface IRocketPart {
    double getMass();

    default Vector createThrust(Vector direction, double time, double deltaTime) {
        return new Vector(0, 0);
    }

    default double changeDirection(double currentDirection, double time) {
        return 0;
    }
}
