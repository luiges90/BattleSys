package battlesys.io;

import java.io.IOException;
import java.util.logging.*;

/**
 * Logger used by BattleSys.
 * @author Peter
 */
public class BattleSysLogger {

    private static Logger logger = null;

    private static void init() {
        logger = Logger.getLogger("BattlesysLogger");
        FileHandler fh = null;
        try {
            fh = new FileHandler("error.log", true);
        } catch (IOException ex){
            ex.printStackTrace();
            System.exit(1);
        }
        logger.addHandler(fh);
        logger.setLevel(Level.ALL);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
    }

    public static Logger getLogger(){
        if (logger == null) init();
        return logger;
    }

    private BattleSysLogger() {
    }

}
