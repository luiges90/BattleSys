package battlesys.move.MoveStatus;

import battlesys.exception.BattleSysException;
import battlesys.*;import java.io.IOException;
 import java.util.*;

/**
 *
 * @author Peter
 */
public class FreezeAtk2 extends MoveStatus{

    /**
     *
     * @param owner
     */
    public FreezeAtk2(Player owner) throws IOException{
        super(owner);
        damage = 8;
        accurancy = 70;
        statusEffect = Player.statusId.FREEZE.getId();
        statusRate = 100;
    }

    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException {
        String s = preMove();

        //number of targets
        int numTarget = Math.min(Utility.randBetween(8, 12), defenders.size());

        //the actual targets
        PlayerList targets = defenders.playerSubset(numTarget);

        List<SingleMoveResult> r = new ArrayList<SingleMoveResult>(numTarget);

        for (Player p : targets){
            r.addAll(super.useMove(owner, p, attackers, defenders));
        }

        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
