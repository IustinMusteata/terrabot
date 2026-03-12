package simulation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.animals.Animal;
import entities.plants.Plant;
import entities.resources.waters.Water;
import entities.resources.air.Air;
import entities.resources.soils.Soil;
import fileio.AirInput;
import fileio.AnimalInput;
import fileio.WaterInput;
import fileio.PlantInput;
import fileio.CommandInput;
import fileio.PairInput;
import fileio.SimulationInput;
import fileio.SoilInput;
import fileio.TerritorySectionParamsInput;
import robot.Terrabot;
import robot.Topic;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Simulation {
    private Map map;
    private boolean running;
    private Terrabot robot;
    private final ObjectMapper mapper = new ObjectMapper();
    private int robotTimeToCharge = -1;
    private int lastTimestamp = 0;
    private static final double NR_OF_NEIGHBOURS = 4;
    private static final double MATURITY_GROWTH = 0.2;
    private static final double HUMIDITY_GROWTH = 0.1;
    private static final double WATER_RETENTION_GROWTH = 0.1;
    private static final int BATTERY_COST_SCANOBJECT = 7;
    private static final int BATTERY_COST_LEARNFACT = 2;
    private static final int BATTERY_COST_IMPROVEENV = 10;
    private static final double VEGETATION_GROWTH = 0.3;
    private static final double FERTILIZE_GROWTH = 0.3;
    private static final double INCREASE_HUMIDITY_GROWTH = 0.2;
    private static final double INCREASE_MOISTURE_GROWTH = 0.2;

    /**
     * Starts the simulation by instantiating the cells, the map and the robot
     * @param simulationInput the Input found in fileio.
     */
    public void startSimulation(final SimulationInput simulationInput) {
        String[] arrayDimensions = simulationInput.getTerritoryDim().split("x");
        int n = Integer.parseInt(arrayDimensions[0]);
        int m = Integer.parseInt(arrayDimensions[1]);
        Cell[][] cells = new Cell[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
        this.map = new Map(n, m, cells);
        this.running = true;
        this.robot = new Terrabot(0, 0, simulationInput.getEnergyPoints());
        fillMap(simulationInput.getTerritorySectionParams());
    }
    private void fillMap(final TerritorySectionParamsInput parameters) {
        if (parameters.getSoil() != null) {
            for (SoilInput soilInput : parameters.getSoil()) {
                for (PairInput sections : soilInput.getSections()) {
                    Soil soil = EntityCreator.createSoil(soilInput);
                    if (map.checkCellPos(sections.getX(), sections.getY())) {
                        map.getCell(sections.getX(), sections.getY()).setSoil(soil);
                    }
                }
            }
        }
        if (parameters.getWater() != null) {
            for (WaterInput waterInput : parameters.getWater()) {
                for (PairInput sections : waterInput.getSections()) {
                    Water water = EntityCreator.createWater(waterInput);
                    if (water != null && map.checkCellPos(sections.getX(), sections.getY())) {
                        map.getCell(sections.getX(), sections.getY()).setWater(water);
                    }
                }
            }
        }
        if (parameters.getAir() != null) {
            for (AirInput airInput : parameters.getAir()) {
                for (PairInput sections : airInput.getSections()) {
                    Air air = EntityCreator.createAir(airInput);
                    if (map.checkCellPos(sections.getX(), sections.getY())) {
                        map.getCell(sections.getX(), sections.getY()).setAir(air);
                    }
                }
            }
        }
        if (parameters.getPlants() != null) {
            for (PlantInput plantInput : parameters.getPlants()) {
                for (PairInput sections : plantInput.getSections()) {
                    Plant plant = EntityCreator.createPlant(plantInput);
                    if (plant != null && map.checkCellPos(sections.getX(), sections.getY())) {
                        map.getCell(sections.getX(), sections.getY()).setPlant(plant);
                    }
                }
            }
        }
        if (parameters.getAnimals() != null) {
            for (AnimalInput animalInput : parameters.getAnimals()) {
                for (PairInput sections : animalInput.getSections()) {
                    Animal animal = EntityCreator.createAnimal(
                            animalInput, sections.getX(), sections.getY());
                    if (animal != null && map.checkCellPos(sections.getX(), sections.getY())) {
                        map.getCell(sections.getX(), sections.getY()).setAnimal(animal);
                    }
                }
            }
        }
    }

    /**
     * Command used to parse and select which command needs to be ran according to the input
     * @param commandInput The command from the input
     * @param simulationInput The simulation coordinates and data for the robot,
     *                        map and cells, from the input
     * @param output The node for the output, where the data for output will be added
     */
    public void runCommand(final CommandInput commandInput,
                           final SimulationInput simulationInput, final ArrayNode output) {
        switch (commandInput.getCommand()) {
            case "startSimulation":
                printStartSimulation(commandInput, simulationInput, output);
                break;
            case "endSimulation":
                printEndSimulation(commandInput, output);
                break;
            case "printMap":
                printMap(commandInput, output);
                break;
            case "printEnvConditions":
                printEnvConditions(commandInput, output);
                break;
            case "moveRobot":
                moveRobot(commandInput, output);
                break;
            case "getEnergyStatus":
                getEnergyStatus(commandInput, output);
                break;
            case "rechargeBattery":
                rechargeBattery(commandInput, output);
                break;
            case "changeWeatherConditions":
                changeWeatherConditions(commandInput, output);
                break;
            case "scanObject":
                scanObject(commandInput, output);
                break;
            case "learnFact":
                learnFact(commandInput, output);
                break;
            case "printKnowledgeBase":
                printKnowledgeBase(commandInput, output);
                break;
            case "improveEnvironment":
                improveEnvironment(commandInput, output);
                break;
            default:
                break;
        }
    }
    private void printStartSimulation(final CommandInput commandInput,
                                      final SimulationInput simulationInput,
                                      final ArrayNode output) {
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "startSimulation");
        if (running) {
            node.put("message", "ERROR: Simulation already started. Cannot perform action");
        } else {
            startSimulation(simulationInput);
            lastTimestamp = commandInput.getTimestamp();
            node.put("message", "Simulation has started.");
        }
        node.put("timestamp", commandInput.getTimestamp());
        output.add(node);
    }
    private void runEnvironmentUpdates(final int timestamp) {
        for (int i = lastTimestamp + 1; i <= timestamp; i++) {
            updateEnvironment(i);
        }
        lastTimestamp = timestamp;
    }
    private void printEndSimulation(final CommandInput commandInput, final ArrayNode output) {
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "endSimulation");
        if (!running) {
            node.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else {
            running = false;
            node.put("message", "Simulation has ended.");
        }
        node.put("timestamp", commandInput.getTimestamp());
        output.add(node);
    }
    private void printMap(final CommandInput commandInput, final ArrayNode output) {
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "printMap");
        if (!running) {
            node.put("message", "ERROR: Simulation not started. Cannot perform action");
            node.put("timestamp", commandInput.getTimestamp());
            output.add(node);
            return;
        }
        if (commandInput.getTimestamp() < robotTimeToCharge) {
            node.put("message", "ERROR: Robot still charging. Cannot perform action");
            node.put("timestamp", commandInput.getTimestamp());
            output.add(node);
            return;
        }
        runEnvironmentUpdates(commandInput.getTimestamp());
        ArrayNode arrayNode = mapper.createArrayNode();
        for (int j = 0; j < map.getM(); j++) {
            for (int i = 0; i < map.getN(); i++) {
                Cell cell =  map.getCell(i, j);
                ObjectNode cellNode = mapper.createObjectNode();
                ArrayNode sections =  mapper.createArrayNode();
                sections.add(i);
                sections.add(j);
                cellNode.set("section", sections);
                int objectsCounter = 0;
                if (cell.hasPlant()) {
                    objectsCounter++;
                }
                if (cell.hasWater()) {
                    objectsCounter++;
                }
                if (cell.hasAnimal()) {
                    objectsCounter++;
                }
                cellNode.put("totalNrOfObjects", objectsCounter);
                cellNode.put("airQuality", cell.getAir().getAirQuality());
                cellNode.put("soilQuality", cell.getSoil().getSoilQuality());
                arrayNode.add(cellNode);
            }
        }
        node.set("output", arrayNode);
        node.put("timestamp", commandInput.getTimestamp());
        output.add(node);
    }
    private void printEnvConditions(final CommandInput commandInput, final ArrayNode output) {
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "printEnvConditions");
        if (!running) {
            node.put("message", "ERROR: Simulation not started. Cannot perform action");
            node.put("timestamp", commandInput.getTimestamp());
            output.add(node);
            return;
        }
        if (commandInput.getTimestamp() < robotTimeToCharge) {
            node.put("message", "ERROR: Robot still charging. Cannot perform action");
            node.put("timestamp", commandInput.getTimestamp());
            output.add(node);
            return;
        }
        runEnvironmentUpdates(commandInput.getTimestamp());
        Cell cell = map.getCell(robot.getX(), robot.getY());
        ObjectNode envOutput = mapper.createObjectNode();
        ObjectNode soilNode = mapper.createObjectNode();
        soilNode.put("type", cell.getSoil().getType());
        soilNode.put("name",  cell.getSoil().getName());
        soilNode.put("mass", cell.getSoil().getMass());
        soilNode.put("nitrogen", cell.getSoil().getNitrogen());
        soilNode.put("waterRetention", cell.getSoil().getWaterRetention());
        soilNode.put("soilpH",  cell.getSoil().getSoilpH());
        soilNode.put("organicMatter", cell.getSoil().getOrganicMatter());
        soilNode.put("soilQuality", cell.getSoil().soilQuality());
        String soilExtraFieldName = cell.getSoil().getExtraFieldName();
        soilNode.put(soilExtraFieldName, cell.getSoil().getExtraField());
        envOutput.set("soil", soilNode);

        if (cell.hasPlant()) {
            ObjectNode plantNode = mapper.createObjectNode();
            plantNode.put("type", cell.getPlant().getType());
            plantNode.put("name",  cell.getPlant().getName());
            plantNode.put("mass", cell.getPlant().getMass());
            envOutput.set("plants", plantNode);
        }
        if (cell.hasAnimal()) {
            ObjectNode animalNode = mapper.createObjectNode();
            animalNode.put("type", cell.getAnimal().getType());
            animalNode.put("name",  cell.getAnimal().getName());
            animalNode.put("mass", cell.getAnimal().getMass());
            envOutput.set("animals", animalNode);
        }
        if (cell.hasWater()) {
            ObjectNode waterNode = mapper.createObjectNode();
            waterNode.put("type", cell.getWater().getType());
            waterNode.put("name",  cell.getWater().getName());
            waterNode.put("mass", cell.getWater().getMass());
            envOutput.set("water", waterNode);
        }
        ObjectNode airNode = mapper.createObjectNode();
        airNode.put("type", cell.getAir().getType());
        airNode.put("name",  cell.getAir().getName());
        airNode.put("mass", cell.getAir().getMass());
        airNode.put("humidity", cell.getAir().getHumidity());
        airNode.put("temperature", cell.getAir().getTemperature());
        airNode.put("oxygenLevel",  cell.getAir().getOxygenLevel());
        airNode.put("airQuality", cell.getAir().airQuality());
        if (Objects.equals(cell.getAir().getType(), "DesertAir")) {
            cell.getAir().getWeatherInfo(airNode);
        } else {
            String airExtraFieldName = cell.getAir().getExtraFieldName();
            airNode.put(airExtraFieldName, cell.getAir().getExtraField());
        }
        envOutput.set("air", airNode);

        node.set("output", envOutput);
        node.put("timestamp", commandInput.getTimestamp());
        output.add(node);
    }
    private int qualityScore(final Cell cell) {
        double sum = 0;
        int count = 0;
        double possibilityToGetStuckInSoil = cell.getSoil().soilPossibility();
        count++;
        sum += possibilityToGetStuckInSoil;
        double possibilityToGetDamagedByAir = cell.getAir().airToxicity();
        sum += possibilityToGetDamagedByAir;
        count++;
        if (cell.hasPlant()) {
            double possibilityToGetStuckInPlants = cell.getPlant().getPlantPossibility();
            sum += possibilityToGetStuckInPlants;
            count++;
        }
        if (cell.hasAnimal()) {
            double possibilityToBeAttackedByAnimal = cell.getAnimal().getAttackPossibility();
            sum += possibilityToBeAttackedByAnimal;
            count++;
        }
        double mean =  Math.abs(sum / count);
        int result = (int) Math.round(mean);
        return result;
    }
    private void moveRobot(final CommandInput commandInput, final ArrayNode output) {
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "moveRobot");
        if (!running) {
            node.put("message", "ERROR: Simulation not started. Cannot perform action");
            node.put("timestamp", commandInput.getTimestamp());
            output.add(node);
            return;
        }
        if (commandInput.getTimestamp() < robotTimeToCharge) {
            node.put("message", "ERROR: Robot still charging. Cannot perform action");
            node.put("timestamp", commandInput.getTimestamp());
            output.add(node);
            return;
        }
        if (robot.getBatteryLvl() <= 0) {
            node.put("message", "ERROR: Not enough battery left. Cannot perform action");
            node.put("timestamp", commandInput.getTimestamp());
            output.add(node);
            return;
        }
        runEnvironmentUpdates(commandInput.getTimestamp());
        int currentposX = robot.getX();
        int currentposY = robot.getY();
        Cell bestCell = null;
        int minScore = Integer.MAX_VALUE;
        for (int i = 0; i < NR_OF_NEIGHBOURS; i++) {
            if (i == 0 && map.checkCellPos(currentposX, currentposY + 1)) {
                Cell neighbour = map.getCell(currentposX, currentposY + 1);
                int score = qualityScore(neighbour);
                if (score < minScore) {
                    bestCell = neighbour;
                    minScore = score;
                }
            } else if (i == 1 && map.checkCellPos(currentposX + 1, currentposY)) {
                Cell neighbour = map.getCell(currentposX + 1, currentposY);
                int score = qualityScore(neighbour);
                if (score < minScore) {
                    bestCell = neighbour;
                    minScore = score;
                }
            } else if (i == 2 && map.checkCellPos(currentposX, currentposY - 1)) {
                Cell neighbour = map.getCell(currentposX, currentposY - 1);
                int score = qualityScore(neighbour);
                if (score < minScore) {
                    bestCell = neighbour;
                    minScore = score;
                }
            } else if (i == NR_OF_NEIGHBOURS - 1
                    && map.checkCellPos(currentposX - 1, currentposY)) {
                Cell neighbour = map.getCell(currentposX - 1, currentposY);
                int score = qualityScore(neighbour);
                if (score < minScore) {
                    bestCell = neighbour;
                    minScore = score;
                }
            }
        }
        if (bestCell != null) {
            if (robot.getBatteryLvl() >= minScore) {
                robot.setX(bestCell.getX());
                robot.setY(bestCell.getY());
                robot.consumeBattery(minScore);
                node.put("message", "The robot has successfully moved to position ("
                        + bestCell.getX() + ", " + bestCell.getY() + ").");
            } else {
                node.put("message", "ERROR: Not enough battery left. Cannot perform action");
            }
        } else {
            node.put("message", "outofbounds");
        }
        node.put("timestamp", commandInput.getTimestamp());
        output.add(node);
    }
    private void getEnergyStatus(final CommandInput commandInput, final ArrayNode output) {
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "getEnergyStatus");
        if (!running) {
            node.put("message", "ERROR: Simulation not started. Cannot perform action");
            node.put("timestamp", commandInput.getTimestamp());
            output.add(node);
            return;
        }
        if (commandInput.getTimestamp() < robotTimeToCharge) {
            node.put("message", "ERROR: Robot still charging. Cannot perform action");
            node.put("timestamp", commandInput.getTimestamp());
            output.add(node);
            return;
        }
        runEnvironmentUpdates(commandInput.getTimestamp());
        node.put("message", "TerraBot has " + robot.getBatteryLvl() + " energy points left.");
        node.put("timestamp", commandInput.getTimestamp());
        output.add(node);
    }
    private void rechargeBattery(final CommandInput commandInput, final ArrayNode output) {
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "rechargeBattery");
        if (!running) {
            node.put("message", "ERROR: Simulation not started. Cannot perform action");
            node.put("timestamp", commandInput.getTimestamp());
            output.add(node);
            return;
        }
        if (commandInput.getTimestamp() < robotTimeToCharge) {
            node.put("message", "ERROR: Robot still charging. Cannot perform action");
            node.put("timestamp", commandInput.getTimestamp());
            output.add(node);
            return;
        }
        runEnvironmentUpdates(commandInput.getTimestamp());
        int timeToCharge = commandInput.getTimeToCharge();
        robot.rechargeBattery(timeToCharge);
        robotTimeToCharge = commandInput.getTimestamp() + timeToCharge;
        node.put("message", "Robot battery is charging.");
        node.put("timestamp", commandInput.getTimestamp());
        output.add(node);

    }
    private void changeWeatherConditions(final CommandInput commandInput, final ArrayNode output) {
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "changeWeatherConditions");
        if (!running) {
            node.put("message", "ERROR: Simulation not started. Cannot perform action");
            node.put("timestamp", commandInput.getTimestamp());
            output.add(node);
            return;
        }
        if (commandInput.getTimestamp() < robotTimeToCharge) {
            node.put("message", "ERROR: Robot still charging. Cannot perform action");
            node.put("timestamp", commandInput.getTimestamp());
            output.add(node);
            return;
        }
        runEnvironmentUpdates(commandInput.getTimestamp());
        String type = commandInput.getType();
        boolean environmentAffected = false;
        for (int j = 0; j < map.getM(); j++) {
            for (int i = 0; i < map.getN(); i++) {
                Cell cell = map.getCell(i, j);
                cell.getAir().changeWeather(commandInput);
                if ((type.equals("polarStorm") && cell.getAir().getType().equals("PolarAir"))
                        || (type.equals("desertStorm")
                        && cell.getAir().getType().equals("DesertAir"))
                        || (type.equals("rainfall")
                        && cell.getAir().getType().equals("TropicalAir"))
                        || (type.equals("peopleHiking")
                        && cell.getAir().getType().equals("MountainAir"))
                        || (type.equals("newSeason")
                        && cell.getAir().getType().equals("TemperateAir"))) {
                    cell.getAir().changeWeather(commandInput);
                    environmentAffected = true;
                }
            }
        }
        if (environmentAffected) {
            node.put("message", "The weather has changed.");
        } else {
            node.put("message", "ERROR: The weather change "
                    + "does not affect the environment. Cannot perform action");
        }
        node.put("timestamp", commandInput.getTimestamp());
        output.add(node);
    }
    private void updateEnvironment(final int timestamp) {
        List<Animal> interactionedAnimals = new ArrayList<>();
        for (int j = 0; j < map.getM(); j++) {
            for (int i = 0; i < map.getN(); i++) {
                Cell cell =  map.getCell(i, j);
                cell.getAir().updateWeather(timestamp);
                boolean plantScanned;
                if (cell.hasPlant() && robot.containsEntity(cell.getPlant())) {
                    plantScanned = true;
                } else {
                    plantScanned = false;
                }
                boolean waterScanned;
                if (cell.hasWater() && robot.containsEntity(cell.getWater())) {
                    waterScanned = true;
                } else {
                    waterScanned = false;
                }
                boolean animalScanned;
                if (cell.hasAnimal() && robot.containsEntity(cell.getAnimal())) {
                    animalScanned = true;
                } else {
                    animalScanned = false;
                }
                if (plantScanned) {
                    cell.getPlant().growMaturityLevel(MATURITY_GROWTH);
                    double plantOxygen = cell.getPlant().getOxygenLevel();
                    double airOxygen = cell.getAir().getOxygenLevel();
                    cell.getAir().setOxygenLevel(plantOxygen + airOxygen);
                    if (waterScanned) {
                        cell.getPlant().growMaturityLevel(MATURITY_GROWTH);
                    }
                    if (cell.getPlant().getOxygenLevel() == 0.0) {
                        cell.removePlant();
                    }
                }
                if (waterScanned) {
                    if (timestamp % 2 != 0) {
                        Air air = cell.getAir();
                        air.setHumidity(air.getHumidity() + HUMIDITY_GROWTH);
                        Soil soil = cell.getSoil();
                        soil.setWaterRetention(soil.getWaterRetention() + WATER_RETENTION_GROWTH);
                    }
                }
                if (cell.hasAnimal()) {
                    Animal animal = cell.getAnimal();
                    if (interactionedAnimals.contains(animal)) {
                        continue;
                    }
                    interactionedAnimals.add(animal);
                    if (animalScanned) {
                        double organicMatter = animal.getAndResetOrganicMatter();
                        if (organicMatter > 0.0) {
                            cell.getSoil().addOrganicMatter(organicMatter);
                        }
                        animal.checkAirToxicity(cell.getAir());
                        animal.eatResources(cell, cell.getPlant(), cell.getWater(),
                                cell.getSoil(), plantScanned, waterScanned);
                        if (animal.canMove()) {
                            List<Cell> neighbours = map.getNeighbours(i, j);
                            Cell moveCell = animal.chooseMoveCell(neighbours, robot);
                            if (moveCell != null) {
                                if (!moveCell.hasAnimal()) {
                                    cell.removeAnimal();
                                    moveCell.setAnimal(animal);
                                    animal.setX(moveCell.getX());
                                    animal.setY(moveCell.getY());
                                } else {
                                    Animal prey = moveCell.getAnimal();
                                    if (animal.checkIfAnimalisCarnivoreOrParasite()) {
                                        animal.eatAnimal(prey, moveCell.getSoil());
                                        moveCell.removeAnimal();
                                        cell.removeAnimal();
                                        moveCell.setAnimal(animal);
                                        animal.setX(moveCell.getX());
                                        animal.setY(moveCell.getY());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    private void scanObject(final CommandInput commandInput, final ArrayNode output) {
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "scanObject");
        if (!running) {
            node.put("message", "ERROR: Simulation not started. Cannot perform action");
            node.put("timestamp", commandInput.getTimestamp());
            output.add(node);
            return;
        }
        if (commandInput.getTimestamp() < robotTimeToCharge) {
            node.put("message", "ERROR: Robot still charging. Cannot perform action");
            node.put("timestamp", commandInput.getTimestamp());
            output.add(node);
            return;
        }
        if (robot.getBatteryLvl() < BATTERY_COST_SCANOBJECT) {
            node.put("message", "ERROR: Not enough energy to perform action");
            node.put("timestamp", commandInput.getTimestamp());
            output.add(node);
            return;
        }
        runEnvironmentUpdates(commandInput.getTimestamp());
        String type = "";
        if (commandInput.getColor().equals("none") && commandInput.getSmell().equals("none")
                && commandInput.getSound().equals("none")) {
            type = "water";
        } else if (commandInput.getSound().equals("none")) {
            type = "plant";
        } else {
            type = "animal";
        }
        Cell cell = map.getCell(robot.getX(), robot.getY());
        boolean isFound = false;
        String entity = "";
        if (type.equals("water") && cell.hasWater()) {
            robot.addInventory(cell.getWater());
            entity = "water";
            isFound = true;
        } else if (type.equals("plant") && cell.hasPlant()) {
            robot.addInventory(cell.getPlant());
            entity = "a plant";
            isFound = true;
        } else if (type.equals("animal") && cell.hasAnimal()) {
            robot.addInventory(cell.getAnimal());
            entity = "an animal";
            isFound = true;
        }
        if (isFound) {
            robot.consumeBattery(BATTERY_COST_SCANOBJECT);
            node.put("message", "The scanned object is " + entity + ".");
        } else {
            node.put("message", "ERROR: Object not found. Cannot perform action");
        }
        node.put("timestamp", commandInput.getTimestamp());
        output.add(node);
    }
    private void learnFact(final CommandInput commandInput, final ArrayNode output) {
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "learnFact");
        if (!running) {
            node.put("message", "ERROR: Simulation not started. Cannot perform action");
            node.put("timestamp", commandInput.getTimestamp());
            output.add(node);
            return;
        }
        runEnvironmentUpdates(commandInput.getTimestamp());
        if (commandInput.getTimestamp() < robotTimeToCharge) {
            node.put("message", "ERROR: Robot still charging. Cannot perform action");
            node.put("timestamp", commandInput.getTimestamp());
            output.add(node);
            return;
        }
        if (robot.getBatteryLvl() < BATTERY_COST_LEARNFACT) {
            node.put("message",  "ERROR: Not enough battery left. Cannot perform action");
            node.put("timestamp", commandInput.getTimestamp());
            output.add(node);
            return;
        }
        String component = commandInput.getComponents();
        if (robot.hasInInventory(component)) {
            robot.consumeBattery(BATTERY_COST_LEARNFACT);
            robot.addFact(component, commandInput.getSubject());
            node.put("message", "The fact has been successfully saved in the database.");
        } else {
            node.put("message", "ERROR: Subject not yet saved. Cannot perform action");
        }
        node.put("timestamp", commandInput.getTimestamp());
        output.add(node);
    }
    private void printKnowledgeBase(final CommandInput commandInput, final ArrayNode output) {
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "printKnowledgeBase");
        if (!running) {
            node.put("message", "Simulation not started. Cannot perform action");
            node.put("timestamp", commandInput.getTimestamp());
            output.add(node);
            return;
        }
        runEnvironmentUpdates(commandInput.getTimestamp());
        if (commandInput.getTimestamp() < robotTimeToCharge) {
            node.put("message", "ERROR: Robot still charging. Cannot perform action");
            node.put("timestamp", commandInput.getTimestamp());
            output.add(node);
            return;
        }
        ArrayNode knowledgeBase = mapper.createArrayNode();
        for (Topic topic : robot.getKnowledgeBase()) {
            if (!topic.getFacts().isEmpty()) {
            ObjectNode topicNode =  mapper.createObjectNode();
            topicNode.put("topic", topic.getTopic());
            ArrayNode factsNode = mapper.createArrayNode();
            for (String fact : topic.getFacts()) {
                factsNode.add(fact);
            }
            topicNode.set("facts", factsNode);
            knowledgeBase.add(topicNode);
            }
        }
        node.set("output",  knowledgeBase);
        node.put("timestamp", commandInput.getTimestamp());
        output.add(node);
    }
    private void improveEnvironment(final CommandInput commandInput, final ArrayNode output) {
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "improveEnvironment");
        if (!running) {
            node.put("message", "ERROR: Simulation not started. Cannot perform action");
            node.put("timestamp", commandInput.getTimestamp());
            output.add(node);
            return;
        }
        runEnvironmentUpdates(commandInput.getTimestamp());
        if (commandInput.getTimestamp() < robotTimeToCharge) {
            node.put("message", "ERROR: Robot still charging. Cannot perform action");
            node.put("timestamp", commandInput.getTimestamp());
            output.add(node);
            return;
        }
        if (robot.getBatteryLvl() < BATTERY_COST_IMPROVEENV) {
            node.put("message", "ERROR: Not enough battery left. Cannot perform action");
            node.put("timestamp", commandInput.getTimestamp());
            output.add(node);
            return;
        }
        String componentName = commandInput.getName();
        String type = commandInput.getImprovementType();
        boolean topicIsInKnowledgeBase = false;
        Topic foundTopic = null;
        for (Topic topic : robot.getKnowledgeBase()) {
            if (topic.getTopic().equals(componentName)) {
                topicIsInKnowledgeBase = true;
                foundTopic = topic;
                break;
            }
        }
        if (!topicIsInKnowledgeBase) {
            node.put("message", "ERROR: Subject not yet saved. Cannot perform action");
            node.put("timestamp", commandInput.getTimestamp());
            output.add(node);
            return;
        }
        boolean factIsFound = false;
        if (foundTopic != null) {
            for (String fact : foundTopic.getFacts()) {
                if (type.equals("plantVegetation")
                        && fact.equals("Method to plant " + componentName)) {
                    factIsFound = true;
                }
                if (type.equals("fertilizeSoil")
                        && fact.contains("Method to fertilize")) {
                    factIsFound = true;
                }
                if (type.equals("increaseHumidity")
                        && fact.contains("Method to increase humidity")) {
                    factIsFound = true;
                }
                if (type.equals("increaseMoisture") && (fact.contains("Method to increase moisture")
                        || fact.contains("Method to increaseMoisture"))) {
                    factIsFound = true;
                }
            }
        }
        if (!factIsFound) {
            node.put("message", "ERROR: Fact not yet saved. Cannot perform action");
            node.put("timestamp", commandInput.getTimestamp());
            output.add(node);
            return;
        }
        if (!robot.hasInInventory(componentName)) {
            node.put("message", "ERROR: Object not found. Cannot perform action");
            node.put("timestamp", commandInput.getTimestamp());
            output.add(node);
            return;
        }
        robot.consumeBattery(BATTERY_COST_IMPROVEENV);
        Cell cell = map.getCell(robot.getX(), robot.getY());
        String printSuccess = "";
        if (type.equals("plantVegetation")) {
            cell.getAir().setOxygenLevel(cell.getAir().getOxygenLevel() + VEGETATION_GROWTH);
            printSuccess = "The " + componentName + " was planted successfully.";
        } else if (type.equals("fertilizeSoil")) {
            cell.getSoil().addOrganicMatter(FERTILIZE_GROWTH);
            printSuccess = "The soil was successfully fertilized using " + componentName;
        } else if (type.equals("increaseHumidity")) {
            cell.getAir().setHumidity(cell.getAir().getHumidity() + INCREASE_HUMIDITY_GROWTH);
            printSuccess = "The humidity was successfully increased using " + componentName;
        } else if (type.equals("increaseMoisture")) {
            cell.getSoil().setWaterRetention(cell.getSoil().getWaterRetention()
                    + INCREASE_MOISTURE_GROWTH);
            printSuccess = "The moisture was successfully increased using " + componentName;
        }
        node.put("message", printSuccess);
        node.put("timestamp", commandInput.getTimestamp());
        output.add(node);
    }
}
