package battlesys;

import battlesys.exception.BattleSysException;
import java.io.IOException;
import java.util.*;

/**
 * Move that splits ability between attacker and defender
 * @author Peter
 */
public abstract class MoveAbilitySplit extends Move {

    /**
     * Accurancy (success probability) of this move, in percentage, 70 means 70% success rate
     */
    protected int accurancy;
    /**
     * What ability to increase, as in Move.INC_ATK etc...
     */
    protected int incType;

    /**
     * Constructor. All subclass should call this constructor. This class sets the default values of the fields.
     * @param owner The owner of the move
     */
    protected MoveAbilitySplit(Player owner) throws IOException {
        super(owner);
        accurancy = 0;
        incType = AFFECT_NONE;
    }

    /**
     * Use the move that splits a certain ability between attacker and defender
     * @param attacker
     * @param defender
     * @param attackers
     * @param defenders
     * @return singleton list that contain the result of the move
     * @throws BattleSysException
     */
    protected List<SingleMoveResult> useMove(Player attacker, Player defender, PlayerList attackers, PlayerList defenders) throws BattleSysException {
        if (!checkUsable(attacker, defender, attackers, defenders)) {
            return Collections.singletonList(new SingleMoveResult(attacker, defender, unusableReasonString(attacker, defender, attackers, defenders)));
        }

        if (basicAtkOnly() && !basic) {
            CompleteMoveResult r = owner.forcedBasicAtk(defender, attackers, defenders);
            return Collections.singletonList(new SingleMoveResult(r.get(0), "但失敗了。\n" + r.getResultString()));
        }

        if (owner.checkDizzy() || owner.checkReflect(defender)) {
            defender = attacker;
        }

        if (Utility.probTest(Utility.inRange(accurancy + (owner.getMor() - defender.getMor()) * 5, 0, 100))) {

            int actualAffected;
            if (incType == AFFECT_ANY) {
                actualAffected = Utility.randBetween(1, 4);
            } else {
                actualAffected = incType;
            }

            switch (actualAffected) { //Relative part influence, and actual change
                case AFFECT_HP: {
                    int tp = (attacker.hp + defender.hp) / 2;
                    attacker.hp = tp;
                    defender.hp = tp;
                    break;
                }
                case AFFECT_ATK: {
                    int tp = (attacker.atk + defender.atk) / 2;
                    attacker.atk = tp;
                    defender.atk = tp;
                    break;
                }
                case AFFECT_DEF: {
                    int tp = (attacker.def + defender.def) / 2;
                    attacker.def = tp;
                    defender.def = tp;
                    break;
                }
                case AFFECT_SPD: {
                    int tp = (attacker.spd + defender.spd) / 2;
                    attacker.spd = tp;
                    defender.spd = tp;
                    break;
                }
                case AFFECT_MOR: {
                    int tp = (attacker.mor + defender.mor) / 2;
                    attacker.mor = tp;
                    defender.mor = tp;
                    break;
                }
                case AFFECT_ALL: {
                    int tp = (attacker.hp + defender.hp) / 2;
                    attacker.hp = tp;
                    defender.hp = tp;
                    tp = (attacker.atk + defender.atk) / 2;
                    attacker.atk = tp;
                    defender.atk = tp;
                    tp = (attacker.def + defender.def) / 2;
                    attacker.def = tp;
                    defender.def = tp;
                    tp = (attacker.spd + defender.spd) / 2;
                    attacker.spd = tp;
                    defender.spd = tp;
                    tp = (attacker.mor + defender.mor) / 2;
                    attacker.mor = tp;
                    defender.mor = tp;
                }
            }
            String s = defender.getName() + "的" + AFFECT_STRING[actualAffected] + "與" + attacker.getName() + "的平分了！";

            return Collections.singletonList(new SingleMoveResult(0, true, false, attacker, defender, s));

        } else {

            String s = ("對" + defender.getName() + "進行攻擊，但失敗了。");
            return Collections.singletonList(new SingleMoveResult(0, false, false, attacker, defender, s));

        }
    }

}
