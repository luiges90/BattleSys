package battlesys.move.MoveNormal;

import battlesys.exception.BattleSysException;
import battlesys.*;import java.io.IOException;
 import java.util.*;

/**
 *
 * @author Peter
 */
public class SurroundAtk2 extends MoveNormal {

    /**
     *
     * @param owner
     */
    public SurroundAtk2(Player owner) throws IOException{
        super(owner);
        damage = 4;
        accurancy = 50;
        showAttacker = true;
        showDamage = false;
    }

    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();
        //determine number of attackers
        int numAttackers = Math.min(Utility.randBetween(10, 14), attackers.size());

        //select a subset of the attackers
        PlayerList selectedAttackers = attackers.playerSubset(numAttackers);

        List<SingleMoveResult> r = new ArrayList<SingleMoveResult>();

        //The output for this move is simply horrible! dump its output to null, and print crafted output done by this specific move
        s += "並向敵方發動大量攻勢，";

        //launch attack from all people of this side
        for (Player a : selectedAttackers){
            for (Player d : defenders){
                if (a.getHp() > 0 && d.getHp() > 0){
                    r.addAll(super.useMove(a, d, attackers, defenders));
                }
            }
        }
        
        //compue total damage
        int totalDamage = 0;
        for (SingleMoveResult t : r){
            totalDamage += t.getDamage();
        }

        s += "並一共造成" + Utility.colorText(totalDamage, 0, 2000, 0, 0, 255, 255, 0, 0) + "傷害！";

        postMove(r);

        return new CompleteMoveResult(r, s);
    }


}
