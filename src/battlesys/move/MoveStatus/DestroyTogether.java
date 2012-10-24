package battlesys.move.MoveStatus;

import battlesys.exception.BattleSysException;
import battlesys.*;import java.io.IOException;
 import java.util.*;

/**
 *
 * @author Peter
 */
public class DestroyTogether extends MoveStatus{

    /**
     *
     * @param owner
     */
    public DestroyTogether(Player owner) throws IOException{
        super(owner);
        accurancy = 70;
        statusEffect = Player.statusId.DESTROY_LINK.getId();
        statusRate = 100;
        doAtk = false;
        applyToSelf = true;
        actOnOpponent = true;
    }

    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();
        List<SingleMoveResult> r = super.useMove(owner, defenders.randomPick(), attackers, defenders);
        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
