package battlesys.move.MoveSplitAbility;

import battlesys.exception.BattleSysException;
import battlesys.*;import java.io.IOException;
 import java.util.*;

/**
 *
 * @author Peter
 */
public class HpSplit extends MoveAbilitySplit{

    /**
     *
     * @param owner
     */
    public HpSplit(Player owner) throws IOException{
        super(owner);
        accurancy = 60;
        incType = AFFECT_HP;
    }

    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();
        List<SingleMoveResult> r = super.useMove(owner, target, attackers, defenders);
        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
