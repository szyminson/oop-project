package oop.project;

/**
 * Just a fuel container.
 */
public class FuelContainer implements IRocketPart {

    /**
     * Mass of empty container.
     */
    private final double containerMass;
    /**
     * Mass of this FuelContainer's fuel.
     */
    private double fuelMass;

    /**
     * Sets containerMass and fuelMass of this FuelContainer.
     *
     * @param containerMass Given container mass.
     * @param fuelMass      Given fuel mass.
     */
    public FuelContainer(double containerMass, double fuelMass) {
        this.containerMass = containerMass;
        this.fuelMass = fuelMass;
    }

    /**
     * Gets RemainingFuel in this FuelContainer.
     *
     * @return fuelMass of this FuelContainer.
     */
    public double getRemainingFuel() {
        return this.fuelMass;
    }

    /**
     * Takes given amount of fuel from this FuelContainer.
     *
     * @param amount Given amount.
     **/
    public void takeFuel(double amount) {
        this.fuelMass -= amount;
        if (this.fuelMass < 0) {
            this.fuelMass = 0;
        }
    }

    /**
     * Gets Mass of this FuelContainer.
     *
     * @return Combined mass of this FuelContainer.
     */
    @Override
    public double getMass() {
        return this.containerMass + this.fuelMass;
    }
}
