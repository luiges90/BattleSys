package battlesys;

import battlesys.exception.BattleSysException;
import java.io.IOException;
import java.util.*;

/**
 * Moves that transfer all bad status of oneself to the target
 * @author Peter
 */
public abstract class MoveTransferStatus extends Move {

    /**
     * Accurancy (success probability) of this move, in percentage, 70 means 70% success rate
     */
    protected int accurancy;

    /**
     * Constructor. All subclass should call this constructor. This class sets the default values of the fields.
     * @param owner The owner of the move
     */
    protected MoveTransferStatus(Player owner) throws IOException {
        super(owner);
        accurancy = 0;
    }

    protected List<SingleMoveResult> useMove(Player attacker, Player defender, PlayerList attackers, PlayerList defenders) throws BattleSysException {
        if (!checkUsable(attacker, defender, attackers, defenders)) {
            return Collections.singletonList(new SingleMoveResult(attacker, defender, unusableReasonString(attacker, defender, attackers, defenders)));
        }

        if (owner.statusInflicted(Player.statusId.DISABLE.getId()) && !basic) {
            return owner.forcedBasicAtk(defender, attackers, defenders);
        }

        if (owner.checkDizzy() || owner.checkReflect(defender)) {
            defender = attacker;
        }

        if (Utility.probTest(Utility.inRange(accurancy + (attacker.getMor() - defender.getMor()) * 5, 0, 100))) {

            String[] statusList = new String[BAD_STATUS_LIST.length];
            int si = 0;
            for (int i = 0; i < BAD_STATUS_LIST.length; ++i) {
                if (attacker.statusInflicted(i)) {
                    defender.status[si] = true;
                    statusList[si] = Player.STATUS_STRING[i];
                }
            }
            Utility.shrinkArray(statusList, si);
            if (si != 0) {

                String s = defender.getName() + "進入" + Utility.listString(statusList, "、") + "狀態了！";

                return Collections.singletonList(new SingleMoveResult(0, true, false, attacker, defender, s));

            } else {

                String s = "對" + defender.getName() + "進行攻擊，但失敗了。";

                return Collections.singletonList(new SingleMoveResult(0, false, false, attacker, defender, s));

            }

        } else {

            String s = "對" + defender.getName() + "進行攻擊，但失敗了。";

            return Collections.singletonList(new SingleMoveResult(0, false, false, attacker, defender, s));

        }
    }
}
