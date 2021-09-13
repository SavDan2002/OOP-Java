package workflow;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import workflow.exceptions.ParsingException;

public class DescriptionReader {
    HashMap<Integer, String> blocks;

    private static final Logger log = Logger.getLogger(WorkflowExecutor.class.getName());

    public DescriptionReader(BufferedReader inputReader) throws ParsingException {
        blocks = new HashMap<>();
        try {
            String currentLine = inputReader.readLine();
            while (!currentLine.startsWith("desc")) {
                currentLine = inputReader.readLine();
                if (currentLine == null) {
                    throw new ParsingException("Can not find begin of description");
                }
            }

            while (true) {
                currentLine = inputReader.readLine();
                if (currentLine == null) {
                    throw new ParsingException("Can not find end of description");
                }
                if (currentLine.equals("csed")) {
                    break;
                }

                String[] idAndCommand = currentLine.split(" = ", 2);
                if (idAndCommand.length < 2) {
                    throw new ParsingException("Can not read command from description");
                }
                Integer id = Integer.valueOf(idAndCommand[0]);
                if (blocks.containsKey(id)) {
                    log.log(Level.SEVERE, "Repeated id in description: " + currentLine
                            + "\t conflicts with: " + blocks.get(id));
                    throw new ParsingException("Repeated id in description");
                }
                blocks.put(id, idAndCommand[1]);
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error while reading description", e);
            throw new ParsingException("Error while reading description", e);
        }
    }

    HashMap<Integer, String> getDescription() {
        return blocks;
    }
}
