package battlesys.move.MoveChangeAbility;

import battlesys.exception.BattleSysException;
import battlesys.*;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author Peter
 */
public class AllDecAtk extends MoveChangeAbility {

    /**
     *
     * @param owner
     */
    public AllDecAtk(Player owner) throws IOException{
        super(owner);       
        accurancy = 90;
        incType = AFFECT_ATK;
        incRateMean = -0.4;
        incRateVar = 0.1;
    }

    
    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException {
        String s = preMove();

        //number of targets
        int numTarget = Math.min(Utility.randBetween(4, 8), defenders.size());

        //the actual targets
        PlayerList targets = defenders.playerSubset(numTarget);

        List<SingleMoveResult> r = new ArrayList<SingleMoveResult>(numTarget);

        for (Player p : targets){
            r.addAll(super.useMove(owner, p, attackers, defenders));
        }

        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
