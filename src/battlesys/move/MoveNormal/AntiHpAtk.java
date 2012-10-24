package battlesys.move.MoveNormal;

import battlesys.exception.BattleSysException;
import battlesys.*;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author Peter
 */
public class AntiHpAtk extends MoveNormal {

    /**
     *
     * @param owner
     */
    public AntiHpAtk(Player owner) throws IOException{
        super(owner);
        damage = 10;
        accurancy = 70;
        hpEffect = -4;
    }

    
    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();
        List<SingleMoveResult> r = super.useMove(owner, target, attackers, defenders);
        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
