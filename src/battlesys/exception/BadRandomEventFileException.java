package battlesys.exception;

/**
 * Thrown when the random event setting file is bad
 * @author Peter
 */
public class BadRandomEventFileException extends BattleSysException{

    public BadRandomEventFileException(String detail, Exception cause){
        super("The Random Event setting file is malformed: " + detail);
        if (cause != null){
            System.err.println("Caused by: ");
            cause.printStackTrace();
        }
    }

}
