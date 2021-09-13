package workflow;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import workflow.blocks.Block;
import workflow.exceptions.BlockNotFoundException;
import workflow.exceptions.ParsingException;
import workflow.exceptions.WorkflowException;

public class WorkflowExecutor {
    private final HashMap<Integer, String> description;
    private int[] blockSequence;

    private static final Logger log = Logger.getLogger(WorkflowExecutor.class.getName());

    private void readBlockSequence(String currentLine) {
        blockSequence = Arrays
                .stream(currentLine.replaceAll(" ", "").split("->"))
                .mapToInt(Integer::parseInt).toArray();
    }

    public WorkflowExecutor(InputStream workflowStream) throws ParsingException {
        log.info("Reading description");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(workflowStream));
        DescriptionReader reader = new DescriptionReader(bufferedReader);
        description = reader.getDescription();
        log.info("Description was read successfully");

        try {
            readBlockSequence(bufferedReader.readLine());
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Can not read sequence", ex);
            throw new ParsingException("Can not read sequence", ex);
        }
    }

    public void execute() throws WorkflowException {
        log.info("Starting execution...");
        log.info("Number of blocks = " + blockSequence.length);

        List<String> text = new ArrayList<>();
        for (int i = 0; i < blockSequence.length; i++) {
            int blockIdx = blockSequence[i];

            String currentBlockDescription = description.get(blockIdx);
            if (currentBlockDescription == null) {
                throw new BlockNotFoundException("No block with " + blockIdx + " id");
            }

            String[] blockNameAndArgs = currentBlockDescription.split(" ");
            String blockName = blockNameAndArgs[0];
            String[] blockArgs = null;
            if (blockNameAndArgs.length > 1) {
                blockArgs = Arrays.copyOfRange(blockNameAndArgs, 1, blockNameAndArgs.length);
            }

            Block currentBlock;
            try {
                currentBlock = BlockFactory.getInstance().getBlock(blockName);
            } catch (IOException e) {
                log.log(Level.SEVERE, "Can not find block with id=" + blockIdx, e);
                throw new BlockNotFoundException("Can not find block ", e);
            }

            BlockType blockType = currentBlock.getType();
            if (i == 0) {
                if (blockType != BlockType.Output) {
                    throw new WorkflowException("First block should be Output type");
                }
            } else if (i == blockSequence.length - 1) {
                if (blockType != BlockType.Input) {
                    throw new WorkflowException("Last block should be Input type");
                }
            } else {
                if (blockType != BlockType.InputOutput) {
                    throw new WorkflowException("The block in the middle should be InputOutput type");
                }
            }

            try {
                text = currentBlock.execute(text, blockArgs);
            } catch (WorkflowException e) {
                log.log(Level.SEVERE, "Can not execute block " + currentBlock, e);
                throw e;
            }

            log.info("Block " + currentBlock.getClass().getName() + " was successfully executed");
        }
    }
}
