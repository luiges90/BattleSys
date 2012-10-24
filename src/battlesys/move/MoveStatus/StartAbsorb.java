package battlesys.move.MoveStatus;

import battlesys.exception.BattleSysException;
import battlesys.*;import java.io.IOException;
 import java.util.*;

/**
 *
 * @author Peter
 */
public class StartAbsorb extends MoveStatus{

    /**
     *
     * @param owner
     */
    public StartAbsorb(Player owner) throws IOException{
        super(owner);
        accurancy = 70;
        statusEffect = Player.statusId.ABSORBING.getId();
        statusRate = 100;
        doAtk = false;
    }

    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();
        List<SingleMoveResult> r = super.useMove(owner, target, attackers, defenders);
        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
