package test;

import java.io.*;

import org.junit.Test;
import workflow.WorkflowExecutor;
import workflow.exceptions.BlockNotFoundException;
import workflow.exceptions.ParsingException;
import workflow.exceptions.WorkflowException;

public class testWorkflow {
    @Test
    public void commonTest() throws WorkflowException {
        InputStream testStream = new ByteArrayInputStream(("""
                desc
                1 = readfile in.txt
                2 = writefile out.txt
                3 = sort
                4 = replace hello goodbye
                csed
                1 -> 4 -> 3 -> 2""").getBytes());

        WorkflowExecutor workflow = new WorkflowExecutor(testStream);
        workflow.execute();
    }

    @Test(expected = ParsingException.class)
    public void parsingTest1() throws WorkflowException {
        InputStream testStream = new ByteArrayInputStream(("""
                desc
                1 = readfile in.txt
                2 = writefile out.txt
                3 = sort""").getBytes());

        WorkflowExecutor workflow = new WorkflowExecutor(testStream);
        workflow.execute();
    }

    @Test(expected = ParsingException.class)
    public void parsingTest2() throws WorkflowException {
        InputStream testStream = new ByteArrayInputStream(("""
                desc
                1 = readfile in.txt
                1 = writefile out.txt
                3 = sort
                4 = replace hello goodbye
                csed
                1 -> 4 -> 3 -> 2""").getBytes());

        WorkflowExecutor workflow = new WorkflowExecutor(testStream);
        workflow.execute();
    }

    @Test(expected = WorkflowException.class)
    public void sequenceTest() throws WorkflowException {
        InputStream testStream = new ByteArrayInputStream(("""
                desc
                desc
                1 = readfile in.txt
                2 = writefile out.txt
                3 = sort
                4 = replace hello goodbye
                csed
                1 -> 2 -> 3""").getBytes());

        WorkflowExecutor workflow = new WorkflowExecutor(testStream);
        workflow.execute();

    }

    @Test(expected = BlockNotFoundException.class)
    public void sequenceTest2() throws WorkflowException {
        InputStream testStream = new ByteArrayInputStream(("""
                desc
                1 = readfile in.txt
                2 = writefile out.txt
                3 = sort
                4 = replace hello goodbye
                csed
                1 -> 3 -> 4 -> 5 -> 2""").getBytes());

        WorkflowExecutor workflow = new WorkflowExecutor(testStream);
        workflow.execute();
    }
}
