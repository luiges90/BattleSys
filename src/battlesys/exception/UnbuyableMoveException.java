package battlesys.exception;

/**
 * Excption thrown when the move is not buyable
 * @author Peter
 */
public class UnbuyableMoveException extends BattleSysException{

    /**
     *
     * @param moveName
     */
    public UnbuyableMoveException(String moveName) {
        super("Move with simple class name " + moveName + " cannot be bought!");
    }

}
