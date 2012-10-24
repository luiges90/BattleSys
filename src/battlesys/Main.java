package battlesys;

import battlesys.exception.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.InvocationTargetException;

/**
 * The main function for BattleSys - Nth battle.
 *
 * TODO create description (sth like pseudocode) from the ctrl files? -- In UI-ize part
 * 
 * TODO input for attack mode : probably by specifying multiple if-s (maybe out of core...)
 *
 * TODO GA/SA npc generation algorithm
 * 
 * TODO Multithreading(!) tournament progress
 * 
 * TODO status out of core
 *
 * @author Peter
 */
public class Main {

    /**
     *
     */
    public static int gametime = 23;
    /**
     * Whether this is the heat or the final
     */
    public static String hf = "h";

    private Main() {
    }

    /**
     * @param args the command line arguments
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws BattleSysException
     */
    public static void main(String[] args) throws
            IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, BattleSysException, IOException {

        //whether player data should be shown in the rankings
        final boolean RANKING_SHOW_DATA = true;

        final int MAX_PLAYER_IN_GROUP = 99999;
        final int REPLAY_TIME = 1;
        final boolean MAKE_IMAGE = false;

        boolean writeFiles = true;

        //Force file writing (which is slow!). For debugging purposes.
        final boolean FORCE_WRITE_FILE = false;

        final String ctrlPath = "src/battlesys/playerCtrl" + gametime;

        int i;

        Move.loadMoves();

        //Get player class files
        File playerClassFiles = new File(ctrlPath);
        String[] playerClassFileLists = playerClassFiles.list();
        int num_players = playerClassFileLists.length;

        PlayerList tp = new PlayerList(num_players);

        //Import all players into one array

        for (i = 0; i < num_players; ++i) {
            //Ignore files that are not java files
            if (!playerClassFileLists[i].substring(playerClassFileLists[i].length() - 5).equalsIgnoreCase(".java")) {
                continue;
            }

            //Import the class
            try {
                String className = "battlesys.playerCtrl" + gametime + "." + playerClassFileLists[i].substring(0, playerClassFileLists[i].length() - 5);
                tp.add((Player) Class.forName(className).getConstructor(new Class[0]).newInstance());
            } catch (Exception ex) {
                throw new BadPlayerFileException(playerClassFileLists[i].substring(0, playerClassFileLists[i].length() - 5), ex);
            }
        }
        List<PlayerList> p = Tournament.prepare(tp);
        //Fight each other.

        writeFiles = writeFiles || FORCE_WRITE_FILE;
        Tournament.tournament(p, MAX_PLAYER_IN_GROUP, REPLAY_TIME, MAKE_IMAGE, RANKING_SHOW_DATA, writeFiles, null, 
                writeFiles?new BufferedWriter(new FileWriter("schedule.txt")):null,
                writeFiles?new BufferedWriter(new FileWriter("totalStat.txt")):null,
                writeFiles?new BufferedWriter(new FileWriter("moveScore.txt")):null,
                new File(""));

    }
}
