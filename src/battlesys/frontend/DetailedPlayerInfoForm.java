/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DetailedPlayerInfoForm.java
 *
 * Created on 2011年3月21日, 下午10:33:14
 */

package battlesys.frontend;

import battlesys.Move;
import battlesys.Player;
import battlesys.Utility;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Peter
 */
public class DetailedPlayerInfoForm extends javax.swing.JFrame {

    Player playerToShow;

    /** Creates new form DetailedPlayerInfoForm */
    public DetailedPlayerInfoForm() {
        initComponents();
    }

    private String[] getMoveNames(Object[] o){
        List<String> s = new ArrayList<String>();
        for (int i = 0; i < o.length; ++i){
            Move m = (Move) o[i];
            if (!m.isBasic()){
                s.add(m.getName());
            }
        }
        if (s.isEmpty()) return new String[]{"無"};
        return s.toArray(new String[1]);
    }

    /**
     * Set which player info to show
     */
    void setPlayer(Player p){
        this.playerToShow = p;
        
        //change all the labels
        labelPlayerName.setText(playerToShow == null ? "" : playerToShow.getName());
        labelTeamName.setText(playerToShow == null ? "" : playerToShow.getTeamName());
        labelHp.setText(playerToShow == null ? "" :  Integer.toString(playerToShow.getInitHp()));
        labelAtk.setText(playerToShow == null ? "" : Integer.toString(playerToShow.getInitAtk()));
        labelDef.setText(playerToShow == null ? "" : Integer.toString(playerToShow.getInitDef()));
        labelSpd.setText(playerToShow == null ? "" : Integer.toString(playerToShow.getInitSpd()));
        labelMor.setText(playerToShow == null ? "" : Integer.toString(playerToShow.getInitMor()));
        labelOtherMoves.setText(playerToShow == null ? "" : Utility.listString(getMoveNames(playerToShow.getBoughtAtk().toArray()), "、"));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        labelPlayerName = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        labelTeamName = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        labelHp = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        labelAtk = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        labelDef = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        labelSpd = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        labelMor = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        labelOtherMoves = new javax.swing.JLabel();
        dummy = new javax.swing.JLabel();
        okButton = new javax.swing.JButton();

        setTitle(NthBattleForm.APP_NAME);
        setMinimumSize(new java.awt.Dimension(362, 361));
        getContentPane().setLayout(new java.awt.GridLayout(9, 2, 8, 8));

        jLabel1.setText("角色名稱：");
        getContentPane().add(jLabel1);
        getContentPane().add(labelPlayerName);

        jLabel3.setText("隊伍名稱：");
        getContentPane().add(jLabel3);
        getContentPane().add(labelTeamName);

        jLabel5.setText("體力：");
        getContentPane().add(jLabel5);
        getContentPane().add(labelHp);

        jLabel7.setText("攻擊力：");
        getContentPane().add(jLabel7);
        getContentPane().add(labelAtk);

        jLabel9.setText("防禦力：");
        getContentPane().add(jLabel9);
        getContentPane().add(labelDef);

        jLabel11.setText("速度：");
        getContentPane().add(jLabel11);
        getContentPane().add(labelSpd);

        jLabel13.setText("鬥志：");
        getContentPane().add(jLabel13);
        getContentPane().add(labelMor);

        jLabel17.setText("使用特殊招式：");
        getContentPane().add(jLabel17);
        getContentPane().add(labelOtherMoves);
        getContentPane().add(dummy);

        okButton.setText("確定");
        okButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                okButtonMousePressed(evt);
            }
        });
        getContentPane().add(okButton);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okButtonMousePressed
        this.setVisible(false);
}//GEN-LAST:event_okButtonMousePressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel dummy;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel labelAtk;
    private javax.swing.JLabel labelDef;
    private javax.swing.JLabel labelHp;
    private javax.swing.JLabel labelMor;
    private javax.swing.JLabel labelOtherMoves;
    private javax.swing.JLabel labelPlayerName;
    private javax.swing.JLabel labelSpd;
    private javax.swing.JLabel labelTeamName;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables

}
