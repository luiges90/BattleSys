package battlesys;

import battlesys.exception.BattleSysException;
import java.io.IOException;
import java.util.*;

/**
 * Class for normal move - moves that do direct attacks
 * Damage way can be modified through changing the parameters
 * @author Peter
 */
public abstract class MoveNormal extends Move {

    /**
     * basic damage
     */
    /**
     * accurancy, in percentage, 70 means 70% success rate
     */
    protected int damage, accurancy;

    /**
     * whether the damage is fixed, regardless of user's or target's attack or defense
     */
    protected boolean fixedDmg;

    /**
     * The mean ratio of the target's hp is dealt the damage.
     */
    /**
     * The variance ratio of the target's hp is dealt the damage.
     */
    protected double damageRatioMean, damageRatioVar;

    /**
     * effect on user hp, negative means the less user hp, the higher the power, and vice versa. 0 means no effect due to user hp
     */
    protected double hpEffect;

    /**
     * whether the move has no critical
     */
    protected boolean noCritical;

    /**
     * Rate of the move become critical, in percentage
     */
    protected int criticalRate;
    /**
     * The mean bonus if this move become critical. 2 means damage dealt by critical attack will be doubled.
     */
    /**
     * The variance bouns if this move become critical
     */
    protected double criticalMean, criticalVar;

    /**
     * The ratio of the damage is added to the user's hp. If negative, using this move would cause the user to lose hp.
     */
    protected double absorbRate;

    /**
     * affect due to speed difference
     */
    protected double spdEffect;

    /**
     * whether the move is certain to hit
     */
    protected boolean mustHit;

    /**
     * whether to show attacker on every single attack, used by surround attacks
     */
    protected boolean showAttacker;

    /**
     * whether to show each damage. For surround attack 2 which, if all damage is shown, will clush the output.
     */
    protected boolean showDamage;

    /**
     * whether this move will destroy the target instantly
     */
    protected boolean instantDestroy;
    
    /**
     * Constructor. All subclass should call this constructor. This class sets the default values of the fields.
     * @param owner The owner of the move
     */
    protected MoveNormal(Player owner) throws IOException {
        super(owner);
        damage = 0;
        accurancy = 0;
        fixedDmg = false;
        damageRatioMean = 0;
        damageRatioVar = 0;
        hpEffect = 0;
        noCritical = false;
        criticalRate = 5;
        criticalMean = 2;
        criticalVar = 0.2;
        spdEffect = 5;
        mustHit = false;
        showAttacker = false;
        showDamage = true;
        instantDestroy = false;
    }

     /**
     * Use the direct attack
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

        boolean hit = false, critical = false;
        int damageDealt = 0;
        String s = "";

        if (Utility.probTest(Utility.inRange(accurancy + (attacker.getSpd() - defender.getSpd()) * spdEffect, 20, 100)) || mustHit) {
            hit = true;
            if (damage > 0 || damageRatioMean > 0 || instantDestroy) {           

                //Deal physical damage
                if (!fixedDmg) {
                    damageDealt = Utility.noSmallerThan(damage + (attacker.getAtk() - defender.getDef()) * 1, 1) * (Utility.randBetween(90, 110));
                } else {
                    damageDealt = (int) (damage * 100 + defender.hp * (damageRatioMean + Utility.randBetween(-damageRatioVar, damageRatioVar)));
                }
                //hpEffect
                if (hpEffect != 0) {
                    damageDealt *= hpEffect > 0 ? (attacker.hp / (attacker.initHp + 0.0)) * (hpEffect + 1) : (1 - Utility.inRange(attacker.hp / (attacker.initHp + 0.0), 0.01, 1)) * (-hpEffect + 1);
                }
                //critical
                if (Utility.probTest(criticalRate + (attacker.getMor() * 5 - defender.getMor()) * 5) && !noCritical) {
                    damageDealt *= Utility.randValue(criticalMean, criticalVar);
                    critical = true;
                }
                //minimum damage is 50.
                if (damageDealt <= 50) {
                    damageDealt = 50;
                }
                //instant destroy - ignores everthing and set damage to defender hp.
                if (instantDestroy){
                    damageDealt = defender.hp;
                }
                //maximum damage is defender's hp.
                if (damageDealt > defender.hp){
                    defender.hp = damageDealt;
                }

                defender.hp -= damageDealt;

                if (showDamage){
                    s += (showAttacker ? (attacker.getName() + "的攻擊") : "") + 
                            (critical ? "發動成暴擊！\n" + attacker.getName() + "使出" + this.getCritName() + "，" : "") + 
                          "命中" + defender.getName() + "，造成" + Utility.colorText(damageDealt, 0, 2000, 0, 0, 255, 255, 0, 0) + "傷害！";
                }

                //Absorb amount
                if (damageDealt > 0) {

                    int absorbAmount = (int) (damageDealt * absorbRate);
                    attacker.hp += absorbAmount;
                    if (showDamage){
                        if (absorbAmount > 0) {
                            s += attacker.getName() + "從敵人吸取了" + absorbAmount + "體力！";
                        } else if (absorbAmount < 0) {
                            s += attacker.getName() + "自己從攻擊中損耗了" + -absorbAmount + "體力！";
                        }
                    }
                }
            } else {
                if (showDamage){
                    s += (showAttacker?(attacker.getName() + "的攻擊") : "") +"命中" + defender.getName() + "！";
                }
            }
        } else {
            if (showDamage){
                s = (showAttacker?(attacker.getName()) : "") + "對" + defender.getName() + "進行攻擊，但沒有命中。";
            }
        }

        s += defender.noHpTriggers(defenders, attackers);
        s += attacker.noHpTriggers(attackers, defenders);

        return Collections.singletonList(new SingleMoveResult(damageDealt, hit, critical, attacker, defender, s));
    }

}
