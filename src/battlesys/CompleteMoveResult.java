package battlesys;

import java.util.ArrayList;
import java.util.List;

/**
 * A complete move result, consists of list of individual move results and a result string.
 * @author Peter
 */
public final class CompleteMoveResult extends ArrayList<SingleMoveResult>{

    private final String resultString;

    @SuppressWarnings("LeakingThisInConstructor")
    public CompleteMoveResult(List<SingleMoveResult> m){
        super(m);
        resultString = SingleMoveResult.combineMoveResultString(this);
    }

    public CompleteMoveResult(List<SingleMoveResult> m, String s){
        super(m);
        resultString = s;
    }

    public CompleteMoveResult(int c){
        super(c);
        resultString = "";
    }


    public String getResultString(){
        return resultString;
    }

}
