package oop.project;

public class FuelContainer implements IRocketPart {

    /**
     * Parameters of this FuelContainer.
     */
    private final double containerMass;
    private double fuelMass;

    /**
     * Sets containerMass and fuelMass of this FuelContainer.
     *
     * @param containerMass of FuelContainer.
     * @param fuelMass      of FuelContainer.
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
