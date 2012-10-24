package battlesys.move.MoveStatus;

import battlesys.exception.BattleSysException;
import battlesys.*;import java.io.IOException;
 import java.util.*;

/**
 *
 * @author Peter
 */
public class StartReflect extends MoveStatus{

    /**
     *
     * @param owner
     */
    public StartReflect(Player owner) throws IOException{
        super(owner);
        accurancy = 50;
        statusEffect = Player.statusId.REFLECT.getId();
        statusRate = 100;
        doAtk = false;
        applyToSelf = true;
        onOpposing = false;
    }

    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();
        List<SingleMoveResult> r = super.useMove(owner, owner, attackers, defenders);
        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
