package workflow.blocks;

import java.util.List;
import java.util.stream.Collectors;

import workflow.BlockType;
import workflow.exceptions.WorkflowException;

public class Sort implements Block {
    @Override
    public List<String> execute(List<String> text, String[] args) throws WorkflowException {
        if (text == null) {
            return null;
        }

        return text.stream().sorted(String::compareTo).collect(Collectors.toList());
    }

    @Override
    public BlockType getType() {
        return BlockType.InputOutput;
    }
}
