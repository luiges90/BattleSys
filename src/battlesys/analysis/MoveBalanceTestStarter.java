
/*
 * MoveBalanceTestStarter.java
 * 
 * Do Move Balancing tests. Progress can be saved, assuming the moves remain unchanged between the runs.
 *
 * Created on 2011年4月4日, 下午11:08:56
 */
package battlesys.analysis;

import battlesys.*;
import battlesys.io.NullPrintStream;
import java.awt.AWTException;
import java.awt.Robot;
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 *
 * @author Peter
 */
public class MoveBalanceTestStarter extends javax.swing.JFrame {
    
    public static final int PLAYER_IN_GROUP = 2;

    private class SaveOuterData {

        int numberUsingMove;
        int pointAllocate;
        int takeFromMove;
        List<PlayerList> finalPlayer;
        int startI;

        private SaveOuterData(int numberUsingMove, int pointAllocate, int takeFromMove, int startI, List<PlayerList> finalPlayer) {
            this.numberUsingMove = numberUsingMove;
            this.pointAllocate = pointAllocate;
            this.takeFromMove = takeFromMove;
            this.finalPlayer = finalPlayer;
            this.startI = startI;
        }

        private SaveOuterData() throws Exception {
            BufferedReader br = new BufferedReader(new FileReader("mbts_o.sav"));

            numberUsingMove = Integer.parseInt(br.readLine());

            pointAllocate = Integer.parseInt(br.readLine());

            takeFromMove = Integer.parseInt(br.readLine());

            startI = Integer.parseInt(br.readLine());

            finalPlayer = new ArrayList<PlayerList>();

            int c = Integer.parseInt(br.readLine());
            for (int i = 0; i < c; ++i) {
                int c2 = Integer.parseInt(br.readLine());
                PlayerList l = new PlayerList();
                for (int j = 0; j < c2; ++j) {
                    String ptname = br.readLine();
                    String pname = br.readLine();
                    int php = Integer.parseInt(br.readLine());

                    int patk = Integer.parseInt(br.readLine());
                    int pdef = Integer.parseInt(br.readLine());
                    int pspd = Integer.parseInt(br.readLine());
                    int pmor = Integer.parseInt(br.readLine());

                    int mc = Integer.parseInt(br.readLine());
                    List<String> ms = new ArrayList<String>();
                    for (int k = 0; k < mc; ++k) {
                        String temp = br.readLine();
                        if (Move.getMove(temp).isBuyable() && !Move.getMove(temp).isBasic()) {
                            ms.add(temp);
                        }
                    }

                    l.add(new AnalysisPlayer(pname, ptname, php, patk, pdef, pspd, pmor, ms));

                }
                finalPlayer.add(l);
            }

            br.close();
        }

        private void save() throws IOException {
            BufferedWriter bw = new BufferedWriter(new FileWriter("mbts_o.sav"));

            bw.write(Integer.toString(numberUsingMove));
            bw.newLine();

            bw.write(Integer.toString(pointAllocate));
            bw.newLine();

            bw.write(Integer.toString(takeFromMove));
            bw.newLine();

            bw.write(Integer.toString(startI));
            bw.newLine();

            //not everything in player will be written, but only:
            //teamname, player name, abilites and move class names for every player
            bw.write(Integer.toString(finalPlayer.size()));
            bw.newLine();
            for (PlayerList pl : finalPlayer) {

                bw.write(Integer.toString(pl.size()));
                bw.newLine();

                for (Player l : pl) {
                    bw.write(l.getTeamName());
                    bw.newLine();
                    bw.write(l.getName());
                    bw.newLine();
                    bw.write(Integer.toString(Player.hpToPoint(l.getInitHp())));
                    bw.newLine();
                    bw.write(Integer.toString(l.getInitAtk()));
                    bw.newLine();
                    bw.write(Integer.toString(l.getInitDef()));
                    bw.newLine();
                    bw.write(Integer.toString(l.getInitSpd()));
                    bw.newLine();
                    bw.write(Integer.toString(l.getInitMor()));
                    bw.newLine();

                    bw.write(Integer.toString(l.getBoughtAtk().size()));
                    bw.newLine();
                    for (Move m : l.getBoughtAtk()) {
                        bw.write(m.getClass().getSimpleName());
                        bw.newLine();
                    }
                }
            }

            bw.close();
        }
    }

    private class SaveBattleData {

        String[] teamNames;
        int numberOfCompletedBattle;
        int totalBattles;
        long hpScore;
        long atkScore;
        long defScore;
        long spdScore;
        long morScore;
        long playerBattleTimeCnt;
        Map<String, Long> moveScore;
        Map<String, Long> moveCnt;
        int startI, startJ;
        List<PlayerList> p;

        private SaveBattleData(String[] teamNames, int numberOfCompletedBattle, int totalBattles, long hpScore, long atkScore, long defScore,
                long spdScore, long morScore, long playerBattleTimeCnt, List<String> allMoveStrings, Map<String, Long> moveScore,
                Map<String, Long> moveCnt, int i, int j, List<PlayerList> p) {
            this.teamNames = teamNames;
            this.numberOfCompletedBattle = numberOfCompletedBattle;
            this.totalBattles = totalBattles;
            this.hpScore = hpScore;
            this.atkScore = atkScore;
            this.defScore = defScore;
            this.spdScore = spdScore;
            this.morScore = morScore;
            this.playerBattleTimeCnt = playerBattleTimeCnt;
            this.moveScore = moveScore;
            this.moveCnt = moveCnt;
            this.startI = i;
            this.startJ = j;
            this.p = p;
        }

        private SaveBattleData() throws Exception {
            BufferedReader br = new BufferedReader(new FileReader("mbts_b.sav"));
            int c;

            c = Integer.parseInt(br.readLine());
            teamNames = new String[c];
            for (int i = 0; i < c; ++i) {
                teamNames[i] = br.readLine();
            }

            numberOfCompletedBattle = Integer.parseInt(br.readLine());

            totalBattles = Integer.parseInt(br.readLine());

            hpScore = Long.parseLong(br.readLine());
            atkScore = Long.parseLong(br.readLine());
            defScore = Long.parseLong(br.readLine());
            spdScore = Long.parseLong(br.readLine());
            morScore = Long.parseLong(br.readLine());

            playerBattleTimeCnt = Long.parseLong(br.readLine());

            c = Integer.parseInt(br.readLine());
            moveScore = new HashMap<String, Long>(c);
            for (int i = 0; i < c; ++i) {
                String ts = br.readLine();
                long tl = Long.parseLong(br.readLine());
                moveScore.put(ts, tl);
            }

            c = Integer.parseInt(br.readLine());
            moveCnt = new HashMap<String, Long>(c);
            for (int i = 0; i < c; ++i) {
                String ts = br.readLine();
                long tl = Long.parseLong(br.readLine());
                moveCnt.put(ts, tl);
            }

            startI = Integer.parseInt(br.readLine());
            startJ = Integer.parseInt(br.readLine());

            c = Integer.parseInt(br.readLine());
            p = new ArrayList<PlayerList>(c);
            for (int i = 0; i < c; ++i) {
                int c2 = Integer.parseInt(br.readLine());
                PlayerList l = new PlayerList();
                for (int j = 0; j < c2; ++j) {
                    String ptname = br.readLine();
                    String pname = br.readLine();
                    int php = Integer.parseInt(br.readLine());

                    int patk = Integer.parseInt(br.readLine());
                    int pdef = Integer.parseInt(br.readLine());
                    int pspd = Integer.parseInt(br.readLine());
                    int pmor = Integer.parseInt(br.readLine());

                    int mc = Integer.parseInt(br.readLine());
                    List<String> ms = new ArrayList<String>();
                    for (int k = 0; k < mc; ++k) {
                        String temp = br.readLine();
                        if (Move.getMove(temp).isBuyable() && !Move.getMove(temp).isBasic()) {
                            ms.add(temp);
                        }
                    }

                    l.add(new AnalysisPlayer(pname, ptname, php, patk, pdef, pspd, pmor, ms));

                }
                p.add(l);
            }

            br.close();

        }

        private void save() throws IOException {
            BufferedWriter bw = new BufferedWriter(new FileWriter("mbts_b.sav"));

            bw.write(Integer.toString(teamNames.length));
            bw.newLine();
            for (String s : teamNames) {
                bw.write(s);
                bw.newLine();
            }

            bw.write(Integer.toString(numberOfCompletedBattle));
            bw.newLine();

            bw.write(Integer.toString(totalBattles));
            bw.newLine();

            bw.write(Long.toString(hpScore));
            bw.newLine();
            bw.write(Long.toString(atkScore));
            bw.newLine();
            bw.write(Long.toString(defScore));
            bw.newLine();
            bw.write(Long.toString(spdScore));
            bw.newLine();
            bw.write(Long.toString(morScore));
            bw.newLine();
            bw.write(Long.toString(playerBattleTimeCnt));
            bw.newLine();

            bw.write(Integer.toString(moveScore.size()));
            bw.newLine();
            for (Map.Entry<String, Long> e : moveScore.entrySet()) {
                bw.write(e.getKey());
                bw.newLine();
                bw.write(Long.toString(e.getValue()));
                bw.newLine();
            }

            bw.write(Integer.toString(moveCnt.size()));
            bw.newLine();
            for (Map.Entry<String, Long> e : moveCnt.entrySet()) {
                bw.write(e.getKey());
                bw.newLine();
                bw.write(Long.toString(e.getValue()));
                bw.newLine();
            }

            bw.write(Integer.toString(startI));
            bw.newLine();
            bw.write(Integer.toString(startJ));
            bw.newLine();

            //not everything in player will be written, but only:
            //teamname, player name, abilites and move class names for every player
            bw.write(Integer.toString(p.size()));
            bw.newLine();
            for (PlayerList pl : p) {

                bw.write(Integer.toString(pl.size()));
                bw.newLine();

                for (Player l : pl) {
                    bw.write(l.getTeamName());
                    bw.newLine();
                    bw.write(l.getName());
                    bw.newLine();
                    bw.write(Integer.toString(Player.hpToPoint(l.getInitHp())));
                    bw.newLine();
                    bw.write(Integer.toString(l.getInitAtk()));
                    bw.newLine();
                    bw.write(Integer.toString(l.getInitDef()));
                    bw.newLine();
                    bw.write(Integer.toString(l.getInitSpd()));
                    bw.newLine();
                    bw.write(Integer.toString(l.getInitMor()));
                    bw.newLine();

                    bw.write(Integer.toString(l.getBoughtAtk().size()));
                    bw.newLine();
                    for (Move m : l.getBoughtAtk()) {
                        bw.write(m.getClass().getSimpleName());
                        bw.newLine();
                    }
                }
            }

            bw.close();
        }
    }
    private volatile boolean saveNow = false;

    /** Creates new form analysisStarter */
    public MoveBalanceTestStarter() {
        initComponents();
        new Runnable(){ //create a thread to move the mouse to make prevent the computer from sleeping
            public void run() {
                try {
                    Robot r = new Robot();
                    while (true){
                        r.mouseMove(683, 384);
                        Thread.sleep(60 * 1000);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }           
        }.run();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        numberUsingMoveText = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        takeFromMoveText = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        totalProgressBar = new javax.swing.JProgressBar();
        groupBattleProgressBar = new javax.swing.JProgressBar();
        startButton = new javax.swing.JButton();
        moveNameLabel = new javax.swing.JLabel();
        stopButton = new javax.swing.JButton();
        continueToggle = new javax.swing.JToggleButton();

        jButton2.setText("jButton2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridLayout(6, 2, 8, 8));

        jLabel1.setText("NUMBER_USING_MOVE");
        getContentPane().add(jLabel1);

        numberUsingMoveText.setText("120");
        getContentPane().add(numberUsingMoveText);

        jLabel2.setText("TAKE_FROM_MOVE");
        getContentPane().add(jLabel2);

        takeFromMoveText.setText("5");
        getContentPane().add(takeFromMoveText);

        jLabel4.setText("完整的進度");
        getContentPane().add(jLabel4);

        jLabel5.setText("一轉的進度");
        getContentPane().add(jLabel5);

        totalProgressBar.setStringPainted(true);
        getContentPane().add(totalProgressBar);

        groupBattleProgressBar.setStringPainted(true);
        getContentPane().add(groupBattleProgressBar);

        startButton.setText("開始");
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                startButtonMousePressed(evt);
            }
        });
        getContentPane().add(startButton);
        getContentPane().add(moveNameLabel);

        stopButton.setText("儲存並停止");
        stopButton.setEnabled(false);
        stopButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                stopButtonMousePressed(evt);
            }
        });
        getContentPane().add(stopButton);

        continueToggle.setText("繼續");
        getContentPane().add(continueToggle);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startButtonMousePressed
        startButton.setEnabled(false);
        continueToggle.setEnabled(false);
        stopButton.setEnabled(true);

        groupBattleTask task = new groupBattleTask();

        task.execute();
    }//GEN-LAST:event_startButtonMousePressed

    private void stopButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stopButtonMousePressed
        stopButton.setEnabled(false);
        saveNow = true;
    }//GEN-LAST:event_stopButtonMousePressed

    private class groupBattleTask extends SwingWorker<Void, Void> {
        
        private long timeStart;

        /**
         * Handle inner battles, that is the actual things to battle for a team
         * @param p
         * @param progressBar
         * @param moveScoreWriter
         * @return
         * @throws Exception 
         */
        private PlayerBattleRecord[] groupBattle(List<PlayerList> p, javax.swing.JProgressBar progressBar, Writer moveScoreWriter) throws Exception {
            int startI = 0, i, startJ = 0, j, k;
            PlayerBattleRecord[] playerBr = new PlayerBattleRecord[p.size()];

            //Get team names
            String[] teamNames;

            //compute number of total battles
            int numberOfCompletedBattle;
            int totalBattles;

            //Total player scores weighted each ability score value
            long hpScore, atkScore, defScore, spdScore, morScore;
            //Player battle time count
            long playerBattleTimeCnt;

            //List of all moves names
            List<Move> allMoves = Move.getAllPublicMoves();
            List<String> allMoveStrings = new ArrayList<String>();
            for (Move m : allMoves) {
                allMoveStrings.add(m.getClass().getSimpleName());
            }

            //Total Scores of the players having this attack
            Map<String, Long> moveScore;

            //How many players (in terms of battle time) have this attack
            Map<String, Long> moveCnt;

            //whether to load data
            if (continueToggle.isSelected()) {
                SaveBattleData d = new SaveBattleData();
                teamNames = d.teamNames;

                numberOfCompletedBattle = d.numberOfCompletedBattle;
                totalBattles = d.totalBattles;

                hpScore = d.hpScore;
                atkScore = d.atkScore;
                defScore = d.defScore;
                spdScore = d.spdScore;
                morScore = d.morScore;

                playerBattleTimeCnt = d.playerBattleTimeCnt;

                moveScore = d.moveScore;

                moveCnt = d.moveCnt;

                p = d.p;

                startI = d.startI;
                startJ = d.startJ;

                //loading are complete. Deselect the toggle.
                continueToggle.setSelected(false);
            } else {
                //if not, initialize
                teamNames = Tournament.getTeamNames(p);

                numberOfCompletedBattle = 0;
                totalBattles = p.size() * (p.size() - 1);

                hpScore = atkScore = defScore = spdScore = morScore = 0;
                playerBattleTimeCnt = 0;

                moveScore = new HashMap<String, Long>();
                moveCnt = new HashMap<String, Long>();

                for (String s : allMoveStrings) {
                    moveScore.put(s, 0L);
                    moveCnt.put(s, 0L);
                }

            }

            if (moveScoreWriter == null) {
                moveScoreWriter = new PrintWriter(new NullPrintStream());
            }

            if (progressBar != null) {
                progressBar.setValue(numberOfCompletedBattle);
                progressBar.setMinimum(0);
                progressBar.setMaximum(totalBattles);
                progressBar.setString("場次： " + numberOfCompletedBattle + "/" + totalBattles
                        + "(進度 " + Math.round((double) numberOfCompletedBattle / totalBattles * 100 * 100) / 100 + "%)");
            }

            //In each group
            for (i = startI; i < p.size() - 1; ++i) {

                //For each pair
                for (j = startJ; j < p.size(); ++j) {

                    if (saveNow) {
                        new SaveBattleData(teamNames, numberOfCompletedBattle, totalBattles, hpScore, atkScore, defScore, spdScore, morScore,
                                playerBattleTimeCnt, allMoveStrings, moveScore, moveCnt, i, j, p).save();
                        return null;
                    }

                    numberOfCompletedBattle++;
                    progressBar.setValue(numberOfCompletedBattle);
                    progressBar.setString("場次： " + numberOfCompletedBattle + "/" + totalBattles
                            + "(進度 " + Math.round((double) numberOfCompletedBattle / totalBattles * 100 * 100) / 100 + "%)");

                    k = (i + j + 1) % (p.size());

                    //Get team member arrays
                    PlayerList t1 = p.get(j);
                    PlayerList t2 = p.get(k);

                    try {
                        Player.battle(t1, t2, teamNames[j], teamNames[k], i, "");
                    } catch (Exception ex) {
                        System.err.println("for players " + teamNames[j] + " and " + teamNames[k] + "!");
                        ex.printStackTrace();
                        return null;
                    }

                }//j for loop - for each pair

                //completed one j loop, reset startJ to 0.
                startJ = 0;

                //obtain new player battle record for each player
                for (int m = 0; m < p.size(); ++m) {
                    //obtain the win/lose/draw for every player and store it in array
                    playerBr[m] = new PlayerBattleRecord(p.get(m));
                }

                //write aby and move statistic - get sum
                for (int g = 0; g < playerBr.length; g++) {
                    for (Player pl : playerBr[g].getPlayers()) {
                        hpScore += Player.hpToPoint(pl.getInitHp()) * playerBr[g].getScore();
                        atkScore += (pl.getInitAtk()) * playerBr[g].getScore();
                        defScore += (pl.getInitDef()) * playerBr[g].getScore();
                        spdScore += (pl.getInitSpd()) * playerBr[g].getScore();
                        morScore += (pl.getInitMor()) * playerBr[g].getScore();
                        playerBattleTimeCnt++;

                        List<String> boughtMoveStrings = new ArrayList<String>();
                        for (Move m : pl.getBoughtAtk()) {
                            boughtMoveStrings.add(m.getClass().getSimpleName());
                        }
                        for (String s : boughtMoveStrings) {
                            if (moveCnt.get(s) != null) {
                                moveCnt.put(s, moveCnt.get(s) + 1);
                                moveScore.put(s, moveScore.get(s) + playerBr[g].getScore());
                            }
                        }
                    }
                }

            }

            //write move score stat
            moveScoreWriter.write(("Abilities and Moves scores: " + "\nHP: " + hpScore / playerBattleTimeCnt + "\nATK: " + atkScore / playerBattleTimeCnt + "\nDEF: " + defScore / playerBattleTimeCnt + "\nSPD: " + spdScore / playerBattleTimeCnt + "\nMOR: " + morScore / playerBattleTimeCnt + '\n'));
            for (String s : allMoveStrings) {
                if (moveScore.get(s) == 0) {
                    moveScoreWriter.write(s + " : never used\n");
                } else {
                    moveScoreWriter.write(s + " : " + moveScore.get(s) / moveCnt.get(s) + '\n');
                }
            }
            moveScoreWriter.flush();

            return playerBr;
        }

        @Override
        /**
         * Handle the outer management - create teams using a certain move, have them battle and extract the best ones, and to final
         */
        public Void doInBackground() {
            timeStart = System.currentTimeMillis();
            try {

                //Number of npc TEAMs using a certain group of moves
                int numberUsingMove;

                //points to allocate
                int pointAllocate;

                //First k npc TEAMs from certain group of moves is taken to final
                int takeFromMove;

                int startI;

                //players entering the final
                List<PlayerList> finalPlayer;

                //whether to load data
                if (continueToggle.isSelected()) {
                    SaveOuterData d = new SaveOuterData();

                    numberUsingMove = d.numberUsingMove;

                    pointAllocate = d.pointAllocate;

                    takeFromMove = d.takeFromMove;

                    startI = d.startI;

                    finalPlayer = d.finalPlayer;
                } else {
                    //if not, initialize
                    numberUsingMove = Integer.parseInt(numberUsingMoveText.getText());

                    pointAllocate = Player.loadTournamentData();

                    takeFromMove = Integer.parseInt(takeFromMoveText.getText());

                    startI = 0;

                    finalPlayer = new ArrayList<PlayerList>();
                }

                List<Move> m = Move.getAllPublicMoves();

                BufferedWriter bw = new BufferedWriter(new FileWriter("moveTimeTaken.txt"));

                totalProgressBar.setValue(startI);
                totalProgressBar.setMinimum(0);
                totalProgressBar.setMaximum(m.size() + 1);
                totalProgressBar.setString("招式： " + (startI) + "/" + (m.size() + 1)
                        + "(進度 " + Math.round((double) (startI) / (double) (m.size() + 1) * 100 * 100) / 100 + "%)");

                //for each pair of moves
                for (int i = startI; i < m.size(); ++i) {

                    moveNameLabel.setText(m.get(i).getName());

                    //obtain players
                    PlayerList p = new PlayerList(numberUsingMove * PLAYER_IN_GROUP);
                    for (int k = 0; k < numberUsingMove * PLAYER_IN_GROUP; k += PLAYER_IN_GROUP) {
                        p.add(new AnalysisPlayer("npc" + i + "-" + k, "npc" + i + "-" + k, pointAllocate, Collections.singletonList(m.get(i).getClass().getSimpleName())));
                        p.add(new AnalysisPlayer("npc" + i + "-" + k, "npc" + i + "-" + k, pointAllocate, Collections.singletonList(m.get(i).getClass().getSimpleName())));
                    }

                    //time how long each move battle takes
                    Long moveTimeStart = System.currentTimeMillis();

                    //have them battle each other
                    PlayerBattleRecord[] pbr = groupBattle(Tournament.prepare(p), groupBattleProgressBar, null);

                    //Write time result to file
                    bw.append("Move " + i + "(" + m.get(i).getName() + ") takes " + Long.toString(System.currentTimeMillis() - moveTimeStart) + "ms \n");

                    //if null received, it means the battle was stopped in the middle
                    if (pbr == null) {
                        new SaveOuterData(numberUsingMove, pointAllocate, takeFromMove, i, finalPlayer).save();
                        System.exit(0);
                    }

                    totalProgressBar.setValue(i);
                    totalProgressBar.setString("招式： " + (i + 1) + "/" + (m.size() + 1)
                            + "(進度 " + Math.round((double) (i + 1) / (double) (m.size() + 1) * 100 * 100) / 100 + "%)");

                    //put the best ones into finalPlayer
                    for (int k = pbr.length - 1; k >= pbr.length - takeFromMove; --k) {
                        finalPlayer.add(pbr[k].getPlayers());
                    }

                }

                Long moveTimeStart = System.currentTimeMillis();

                PlayerBattleRecord[] pbr = groupBattle(finalPlayer, groupBattleProgressBar, new BufferedWriter(new FileWriter("moveScore.txt")));
                //if null received, it means the battle was stopped in the middle
                if (pbr == null) {
                    new SaveOuterData(numberUsingMove, pointAllocate, takeFromMove, m.size(), finalPlayer).save();
                    System.exit(0);
                }

                bw.append("final takes " + Long.toString(System.currentTimeMillis() - moveTimeStart) + "ms \n");

                bw.close();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.toString());
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        public void done() {
            long timeTaken = System.currentTimeMillis() - timeStart;
            stopButton.setEnabled(false);
            JOptionPane.showMessageDialog(null, "已完成。運算時間：" + timeTaken / 60000 + "分" + timeTaken % 60000 / 1000.0 + "秒");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new MoveBalanceTestStarter().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton continueToggle;
    private javax.swing.JProgressBar groupBattleProgressBar;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel moveNameLabel;
    private javax.swing.JTextField numberUsingMoveText;
    private javax.swing.JButton startButton;
    private javax.swing.JButton stopButton;
    private javax.swing.JTextField takeFromMoveText;
    private javax.swing.JProgressBar totalProgressBar;
    // End of variables declaration//GEN-END:variables
}
