package battlesys.move.MoveChangeAbilityAttack;

import battlesys.exception.BattleSysException;
import battlesys.*;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author Peter
 */
public class AbsorbAbyAtk extends MoveChangeAbilityAttack {

    /**
     *
     * @param owner
     */
    public AbsorbAbyAtk(Player owner) throws IOException{
        super(owner);
        damage = 6;
        accurancy = 80;
        decAby = AFFECT_ANY;
        decAbyValueMean = 2;
        decAbyValueVar = 1;
        statusRate = 50;
    }

    
    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();
        List<SingleMoveResult> r = super.useMove(owner, target, attackers, defenders);
        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
