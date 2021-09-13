package workflow.blocks;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import workflow.BlockType;
import workflow.exceptions.WorkflowException;

public class Dump implements Block {
    @Override
    public List<String> execute(List<String> text, String[] args) throws WorkflowException {
        if (args == null || args.length < 1) {
            throw new WorkflowException("Not enough args for the command");
        }
        if (text == null) {
            return null;
        }

        try {
            FileWriter fileWriter = new FileWriter(args[0]);
            for (String line : text) {
                fileWriter.write(line + "\n");
            }
        } catch (IOException ex) {
            throw new WorkflowException("Can not write in file", ex);
        }

        return text;
    }

    @Override
    public BlockType getType() {
        return BlockType.InputOutput;
    }
}
