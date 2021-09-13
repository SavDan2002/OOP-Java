package workflow.blocks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import workflow.BlockType;
import workflow.exceptions.WorkflowException;

public class ReadFile implements Block {
    @Override
    public List<String> execute(List<String> text, String[] args) throws WorkflowException {
        if (args == null || args.length < 1) {
            throw new WorkflowException("Not enough args");
        }

        try {
            FileReader fileReader = new FileReader(args[0]);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String currLine = bufferedReader.readLine();
            while (currLine != null) {
                text.add(currLine);
                currLine = bufferedReader.readLine();
            }
        } catch (IOException ex) {
            throw new WorkflowException("Can not read line", ex);
        }

        return text;
    }

    @Override
    public BlockType getType() {
        return BlockType.Output;
    }
}
