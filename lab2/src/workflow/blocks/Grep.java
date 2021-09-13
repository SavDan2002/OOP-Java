package workflow.blocks;

import java.util.ArrayList;
import java.util.List;

import workflow.BlockType;
import workflow.exceptions.WorkflowException;

public class Grep implements Block {
    @Override
    public List<String> execute(List<String> text, String[] args) throws WorkflowException {
        if (args == null || args.length < 1) {
            throw new WorkflowException("Not enough args");
        }
        if (text == null) {
            return null;
        }

        List<String> textWithKeyWord = new ArrayList<>();
        String keyWord = args[0];
        for (String line : text) {
            if(line.contains(" " + keyWord + " ")) {
                textWithKeyWord.add(line);
            }
        }
        return textWithKeyWord;
    }

    @Override
    public BlockType getType() {
        return BlockType.InputOutput;
    }
}
