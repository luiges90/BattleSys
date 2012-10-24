package battlesys.move.MoveStatus;

import battlesys.exception.BattleSysException;
import battlesys.*;import java.io.IOException;
 import java.util.*;

/**
 *
 * @author Peter
 */
public class WeakeningAtk3 extends MoveStatus{

    /**
     *
     * @param owner
     */
    public WeakeningAtk3(Player owner) throws IOException{
        super(owner);
        damage = 7;
        accurancy = 90;
        statusEffect = Player.statusId.WEAKENING_2.getId();
        statusRate = 100;
    }

    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException {
        String s = preMove();

        List<SingleMoveResult> r = new ArrayList<SingleMoveResult>(defenders.size());

        for (Player p : defenders){
            r.addAll(super.useMove(owner, p, attackers, defenders));
        }

        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
