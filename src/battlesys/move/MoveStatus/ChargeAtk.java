package battlesys.move.MoveStatus;

import battlesys.exception.BattleSysException;
import battlesys.*;import battlesys.exception.InvalidMoveNameException;
import java.io.IOException;
 import java.util.*;

/**
 * Some note on charge atk: As it is an attack that needs 2 rounds, this move first apply the CHARGE status, and store the target given in
 * the call to using this move.
 * The actual attack (incuding its damage, and accurancy)
 * is hard-coded into Player.java (preRound), such that the player with this status will attack at the next round.
 * @author Peter
 */
public class ChargeAtk extends MoveStatus{

    /**
     *
     * @param owner
     */
    public ChargeAtk(Player owner) throws IOException{
        super(owner);
        //this is the accurancy to apply the CHARGE status, but not the actual attack!
        accurancy = 100;
        statusEffect = Player.statusId.CHARGE.getId();
        statusRate = 100;
        doAtk = false;
        applyToSelf = true;
        mustApply = true;
        noKeepStat = true;
        alsoGetMove.add("ChargeAtk_Real");
    }

    public CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException{
        String s = preMove();
        List<SingleMoveResult> r = super.useMove(owner, target, attackers, defenders);
        postMove(r);
        return new CompleteMoveResult(r, s + SingleMoveResult.combineMoveResultString(r));
    }

}
