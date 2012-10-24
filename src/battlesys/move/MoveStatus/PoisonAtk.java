package battlesys.move.MoveStatus;

import battlesys.exception.BattleSysException;
import battlesys.*;import java.io.IOException;
 import java.util.*;

/**
 *
 * @author Peter
 */
public class PoisonAtk extends MoveStatus{

    /**
     *
     * @param owner
     */
    public PoisonAtk(Player owner) throws IOException{
        super(owner);
        damage = 5;
        accurancy = 65;
        statusEffect = Player.statusId.POISON.getId();
        statusRate = 100;
    }

    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();
        List<SingleMoveResult> r = super.useMove(owner, target, attackers, defenders);
        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
