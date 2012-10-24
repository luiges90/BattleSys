package battlesys.move.MoveNormal;

import battlesys.exception.BattleSysException;
import battlesys.*;import java.io.IOException;
 import java.util.*;

/**
 *
 * @author Peter
 */
public class SelfDestruct extends MoveNormal {

    /**
     *
     * @param owner
     */
    public SelfDestruct(Player owner) throws IOException{
        super(owner);
        damage = 50;
        //over 100 accurancy means the user is certain to hit the opponent even the opponent has higher speed for some amount.
        accurancy = 120;

        usableByRandomMove = false;
    }


    
    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();

        List<SingleMoveResult> r = new ArrayList<SingleMoveResult>(defenders.size());

        //launch attack to all these opponents
        for (Player p : defenders){
            r.addAll(super.useMove(owner, p, attackers, defenders));
        }

        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }


}
