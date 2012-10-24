package battlesys.move.MoveStatus;

import battlesys.exception.BattleSysException;
import battlesys.*;import java.io.IOException;
 import java.util.*;

/**
 *
 * @author Peter
 */
public class FreezeAtk extends MoveStatus{

    /**
     *
     * @param owner
     */
    public FreezeAtk(Player owner) throws IOException{
        super(owner);
        damage = 5;
        accurancy = 70;
        statusEffect = Player.statusId.FREEZE.getId();
        statusRate = 100;
    }

    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();
        List<SingleMoveResult> r = super.useMove(owner, target, attackers, defenders);
        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }
}
