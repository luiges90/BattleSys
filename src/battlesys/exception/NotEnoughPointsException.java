package battlesys.exception;

import battlesys.Player;

/**
 * Excption thrown when the player do not have enough points
 * @author Peter
 */
public class NotEnoughPointsException extends BattleSysException{

    /**
     *
     * @param p The player concerned
     * @param moveName
     */
    public NotEnoughPointsException(Player p, String actionMsg) {
        super("Player " + p.getName() + " does not have enough points to " + actionMsg + "!");
    }

}
