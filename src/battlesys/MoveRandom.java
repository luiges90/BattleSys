package battlesys;

import battlesys.exception.BattleSysException;
import java.io.IOException;
import java.util.*;

/**
 * Move that make other moves at random.
 * @author Peter
 */
public abstract class MoveRandom extends Move {

    /**
     * How many moves will this randomMove produce (or, number of attacks)
     */
    protected int times;

    /**
     * Constructor. All subclass should call this constructor. This class sets the default values of the fields.
     * @param owner The owner of the move
     */
    protected MoveRandom(Player owner) throws IOException{
        super(owner);
        times = 1;
    }

    @Override
    protected boolean checkUsable(Player user, Player opponent, PlayerList attackers, PlayerList defenders){
        return true;
    }

     /**
     * Use the move that use other moves randomly
     * @param user
     * @param target
     * @param attackers
     * @param defenders
     * @return list that contain the individual result of every move used
     * @throws BattleSysException
     */
    protected List<SingleMoveResult> useMove(Player user, Player defender, PlayerList attackers, PlayerList defenders) throws BattleSysException {
        Player attackerTarget = attackers.randomPick();
        Player defenderTarget = defenders.randomPick();

        if (basicAtkOnly() && !basic) {
            CompleteMoveResult r = owner.forcedBasicAtk(defender, attackers, defenders);
            return Collections.singletonList(new SingleMoveResult(r.get(0), "但失敗了。\n" + r.getResultString()));
        }

        Player oldOwner = owner;

        //store whether the user (new owner) has already moved
        boolean userMoved = user.moved;

        List<SingleMoveResult> r = new ArrayList<SingleMoveResult>();
        for (int i = 0; i < times; ++i) {
            //set the moved flag such that the player can use the next move
            user.moved = false;

            Move pickedMove = Utility.randomPick(Move.getAllRandomMoves());

            //store whether this player do really bought this attack or not
            boolean originallyHaveMove = user.isAtkBought(pickedMove);

            pickedMove.owner = user;
            if (!originallyHaveMove) {
                //the player has not bought this move
                user.boughtAtk.add(pickedMove);
            } else {
                //don't make it lose the usage - increase it
                pickedMove.moveTime++;
            }

            r.addAll(user.useMove(pickedMove, attackerTarget, attackers, defenderTarget, defenders));

            //revert the bought status
            if (!originallyHaveMove){
                user.boughtAtk.remove(pickedMove);
            }

            //revert the moved flag such that the player can use the next move
            user.moved = false;

        }

        //reset whether the user has been moved
        user.moved = userMoved;

        //reset owner
        owner = oldOwner;

        //Now, the real owner of this move has moved.
        owner.moved = true;

        return Collections.unmodifiableList(r);
    }

}
