package battlesys.move.MoveChangeAbility;

import battlesys.exception.BattleSysException;
import battlesys.*;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author Peter
 */
public class PowerRise3 extends MoveChangeAbility {

    /**
     *
     * @param owner
     */
    public PowerRise3(Player owner) throws IOException{
        super(owner);
        accurancy = 90;
        incType = AFFECT_ALL;
        incRateMean = 0.4;
        incRateVar = 0.1;
        onOpposing = false;
    }

    
    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();

        List<SingleMoveResult> r = new ArrayList<SingleMoveResult>(attackers.size());

        for (Player p : attackers) {
            r.addAll(super.useMove(owner, p, attackers, defenders));
        }

        postMove(r);

        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
