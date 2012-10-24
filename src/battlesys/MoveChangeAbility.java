package battlesys;

import battlesys.exception.BattleSysException;
import java.io.IOException;
import java.util.*;

/**
 * Moves that solely changes ability
 * @author Peter
 */
public abstract class MoveChangeAbility extends Move {

    /**
     * The mean for the absolute value increase
     */
    /**
     * The variance for the absolute value increase
     */
    protected int incValueMean, incValueVar;
    /**
     * The mean for the absolute relative value increase
     */
    /**
     * The variance for the absolute relative value increase
     */
    protected double incRateMean, incRateVar;

    /**
     * the effect due to morale
     */
    protected double morAffectValue;

    /**
     * the probability effect due to morale
     */
    protected double morAffectProbRate;

    /**
     * type of increase
     */
    protected int incType;

    /**
     * which move to increase
     */
    protected int incMpMove;

    /**
     * success rate, in percentage, 70 means 70% success rate
     */
    protected int accurancy;

    /**
     * Whether this move recovers all hp of the target
     */
    protected boolean recoverAll;

    /**
     * Constructor. All subclass should call this constructor. This class sets the default values of the fields.
     * @param owner The owner of the move
     */
    protected MoveChangeAbility(Player owner) throws IOException {
        super(owner);
        incType = AFFECT_HP;
        incMpMove = 0;
        incRateMean = 0;
        incRateVar = 0;
        incValueMean = 0;
        incValueVar = 0;
        morAffectValue = 0.1;
        morAffectProbRate = 5;
        ignoreHp = false;
        accurancy = 0;
        recoverAll = false;
    }

    /**
     * Do recover the status of the given target
     * @param target
     */
    protected String recoverStatus(Player target) {
        return target.recoverStatus();
    }

    /**
     * Use the move that changes a certain ability of the target
     * @param attacker
     * @param defender
     * @param attackers
     * @param defenders
     * @return singleton list that contain the result of the move
     * @throws BattleSysException
     */
    protected List<SingleMoveResult> useMove(Player attacker, Player defender, PlayerList attackers, PlayerList defenders) throws BattleSysException {
        boolean increase = incValueMean > 0 || incRateMean > 0;

        //effect due to morale value of both sides
        int morEffect = increase ? owner.getMor() : owner.getMor() - defender.getMor();

        if (!checkUsable(attacker, defender, attackers, defenders)) {
            return Collections.singletonList(new SingleMoveResult(attacker, defender, unusableReasonString(attacker, defender, attackers, defenders)));
        }

        if (basicAtkOnly() && !basic) {
            CompleteMoveResult r = owner.forcedBasicAtk(defender, attackers, defenders);
            return Collections.singletonList(new SingleMoveResult(r.get(0), "但失敗了。\n" + r.getResultString()));
        }

        if (Utility.probTest(Utility.inRange(accurancy + morEffect * morAffectProbRate, 0, 100))) {

            int incValue = (int) Math.round(Utility.randValue(incValueMean-incValueVar, incValueMean+incValueVar)
                    * (1 + morEffect * morAffectValue)); //Absolute part influence

            int actualAffected;
            if (incType == AFFECT_ANY) {
                actualAffected = Utility.randBetween(1, 4);
            } else {
                actualAffected = incType;
            }

            switch (actualAffected) { //Relative part influence, and actual change
                case AFFECT_HP: {
                    if (defender.hp < 0) {
                        defender.hp = defender.btHp = 0; //Hp negative initially, and absolute addition would fail - set zero first
                    }
                    incValue += defender.initHp * Utility.randValue(incRateMean-incRateVar, incRateMean+incRateVar);
                    incValue *= 1 + morEffect * morAffectValue;
                    incValue = increase ? Math.max(incValue, 100) : Math.min(incValue, -100);
                    if (recoverAll){
                        incValue = defender.maxHp - defender.hp;
                    }
                    defender.hp += incValue;
                    defender.btHp += incValue;
                    break;
                }
                /*case AFFECT_MP: {
                int mp = target.initialMoveTime[incMpMove];
                if (mp < 0) {
                mp = 0; //Hp negative initially, and absolute addition would fail - set zero first
                }
                incValue += INITIAL_MOVE_TIME[incMpMove] * Utility.randValue(incRateMean-incRateVar, incRateMean+incRateVar);
                incValue *= 1 + morEffect * morAffectValue;
                incValue = increase ? Math.max(incValue, 1) : Math.min(incValue, -1);
                target.initialMoveTime[incMpMove] += incValue;
                if (Main.r1) {
                if (target.initialMoveTime[incMpMove] > R1_INITIAL_MOVE_TIME[incMpMove]) {
                target.initialMoveTime[incMpMove] = R1_INITIAL_MOVE_TIME[incMpMove];
                }
                }
                break;
                }*/
                case AFFECT_ATK: {
                    incValue += defender.initAtk * Utility.randValue(incRateMean-incRateVar, incRateMean+incRateVar);
                    incValue *= (1 + morEffect * morAffectValue);
                    incValue = increase ? Math.max(incValue, 1) : Math.min(incValue, -1);
                    defender.atk += incValue;
                    defender.btAtk += incValue;
                    break;
                }
                case AFFECT_DEF: {
                    incValue += defender.initDef * Utility.randValue(incRateMean-incRateVar, incRateMean+incRateVar);
                    incValue *= (1 + morEffect * morAffectValue);
                    incValue = increase ? Math.max(incValue, 1) : Math.min(incValue, -1);
                    defender.def += incValue;
                    defender.btDef += incValue;
                    break;
                }
                case AFFECT_SPD: {
                    incValue += defender.initSpd * Utility.randValue(incRateMean-incRateVar, incRateMean+incRateVar);
                    incValue *= (1 + morEffect * morAffectValue);
                    incValue = increase ? Math.max(incValue, 1) : Math.min(incValue, -1);
                    defender.spd += incValue;
                    defender.btSpd += incValue;
                    break;
                }
                case AFFECT_MOR: {
                    incValue += defender.initMor * Utility.randValue(incRateMean-incRateVar, incRateMean+incRateVar);
                    incValue *= (1 + morEffect * morAffectValue);
                    incValue = increase ? Math.max(incValue, 1) : Math.min(incValue, -1);
                    defender.mor += incValue;
                    defender.btMor += incValue;
                    break;
                }
                case AFFECT_ALL: {
                    //incValue represents all power up added up
                    //absValue for absolute increase of the abilities
                    //abyValue for a specific ability increase
                    double absValue = incValue;
                    double abyValue = incValue;

                    //incValue reset to 0, and add them by each of ability
                    incValue = 0;

                    //attack
                    abyValue = defender.initAtk * (Utility.randValue(incRateMean-incRateVar, incRateMean+incRateVar)
                            * (1 + morEffect * morAffectValue)) + absValue;
                    abyValue = increase ? Math.max(abyValue, 1) : Math.min(abyValue, -1);
                    defender.atk += abyValue;
                    defender.btAtk += abyValue;
                    incValue += abyValue;

                    //defense
                    abyValue = defender.initDef * (Utility.randValue(incRateMean-incRateVar, incRateMean+incRateVar)
                            * (1 + morEffect * morAffectValue)) + absValue;
                    abyValue = increase ? Math.max(abyValue, 1) : Math.min(abyValue, -1);
                    defender.def += abyValue;
                    defender.btDef += abyValue;
                    incValue += abyValue;

                    //speed
                    abyValue = defender.initSpd * (Utility.randValue(incRateMean-incRateVar, incRateMean+incRateVar)
                            * (1 + morEffect * morAffectValue)) + absValue;
                    abyValue = increase ? Math.max(abyValue, 1) : Math.min(abyValue, -1);
                    defender.spd += abyValue;
                    defender.btSpd += abyValue;
                    incValue += abyValue;

                    //morale
                    abyValue = defender.initMor * (Utility.randValue(incRateMean-incRateVar, incRateMean+incRateVar)
                            * (1 + morEffect * morAffectValue)) + absValue;
                    abyValue = increase ? Math.max(abyValue, 1) : Math.min(abyValue, -1);
                    defender.mor += abyValue;
                    defender.btMor += abyValue;
                    incValue += abyValue;

                }
            }

            String s = null;
            if (incType != AFFECT_MP || incType != AFFECT_ALL) {
                s = "令" + (defender == owner ? "自己" : defender.getName()) + "的"
                        + (incType == AFFECT_ALL ? "所有能力" : AFFECT_STRING[actualAffected]) + (incValue > 0 ? "增加" : "減少")
                        + Utility.colorText(Math.abs(incValue), 0, 30, 0, 0, 255, 0, 255, 0) + "。";
            } else if (incType == AFFECT_ALL) {
                s = "令" + (defender == owner ? "自己" : defender.getName()) + "的所有能力出現變化！";
            }/* else {
            System.out.print("令" + (target == owner ? "自己" : target.getName()) + "的"
            + (target.atkName[incMpMove]) + "可用次數" + (incValue > 0 ? "增加" : "減少")
            + Utility.colorText(Math.abs(incValue), 0, 30, 0, 0, 255, 0, 255, 0) + "。");
            }*/
            return Collections.singletonList(new SingleMoveResult(0, true, false, owner, defender, s));
        } else {
            String s = "向" + (defender == owner ? "自己" : defender.getName()) + "發動，但失敗了。";
            return Collections.singletonList(new SingleMoveResult(0, false, false, owner, defender, s));
        }

    }
        
}
