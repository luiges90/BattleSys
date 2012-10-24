package battlesys;

import battlesys.exception.BattleTooLongException;
import battlesys.io.BattleSysLogger;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import org.jibble.simpleftp.*;

/**
 * Recording the complete course of battle and draw, save and upload the line graph
 * @author Peter
 */
public final class BattleRecord {

    private final int[][] result;
    private final String gTitle;
    //Maximum HP Value for the whole battle
    private int maxValue = 0;
    private int totalRoundNo = 0;
    private final String filePath;
    private final String teamName;
    private final PlayerList p;
    private static final int MAX_ROUND = 1000;

    /**
     * Constructor. Also set data for round zero.
     * @param s String of the file path where the battle record graphs
     * @param p Array of players involved in this battle
     * @param title The title of the graph generated
     */
    public BattleRecord(String s, PlayerList p, String title) {
        result = new int[p.size()][MAX_ROUND];
        filePath = s;
        gTitle = title;

        this.p = p;
        teamName = p.get(0).getTeamName();

        for (int i = 0; i < p.size(); ++i) {
            result[i][0] = p.get(i).getHp();
            if (p.get(i).getHp() > maxValue) {
                maxValue = p.get(i).getHp();
            }
        }
    }

    public BattleRecord(BattleRecord r) {
        result = new int[r.result.length][];
        for (int i = 0; i < r.result.length; ++i) {
            result[i] = Arrays.copyOf(r.result[i], r.result[i].length);
        }
        gTitle = r.gTitle;
        maxValue = r.maxValue;
        totalRoundNo = r.totalRoundNo;
        filePath = r.filePath;
        teamName = r.teamName;
        p = r.p;
    }

    /**
     * Set the battle record for all other rounds.
     * @param round Round number of the game being run
     * @throws BattleTooLongException if the battle is too long.
     */
    public void setBattleRecord(int round) {
        if (round >= MAX_ROUND){
            throw new BattleTooLongException();
        }
        for (int i = 0; i < p.size(); ++i) {
            //result[i][round] = Math.max(p[i].getHP(),0);
            result[i][round] = p.get(i).getHp();
            if (p.get(i).getHp() > maxValue) {
                maxValue = p.get(i).getHp();
            }
            if (round > totalRoundNo) {
                totalRoundNo = round;
            }
        }
    }

    /**
     * @return the complete network path to the image (to be) generated
     */
    public String getFilePath(){
        return "http://hksan.net/randomsurname/battleSysImage/results/" + Main.gametime + Main.hf + "/" + filePath.replace("%", "%25") + ".png";
    }

    /**
     * Generate the broken line graph for this record, save the image, upload it, and add the corresponding
     * IMG line.
     * @return network path to the file uploaded
     */
    public String generateGraph(File baseFilePath) {
        final int IMAGE_WIDTH = 640;
        final int IMAGE_HEIGHT = 480;

        BufferedImage graph = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT,
                java.awt.image.BufferedImage.TYPE_INT_RGB);
        Graphics2D drawer = graph.createGraphics();

        //title
        Font titleFont = new Font("微軟正黑體", java.awt.Font.PLAIN, 16);
        drawer.setFont(titleFont);
        String title = gTitle;
        drawer.drawString(title, (int) (IMAGE_WIDTH / 2 - titleFont.getStringBounds(title, drawer.getFontRenderContext()).getCenterX()), 30);

        //axis
        Font axisFont = new Font("微軟正黑體", java.awt.Font.PLAIN, 10);
        drawer.setFont(axisFont);
        //y-axis
        drawer.drawLine(30, 60, 30, 450);
        for (int y = 450, i = 0; y >= 60; y -= 39, i += maxValue / 10) {
            drawer.drawLine(30, y, 25, y);
            drawer.drawString(Integer.toString(i), 0, y + 5);
        }
        //x-axis
        drawer.drawLine(30, 450, 600, 450);
        int lastMark = -100; //Marking position of last mark, the first mark is always drawn
        for (int x = 30, i = 0; i <= totalRoundNo; i++, x += (600 - 30) / totalRoundNo) {
            //If the position is too close to the last mark, don't draw this mark - unless it is the last mark
            if (x - lastMark < 30 && i != totalRoundNo) {
                continue;
            }
            drawer.drawLine(x, 450, x, 455);
            drawer.drawString(Integer.toString(i), x - 5, 470);
            lastMark = x;
        }

        //broken lines for each players
        for (int k = 0; k < result.length; ++k) {
            if (p.get(k).getTeamName().equals(teamName)) {
                drawer.setColor(java.awt.Color.CYAN);
            } else {
                drawer.setColor(java.awt.Color.MAGENTA);
            }
            int lx = -100, ly = 0, x, y;
            for (int i = 0; i <= totalRoundNo; ++i) {
                x = 30 + (600 - 30) / totalRoundNo * i;
                //If the position is too close to the last mark, don't draw this mark - unless it is the last mark
                if (x - lx < 30 && i != totalRoundNo) {
                    continue;
                }
                y = (int) (450 - (450 - 60.0) / maxValue * result[k][i]);
                drawer.drawLine(x - 4, y - 4, x + 4, y + 4);
                drawer.drawLine(x - 4, y + 4, x + 4, y - 4);
                if (i != 0) {
                    drawer.drawLine(lx, ly, x, y);
                }
                lx = x;
                ly = y;
            }
            drawer.drawString(p.get(k).getName(), lx + 5, ly - 5);
        }

        //save image onto disk
        try {
            ImageIO.write(graph, "png", new File(baseFilePath, filePath + ".png"));
        } catch (IOException ex) {
            System.err.println("Error writing file: " + filePath + ".png: " + ex.getMessage());
        }
        
        //read ftp username and password if not yet done.
        if (ftpUser == null || ftpPass == null){
            try {
                BufferedReader r = new BufferedReader(new FileReader("ftp.txt"));
                ftpUser = r.readLine();
                ftpPass = r.readLine();
                r.close();
            } catch (IOException ex) {
                BattleSysLogger.getLogger().log(Level.WARNING, "FTP failed - cannot read necessary information from ftp.txt", ex);
            }
        }
        
        //save image onto FTP server
        try {
            SimpleFTP ftp = new SimpleFTP();

            ftp.connect("hksan.net", 21, ftpUser, ftpPass);

            ftp.bin();

            ftp.cwd("battleSysImage/results/" + Main.gametime + Main.hf);

            ftp.stor(new File(filePath + ".png"));

            ftp.disconnect();
        } catch (IOException ex) {
            System.err.println("Error uploading file: " + filePath + ".png: " + ex.getMessage());
        }

        return getFilePath();
    }
    private static String ftpUser, ftpPass;
}
