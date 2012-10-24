package battlesys;

/**
 * Class storing the result of a single battle
 * @author Peter
 */
public final class SingleBattleResult {

    private final BattleRecord battleRecord;
    private final int round, winner;

    private final String resultString;

    /**
     *
     * @param round Number of rounds the battle has taken
     * @param winner Winner of the battle. 1 Stands for side 1 (team 1), 2 Stands for side 2, 0 stands for draw
     * @param br The complete record for the course of the battle
     * @throws IllegalArgumentException when Param winner is not between 0 and 2
     */
    SingleBattleResult(int round, int winner, BattleRecord br, String s){
        if (winner < 0 || winner > 2) throw new IllegalArgumentException("Param winner must between 0 and 2");
        this.battleRecord = new BattleRecord(br);
        this.round = round;
        this.winner = winner;
        this.resultString = s;
    }

    /**
     * @return the br
     */
    public BattleRecord getBattleRecord() {
        return new BattleRecord(battleRecord);
    }

    /**
     * @return the round
     */
    public int getRound() {
        return round;
    }

    /**
     * @return the winner
     */
    public int getWinner() {
        return winner;
    }

    public String getResultString(){
        return resultString;
    }

}
