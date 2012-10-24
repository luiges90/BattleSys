package battlesys.move.MoveNormal;

import battlesys.exception.BattleSysException;
import battlesys.*;import java.io.IOException;
 import java.util.*;

/**
 *
 * @author Peter
 */
public class SurroundAtk extends MoveNormal {

    /**
     *
     * @param owner
     */
    public SurroundAtk(Player owner) throws IOException{
        super(owner);
        damage = 4;
        accurancy = 50;
        showAttacker = true;
    }

    
    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();

        //determine number of attackers
        int numTargets = Math.min(Utility.randBetween(6,10), attackers.size());

        //select a subset of the attackers
        PlayerList attacker = attackers.playerSubset(numTargets);

        List<SingleMoveResult> r = new ArrayList<SingleMoveResult>();

        //launch attack from all people of this side
        for (Player p : attacker){
            if (p.getHp() > 0){
                r.addAll(super.useMove(p, target, attackers, defenders));
            }
        }

        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
