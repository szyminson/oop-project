package oop.project;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class TestRocket {
    @Test
    public void testRocketThrust() {
        int stepsPerSecond = 10000;
        double dt = 1.0 / stepsPerSecond;

        final Vector zero = new Vector(0, 0);

        IRocketPart testEngine = new IRocketPart() {
            @Override
            public double getMass() {
                return 1;
            }

            @Override
            public Vector createThrust(Vector direction, double time, double deltaTime) {
                return direction;
            }
        };
        Rocket rocket = new Rocket(Collections.singletonList(testEngine), zero, 0);
        for (int i = 0; i < stepsPerSecond * 2; i++) {
            double time = i / (double) stepsPerSecond;
            rocket.updateRocket(zero, time, dt);
        }
        // mass = 1, force = 1, time = 2, a = F/m -> a = 1. s = a*t*t/2 = 1 * 2 * 2 / 2 = 2
        Vector pos = rocket.getPosition();
        Vector expectedPos = new Vector(2, 0);
        double posDiff = expectedPos.sub(pos).length();
        assertEquals("rocketPos = " + pos + " but expected " + expectedPos, 0, posDiff, 0.0001);
    }

    @Test
    public void testRocketExternalForce() {
        int stepsPerSecond = 10000;
        double dt = 1.0 / stepsPerSecond;

        final Vector zero = new Vector(0, 0);

        IRocketPart testPart = new IRocketPart() {
            @Override
            public double getMass() {
                return 1;
            }
        };
        Rocket rocket = new Rocket(Collections.singletonList(testPart), zero, 0);
        for (int i = 0; i < stepsPerSecond * 2; i++) {
            double time = i / (double) stepsPerSecond;
            rocket.updateRocket(new Vector(1, 0), time, dt);
        }
        // mass = 1, force = 1, time = 2, a = F/m -> a = 1. s = a*t*t/2 = 1 * 2 * 2 / 2 = 2
        Vector pos = rocket.getPosition();
        Vector expectedPos = new Vector(2, 0);
        double posDiff = expectedPos.sub(pos).length();
        assertEquals("rocketPos = " + pos + " but expected " + expectedPos, 0, posDiff, 0.0001);
    }

    @Test
    public void testRocketChangeDirection() {
        int stepsPerSecond = 100000;
        double dt = 1.0 / stepsPerSecond;

        final Vector zero = new Vector(0, 0);

        IRocketPart testEngine = new IRocketPart() {
            @Override
            public double getMass() {
                return 1;
            }

            @Override
            public double changeDirection(double currentDirection, double time) {
                double target = time * Math.PI;
                return target - currentDirection;
            }
        };
        Rocket rocket = new Rocket(Collections.singletonList(testEngine), zero, 0);
        // do half of a full rotation and check if the direction changed sign
        for (int i = 0; i < stepsPerSecond; i++) {
            double time = i / (double) stepsPerSecond;
            rocket.updateRocket(zero, time, dt);
        }
        double direction = rocket.getDirection();
        double expectedDirection = Math.PI;
        assertEquals("rocketDirection = " + direction + " but expected " + expectedDirection, expectedDirection, direction, 0.0001);
    }
}
