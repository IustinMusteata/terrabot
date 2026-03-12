package simulation;
import entities.animals.Animal;
import entities.animals.Carnivore;
import entities.animals.Herbivore;
import entities.animals.Detritivore;
import entities.animals.Omnivore;
import entities.animals.Parasite;
import entities.plants.Plant;
import entities.plants.FloweringPlant;
import entities.plants.GymnospermsPlant;
import entities.plants.Alga;
import entities.plants.Fern;
import entities.plants.Moss;
import entities.resources.waters.Lake;
import entities.resources.waters.Pond;
import entities.resources.waters.River;
import entities.resources.waters.Water;
import entities.resources.air.Air;
import entities.resources.air.TemperateAir;
import entities.resources.air.DesertAir;
import entities.resources.air.PolarAir;
import entities.resources.air.MountainAir;
import entities.resources.air.TropicalAir;
import entities.resources.soils.Soil;
import entities.resources.soils.DesertSoil;
import entities.resources.soils.ForestSoil;
import entities.resources.soils.SwampSoil;
import entities.resources.soils.GrasslandSoil;
import entities.resources.soils.TundraSoil;
import fileio.AirInput;
import fileio.AnimalInput;
import fileio.PlantInput;
import fileio.SoilInput;
import fileio.WaterInput;

public final class EntityCreator {
    private EntityCreator() { }

    /**
     * This method instantiates the class required for the specific type of soil based on input
     * @param input the type of soil required
     * @return an instance of the specific type of soil
     */
    public static Soil createSoil(final SoilInput input) {
        switch (input.getType()) {
            case "ForestSoil": return new ForestSoil(input.getName(), input.getMass(),
                    input.getNitrogen(), input.getWaterRetention(),
                    input.getSoilpH(), input.getOrganicMatter(), input.getLeafLitter());
            case "DesertSoil": return new DesertSoil(input.getName(), input.getMass(),
                    input.getNitrogen(), input.getWaterRetention(), input.getSoilpH(),
                    input.getOrganicMatter(), input.getSalinity());
            case "GrasslandSoil": return new GrasslandSoil(input.getName(),
                    input.getMass(), input.getNitrogen(),
                    input.getWaterRetention(), input.getSoilpH(),
                    input.getOrganicMatter(), input.getRootDensity());
            case "SwampSoil": return new SwampSoil(input.getName(), input.getMass(),
                    input.getNitrogen(), input.getWaterRetention(),
                    input.getSoilpH(), input.getOrganicMatter(), input.getWaterLogging());
            case "TundraSoil": return new TundraSoil(input.getName(), input.getMass(),
                    input.getNitrogen(), input.getWaterRetention(), input.getSoilpH(),
                    input.getOrganicMatter(), input.getPermafrostDepth());
            default: return null;
        }
    }

    /**
     * This method instantiates the class required for the specific type of plant based on input
     * @param input The type of plant
     * @return an instance of the specific type of plant
     */
    public static Plant createPlant(final PlantInput input) {
        switch (input.getType()) {
            case "Algae": return new Alga(input.getName(), input.getMass());
            case "Ferns": return new Fern(input.getName(), input.getMass());
            case "FloweringPlants": return new FloweringPlant(input.getName(), input.getMass());
            case "GymnospermsPlants": return new GymnospermsPlant(input.getName(),
                    input.getMass());
            case "Mosses": return new Moss(input.getName(), input.getMass());
            default: return null;
        }
    }

    /**
     * This method instantiates the class required for the specific type of Air based on input
     * @param input The type of Air
     * @return an instance of the specific type of air
     */
    public static Air createAir(final AirInput input) {
        switch (input.getType()) {
            case "DesertAir": return new DesertAir(input.getName(), input.getMass(),
                    input.getHumidity(), input.getTemperature(),
                    input.getOxygenLevel(), input.getDustParticles());
            case "MountainAir": return new MountainAir(input.getName(), input.getMass(),
                    input.getHumidity(), input.getTemperature(),
                    input.getOxygenLevel(), input.getAltitude());
            case "PolarAir": return new PolarAir(input.getName(), input.getMass(),
                    input.getHumidity(), input.getTemperature(),
                    input.getOxygenLevel(), input.getIceCrystalConcentration());
            case "TemperateAir": return new TemperateAir(input.getName(), input.getMass(),
                    input.getHumidity(), input.getTemperature(),
                    input.getOxygenLevel(), input.getPollenLevel());
            case "TropicalAir": return new TropicalAir(input.getName(),
                    input.getMass(), input.getHumidity(), input.getTemperature(),
                    input.getOxygenLevel(), input.getCo2Level());
            default: return null;
        }
    }

    /**
     * This method instantiates the class required
     * for the specific type of animal, based on input and coordinates
     * @param input The type of animal
     * @param x row index to put the Animal
     * @param y column index to put the Animal
     * @return an instance of the specific type of Animal
     */
    public static Animal createAnimal(final AnimalInput input, final int x, final int y) {
        switch (input.getType()) {
            case "Carnivores": return new Carnivore(input.getName(), input.getMass(), x, y);
            case "Detritivores": return new Detritivore(input.getName(), input.getMass(), x, y);
            case "Herbivores": return new Herbivore(input.getName(), input.getMass(), x, y);
            case "Omnivores": return new Omnivore(input.getName(), input.getMass(), x, y);
            case "Parasites": return new Parasite(input.getName(), input.getMass(), x, y);
            default: return null;
        }
    }

    /**
     * This method instantiates the class required for the specific type of Water, based on input
     * @param input The type of water
     * @return an instanced of the specific type of Water
     */
    public static Water createWater(final WaterInput input) {
        switch (input.getType()) {
            case "lake": return new Lake(input.getName(), input.getMass(), input.getSalinity(),
                    input.getPH(), input.getPurity(), input.getTurbidity(),
                    input.getContaminantIndex(), input.isFrozen());
            case "river": return new River(input.getName(), input.getMass(), input.getSalinity(),
                    input.getPH(), input.getPurity(), input.getTurbidity(),
                    input.getContaminantIndex(), input.isFrozen());
            case "pond": return new Pond(input.getName(), input.getMass(), input.getSalinity(),
                    input.getPH(), input.getPurity(), input.getTurbidity(),
                    input.getContaminantIndex(), input.isFrozen());
            default: return null;
        }
    }
}
