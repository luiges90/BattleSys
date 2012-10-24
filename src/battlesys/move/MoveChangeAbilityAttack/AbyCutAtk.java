package battlesys.move.MoveChangeAbilityAttack;

import battlesys.exception.BattleSysException;
import battlesys.*;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author Peter
 */
public class AbyCutAtk extends MoveChangeAbilityAttack {

    /**
     *
     * @param owner
     */
    public AbyCutAtk(Player owner) throws IOException{
        super(owner);
        damage = 5;
        accurancy = 75;
        decAby = AFFECT_ANY;
        decAbyValueMean = 2;
        decAbyValueVar = 1;
        statusRate = 80;
    }

    
    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();
        List<SingleMoveResult> r = super.useMove(owner, target, attackers, defenders);
        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
