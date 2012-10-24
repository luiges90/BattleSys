package battlesys.analysis;

import battlesys.exception.BattleSysException;
import battlesys.*;
import java.io.*;
import java.util.*;

/**
 * Main function to balance the moves.
 *
 * Algorithm:
 * 1. Generate number of npcs using move
 * 2. Have them fight each other
 * 3. the number of best npcs are then grouped together and fight each other. MoveScore of this battle is recorded and sent to analysis
 *
 * @author Peter
 */
public class moveBalanceTest {

    /**
     *
     * @param args
     * @throws BattleSysException
     */
    public static void main(String[] args) throws BattleSysException, IOException {

        //Number of npc TEAMs using a certain group of moves
        final int NUMBER_USING_MOVE = 50;

        //points to allocate
        final int POINT_ALLOCATE = Player.loadTournamentData();

        //How many times to replay for same-move battle
        final int REPLAY_TIME_MOVE = 1;

        //How many times to replay for final
        final int REPLAY_TIME_FINAL = 1;

        //First k npc TEAMs from certain group of moves is taken to final
        final int TAKE_FROM_MOVE = 5;

        List<PlayerList> finalPlayer = new ArrayList<PlayerList>();
        
        List<Move> m = Move.getAllPublicMoves();
        
        BufferedWriter bw = new BufferedWriter(new FileWriter("moveTimeTaken.txt"));

        //for each pair of moves
        for (int i = 0; i < m.size(); ++i) {

            System.err.println("~~~~~~~~Move " + i + "(" + m.get(i).getName() + ") battle started.~~~~~~~~");

            //obtain players
            PlayerList p = new PlayerList(NUMBER_USING_MOVE * 2);
            for (int k = 0; k < NUMBER_USING_MOVE * 2; k += 2) {
                p.add(new AnalysisPlayer("npc" + i + "-" + k, "npc" + i + "-" + k, POINT_ALLOCATE, Collections.singletonList(m.get(i).getClass().getSimpleName())));
                p.add(new AnalysisPlayer("npc" + i + "-" + k, "npc" + i + "-" + k, POINT_ALLOCATE, Collections.singletonList(m.get(i).getClass().getSimpleName())));
            }

            //time how long each move battle takes
            Long moveTimeStart = System.currentTimeMillis();
            
            //have them battle each other
            PlayerBattleRecord[] pbr = Tournament.tournament(Tournament.prepare(p), Integer.MAX_VALUE, REPLAY_TIME_MOVE, false, false, false, null, null, null, null, null);
            
            //Write time result to file
            bw.write("Move " + i + "(" + m.get(i).getName() + ") takes " + Long.toString(System.currentTimeMillis() - moveTimeStart) + "ms \n");
            
            System.err.println(
                    "~~~~~~~~Move " + (i+1) + " battle completed, out of " + m.size() + ". (Progress " + (i+1) / (double) (m.size()) * 100 + "%)~~~~~~~~");
            //put the best ones into finalPlayer
            for (int k = pbr.length - 1; k >= pbr.length - TAKE_FROM_MOVE; --k) {
                finalPlayer.add(pbr[k].getPlayers());
            }

        }

        /*for (int i = 0; i < finalPlayer.length; ++i) {
            System.err.println("Final player " + i + ":");
            for (int j = 0; j < finalPlayer[i].length; ++j) {
                int[] atkCodes = finalPlayer[i][j].getBoughtAtkCodes(false);
                if (atkCodes.length == 0) {
                    System.err.println("No attack bought");
                } else {
                    System.err.print("Attack bought: ");
                    for (int k = 0; k < atkCodes.length; ++k) {
                        System.err.print(atkCodes[k] + ", ");
                    }
                    System.err.println();
                }
            }
        }*/

        System.err.println("~~~~~~~~Heat completed, go to final.~~~~~~~~");
        
        Long moveTimeStart = System.currentTimeMillis();
        
        Tournament.tournament(finalPlayer, Integer.MAX_VALUE, REPLAY_TIME_FINAL, false, false, false, null,
                null, null, new BufferedWriter(new FileWriter("moveScore.txt")), new File(""));
        
        bw.write("final takes " + Long.toString(System.currentTimeMillis() - moveTimeStart) + "ms \n");
        
        bw.close();

    }

    private moveBalanceTest() {
    }
}
