package battlesys.io;

import battlesys.Move;
import java.util.*;
import java.io.*;

/**
 * Retrieve basic settings of all moves
 * @author Peter
 */
public class moveSettingOutput {
    
    public static void main(String[] args) throws IOException{
        List<Move> allMoves = Move.getAllMoves();
        BufferedWriter bw = new BufferedWriter(new FileWriter("moveSettings.csv"));
        bw.write("//內部名稱,名稱,說明,點數,可用次數,基本,可買,可用");
        bw.newLine();
        for (Move m : allMoves){
            bw.write(m.getClass().getSimpleName() + "," + m.getName() + "," + m.getDescription() + "," +
                    m.getCost() + "," + m.getInitialMoveTime() + "," + m.isBasic() + "," + m.isBuyable() + "," + m.isMovePublic());
            bw.newLine();
        }
        bw.close();
    }
    
}
