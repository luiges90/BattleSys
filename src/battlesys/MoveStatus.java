package battlesys;

import battlesys.exception.BattleSysException;
import java.io.IOException;
import java.util.*;

/**
 * For moves that are attacking and also inflict status to the target.
 * For moves that inflict status only, those move are under this class, with setting doAtk to false in the respective constructor
 * @author Peter
 */
public abstract class MoveStatus extends MoveNormal {

    /**
     * The status effect, using Player.statusId.STATUS_NAME.getId(), or RANDOM_BAD indicating a random bad status
     */
    protected int statusEffect;
    /**
     * rate of getting status, in percentage. 70 means 70% of probability
     */
    protected int statusRate;
    /**
     * Whether the move meant to apply status on oneself
     */
    protected boolean applyToSelf;
    /**
     * Whether the move contain an attack part
     */
    protected boolean doAtk;
    /**
     * for non-attacking moves, whether it is unaffected by morale of both sides. If it is set to true, the success rate of opponent getting
     * the status will subject to statusRate only.
     */
    protected boolean mustApply;
    /**
     * For moves that apply to self, whether it act on opponent? (For DestroyTogether).
     */
    protected boolean actOnOpponent;

    /**
     * Constructor. All subclass should call this constructor. This class sets the default values of the fields.
     * @param owner The owner of the move
     */
    protected MoveStatus(Player owner) throws IOException {
        super(owner);
        statusEffect = Player.statusId.NO_EFFECT.getId();
        statusRate = 0;
        applyToSelf = false;
        doAtk = true;
        mustApply = false;
        actOnOpponent = false;
    }

     /**
     * Use the move that inflict a certain status the defendef
     * @param attacker
     * @param defender
     * @param attackers
     * @param defenders
     * @return singleton list that contain the result of the move
     * @throws BattleSysException
     */
    @Override
    protected List<SingleMoveResult> useMove(Player attacker, Player defender, PlayerList attackers, PlayerList defenders) throws BattleSysException {
        SingleMoveResult r = null;

        //normal attack first. But if the attack is not to attack other, skip this part but do testing on non-attack moves
        if (doAtk) {
            r = super.useMove(attacker, defender, attackers, defenders).get(0);
        } else {
            if (!applyToSelf) {
                return Collections.singletonList(new SingleMoveResult(attacker, defender, unusableReasonString(attacker, defender, attackers, defenders)));
            }

            if (basicAtkOnly() && !basic) {
                if (defender != null) {
                    CompleteMoveResult m = owner.forcedBasicAtk(defender, attackers, defenders);
                    return Collections.singletonList(new SingleMoveResult(m.get(0), "但失敗了。\n" + m.getResultString()));
                } else {
                    return Collections.singletonList(new SingleMoveResult(attacker, defender));
                }
            }

            if (!applyToSelf) {
                if (owner.checkDizzy() || owner.checkReflect(defender)) {
                    defender = attacker;
                }
            }

            int successRate = (defender == null ? attacker.getMor() : (attacker.getMor() - defender.getMor()));

            if (Utility.probTest(Utility.inRange(accurancy + successRate * 5, 0, 100)) || mustApply) {
                //result string will be inserted later
                r = new SingleMoveResult(0, true, false, attacker, defender, "");
            } else {
                String s = "向" + ((applyToSelf && !actOnOpponent) ? attacker.getName() : defender.getName()) + "發動，但失敗了。";
                r = new SingleMoveResult(0, false, false, attacker, defender, s);
            }
        }

        //who has the status inflicted?
        Player inflicted = applyToSelf ? attacker : defender;

        //if the direct attack is hit, apply status
        if (r.isHit() && r.isSuccessful()) {
            int inflictedStatus = Player.statusId.NO_EFFECT.getId();

            if (statusEffect != Player.statusId.NO_EFFECT.getId() && Utility.probTest(statusRate)) {
                int actualEffect = statusEffect;
                if (statusEffect == RANDOM_BAD) {
                    actualEffect = Utility.randomPick(Player.BAD_STATUS_LIST);
                }
                inflicted.status[actualEffect] = true;
                inflictedStatus = actualEffect;
                if (actualEffect == Player.statusId.CHARGE.getId()) {
                    attacker.playerToAtk = defender;
                }
                if (actualEffect == Player.statusId.SHIELD_2.getId()) {
                    attacker.shield2Finishing = false;
                }
                if (actualEffect == Player.statusId.ABSORBING.getId()) {
                    defender.absorbTo.add(attacker);
                }
                if (actualEffect == Player.statusId.DESTROY_LINK.getId()) {
                    attacker.destroyLinkTarget = defender;
                }
                String s = (applyToSelf ? attacker.getName() : defender.getName()) + Player.STATUS_STRING[inflictedStatus] + "了！";
                r = new SingleMoveResult(r, s);
            }

        }

        return Collections.singletonList(r);
    }
}
