package battlesys.move.MoveStatus;

import battlesys.exception.BattleSysException;
import battlesys.*;import java.io.IOException;
 import java.util.*;

/**
 *
 * @author Peter
 */
public class FreezeAtk3 extends MoveStatus{

    /**
     *
     * @param owner
     */
    public FreezeAtk3(Player owner) throws IOException{
        super(owner);
        damage = 8;
        accurancy = 70;
        statusEffect = Player.statusId.FREEZE_3.getId();
        statusRate = 100;
    }

    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException {
        String s = preMove();

        List<SingleMoveResult> r = new ArrayList<SingleMoveResult>(defenders.size());

        for (Player p : defenders){
            r.addAll(super.useMove(owner, p, attackers, defenders));
        }

        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
