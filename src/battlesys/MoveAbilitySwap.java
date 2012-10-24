package battlesys;

import battlesys.exception.BattleSysException;
import java.io.IOException;
import java.util.*;

/**
 * Move that swaps ability between attacker and defender
 * @author Peter
 */
public abstract class MoveAbilitySwap extends Move {

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
    protected MoveAbilitySwap(Player owner) throws IOException {
        super(owner);
        accurancy = 0;
        incType = AFFECT_NONE;
    }

    /**
     * Use the move that swaps a certain ability between attacker and defender
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
                    int tp = attacker.hp;
                    if (attacker.hp > defender.hp) {
                        break;
                    }
                    attacker.hp = defender.hp;
                    defender.hp = tp;
                    break;
                }
                case AFFECT_ATK: {
                    int tp = attacker.atk;
                    if (attacker.atk > defender.atk) {
                        break;
                    }
                    attacker.atk = defender.atk;
                    defender.atk = tp;
                    break;
                }
                case AFFECT_DEF: {
                    int tp = attacker.def;
                    if (attacker.def > defender.def) {
                        break;
                    }
                    attacker.def = defender.def;
                    defender.def = tp;
                    break;
                }
                case AFFECT_SPD: {
                    int tp = attacker.spd;
                    if (attacker.spd > defender.spd) {
                        break;
                    }
                    attacker.spd = defender.spd;
                    defender.spd = tp;
                    break;
                }
                case AFFECT_MOR: {
                    int tp = attacker.mor;
                    if (attacker.mor > defender.mor) {
                        break;
                    }
                    attacker.mor = defender.mor;
                    defender.mor = tp;
                    break;
                }
                case AFFECT_ALL: {
                    if (attacker.hp < defender.hp) {
                        int tp = attacker.hp;
                        attacker.hp = defender.hp;
                        defender.hp = tp;
                    }
                    if (attacker.atk < defender.atk) {
                        int tp = attacker.atk;
                        attacker.atk = defender.atk;
                        defender.atk = tp;
                    }
                    if (attacker.def < defender.def) {
                        int tp = attacker.def;
                        attacker.def = defender.def;
                        defender.def = tp;
                    }
                    if (attacker.spd < defender.spd) {
                        int tp = attacker.spd;
                        attacker.spd = defender.spd;
                        defender.spd = tp;
                    }
                    if (attacker.mor < defender.mor) {
                        int tp = attacker.mor;
                        attacker.mor = defender.mor;
                        defender.mor = tp;
                    }
                }
            }
            String s = defender.getName() + "的" + AFFECT_STRING[actualAffected] + "與" + attacker.getName() + "的互換了！";
            return Collections.singletonList(new SingleMoveResult(0, true, false, attacker, defender, s));
        } else {
            String s = "對" + defender.getName() + "進行攻擊，但失敗了。";
            return Collections.singletonList(new SingleMoveResult(0, false, false, attacker, defender, s));
        }
    }

}
