package workflow;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import workflow.blocks.Block;
import workflow.exceptions.BlockNotFoundException;
import workflow.exceptions.FactoryException;
import workflow.exceptions.BlockCreationException;

public class BlockFactory {
    private static final Logger log = Logger.getLogger(BlockFactory.class.getName());

    private final Map<String, Class<?>> cfg = new HashMap<>();


    private static volatile BlockFactory factory;

    private BlockFactory() throws IOException, FactoryException, BlockCreationException {
        String cfgName = "config.cfg";

        FileReader reader = new FileReader(cfgName);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String currLine = bufferedReader.readLine();
        String[] keyValue;
        while (currLine != null) {
            keyValue = currLine.split("=");
            if (keyValue.length != 2){
                throw new FactoryException("incorrect config");
            }
            try {
                cfg.put(keyValue[0],  Class.forName(keyValue[1]));
            } catch (ClassNotFoundException ex) {
                log.log(Level.SEVERE, "Factory can not find class by name", ex);
                throw new BlockCreationException("Factory can not find class by name", ex);
            }
            currLine = bufferedReader.readLine();
        }

    }

    public static BlockFactory getInstance() throws IOException, FactoryException, BlockCreationException {
        if (factory == null) {
            factory = new BlockFactory();
        }
        return factory;
    }

    public Block getBlock(String blockName) throws BlockNotFoundException, BlockCreationException {
        if (!cfg.containsKey(blockName)) {
            log.severe("Block with name " + blockName + " not found in config");
            throw new BlockNotFoundException("Block with name " + blockName + " not found in config");
        }

        Block newBlock;
        try {
            var blockClass = cfg.get(blockName);
            newBlock = (Block) blockClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException ex) {
            log.log(Level.SEVERE, "Factory can not find constructor in block " + blockName, ex);
            throw new BlockCreationException("Factory can not find constructor in block", ex);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            log.log(Level.SEVERE, "Factory can not create new instance of " + blockName, ex);
            throw new BlockCreationException("Factory can not create new instance of", ex);
        }
        return newBlock;
    }
}
