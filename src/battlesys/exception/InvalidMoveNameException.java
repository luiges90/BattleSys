package battlesys.exception;

/**
 * Excption thrown when the move name is invalid.
 * @author Peter
 */
public class InvalidMoveNameException extends BattleSysException{

    /**
     *
     * @param moveName
     */
    public InvalidMoveNameException(String moveName) {
        super("Move with simple class name " + moveName + " does not exist!");
    }

}
