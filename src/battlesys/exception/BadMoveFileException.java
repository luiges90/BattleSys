package battlesys.exception;

/**
 * Thrown when the move file is bad e.g. do not have valid constructor
 * @author Peter
 */
public class BadMoveFileException extends BattleSysRuntimeException{

    public BadMoveFileException(String moveName, Exception cause){
        super("Move file for " + moveName + " is malformed.");
        if (cause != null){
            System.err.println("Caused by: ");
            cause.printStackTrace();
        }
    }

}
