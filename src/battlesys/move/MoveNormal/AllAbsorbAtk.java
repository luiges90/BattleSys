package battlesys.move.MoveNormal;

import battlesys.exception.BattleSysException;
import battlesys.*;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author Peter
 */
public class AllAbsorbAtk extends MoveNormal {

    /**
     *
     * @param owner
     */
    public AllAbsorbAtk(Player owner) throws IOException{
        super(owner);
        damage = 8;
        accurancy = 60;
        absorbRate = 0.5;
    }

    
    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();

        //determine number of targets
        int numTargets = Math.min(Utility.randBetween(3,5), defenders.size());

        //select a subset of the opponents
        PlayerList targets = defenders.playerSubset(numTargets);

        List<SingleMoveResult> r = new ArrayList<SingleMoveResult>(numTargets);

        //launch attack to all these opponents
        for (Player p : targets){
            r.addAll(super.useMove(owner, p, attackers, defenders));
        }

        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }


}
