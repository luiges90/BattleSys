package battlesys;

/**
 * Records how a player plays the battles, that is, number of wins, loses, draws, etc...
 * @author Peter
 */
public class PlayerBattleRecord implements Comparable<PlayerBattleRecord>, Cloneable {

    private final long win, lose, draw, score;
    private final PlayerList p; //The player team it represents

    /**
     * Constructor.
     * @param team The Player team concerned
     */
    public PlayerBattleRecord(PlayerList team) {
        win = team.getWin();
        lose = team.getLose();
        draw = team.getDraw();
        score = team.getScore();
        this.p = team;
    }
    
    PlayerBattleRecord(long win, long lose, long draw, long score, PlayerList p){
        this.win = win;
        this.lose = lose;
        this.draw = draw;
        this.score = score;
        this.p = p;
    }

    public final int compareTo(PlayerBattleRecord o) {
        //Always treat null pointer as smallest - such that it always keep at the last element of the array
        if (o == null) {
            return -1;
        }
        PlayerBattleRecord s = o;
        if (getScore() != s.getScore()) {
            return (int) (getScore() - s.getScore());
        } else if (getWin() != s.getWin()) {
            return (int) (getWin() - s.getWin());
        } else {
            return 0;
        }
    }

    @Override
    /**
     * Returns the shallow copy of this object (The player team is not copied)
     */
    public Object clone(){
        PlayerBattleRecord newObj = new PlayerBattleRecord(win, lose, draw, score, p);
        //The player team may have data changed, overwrite them with original data
        return newObj;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof PlayerBattleRecord) && o != null) {
            return false;
        }
        return this.compareTo((PlayerBattleRecord) o) == 0;
    }

    @Override
    public final int hashCode() {
        return (int) ((win * 249 + score) % Integer.MAX_VALUE);
    }

    /**
     * @return the name
     */
    public String getName() {
        return p.get(0).getTeamName();
    }

    /**
     * @return the win
     */
    public long getWin() {
        return win;
    }

    /**
     * @return the lose
     */
    public long getLose() {
        return lose;
    }

    /**
     * @return the draw
     */
    public long getDraw() {
        return draw;
    }

    /**
     * @return the score
     */
    public long getScore() {
        return score;
    }

    /**
     * @return the score
     */
    public PlayerList getPlayers() {
        return p;
    }

}//class PlayerBattleRecord
