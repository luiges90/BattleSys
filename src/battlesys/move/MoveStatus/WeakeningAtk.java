package battlesys.move.MoveStatus;

import battlesys.exception.BattleSysException;
import battlesys.*;import java.io.IOException;
 import java.util.*;

/**
 *
 * @author Peter
 */
public class WeakeningAtk extends MoveStatus{

    /**
     *
     * @param owner
     */
    public WeakeningAtk(Player owner) throws IOException{
        super(owner);
        damage = 2;
        accurancy = 100;
        statusEffect = Player.statusId.WEAKENING.getId();
        statusRate = 80;
    }

    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();
        List<SingleMoveResult> r = super.useMove(owner, target, attackers, defenders);
        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
