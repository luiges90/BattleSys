package battlesys.move.MoveNormal;

import battlesys.exception.BattleSysException;
import battlesys.*;import java.io.IOException;
 import java.util.*;

/**
 *
 * @author Peter
 */
public class ArmourThrough extends MoveNormal {

    /**
     *
     * @param owner
     */
    public ArmourThrough(Player owner) throws IOException{
        super(owner);
        damage = 15;
        accurancy = 70;
        noCritical = true;
        fixedDmg = true;
    }

    
    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();
        List<SingleMoveResult> r = super.useMove(owner, target, attackers, defenders);
        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
