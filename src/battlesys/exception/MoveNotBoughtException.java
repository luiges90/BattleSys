package battlesys.exception;

import battlesys.Player;

/**
 * Excption thrown when the player did not buy a certain move.
 * @author Peter
 */
public class MoveNotBoughtException extends BattleSysException{

    /**
     *
     * @param p The player concerned
     * @param moveName
     */
    public MoveNotBoughtException(Player p, String moveName) {
        super("Player " + p.getName() + " does not buy move with simple class name " + moveName + "!");
    }

}
