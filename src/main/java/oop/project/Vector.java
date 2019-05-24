package oop.project;

import java.util.Objects;

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

    public double getAngle() {
        return Math.atan2(this.x, this.y);
    }

    public Vector normalize() {
        double lengthInverse = 1.0 / this.length();
        return new Vector(this.x * lengthInverse, this.y * lengthInverse);
    }

    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return Double.compare(vector.x, x) == 0 &&
                Double.compare(vector.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
