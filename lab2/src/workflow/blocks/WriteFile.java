package workflow.blocks;

import java.io.*;
import java.util.List;

import workflow.BlockType;
import workflow.exceptions.WorkflowException;

public class WriteFile implements Block {
    @Override
    public List<String> execute(List<String> text, String[] args) throws WorkflowException {
        if (args == null || args.length < 1) {
            throw new WorkflowException("Not enough args");
        }
        try {
            FileWriter fileWriter = new FileWriter(args[0]);
            for (String line : text) {
                fileWriter.write(line + "\n");
            }
        } catch (IOException ex) {
            throw new WorkflowException("Can not write in file", ex);
        }
        return null;
    }

    @Override
    public BlockType getType() {
        return BlockType.Input;
    }
}
