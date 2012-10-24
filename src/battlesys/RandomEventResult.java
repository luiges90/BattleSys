package battlesys;

/**
 * Immutable class stores a result of the random event phase
 *
 * @author Peter
 */
public final class RandomEventResult {

    private final int affectQuality, effectRound;
    private final int effectValue;
    private final String affectPlayer;
    private final String resultString;

    /**
     * Constructor setting up the Random Event Result
     * @param affectPlayer Which player is affected. The team number of the player is returned.
     * @param affectQuality What is affected, as specified by Player.AFFECT_HP, Player.AFFECT_ATK etc...
     * @param effectValue The quantity of the effect, i.e. the value of change due to this event. 0 for all players
     * @param resultString The message to show for this event
     * @param effectRound How many rounds do the effect last
     */
    public RandomEventResult(String affectPlayer, int affectQuality, int effectValue, int effectRound, String resultString){
	this.affectPlayer = affectPlayer;
	this.affectQuality = affectQuality;
	this.effectValue = effectValue;
	this.effectRound = effectRound;
        this.resultString = resultString;
    }

    /**
     * Constructor setting up the Random Event Result from a given result, only with result string replaced.
     * @param affectPlayer Which player is affected. The team number of the player is returned.
     * @param affectQuality What is affected, as specified by Player.AFFECT_HP, Player.AFFECT_ATK etc...
     * @param effectValue The quantity of the effect, i.e. the value of change due to this event. 0 for all players
     * @param effectRound How many rounds do the effect last
     */
    public RandomEventResult(RandomEventResult r, String resultString){
	this.affectPlayer = r.affectPlayer;
	this.affectQuality = r.affectQuality;
	this.effectValue = r.effectValue;
	this.effectRound = r.effectRound;
        this.resultString = resultString;
    }

    /**
     * @return the affectPlayer, -1 if no random event happened
     */
    public String getAffectPlayer() {
	return affectPlayer;
    }

    /**
     * @return the affectQuality, -1 if no random event happened
     */
    public int getAffectQuality() {
	return affectQuality;
    }

    /**
     * @return the effectRound, -1 if no random event happened
     */
    public int getEffectRound() {
	return effectRound;
    }

    /**
     * @return the effectValue, -1 if no random event happened
     */
    public int getEffectValue() {
	return effectValue;
    }

    public String getResultString() {
        return resultString;
    }

}
