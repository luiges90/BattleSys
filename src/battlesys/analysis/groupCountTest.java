package battlesys.analysis;

import battlesys.exception.BattleSysException;
import battlesys.*;
import java.io.*;

/**
 * Main function to find the equivalence of 2 npcs of different point over 1 npcs (probably higher point)
 *
 * @author Peter
 */
public class groupCountTest {

    /**
     *
     * @param args
     * @throws BattleSysException
     */
    public static void main(String[] args) throws BattleSysException, IOException {

        //Number of npcs (groups) for each point allocation
        final int NUM_GROUPS = 10;

        //points to allocate to npc 1
        final int POINT_ALLOCATE_1 = 25;

        //points to allocate to npc 2
        final int POINT_ALLOCATE_2 = 5;

        //lower point, step of opposing group and number of point segments
        final int OPPOSING_POINT = 30;
        final int OPPOSING_POINT_STEP = 2;
        final int NUMBER_OPPOSING_POINT = 10;

        final int MAX_ATK_BUY = 2;
        final double NO_MOVE_PROB = 0.1;

        //How many times to replay
        final int REPLAY_TIME = 1;

        PlayerList p = new PlayerList((2 + NUMBER_OPPOSING_POINT) * NUM_GROUPS);

        for (int i = 0; i < NUM_GROUPS * 2; i+=2){
            p.add(new AnalysisPlayer("npcgroup-" + i, "npcgroup-" + i, POINT_ALLOCATE_1, MAX_ATK_BUY, NO_MOVE_PROB));
            p.add(new AnalysisPlayer("npcgroup-" + (i + 1), "npcgroup-" + i, POINT_ALLOCATE_2, MAX_ATK_BUY, NO_MOVE_PROB));
        }

        for (int i = NUM_GROUPS * 2; i < p.size(); ++i){
            p.set(i, new AnalysisPlayer("npc" + (OPPOSING_POINT + OPPOSING_POINT_STEP * ((i - NUM_GROUPS * 2) / NUM_GROUPS)) + "-" + i,
                    "npc" + (OPPOSING_POINT + OPPOSING_POINT_STEP * ((i - NUM_GROUPS * 2) / NUM_GROUPS)) + "-" + i
                    , OPPOSING_POINT + OPPOSING_POINT_STEP * ((i - NUM_GROUPS * 2) / NUM_GROUPS), MAX_ATK_BUY, NO_MOVE_PROB));
        }

        Tournament.tournament(Tournament.prepare(p), Integer.MAX_VALUE, REPLAY_TIME, false, false, false, null, 
                null, null, new BufferedWriter(new FileWriter("moveScore.txt")), new File(""));

    }

    private groupCountTest() {
    }

}
