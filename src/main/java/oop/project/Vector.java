package oop.project;

public class Vector {
    private final double x, y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
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
        return this.x * v.x + this.y * v.y;
    }

    public double lengthSquared() {
        return this.x*this.x + this.y*this.y;
    }

    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    public Vector normalize() {
        double lengthInverse = 1.0 / this.length();
        return new Vector(this.x * lengthInverse, this.y * lengthInverse);
    }
}
