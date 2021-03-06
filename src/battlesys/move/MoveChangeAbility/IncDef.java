package battlesys.move.MoveChangeAbility;

import battlesys.exception.BattleSysException;
import battlesys.*;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author Peter
 */
public class IncDef extends MoveChangeAbility {

    /**
     *
     * @param owner
     */
    public IncDef(Player owner) throws IOException{
        super(owner);
        accurancy = 100;
        incType = AFFECT_DEF;
        incRateMean = 0.75;
        incRateVar = 0.15;
        onOpposing = false;
    }

    
    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();
        List<SingleMoveResult> r = super.useMove(owner, target, attackers, defenders);
        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
