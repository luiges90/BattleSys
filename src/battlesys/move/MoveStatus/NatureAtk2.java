package battlesys.move.MoveStatus;

import battlesys.exception.BattleSysException;
import battlesys.*;import java.io.IOException;
 import java.util.*;

/**
 *
 * @author Peter
 */
public class NatureAtk2 extends MoveStatus{

    /**
     *
     * @param owner
     */
    public NatureAtk2(Player owner) throws IOException{
        super(owner);
        damage = 18;
        accurancy = 100;
        statusEffect = RANDOM_BAD;
        statusRate = 80;
        mustHit = true;
    }

    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();
        List<SingleMoveResult> r = super.useMove(owner, target, attackers, defenders);
        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
