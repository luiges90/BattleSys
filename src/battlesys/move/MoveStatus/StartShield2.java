package battlesys.move.MoveStatus;

import battlesys.exception.BattleSysException;
import battlesys.*;import java.io.IOException;
 import java.util.*;

/**
 *
 * @author Peter
 */
public class StartShield2 extends MoveStatus{

    /**
     *
     * @param owner
     */
    public StartShield2(Player owner) throws IOException{
        super(owner);
        accurancy = 100;
        statusEffect = Player.statusId.SHIELD_2.getId();
        statusRate = 100;
        doAtk = false;
        applyToSelf = true;
        mustApply = true;
        onOpposing = false;
    }

    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();
        List<SingleMoveResult> r = super.useMove(owner, owner, attackers, defenders);
        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
