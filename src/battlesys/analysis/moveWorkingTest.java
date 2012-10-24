package battlesys.analysis;

import battlesys.exception.BattleSysException;
import battlesys.*;
import java.util.*;
import java.io.*;

/**
 * main function used to test whether each move is working and such npc is generated correctly
 * @author Peter
 */
public class moveWorkingTest {

    /**
     * Test function which tests if the moves are working
     * @param args
     * @throws BattleSysException
     */
    public static void main(String[] args) throws BattleSysException, IOException {

        final int MEMBER_IN_TEAMS = 2;

        PlayerList p = new PlayerList();
        List<String> moveNames = Move.getNamesOfAllMoves();

        for (int teamNo = 0, teamMemberNo = 1; teamNo < moveNames.size(); teamMemberNo++) {

            p.add(new AnalysisPlayer("npc" + teamNo + "-" + teamMemberNo, "npc" + teamNo, Player.TOTAL_POINTS, 1, 0, Collections.singletonList(moveNames.get(teamNo))));

            if (teamMemberNo > MEMBER_IN_TEAMS) {
                teamNo++;
                teamMemberNo = 1;
            }

        }

        Tournament.tournament(Tournament.prepare(p), Integer.MAX_VALUE, 1, false, false, false, null,
                null, null, new BufferedWriter(new FileWriter("moveScore.txt")), new File(""));

    }

    private moveWorkingTest() {
    }

}
