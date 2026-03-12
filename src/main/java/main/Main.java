package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.CommandInput;
import fileio.InputLoader;
import fileio.SimulationInput;
import simulation.Simulation;

import java.io.File;
import java.io.IOException;

/**
 * The entry point to this homework. It runs the checker that tests your implementation.
 */
public final class Main {

    private Main() {
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static final ObjectWriter WRITER = MAPPER.writer().withDefaultPrettyPrinter();

    /**
     * @param inputPath input file path
     * @param outputPath output file path
     * @throws IOException when files cannot be loaded.
     */
    public static void action(final String inputPath,
                              final String outputPath) throws IOException {

        InputLoader inputLoader = new InputLoader(inputPath);
        ArrayNode output = MAPPER.createArrayNode();
        /*
         * TODO Implement your function here
         *
         * How to add output to the output array?
         * There are multiple ways to do this, here is one example:
         *
         *
         * ObjectNode objectNode = MAPPER.createObjectNode();
         * objectNode.put("field_name", "field_value");
         *
         * ArrayNode arrayNode = MAPPER.createArrayNode();
         * arrayNode.add(objectNode);
         *
         * output.add(arrayNode);
         * output.add(objectNode);
         *
         */
        Simulation simulation = new Simulation();
        SimulationInput simulationInput = null;
        int simulationsCounter = 0;
        if (inputLoader.getCommands() != null) {
            for (CommandInput commandInput : inputLoader.getCommands()) {
                SimulationInput currentSimulationInput = null;
                if (commandInput.getCommand().equals("startSimulation")) {
                    if (inputLoader.getSimulations() != null
                            && simulationsCounter < inputLoader.getSimulations().size()) {
                        currentSimulationInput =
                                inputLoader.getSimulations().get(simulationsCounter);
                        simulationsCounter++;
                    }
                }
                simulation.runCommand(commandInput, currentSimulationInput, output);
            }
        }


        File outputFile = new File(outputPath);
        outputFile.getParentFile().mkdirs();
        WRITER.writeValue(outputFile, output);
    }
}
