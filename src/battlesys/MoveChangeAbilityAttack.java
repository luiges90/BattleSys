package battlesys;

import battlesys.exception.BattleSysException;
import java.io.IOException;
import java.util.*;

/**
 * Move that changes or absorbs ability in addition to normal attack
 * @author Peter
 */
public abstract class MoveChangeAbilityAttack extends MoveNormal {

    /**
     * which ability to absorb/decrease
     */
    protected int decAby;
    /**
     * Mean for absorb value
     */
    /**
     * Variance for absorb value
     */
    protected int decAbyValueMean, decAbyValueVar;
    /**
     * probability of ability absorption/decrease taking effect, in percentage, 70 means 70% success rate
     */
    protected int statusRate;
    /**
     * whether absorption is taken place
     */
    protected boolean absorb;

    /**
     * Constructor. All subclass should call this constructor. This class sets the default values of the fields.
     * @param owner The owner of the move
     */
    protected MoveChangeAbilityAttack(Player owner) throws IOException {
        super(owner);
        decAby = AFFECT_NONE;
        decAbyValueMean = 0;
        decAbyValueVar = 0;
        statusRate = 0;
        absorb = false;
    }


     /**
     * Use the move that changes or absorbs ability in addition to normal attack
     * @param user
     * @param target
     * @param attackers
     * @param defenders
     * @return singleton list that contain the result of the move
     * @throws BattleSysException
     */
    @Override
    protected List<SingleMoveResult> useMove(Player attacker, Player defender, PlayerList attackers, PlayerList defenders) throws BattleSysException {
        SingleMoveResult r = super.useMove(attacker, defender, attackers, defenders).get(0);
        String s = r.getResultString();

        if (r.isHit()) {
            if (decAby != AFFECT_NONE && Utility.probTest(statusRate)) {
                int affectAby = decAby;
                if (decAby == AFFECT_ANY) {
                    affectAby = Utility.randBetween(1, 4);
                }
                int affectValue = Utility.randValue(decAbyValueMean - decAbyValueVar, decAbyValueMean + decAbyValueVar);
                switch (affectAby) {
                    case AFFECT_ATK: {
                        defender.atk -= affectValue;
                        if (absorb) {
                            attacker.atk += affectValue;
                            s += attacker.getName() + "吸取了" + defender.getName() + "的" + affectValue + "攻擊力！";
                        } else {
                            s += defender.getName() + "的攻擊力下降了" + affectValue + "！";
                        }
                        break;
                    }
                    case AFFECT_DEF: {
                        defender.def -= affectValue;
                        if (absorb) {
                            attacker.def += affectValue;
                            s += attacker.getName() + "吸取了" + defender.getName() + "的" + affectValue + "防禦力！";
                        } else {
                            s += defender.getName() + "的防禦力下降了" + affectValue + "！";
                        }
                        break;
                    }
                    case AFFECT_SPD: {
                        defender.spd -= affectValue;
                        if (absorb) {
                            attacker.spd += affectValue;
                            s += attacker.getName() + "吸取了" + defender.getName() + "的" + affectValue + "速度！";
                        } else {
                            s += defender.getName() + "的速度下降了" + affectValue + "！";
                        }
                        break;
                    }
                    case AFFECT_MOR: {
                        if (absorb) {
                            attacker.mor += affectValue;
                            s += attacker.getName() + "吸取了" + defender.getName() + "的" + affectValue + "鬥志！";
                        } else {
                            s += defender.getName() + "的鬥志下降了" + affectValue + "！";
                        }
                    }
                }
            }
        }

        return Collections.singletonList(new SingleMoveResult(r, s));
    }
}
