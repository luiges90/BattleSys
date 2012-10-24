package battlesys.move.MoveChangeAbility;

import battlesys.exception.BattleSysException;
import battlesys.*;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author Peter
 */
public class Recover extends MoveChangeAbility {

    /**
     *
     * @param owner
     */
    public Recover(Player owner) throws IOException{
        super(owner);
        accurancy = 100;
        incType = AFFECT_HP;
        incValueMean = 2400;
        incValueVar = 400;
        morAffectValue = 0.05;
        onOpposing = false;
    }

    
    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();
        List<SingleMoveResult> r = super.useMove(owner, target, attackers, defenders);
        if (r.get(0).isHit()){
            s += recoverStatus(r.get(0).getDefender());
        }
        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
