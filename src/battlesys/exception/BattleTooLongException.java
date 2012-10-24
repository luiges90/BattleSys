package battlesys.exception;

/**
 * Thrown when the battle becomes too long.
 * @author Peter
 */
public class BattleTooLongException extends BattleSysRuntimeException{

    public BattleTooLongException(){
        super("The battle become too long!");
    }

}
