package battlesys.move.MoveStatus;

import battlesys.exception.BattleSysException;
import battlesys.*;import java.io.IOException;
 import java.util.*;

/**
 *
 * @author Peter
 */
public class WeakeningAtk2 extends MoveStatus{

    /**
     *
     * @param owner
     */
    public WeakeningAtk2(Player owner) throws IOException{
        super(owner);
        damage = 6;
        accurancy = 100;
        statusEffect = Player.statusId.WEAKENING_2.getId();
        statusRate = 100;
    }

    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();
        List<SingleMoveResult> r = super.useMove(owner, target, attackers, defenders);
        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
