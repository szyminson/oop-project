package oop.project;


import java.util.Objects;

/**
 * An implementation of mathematical vector in the project.
 */
public class Vector {
    private final double x, y;

    /**
     * Sets x and y coordinates' of the vector.
     * @param x Vector coordinate.
     * @param y Vector coordinate.
     */
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gives access to x coordinate from outside of the class.
     * @return Returns x coordinate of the vector.
     */
    public double getX() {
        return this.x;
    }

    /**
     * Gives access to y coordinate from outside of the class.
     * @return Returns y coordinate of the vector.
     */
    public double getY() {
        return this.y;
    }

    /**
     * Adds other vector to this vector.
     * @param v Other vector object.
     * @return Sum of two vectors.
     */
    public Vector add(Vector v) {
        return new Vector(this.x + v.x, this.y + v.y);
    }

    /**
     * Subtracts other vector from this vector.
     * @param v Other vector object.
     * @return Difference between two vectors.
     */
    public Vector sub(Vector v) {
        return new Vector(this.x - v.x, this.y - v.y);
    }

    /**
     * Multiplies this vector by given number.
     * @param d Number.
     * @return This vector multiplied.
     */
    public Vector mul(double d) {
        return new Vector(this.x * d, this.y * d);
    }

    /**
     * Divides this vector by given number.
     * @param d Number.
     * @return This vector divided.
     */
    public Vector div(double d) {
        return new Vector(this.x / d, this.y / d);
    }

    /**
     * Scalar multiplication of this vector and other vector.
     * @param v Other vector object.
     * @return A number, scalar product of multiplication.
     */
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
