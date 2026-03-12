package simulation;
import entities.animals.Animal;
import entities.plants.Plant;
import entities.resources.waters.Water;
import entities.resources.air.Air;
import entities.resources.soils.Soil;

public final class Cell {
    private int x;
    private int y;
    private Animal animal;
    private Plant plant;
    private Air air;
    private Soil soil;
    private Water water;
    public Cell(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setSoil(final Soil soil) {
        this.soil = soil;
    }
    public void setAir(final Air air) {
        this.air = air;
    }
    public void setPlant(final Plant plant) {
        this.plant = plant;
    }
    public void setWater(final Water water) {
        this.water = water;
    }
    public void setAnimal(final Animal animal) {
        this.animal = animal;
    }
    public Soil getSoil() {
        return soil;
    }
    public Air getAir() {
        return air;
    }
    public Plant getPlant() {
        return plant;
    }
    public Water getWater() {
        return water;
    }
    public Animal getAnimal() {
        return animal;
    }

    /**
     * Checks if cell has Animal in it.
     * @return true if cell has Animal, false otherwise
     */
    public boolean hasAnimal() {
        return animal != null;
    }

    /**
     * Checks if cell has Plant in it.
     * @return true if Cell has plant, false otherwise
     */
    public boolean hasPlant() {
        return plant != null;
    }

    /**
     * Checks if cell has Water in it.
     * @return true if cell has Water, false otherwise
     */
    public boolean hasWater() {
        return water != null;
    }

    /**
     * Removes the animal from the cell, setting it to null.
     */
    public void removeAnimal() {
        this.animal = null;
    }

    /**
     * Removes the plant from the cell, setting it to null.
     */
    public void removePlant() {
        this.plant = null;
    }

    /**
     * Removes the water from the cell, setting it to null.
     */
    public void removeWater() {
        this.water = null;
    }





}
