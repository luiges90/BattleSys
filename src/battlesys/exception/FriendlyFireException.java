package battlesys.exception;

import battlesys.Player;

/**
 * Thrown when the battle becomes too long.
 * @author Peter
 */
public class FriendlyFireException extends BattleSysRuntimeException{

    public FriendlyFireException(Player culprit, Player target){
        super(culprit + " is attempting to friendly fire against " + target + "!");
    }

}
