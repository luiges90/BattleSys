/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NthBattleForm.java
 *
 * Created on 2011年3月1日, 下午08:51:21
 */
package battlesys.frontend;

import battlesys.Main;
import battlesys.io.BattleSysLogger;
import battlesys.Player;
import battlesys.PlayerList;
import battlesys.Tournament;
import battlesys.exception.BattleSysException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 *
 * @author Peter
 */
public class NthBattleForm extends javax.swing.JFrame {

    List<PlayerList> players;
    public static final String APP_NAME = "N戰系統戰鬥啟動介面 / 耒戈氏@HKSAN";
    private final PlayerListForm listForm;
    
    private final int TOTAL_POINTS = Player.loadTournamentData();

    /** Creates new form TotalFrontend */
    public NthBattleForm() {
        initComponents();
        
        pointsLabel.setText(Integer.toString(TOTAL_POINTS));
        roundLabel.setText(Integer.toString(Main.gametime) + Main.hf);

        File playerClassFiles = new File("src/battlesys/playerCtrl" + Main.gametime);
        String[] playerClassFileLists = playerClassFiles.list();
        int num_players = playerClassFileLists.length;

        PlayerList tp = new PlayerList(num_players);

        //Import all players into one array

        for (int i = 0; i < num_players; ++i) {
            //Ignore files that are not java files
            if (!playerClassFileLists[i].substring(playerClassFileLists[i].length() - 5).equalsIgnoreCase(".java")) {
                continue;
            }

            //Import the class
            try {
                String className = "battlesys.playerCtrl" + Main.gametime + "." + playerClassFileLists[i].substring(0, playerClassFileLists[i].length() - 5);
                tp.add((Player) Class.forName(className).getConstructor(new Class[0]).newInstance());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "玩家檔案" + playerClassFileLists[i] + "出現問題！", APP_NAME, JOptionPane.ERROR_MESSAGE);
                BattleSysLogger.getLogger().logp(Level.SEVERE, "NthBattleForm", "<init>", "玩家檔案" + playerClassFileLists[i] + "出現問題！", ex);
            }
        }
        players = Tournament.prepare(tp);
        listForm = new PlayerListForm(players);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        playerInGroupText = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        vwe = new javax.swing.JLabel();
        replayTimeText = new javax.swing.JTextField();
        writeFileCheckbox = new javax.swing.JCheckBox();
        makeImageCheckbox = new javax.swing.JCheckBox();
        rankingShowDataCheckbox = new javax.swing.JCheckBox();
        battleProgressBar = new javax.swing.JProgressBar();
        showPlayerButton = new javax.swing.JButton();
        startBattleButton = new javax.swing.JButton();
        endButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        pointsLabel = new javax.swing.JLabel();
        roundLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(APP_NAME);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("每組人數：");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(jLabel1, gridBagConstraints);

        playerInGroupText.setText("10");
        playerInGroupText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                playerInGroupTextFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(playerInGroupText, gridBagConstraints);
        getContentPane().add(jLabel3, new java.awt.GridBagConstraints());

        vwe.setText("重玩次數：");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(vwe, gridBagConstraints);

        replayTimeText.setText("1");
        replayTimeText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                replayTimeTextMousePressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(replayTimeText, gridBagConstraints);

        writeFileCheckbox.setSelected(true);
        writeFileCheckbox.setText("生成所有結果　　");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(writeFileCheckbox, gridBagConstraints);

        makeImageCheckbox.setText("生成圖片　　　　");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(makeImageCheckbox, gridBagConstraints);

        rankingShowDataCheckbox.setText("於結果顯示玩家資料");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(rankingShowDataCheckbox, gridBagConstraints);

        battleProgressBar.setString("場次： 0/0 (進度 0%)");
        battleProgressBar.setStringPainted(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(battleProgressBar, gridBagConstraints);

        showPlayerButton.setText("顯示玩家資料");
        showPlayerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                showPlayerButtonMousePressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(showPlayerButton, gridBagConstraints);

        startBattleButton.setText("進行比鬥");
        startBattleButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                startBattleButtonMousePressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(startBattleButton, gridBagConstraints);

        endButton.setText("結束");
        endButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                endButtonMousePressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(endButton, gridBagConstraints);

        jLabel2.setText("回數：");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(jLabel2, gridBagConstraints);

        pointsLabel.setText("jLabel4");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(pointsLabel, gridBagConstraints);

        roundLabel.setText("jLabel4");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(roundLabel, gridBagConstraints);

        jLabel4.setText("每玩家點數：");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(jLabel4, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    class TournamentTask extends SwingWorker<Void, Void> {

        @Override
        public Void doInBackground() {
            try {
                File baseFilePath = new File("RESULTS/" + Main.gametime + Main.hf);
                baseFilePath.mkdir();
                Tournament.tournament(players, Integer.parseInt(playerInGroupText.getText()), Integer.parseInt(replayTimeText.getText()),
                        makeImageCheckbox.isSelected(), rankingShowDataCheckbox.isSelected(), writeFileCheckbox.isSelected(), battleProgressBar,
                        new BufferedWriter(new PrintWriter(new File(baseFilePath, "schedule.txt"))), 
                        new BufferedWriter(new PrintWriter(new File(baseFilePath, "TotalStat.txt"))), null, baseFilePath);
                JOptionPane.showMessageDialog(null, "比鬥已完成", APP_NAME, JOptionPane.INFORMATION_MESSAGE);
            } catch (BattleSysException ex) {
                JOptionPane.showMessageDialog(null, "戰鬥途中出現問題！", APP_NAME, JOptionPane.ERROR_MESSAGE);
                BattleSysLogger.getLogger().logp(Level.SEVERE, "NthBattleForm", "startBattleButtonMousePressed", "戰鬥途中出現問題！", ex);
                this.cancel(true);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "沒法生成必須檔案！", APP_NAME, JOptionPane.ERROR_MESSAGE);
                BattleSysLogger.getLogger().logp(Level.SEVERE, "NthBattleForm", "startBattleButtonMousePressed", "沒法生成必須檔案！", ex);
                this.cancel(true);
            }
            return null;
        }

        @Override
        public void done() {
            startBattleButton.setEnabled(true);
            endButton.setEnabled(true);
            //JOptionPane.showMessageDialog(null, "比鬥已完成", APP_NAME, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void startBattleButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startBattleButtonMousePressed
        startBattleButton.setEnabled(false);
        endButton.setEnabled(false);

        TournamentTask task = new TournamentTask();

        task.execute();
        
    }//GEN-LAST:event_startBattleButtonMousePressed

    private void endButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_endButtonMousePressed
        System.exit(0);
    }//GEN-LAST:event_endButtonMousePressed

    private void playerInGroupTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_playerInGroupTextFocusLost
        FrontendUtility.checkPositiveTextField(this, playerInGroupText, "每組人數", APP_NAME);
    }//GEN-LAST:event_playerInGroupTextFocusLost

    private void replayTimeTextMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_replayTimeTextMousePressed
        FrontendUtility.checkPositiveTextField(this, replayTimeText, "重玩次數", APP_NAME);
    }//GEN-LAST:event_replayTimeTextMousePressed

    private void showPlayerButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showPlayerButtonMousePressed
        //show player information
        listForm.setVisible(true);
    }//GEN-LAST:event_showPlayerButtonMousePressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new NthBattleForm().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar battleProgressBar;
    private javax.swing.JButton endButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JCheckBox makeImageCheckbox;
    private javax.swing.JTextField playerInGroupText;
    private javax.swing.JLabel pointsLabel;
    private javax.swing.JCheckBox rankingShowDataCheckbox;
    private javax.swing.JTextField replayTimeText;
    private javax.swing.JLabel roundLabel;
    private javax.swing.JButton showPlayerButton;
    private javax.swing.JButton startBattleButton;
    private javax.swing.JLabel vwe;
    private javax.swing.JCheckBox writeFileCheckbox;
    // End of variables declaration//GEN-END:variables
}
