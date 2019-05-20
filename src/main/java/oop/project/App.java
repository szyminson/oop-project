/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package oop.project;

import java.util.List;
import java.util.Vector;

public class App
{
    public String getGreeting()
    {
        return "Hello world.";
    }

    public static void main(String[] args)
    {
        System.out.println(new App().getGreeting());
    }
}

class World
{
    private Rocket rocket;
    private List <IForceField>forceFields;

    public void runSimulation(){};
}

interface IForceField
{
    public Vector getForce(Vector position, Vector rotation, Vector velocity, double mass, double time);
}

class Wind implements IForceField
{
    private double strengthVariation;
    private Vector windForce;

    @Override
    public Vector getForce(Vector position, Vector rotation, Vector velocity, double mass, double time) {return null;}
}

class GravityField implements IForceField
{
    private double strength;
    private Vector sourcePosition;

    @Override
    public Vector getForce(Vector position, Vector rotation, Vector velocity, double mass, double time) {return null;}
}

class AirResistance implements IForceField
{
    private double dragCoefficient;

    @Override
    public Vector getForce(Vector position, Vector rotation, Vector velocity, double mass, double time) {return null;}
}

class Rocket
{
    private List <IRocketPart> parts;
    private Vector position;
    private Vector direction;
    private Vector velocity;

    public double getMass() {return 0;}
    public Vector getVelocity() {return null;}
    public Vector getPosition() {return null;}
    public Vector getDirection() {return null;}
    public Iterable <IRocketPart> getParts() {return null;}
    public void updateRocket (Vector externalForce, double deltaTime) {};
}

interface IRocketPart
{
    public double getMass();
    public Vector createThrust(Vector rotation);
}

class Engine implements IRocketPart
{
    private double mass;
    private FuelContainer fuelSource;

    @Override
    public double getMass() {return 0;}
    @Override
    public Vector createThrust(Vector rotation) {return null;}
}

class FuelContainer implements IRocketPart
{
    private double containerMass;
    private double fuelMass;

    public double getRemainingFuel() {return 0;}
    public void takeFuel(double amount) {}

    @Override
    public double getMass() {return 0;}
    @Override
    public Vector createThrust(Vector rotation) {return null;}
}
