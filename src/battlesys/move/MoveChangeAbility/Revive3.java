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
public class Revive3 extends MoveChangeAbility {

    /**
     *
     * @param owner
     */
    public Revive3(Player owner) throws IOException{
        super(owner);
        accurancy = 100;
        incType = AFFECT_HP;
        recoverAll = true;
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
    
    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException {
        String s = preMove();

        List<SingleMoveResult> r = new ArrayList<SingleMoveResult>(attackers.size());

        for (Player p : attackers) {
            r.addAll(super.useMove(owner, p, attackers, defenders));
        }
        for (SingleMoveResult t : r){
            if (t.isHit()){
                s += recoverStatus(t.getDefender());
            }
        }

        postMove(r);

        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
