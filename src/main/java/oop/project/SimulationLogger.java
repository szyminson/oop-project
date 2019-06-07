package oop.project;

import java.io.*;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Is used to log simulation's output into a csv file.
 */
public class SimulationLogger implements AutoCloseable {

    /**
     * Locale parameter to prevent from inserting additional comas into csv file.
     */
    private final Locale lcl = Locale.ROOT;

    /**
     * This SimulationLogger's PrintWriter.
     */
    private final PrintWriter writer;

    /**
     * Creates a PrintWriter for given file and writes headers to the file.
     *
     * @param filePath Given file path.
     * @throws FileNotFoundException Throws exception if given file does not exist.
     */
    public SimulationLogger(String filePath) throws FileNotFoundException {
        this.writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(new File(filePath))));

        this.writer.println("time," +
                "rocketMass," +
                "fuelMass," +
                "rocketPositionX," +
                "rocketPositionY," +
                "rocketDirectionX," +
                "rocketDirectionY," +
                "rocketVelocityX," +
                "rocketVelocityY," +
                "windDirectionX," +
                "windDirectionY," +
                "windForceX," +
                "windForceY," +
                "gravityFieldDirectionX," +
                "gravityFieldDirectionY," +
                "gravityFieldForceX," +
                "gravityFieldForceY," +
                "airResistanceDirectionX," +
                "airResistanceDirectionY," +
                "airResistanceForceX," +
                "airResistanceForceY");
    }

    /**
     * Logs given parameters into csv file opened in this SimulationLogger's constructor.
     *
     * @param time                   Given time.
     * @param rocketMass             Given mass of the rocket.
     * @param fuelMass               Given mass of rocket's fuel.
     * @param rocketPosition         Given position of the rocket.
     * @param rocketDirection        Given direction of the rocket.
     * @param rocketVelocity         Given velocity of the rocket.
     * @param windDirection          Given direction of wind.
     * @param windForce              Given force of wind.
     * @param gravityFieldDirection  Given direction of gravity field.
     * @param gravityFieldForce      Given force of gravity field
     * @param airResistanceDirection Given direction of air resistance.
     * @param airResistanceForce     Given force of air resistance.
     */
    public void log(double time, double rocketMass, double fuelMass, Vector rocketPosition, Vector rocketDirection, Vector rocketVelocity, Vector windDirection, Vector windForce, Vector gravityFieldDirection, Vector gravityFieldForce, Vector airResistanceDirection, Vector airResistanceForce) {
        NumberFormat nf = NumberFormat.getNumberInstance(this.lcl);
        nf.setGroupingUsed(false);
        this.writer.println(nf.format(time) +
                ',' + nf.format(rocketMass) +
                ',' + nf.format(fuelMass) +
                ',' + nf.format(rocketPosition.getX()) +
                ',' + nf.format(rocketPosition.getY()) +
                ',' + nf.format(rocketDirection.getX()) +
                ',' + nf.format(rocketDirection.getY()) +
                ',' + nf.format(rocketVelocity.getX()) +
                ',' + nf.format(rocketVelocity.getY()) +
                ',' + nf.format(windDirection.getX()) +
                ',' + nf.format(windDirection.getY()) +
                ',' + nf.format(windForce.getX()) +
                ',' + nf.format(windForce.getY()) +
                ',' + nf.format(gravityFieldDirection.getX()) +
                ',' + nf.format(gravityFieldDirection.getY()) +
                ',' + nf.format(gravityFieldForce.getX()) +
                ',' + nf.format(gravityFieldForce.getY()) +
                ',' + nf.format(airResistanceDirection.getX()) +
                ',' + nf.format(airResistanceDirection.getY()) +
                ',' + nf.format(airResistanceForce.getX()) +
                ',' + nf.format(airResistanceForce.getY())
        );
    }

    public void close() {
        this.writer.close();
    }
}
