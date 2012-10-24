package battlesys.move.MoveSwapAbility;

import battlesys.exception.BattleSysException;
import battlesys.*;import java.io.IOException;
 import java.util.*;

/**
 *
 * @author Peter
 */
public class AbilitySwap extends MoveAbilitySwap{

    /**
     *
     * @param owner
     */
    public AbilitySwap(Player owner) throws IOException{
        super(owner);
        accurancy = 70;
        incType = AFFECT_ANY;
    }

    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();
        List<SingleMoveResult> r = super.useMove(owner, target, attackers, defenders);
        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
