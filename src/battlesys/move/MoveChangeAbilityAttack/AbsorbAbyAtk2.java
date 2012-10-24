package battlesys.move.MoveChangeAbilityAttack;

import battlesys.exception.BattleSysException;
import battlesys.*;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author Peter
 */
public class AbsorbAbyAtk2 extends MoveChangeAbilityAttack {

    /**
     *
     * @param owner
     */
    public AbsorbAbyAtk2(Player owner) throws IOException{
        super(owner);
        damage = 7;
        accurancy = 80;
        decAby = AFFECT_ANY;
        decAbyValueMean = 2;
        decAbyValueVar = 1;
        statusRate = 100;
    }

    
    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();
        List<SingleMoveResult> r = super.useMove(owner, target, attackers, defenders);
        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
