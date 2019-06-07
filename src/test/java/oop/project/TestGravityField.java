package oop.project;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestGravityField {

    @Test
    public void testGravity() {
        GravityField field = new GravityField(new Vector(-10, 0), 2, 100);
        Vector pos = new Vector(-5, 0);

        Vector force = field.getForce(pos, null, null, 1.2, 0);
        Vector expected = new Vector(-1.2 * 2 / 25.0, 0);

        double diffLengthSq = force.sub(expected).lengthSquared();
        assertEquals(0, diffLengthSq, 0.0001);
    }
}
