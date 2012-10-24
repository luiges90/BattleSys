package battlesys.move.MoveNormal;

import battlesys.exception.BattleSysException;
import battlesys.*;import java.io.IOException;
 import java.util.*;

/**
 *
 * @author Peter
 */
public class PercentCutAtk extends MoveNormal {

    /**
     *
     * @param owner
     */
    public PercentCutAtk(Player owner) throws IOException{
        super(owner);
        accurancy = 75;
        fixedDmg = true;
        noCritical = true;
        damageRatioMean = 0.15;
        damageRatioVar = 0.05;
    }

    
    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();
        List<SingleMoveResult> r = super.useMove(owner, target, attackers, defenders);
        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
