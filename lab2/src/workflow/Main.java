package workflow;

import workflow.exceptions.WorkflowException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

public class Main {

    public static void main(String[] args) {

        try
        {
            FileInputStream fin = new FileInputStream("workflow.txt");
            byte[] buffer = new byte[fin.available()];
            fin.read(buffer, 0, fin.available());
            InputStream stream = new ByteArrayInputStream(buffer);

            WorkflowExecutor executor = new WorkflowExecutor(stream);
            executor.execute();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        catch (WorkflowException ex) {
            ex.printStackTrace();
        }

    }
}
