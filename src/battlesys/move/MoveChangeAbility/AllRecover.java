package battlesys.move.MoveChangeAbility;

import battlesys.exception.BattleSysException;
import battlesys.*;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author Peter
 */
public class AllRecover extends MoveChangeAbility {

    /**
     *
     * @param owner
     */
    public AllRecover(Player owner) throws IOException{
        super(owner);
        accurancy = 90;
        incType = AFFECT_HP;
        incValueMean = 2400;
        incValueVar = 400;
        morAffectValue = 0.05;
        onOpposing = false;
    }

    
    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException {
        String s = preMove();

        //number of targets
        int numTarget = Math.min(Utility.randBetween(4, 8), attackers.size());

        //the actual targets
        PlayerList targets = attackers.playerSubset(numTarget);

        List<SingleMoveResult> r = new ArrayList<SingleMoveResult>(numTarget);

        for (Player p : targets){
            r.addAll(super.useMove(owner, p, attackers, defenders));
        }
        for (SingleMoveResult t : r){
            if (t.isHit()){
                s += recoverStatus(t.getDefender());
            }
        }

        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
