package battlesys.move.MoveRandom;

import battlesys.exception.BattleSysException;
import battlesys.*;import java.io.IOException;
 import java.util.*;

/**
 *
 * @author Peter
 */
public class RandomMove3 extends MoveRandom{

    /**
     *
     * @param owner
     */
    public RandomMove3(Player owner) throws IOException{
        super(owner);
    }

    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        times = Utility.randBetween(2,3);
        String s = preMove();

        List<SingleMoveResult> r = new ArrayList<SingleMoveResult>(attackers.size());

        for (Player p : attackers){
            s += p.getName() + "隨意使用招式！";
            r.addAll(super.useMove(p, target, attackers, defenders));
        }

        //postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
