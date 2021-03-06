package battlesys.move.MoveRandom;

import battlesys.exception.BattleSysException;
import battlesys.*;import java.io.IOException;
 import java.util.*;

/**
 *
 * @author Peter
 */
public class RandomMove extends MoveRandom{

    /**
     *
     * @param owner
     */
    public RandomMove(Player owner) throws IOException{
        super(owner);
    }

    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        times = 2;
        String s = preMove();
        s += owner.getName() + "隨意使用招式！";

        List<SingleMoveResult> r = super.useMove(owner, target, attackers, defenders);

        //postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
