package entities.animals;

import entities.Entity;
import entities.plants.Plant;
import entities.resources.waters.Water;
import entities.resources.soils.Soil;
import entities.resources.air.Air;
import simulation.Cell;
import java.util.List;
import robot.Terrabot;


public abstract class Animal extends Entity {
    private AnimalState currentAnimalState;
    private int timestampCounter;
    private double nextOrganicMatter = 0.0;
    private int x;
    private int y;
    private static final double INTAKE_RATE = 0.08;
    private static final double ORGANIC_MATTER_GROWTH = 0.5;
    public Animal(final String name, final double mass, final int x, final int y) {
        super(name, mass);
        this.x = x;
        this.y = y;
        this.currentAnimalState = AnimalState.HUNGRY;
        this.timestampCounter = 0;
    }
    public final int getX() {
        return x;
    }
    public final int getY() {
        return y;
    }
    public final void setX(final int x) {
        this.x = x;
    }
    public final void setY(final int y) {
        this.y = y;
    }
    public final AnimalState getCurrentAnimalState() {
        return currentAnimalState;
    }
    public final void setCurrentAnimalState(final AnimalState currentAnimalState) {
        this.currentAnimalState = currentAnimalState;
    }
    public abstract double getAttackPossibility();
    public abstract boolean checkIfAnimalisCarnivoreOrParasite();
    public final boolean canMove() {
        timestampCounter++;
        if (timestampCounter >= 2) {
            timestampCounter = 0;
            return true;
        }
        return false;
    }
    public final Cell chooseMoveCell(final List<Cell> neighbours, final Terrabot robot) {
        if (neighbours.isEmpty()) {
            return null;
        }
        Cell bestWaterQualityCell = null;
        double bestWaterQuality = 0.0;
        for (Cell neighbour : neighbours) {
            if (neighbour.hasPlant() && neighbour.hasWater()) {
                if (robot.containsEntity(neighbour.getPlant())
                        && robot.containsEntity(neighbour.getWater())) {
                    double currentNeighbourWaterQuality = neighbour.getWater().waterQuality();
                    if (bestWaterQualityCell == null
                            || currentNeighbourWaterQuality
                            > bestWaterQualityCell.getWater().waterQuality()) {
                        bestWaterQualityCell = neighbour;
                        bestWaterQuality = currentNeighbourWaterQuality;
                    }
                }
            }
        }
        if (bestWaterQualityCell != null) {
            return bestWaterQualityCell;
        }
        for (Cell neighbour : neighbours) {
            if (neighbour.hasPlant()) {
                if (robot.containsEntity(neighbour.getPlant())) {
                    return neighbour;
                }
            }
        }
        for (Cell neighbour : neighbours) {
            if (neighbour.hasWater()) {
                if (robot.containsEntity(neighbour.getWater())) {
                    double currentNeighbourWaterQuality = neighbour.getWater().waterQuality();
                    if (bestWaterQualityCell == null
                            || currentNeighbourWaterQuality > bestWaterQuality) {
                        bestWaterQualityCell = neighbour;
                        bestWaterQuality = currentNeighbourWaterQuality;
                    }
                }
            }
        }
        if (bestWaterQualityCell != null) {
            return bestWaterQualityCell;
        }
        return neighbours.getFirst();
    }
    public final void eatAnimal(final Animal prey, final Soil soil) {
        this.setMass(this.getMass() + prey.getMass());
        if (this.currentAnimalState != AnimalState.SICK) {
            this.currentAnimalState = AnimalState.WELL_FED;
            this.nextOrganicMatter += ORGANIC_MATTER_GROWTH;
        }
    }
    public final void eatResources(final Cell cell, final Plant plant, final Water water,
                             final Soil soil, final boolean plantScanned,
                             final boolean waterScanned) {
        if (plant != null && water != null && plantScanned && waterScanned) {
            cell.removePlant();
            double waterToDrink = Math.min(this.getMass() * INTAKE_RATE, water.getMass());
            water.drinkWater(waterToDrink);
            if (water.getMass() <= 0) {
                cell.removeWater();
            }
            this.setMass(this.getMass() + waterToDrink + plant.getMass());
            if (this.currentAnimalState != AnimalState.SICK) {
                this.currentAnimalState = AnimalState.WELL_FED;
                this.nextOrganicMatter += INTAKE_RATE;
            }
        } else if (plant != null && plantScanned) {
            cell.removePlant();
            this.setMass(this.getMass() + plant.getMass());
            if (this.currentAnimalState != AnimalState.SICK) {
                this.currentAnimalState = AnimalState.WELL_FED;
                this.nextOrganicMatter += ORGANIC_MATTER_GROWTH;
            }
        } else if (water != null && waterScanned) {
            double waterToDrink = Math.min(this.getMass() * INTAKE_RATE, water.getMass());
            water.drinkWater(waterToDrink);
            if (water.getMass() <= 0) {
                cell.removeWater();
            }
            this.setMass(this.getMass() + waterToDrink);
            if (this.currentAnimalState != AnimalState.SICK) {
                this.currentAnimalState = AnimalState.WELL_FED;
                this.nextOrganicMatter += ORGANIC_MATTER_GROWTH;
            }
        } else {
            if (this.currentAnimalState != AnimalState.SICK) {
                this.currentAnimalState = AnimalState.HUNGRY;
            }
        }
    }
    public final double getAndResetOrganicMatter() {
        double amount = this.nextOrganicMatter;
        this.nextOrganicMatter = 0.0;
        return amount;
    }
    public final void checkAirToxicity(final Air air) {
        if (air != null && air.isAirToxic()) {
            this.currentAnimalState = AnimalState.SICK;
        }
    }
    public abstract String getType();

}
