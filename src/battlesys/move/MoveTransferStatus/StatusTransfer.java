package battlesys.move.MoveTransferStatus;

import battlesys.exception.BattleSysException;
import battlesys.*;import java.io.IOException;
 import java.util.*;

/**
 *
 * @author Peter
 */
public class StatusTransfer extends MoveTransferStatus{

    /**
     *
     * @param owner
     */
    public StatusTransfer(Player owner) throws IOException{
        super(owner);
        accurancy = 80;
    }

    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();
        List<SingleMoveResult> r = super.useMove(owner, target, attackers, defenders);
        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
