package battlesys;

import java.util.List;

/**
 * Records results of a move.
 * @author Peter
 */
public final class SingleMoveResult {

    private final int damage;
    private final boolean hit, critical, successful;

    private final Player attacker, defender;

    private final String resultString;

    /**
     * Complete Constructor, for moves that has successfully used
     * @param damage The damage dealt by this specific attack
     * @param hit Whether this attack is hit
     * @param critical Whether this attack become critical
     * @param attacker Object referece to the attacker for this attack
     * @param defender Object referece to the defender for this attack
     * @param resultString Result to be shown
     */
    SingleMoveResult(int damage, boolean hit, boolean critical, Player attacker, Player defender, String resultString){
        this.successful = true;
	this.damage = damage;
	this.hit = hit;
	this.critical = critical;
        this.attacker = attacker;
        this.defender = defender;
        this.resultString = resultString;
    }

    /**
     * Constructor, for copying the original data with replaced resultString
     * @param r the original data
     * @param resultString Result to be shown
     */
    SingleMoveResult(SingleMoveResult r, String resultString){
        this.successful = r.successful;
	this.damage = r.damage;
	this.hit = r.hit;
	this.critical = r.critical;
        this.attacker = r.attacker;
        this.defender = r.defender;
        this.resultString = resultString;
    }

    /**
     * Constructor for a result of a failed move. Damage is set to 0, hit and critical is set to false, and result string will be empty.
     * @param attacker Object referece to the attacker for this attack
     * @param defender Object referece to the defender for this attack
     */
    SingleMoveResult(Player attacker, Player defender) {
        this.successful = false;
        this.damage = 0;
        this.hit = false;
        this.critical = false;
        this.attacker = attacker;
        this.defender = defender;
        this.resultString = "";
    }

    /**
     * Constructor for a result of a failed move with an additional result string. Damage is set to 0, hit and critical is set to false, and result string will be empty.
     * @param attacker Object referece to the attacker for this attack
     * @param defender Object referece to the defender for this attack
     */
    SingleMoveResult(Player attacker, Player defender, String s) {
        this.successful = false;
        this.damage = 0;
        this.hit = false;
        this.critical = false;
        this.attacker = attacker;
        this.defender = defender;
        this.resultString = s;
    }

    @Override
    /**
     * Get the hash code of this MoveResult
     * @return
     */
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this.damage;
        hash = 59 * hash + (this.isSuccessful() ? 1 : 0);
        hash = 59 * hash + (this.hit ? 1 : 0);
        hash = 59 * hash + (this.critical ? 1 : 0);
        hash = 59 * hash + (this.attacker != null ? this.attacker.hashCode() : 0);
        hash = 59 * hash + (this.defender != null ? this.defender.hashCode() : 0);
        hash = 59 * hash + (this.resultString.hashCode());
        return hash;
    }

    /**
     * Whether 2 MoveResults are equal. Two moveresults are equal iff all fields are being equal.
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o){
        if (!(o instanceof SingleMoveResult)) return false;
        SingleMoveResult r = (SingleMoveResult) o;
        return damage == r.damage && hit == r.hit && critical == r.critical && attacker == r.attacker && defender == r.defender 
                && isSuccessful() == r.isSuccessful() && (resultString == null ? r.resultString == null : resultString.equals(r.resultString));
    }

    /**
     * Return string representation of this object. For debugging purposes only.
     * @return
     */
    @Override
    public String toString(){
        String s = "Move used by " + attacker.toString() + " against " + defender.toString() + ". ";
        if (successful){
            s += "Move used successfully, ";
            if (hit){
                s += "hit the opponent and dealt " + damage + " damage. ";
                s += "The move is " + (critical?"":"not") + " critical. ";
            } else {
                s += "but does not hit";
            }
        } else {
            s += "Move failed to use. ";
        }
        return s;
    }

    /**
     * Get the damage dealt by the move
     * @return the damage
     */
    public int getDamage() {
	return damage;
    }

    /**
     * Get whether the last attack is hit
     * @return the hit
     */
    public boolean isHit() {
	return hit;
    }

    /**
     * Get whether the last attack is critical
     * @return the critical
     */
    public boolean isCritical() {
	return critical;
    }

    /**
     * Get the reference to attacker of the move.
     * Care must be taken that the information of attacker can change.
     * @return the attacker
     */
    public Player getAttacker() {
        return attacker;
    }

    /**
     * Get the reference to defender of the move.
     * Care must be taken that the information of defender can change.
     * @return the defender
     */
    public Player getDefender() {
        return defender;
    }

    /**
     * @return the successful
     */
    public boolean isSuccessful() {
        return successful;
    }

    public String getResultString(){
        return resultString;
    }

    /**
     * Combine all result strings into one string from given list of move results
     * @param r
     * @return
     */
    public final static String combineMoveResultString(List<SingleMoveResult> m){
        StringBuilder s = new StringBuilder();
        for (SingleMoveResult r : m){
            s.append(r.resultString);
            s.append('\n');
        }
        return s.toString();
    }

}
