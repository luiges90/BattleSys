package battlesys.move.MoveChangeAbility;

import battlesys.exception.BattleSysException;
import battlesys.*;
import java.io.IOException;
import java.util.*;

/**
 * Revive 2.
 * Self-revival is hard-coded in Player.java, noHpTriggers
 * @author Peter
 */
public class Revive2 extends MoveChangeAbility {

    /**
     *
     * @param owner
     */
    public Revive2(Player owner) throws IOException{
        super(owner);
        accurancy = 100;
        incType = AFFECT_HP;
        incValueMean = 3000;
        incValueVar = 0;
        ignoreHp = true;
        onOpposing = false;

    }

    @Override
    protected String noHpTriggers(PlayerList thisSide, PlayerList opposingSide){
        try {
            if (this.getMoveTime() > 0){
                return this.useMove(owner, thisSide, opposingSide).getResultString();
            }
        } catch (BattleSysException ex){
            //move must exist and is bought!
            assert false;
        }
        return "";
    }
    
    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();
        List<SingleMoveResult> r = super.useMove(owner, target, attackers, defenders);
        if (r.get(0).isHit()){
            s += recoverStatus(r.get(0).getDefender());
        }
        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
