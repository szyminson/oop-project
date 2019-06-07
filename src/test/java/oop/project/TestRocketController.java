package oop.project;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TestRocketController {
    @Test
    public void testDirectionEmpty() {
        Map<Double, Vector> path = new HashMap<>();
        RocketController controller = new RocketController(path);

        double expected = 0;

        assertEquals(expected, controller.changeDirection(new Vector(1, 1).normalize(), 1.234), 0);
    }

    @Test
    public void testDirectionSingleValue() {
        Map<Double, Vector> path = new HashMap<>();
        path.put(1.0, new Vector(0, 1));

        RocketController controller = new RocketController(path);

        Vector currentRotation = new Vector(1, 0);
        double expected = Math.PI/2; // 90 degrees rotation

        assertEquals(expected, controller.changeDirection(currentRotation, 1.234), 0.0001);
    }

    @Test
    public void testDirection() {
        Map<Double, Vector> path = new HashMap<>();
        path.put(0.0, new Vector(0, 1));
        path.put(2.0, new Vector(1, 0));

        RocketController controller = new RocketController(path);

        Vector currentRotation = new Vector(0, 1);
        double expected = -Math.PI/4; // -45 degrees rotation

        assertEquals(expected, controller.changeDirection(currentRotation, 1), 0.0001);
    }
}
