package battlesys.move.MoveNormal;

import battlesys.exception.BattleSysException;
import battlesys.*;import java.io.IOException;
 import java.util.*;

/**
 * The actual attack of the ChargeAtk
 * @author Peter
 */
public class ChargeAtk_Real extends MoveNormal{

    /**
     *
     * @param owner
     */
    public ChargeAtk_Real(Player owner) throws IOException{
        super(owner);
        damage = 36;
        accurancy = 70;
    }
    
    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();
        List<SingleMoveResult> r = super.useMove(owner, target, attackers, defenders);
        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
