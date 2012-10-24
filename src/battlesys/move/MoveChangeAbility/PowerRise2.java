package battlesys.move.MoveChangeAbility;

import battlesys.exception.BattleSysException;
import battlesys.*;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author Peter
 */
public class PowerRise2 extends MoveChangeAbility {

    /**
     *
     * @param owner
     */
    public PowerRise2(Player owner) throws IOException{
        super(owner);
        accurancy = 100;
        incType = AFFECT_ALL;
        incRateMean = 0.5;
        incRateVar = 0.1;
        onOpposing = false;
    }

    
    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();
        List<SingleMoveResult> r = super.useMove(owner, owner, attackers, defenders);
        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
