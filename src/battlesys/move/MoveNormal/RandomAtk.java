package battlesys.move.MoveNormal;

import battlesys.exception.BattleSysException;
import battlesys.*;import java.io.IOException;
 import java.util.*;

/**
 *
 * @author Peter
 */
public class RandomAtk extends MoveNormal {

    /**
     *
     * @param owner
     */
    public RandomAtk(Player owner) throws IOException{
        super(owner);
        damage = 8;
        accurancy = 50;
    }

    
    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();

        List<SingleMoveResult> r = new ArrayList<SingleMoveResult>();

        for (int i = 0; i < Math.min(Utility.randBetween(2,4), defenders.size()); ++i){
             r.addAll(super.useMove(owner, defenders.randomPick(), attackers, defenders));
        }

        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
