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
     * Sets x and y coordinates of this Vector.
     *
     * @param x Vector coordinate.
     * @param y Vector coordinate.
     */
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets x coordinate of this Vector.
     *
     * @return x coordinate of this Vector.
     */
    public double getX() {
        return this.x;
    }

    /**
     * Gets y coordinate of this Vector.
     *
     * @return y coordinate of this Vector.
     */
    public double getY() {
        return this.y;
    }

    /**
     * Adds given vector to this Vector.
     *
     * @param v Given vector object.
     * @return Sum of two Vectors.
     */
    public Vector add(Vector v) {
        return new Vector(this.x + v.x, this.y + v.y);
    }

    /**
     * Subtracts given Vector from this Vector.
     *
     * @param v Given Vector object.
     * @return Difference between two Vectors.
     */
    public Vector sub(Vector v) {
        return new Vector(this.x - v.x, this.y - v.y);
    }

    /**
     * Multiplies this Vector by given number.
     *
     * @param d Number.
     * @return This Vector multiplied.
     */
    public Vector mul(double d) {
        return new Vector(this.x * d, this.y * d);
    }

    /**
     * Divides this Vector by given number.
     *
     * @param d Number.
     * @return This Vector divided.
     */
    public Vector div(double d) {
        return new Vector(this.x / d, this.y / d);
    }

    /**
     * Scalar multiplication of this Vector by given Vector.
     *
     * @param v Given Vector object.
     * @return A number, scalar product of multiplication.
     */
    public double dotProduct(Vector v) {
        return this.x * v.x + this.y * v.y;
    }

    /**
     * Gets squared length of this Vector.
     *
     * @return Squared length of this Vector.
     */
    public double lengthSquared() {
        return this.x * this.x + this.y * this.y;
    }

    /**
     * Gets length of this Vector.
     *
     * @return Length of this Vector.
     */
    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    /**
     * Gets angle of this Vector.
     *
     * @return Angle of this Vector.
     */
    public double getAngle() {
        return Math.atan2(this.x, this.y);
    }

    /**
     * Normalizes this Vector.
     *
     * @return New, normalized Vector object.
     */
    public Vector normalize() {
        double lengthInverse = 1.0 / this.length();
        return new Vector(this.x * lengthInverse, this.y * lengthInverse);
    }

    /**
     * Creates a string representation of this Vector.
     *
     * @return String representation of this Vector.
     */
    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    /**
     * Checks whether this Vector is equal to given object.
     *
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
     * Generates a hash code of this Vector.
     *
     * @return Generated hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
