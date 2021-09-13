package workflow.blocks;

import java.util.List;

import workflow.BlockType;
import workflow.exceptions.WorkflowException;

public class Replace implements Block {
    @Override
    public List<String> execute(List<String> text, String[] args) throws WorkflowException {
        if (args == null || args.length < 2) {
            throw new WorkflowException("Not enough args");
        }
        if (text == null) {
            return null;
        }

        String keyWord = args[0];
        String newWord = args[1];
        for (int i = 0; i < text.size(); i++) {
            String line = text.get(i);
            if (line.contains(keyWord)) {
                String newLine = line.replace(keyWord, newWord);
                text.set(i, newLine);
            }
        }
        return text;
    }

    @Override
    public BlockType getType() {
        return BlockType.InputOutput;
    }
}
