package oop.project;


import java.util.Objects;

/**
 * An implementation of mathematical vector in the project.
 */
public class Vector {

    /**
     * Coordinates of this vector.
     */
    private final double x, y;

    /**
     * Sets x and y coordinates of this vector.
     * @param x Vector coordinate.
     * @param y Vector coordinate.
     */
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets x coordinate of this vector.
     * @return x coordinate of this vector.
     */
    public double getX() {
        return this.x;
    }

    /**
     * Gets y coordinate of this vector.
     * @return y coordinate of this vector.
     */
    public double getY() {
        return this.y;
    }

    /**
     * Adds given vector to this vector.
     * @param v Given vector object.
     * @return Sum of two vectors.
     */
    public Vector add(Vector v) {
        return new Vector(this.x + v.x, this.y + v.y);
    }

    /**
     * Subtracts given vector from this vector.
     * @param v Given vector object.
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
     * Scalar multiplication of this vector by given vector.
     * @param v Given vector object.
     * @return A number, scalar product of multiplication.
     */
    public double dotProduct(Vector v) {
        return this.x * v.x + this.y * v.y;
    }

    /**
     * Gets squared length of this vector.
     * @return Squared length of this vector.
     */
    public double lengthSquared() {
        return this.x*this.x + this.y*this.y;
    }

    /**
     * Gets length of this vector.
     * @return Length of this vector.
     */
    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    /**
     * Gets angle of this vector.
     * @return Angle of this vector.
     */
    public double getAngle() {
        return Math.atan2(this.y, this.x);
    }

    /**
     * Normalizes this vector.
     * @return New, normalized vector object.
     */
    public Vector normalize() {
        double lengthInverse = 1.0 / this.length();
        return new Vector(this.x * lengthInverse, this.y * lengthInverse);
    }

    /**
     * Creates a string representation of this vector.
     * @return String representation of this vector.
     */
    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    /**
     * Checks whether this vector is equal to given object.
     * @param o Given object.
     * @return Boolean value depending on the result.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return Double.compare(vector.x, x) == 0 &&
                Double.compare(vector.y, y) == 0;
    }

    /**
     * Generates a hash code of this vector.
     * @return Generated hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
