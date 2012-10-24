package battlesys.move.MoveNormal;

import battlesys.exception.BattleSysException;
import battlesys.*;import java.io.IOException;
 import java.util.*;

/**
 *
 * @author Peter
 */
public class ConsecutiveAtk extends MoveNormal {

    /**
     *
     * @param owner
     */
    public ConsecutiveAtk(Player owner) throws IOException{
        super(owner);
        damage = 6;
        accurancy = 70;
    }

    
    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();

        List<SingleMoveResult> r = new ArrayList<SingleMoveResult>();

        for (int i = 0; i < Utility.randBetween(2,4); ++i){
             r.addAll(super.useMove(owner, target, attackers, defenders));
        }

        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
