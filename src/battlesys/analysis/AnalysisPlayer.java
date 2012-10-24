package battlesys.analysis;

import battlesys.exception.*;
import battlesys.*;
import java.util.*;

/**
 * A concrete implementation for the Player, with simple move usage implemented.
 * This is used for testing and balancing purposes.
 * @author Peter
 */
public class AnalysisPlayer extends Player {

    private ArrayList<String> moves;

    /**
     * Generate a player
     * @param name Name of the player
     * @param teamName Team name of the player. Always note that players with same team name are considered the same team
     * @param point Point allocated to this player
     * @param buyAtkNames Simple class names of the moves that the player buys
     * @throws BattleSysException When one of the move names specified in buyAtkNames does not exist
     * @throws IllegalArgumentException When the point allocated is not sufficient to buy all move listed in buyAtkNames
     */
    public AnalysisPlayer(String name, String teamName, int point, List<String> buyAtkNames) throws InvalidMoveNameException, NotEnoughPointsException, UnbuyableMoveException{
        super();

        int pointLeft = point;

        //scan the buyAtkCodes array and reduce pointleft
        for (int i = 0; i < buyAtkNames.size(); ++i){
            pointLeft -= Move.getMove(buyAtkNames.get(i)).getCost();
        }

        //If negative point left - throw exception
        if (pointLeft < 0){
            throw new NotEnoughPointsException(this, "The point given is not sufficent to buy all the attack listed!");
        }

        //Distribute points
        int chosenPts[] = Utility.intSubset(pointLeft + 4, 4);

        //Then the 1's partitions the totalPoints 0's into five parts
        //The # of 0's are the quantities
        int hp_l, atk, def, spd, mor;
        hp_l = chosenPts[0];
        atk = chosenPts[1] - chosenPts[0] - 1;
        def = chosenPts[2] - chosenPts[1] - 1;
        spd = chosenPts[3] - chosenPts[2] - 1;
        mor = pointLeft + 4 - chosenPts[3] - 1;

        //set things
        setName(name);
        setTeamName(teamName);
        giveHpPoint(hp_l);
        giveAtkPoint(atk);
        giveDefPoint(def);
        giveSpdPoint(spd);
        giveMorPoint(mor);
        for (int i = 0; i < buyAtkNames.size(); ++i) {
            buyAtk(buyAtkNames.get(i));
        }
        moves = new ArrayList<String>(buyAtkNames);

    }

    /**
     * Generate a player
     * @param name Name of the player
     * @param teamName Team name of the player. Always note that players with same team name are considered the same team
     * @param point Point allocated to this player
     * @param maxAtkBuy Maximum number of attacks to buy
     * @param noMoveProb Probability of this player getting no moves
     * @param chooseFromMove List of moves (names, specified using their simple class name) that the player can choose from
     * @throws InvalidMoveNameException Thrown when move names specifed in chooseFromMove does not exist.
     * @throws IllegalArgumentException if the moves that can choose from contains not buyable moves or moves that the given point is not
     * sufficient to buy.
     */
    public AnalysisPlayer(String name, String teamName, int point, int maxAtkBuy, double noMoveProb, List<String> chooseFromMove) throws InvalidMoveNameException, NotEnoughPointsException, UnbuyableMoveException{
        super();

        int hp_l = 0, atk = 0, def = 0, spd = 0, mor = 0;

        int pointLeft = point;

        //check the given chooseFromMove List
        for (String s : chooseFromMove){
            Move m = Move.getMove(s);
            if (!m.isBuyable()) throw new IllegalArgumentException("The list of moves that can choose from contains unbuyable moves!");
            if (m.getCost() > point) throw new IllegalArgumentException("The list of moves that can choose from contains moves that costs more than the point (" + point + ") allocated");
        }

        do {
            //Buying moves just fail, reset pointLeft
            pointLeft = point;

            //number of attacks to buy
            int noAtkBuy;
            if (Utility.probTest(noMoveProb) || maxAtkBuy == 0) {
                noAtkBuy = 0;
            } else {
                noAtkBuy = Utility.randBetween(1, maxAtkBuy);
            }

            //buy the moves
            moves = Utility.subset(chooseFromMove, noAtkBuy);
            for (String s: moves){
                Move m = Move.getMove(s);
                pointLeft -= m.getCost();
                buyAtk(m);
            }
        } while (pointLeft < 0);

        //Distribute points
        int chosenPts[] = Utility.intSubset(pointLeft + 4, 4);

        //Then the 1's partitions the totalPoints 0's into five parts
        //The # of 0's are the quantities
        hp_l = chosenPts[0];
        atk = chosenPts[1] - chosenPts[0] - 1;
        def = chosenPts[2] - chosenPts[1] - 1;
        spd = chosenPts[3] - chosenPts[2] - 1;
        mor = pointLeft + 4 - chosenPts[3] - 1;

        //set things
        setName(name);
        setTeamName(teamName);
        giveHpPoint(hp_l);
        giveAtkPoint(atk);
        giveDefPoint(def);
        giveSpdPoint(spd);
        giveMorPoint(mor);
        
    }

    /**
     * Generate a player
     * @param name Name of the player
     * @param teamName Team name of the player. Always note that players with same team name are considered the same team
     * @param loPt Lower bound of point allocated to this player
     * @param hiPt Upper bound of point allocated to this player
     * @param maxAtkBuy Maximum number of attacks to buy
     * @param noMoveProb Probability of this player getting no moves
     * @param chooseFromMove List of moves (names, specified using their simple class name) that the player can choose from
     * @throws BattleSysException Thrown when move names specifed in chooseFromMove does not exist.
     */
    public AnalysisPlayer(String name, String teamName, int loPt, int hiPt, int maxAtkBuy, double noMoveProb, List<String> chooseFromMove) throws BattleSysException{
        this(name, teamName, Utility.randBetween(loPt, hiPt), maxAtkBuy, noMoveProb, chooseFromMove);
    }

    /**
     * Generate a player
     * @param name Name of the player
     * @param teamName Team name of the player. Always note that players with same team name are considered the same team
     * @param point Point allocated to this player
     * @param maxAtkBuy Maximum number of attacks to buy
     * @param noMoveProb Probability of this player getting no moves
     */
    public AnalysisPlayer(String name, String teamName, int point, int maxAtkBuy, double noMoveProb) throws BattleSysException{
        this(name, teamName, point, maxAtkBuy, noMoveProb, Move.getNamesOfAllMoves());
    }

    /**
     * Primitive version of constructor
     * @param name Name of player
     * @param teamName Name of team of player
     * @param hp
     * @param atk
     * @param def
     * @param spd
     * @param mor
     * @param moveNames List of move names (simple class name) to use
     * @throws InvalidMoveNameException Thrown when move names specifed in chooseFromMove does not exist.
     */
    public AnalysisPlayer(String name, String teamName, int hp, int atk, int def, int spd, int mor, List<String> moveNames) throws InvalidMoveNameException, NotEnoughPointsException, UnbuyableMoveException {
        super();
        setName(name);
        setTeamName(teamName);
        giveHpPoint(hp);
        giveAtkPoint(atk);
        giveDefPoint(def);
        giveSpdPoint(spd);
        giveMorPoint(mor);
        for (String m : moveNames) {
            buyAtk(m);
        }
        moves = new ArrayList<String>(moveNames);
    }

    @Override
    protected CompleteMoveResult move(PlayerList thisPlayers, PlayerList opposingPlayers, int round, int battleNo, RandomEventResult reResult, List<SingleMoveResult> mResult) throws BattleSysException {

        Player target = opposingPlayers.randomPick();
        Player thisTarget = thisPlayers.randomPick();

        for (int i = 0; i < moves.size(); ++i) {
            //skip basic moves
            if (moves.get(i).equals("BasicAtk") || moves.get(i).equals("AimedAtk") || moves.get(i).equals("StrongAtk")) continue;
            //see if move time is enough
            if (getMoveTime(moves.get(i)) > 0) {
                if (moves.get(i).equals("Recover")) {
                    //recover hp - use only when target has status inflicted or little hp
                    if ((thisTarget.statusInflicted() || thisTarget.getHp() < Utility.randBetween(500, 2000))) {
                        return useMove("Recover", thisTarget, thisPlayers, target, opposingPlayers);
                    }
                } else if (moves.get(i).equals("AllRecover")) {
                    //recover hp - use only when someone status inflicted or little hp
                    if ((thisPlayers.statusInflicted() || thisPlayers.getPlayerOfMinQuality("HP", true, true).getHp() < Utility.randBetween(500, 2000))) {
                        return useMove("AllRecover", thisTarget, thisPlayers, target, opposingPlayers);
                    }
                } else if (moves.get(i).equals("AntiHpAtk")) {
                    //use anti-hp atk on little hp only
                    if ((getHp() < Utility.randBetween(500, 3000))) {
                        return useMove("AntiHpAtk", thisTarget, thisPlayers, target, opposingPlayers);
                    }
                } else if (moves.get(i).equals("SelfDestruct")) {
                    //use self-destruct on very little hp only
                    if ((getHp() < Utility.randBetween(500, 2000))) {
                        return useMove("SelfDestruct", thisTarget, thisPlayers, target, opposingPlayers);
                    }
                } else if (moves.get(i).equals("Revive")) {
                    //use revive on someone is dead
                    //Note: assignment within condition statement warning!
                    if (((thisTarget = thisPlayers.getPlayerOfMinQuality("HP", false, true)).getHp() <= 0)) {
                        return useMove("Revive", thisTarget, thisPlayers, target, opposingPlayers);
                    }
                } else if (moves.get(i).equals("Revive2")) {
                    //use revive on someone is dead
                    //Note: assignment within condition statement warning!
                    if (((thisTarget = thisPlayers.getPlayerOfMinQuality("HP", false, true)).getHp() <= 0)) {
                        return useMove("Revive2", thisTarget, thisPlayers, target, opposingPlayers);
                    }
                } else if (moves.get(i).equals("Revive3")) {
                    //use revive if anyone is dead
                    //Note: assignment within condition statement warning!
                    if (((thisTarget = thisPlayers.getPlayerOfMinQuality("HP", false, true)).getHp() <= 0)) {
                        return useMove("Revive3", thisTarget, thisPlayers, target, opposingPlayers);
                    }
                } else {//other move codes

                    //Don't use status-inflicting attacks if opponent already have the status inflicted!
                    if (moves.get(i).equals("ParalysisAtk")){
                        if (!target.statusInflicted(Player.statusId.PARALYSIS.getId())){
                            return useMove(moves.get(i), thisTarget, thisPlayers, target, opposingPlayers);
                        }
                    }
                    if (moves.get(i).equals("PinAtk")){
                        if (!target.statusInflicted(Player.statusId.PIN.getId())){
                            return useMove(moves.get(i), thisTarget, thisPlayers, target, opposingPlayers);
                        }
                    }
                    if (moves.get(i).equals("PoisonAtk")){
                        if (!target.statusInflicted(Player.statusId.POISON.getId())){
                            return useMove(moves.get(i), thisTarget, thisPlayers, target, opposingPlayers);
                        }
                    }
                    if (moves.get(i).equals("DizzyAtk")){
                        if (!target.statusInflicted(Player.statusId.DIZZY.getId())){
                            return useMove(moves.get(i), thisTarget, thisPlayers, target, opposingPlayers);
                        }
                    }
                    if (moves.get(i).equals("DisableAtk")){
                        if (!target.statusInflicted(Player.statusId.DISABLE.getId())){
                            return useMove(moves.get(i), thisTarget, thisPlayers, target, opposingPlayers);
                        }
                    }
                    if (moves.get(i).equals("AllParalysisAtk")){
                        if (!opposingPlayers.statusInflicted(Player.statusId.PARALYSIS.getId())){
                            return useMove(moves.get(i), thisTarget, thisPlayers, target, opposingPlayers);
                        }
                    }
                    if (moves.get(i).equals("AllPinAtk")){
                        if (!opposingPlayers.statusInflicted(Player.statusId.PIN.getId())){
                            return useMove(moves.get(i), thisTarget, thisPlayers, target, opposingPlayers);
                        }
                    }
                    if (moves.get(i).equals("AllPoisonAtk")){
                        if (!opposingPlayers.statusInflicted(Player.statusId.POISON.getId())){
                            return useMove(moves.get(i), thisTarget, thisPlayers, target, opposingPlayers);
                        }
                    }
                    if (moves.get(i).equals("AllDizzyAtk")){
                        if (!opposingPlayers.statusInflicted(Player.statusId.DIZZY.getId())){
                            return useMove(moves.get(i), thisTarget, thisPlayers, target, opposingPlayers);
                        }
                    }
                    if (moves.get(i).equals("AllDisableAtk")){
                        if (!opposingPlayers.statusInflicted(Player.statusId.DISABLE.getId())){
                            return useMove(moves.get(i), thisTarget, thisPlayers, target, opposingPlayers);
                        }
                    }
                    //Other status infliction need to be prevented
                    if (moves.get(i).equals("FreezeAtk") || moves.get(i).equals("FreezeAtk2")) {
                        if (!opposingPlayers.statusInflicted(Player.statusId.FREEZE.getId())) {
                            return useMove(moves.get(i), thisTarget, thisPlayers, target, opposingPlayers);
                        }
                    }
                    if (moves.get(i).equals("FreezeAtk3")) {
                        if (!opposingPlayers.statusInflicted(Player.statusId.FREEZE_3.getId())) {
                            return useMove(moves.get(i), thisTarget, thisPlayers, target, opposingPlayers);
                        }
                    }
                    if (moves.get(i).equals("Shield")) {
                        if (!statusInflicted(Player.statusId.SHIELD.getId())) {
                            return useMove(moves.get(i), thisTarget, thisPlayers, target, opposingPlayers);
                        }
                    }
                    if (moves.get(i).equals("Shield2")) {
                        if (!statusInflicted(Player.statusId.SHIELD_2.getId())) {
                            return useMove(moves.get(i), thisTarget, thisPlayers, target, opposingPlayers);
                        }
                    }
                    if (moves.get(i).equals("Weakening")) {
                        if (!target.statusInflicted(Player.statusId.WEAKENING.getId())) {
                            return useMove(moves.get(i), thisTarget, thisPlayers, target, opposingPlayers);
                        }
                    }
                    if (moves.get(i).equals("Weakening2") || moves.get(i).equals("Weakening3")) {
                        if (!target.statusInflicted(Player.statusId.WEAKENING_2.getId())) {
                            return useMove(moves.get(i), thisTarget, thisPlayers, target, opposingPlayers);
                        }
                    }
                    if (moves.get(i).equals("StartReflect")) {
                        if (!statusInflicted(Player.statusId.REFLECT.getId())) {
                            return useMove(moves.get(i), thisTarget, thisPlayers, target, opposingPlayers);
                        }
                    }
                    if (moves.get(i).equals("StartAbsorb")) {
                        if (!target.statusInflicted(Player.statusId.ABSORBING.getId())) {
                            return useMove(moves.get(i), thisTarget, thisPlayers, target, opposingPlayers);
                        }
                    }
                    if (moves.get(i).equals("DestroyTogether")) {
                        if (!statusInflicted(Player.statusId.DESTROY_LINK.getId())) {
                            return useMove(moves.get(i), thisTarget, thisPlayers, target, opposingPlayers);
                        }
                    }
                    if (moves.get(i).equals("StartProtect")) {
                        if (!thisTarget.statusInflicted(Player.statusId.PROTECT.getId())) {
                            return useMove(moves.get(i), thisTarget, thisPlayers, target, opposingPlayers);
                        }
                    }
                    //don't use STATUS_TRANSFER if oneself has no status inflicted
                    if (moves.get(i).equals("StatusTransfer")) {
                        if (statusInflicted()) {
                            return useMove(moves.get(i), thisTarget, thisPlayers, target, opposingPlayers);
                        }
                    }
                    //don't use HP_SPLIT if the other has less HP than user
                    if (moves.get(i).equals("HpSplit")) {
                        if (target.getHp() > this.getHp() - Utility.randBetween(1, 1000)) {
                            return useMove(moves.get(i), thisTarget, thisPlayers, target, opposingPlayers);
                        }
                    }
                    /*//don't use RECOVER_MP in case the target has no bought atks
                    if (moves.get(i) == Player.moveCode.RECOVER_MP.getCode()) {
                        if (thisTarget.getBoughtAtk(false).length == 0) {
                            //Use basic moves
                            return useMove(Utility.randBetween(0, 2), thisTarget, thisPlayers, target, opposingPlayers, 0)[0];
                        } else {
                            return useMove(moves.get(i), thisTarget, thisPlayers, target, opposingPlayers, Utility.randomPick(thisTarget.getBoughtAtk(false)))[0];
                        }
                    }
                    //same goes to DEC_MP
                    if (moves.get(i) == Player.moveCode.DEC_MP.getCode()) {
                        if (target.getBoughtAtk(false).length == 0) {
                            //Use basic moves
                            return useMove(Utility.randBetween(0, 2), thisTarget, thisPlayers, target, opposingPlayers, 0)[0];
                        } else {
                            return useMove(moves.get(i), thisTarget, thisPlayers, target, opposingPlayers, Utility.randomPick(target.getBoughtAtk(false)))[0];
                        }
                    }*/
                    //use this move
                    return useMove(moves.get(i), thisTarget, thisPlayers, target, opposingPlayers);
                }

            } // get move name
        } //for move loop

        //no special move used - use basic moves randomly(?)
        switch (Utility.randBetween(0, 2)) {
            case 0: {
                return useMove("BasicAtk", thisTarget, thisPlayers, target, opposingPlayers);
            }
            case 1: {
                return useMove("AimedAtk", thisTarget, thisPlayers, target, opposingPlayers);
            }
            case 2: {
                return useMove("StrongAtk", thisTarget, thisPlayers, target, opposingPlayers);
            }
            default:{
                return useMove("BasicAtk", thisTarget, thisPlayers, target, opposingPlayers);
            }
        }

    }
}
