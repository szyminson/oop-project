package oop.project;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TestEngine {
    @Test
    public void testEngineThrust() {
        final double delta = 0.0001;
        Vector direction = new Vector(1, 0); // unit vector
        Map<Double, Double> thrustMap = new HashMap<>();
        thrustMap.put(0.0, 1.0);
        thrustMap.put(1.0, 2.0);
        thrustMap.put(3.0, 3.0);
        Engine engine = new Engine(1, new FuelContainer(1, Double.MAX_VALUE), thrustMap);
        assertEquals(1.5, engine.createThrust(direction, 0.5, 0).length(), delta);
        assertEquals(2.5, engine.createThrust(direction, 2, 0).length(), delta);
    }

    @Test
    public void testEngineThrustSingleValue() {
        final double delta = 0.0001;
        Vector direction = new Vector(1, 0); // unit vector
        Map<Double, Double> thrustMap = new HashMap<>();
        thrustMap.put(0.0, 1.0);
        Engine engine = new Engine(1, new FuelContainer(1, Double.MAX_VALUE), thrustMap);
        assertEquals(1.0, engine.createThrust(direction, 0.5, 0).length(), delta);
        assertEquals(1.0, engine.createThrust(direction, 2, 0).length(), delta);
    }
}
