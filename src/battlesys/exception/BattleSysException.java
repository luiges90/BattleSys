package battlesys.exception;

/**
 * Exception designed for BattleSys. Also the root of all other battlesys exceptions
 * @author Peter
 */
public class BattleSysException extends Exception {

    /**
     *
     * @param message
     */
    public BattleSysException(String message) {
        super(message);
    }
}
