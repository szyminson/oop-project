package oop.project;

public class Vector {
    private final double x, y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vector add(Vector v) {
        return new Vector(this.x + v.x, this.y + v.y);
    }

    public Vector sub(Vector v) {
        return new Vector(this.x - v.x, this.y - v.y);
    }

    public Vector mul(double d) {
        return new Vector(this.x * d, this.y * d);
    }

    public Vector div(double d) {
        return new Vector(this.x / d, this.y / d);
    }

    public double dotProduct(Vector v) {
        return this.x*v.x + this.y*v.y;
    }
}
