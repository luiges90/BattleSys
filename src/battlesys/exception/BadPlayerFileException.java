package battlesys.exception;

/**
 * Thrown when the player file is bad e.g. do not have valid constructor
 * @author Peter
 */
public class BadPlayerFileException extends BattleSysRuntimeException{

    public BadPlayerFileException(String playerName, Exception cause){
        super("Player file for " + playerName + " is malformed.");
        if (cause != null){
            System.err.println("Caused by: ");
            cause.printStackTrace();
        }
    }


}
