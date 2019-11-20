/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Color;

/**
 *
 * @author pc
 */
public class ChooseColor extends javax.swing.JFrame {

    /**
     * Creates new form ChooseColor
     */
    public ChooseColor() {
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rbgColor = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        rbtRed = new javax.swing.JRadioButton();
        rbtBlue = new javax.swing.JRadioButton();
        rbtGreen = new javax.swing.JRadioButton();
        rbtBlack = new javax.swing.JRadioButton();
        txtChooseColor = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        rbtRed.setBackground(java.awt.Color.red);
        rbgColor.add(rbtRed);
        rbtRed.setText("Red");
        rbtRed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtRedActionPerformed(evt);
            }
        });

        rbtBlue.setBackground(java.awt.Color.blue);
        rbgColor.add(rbtBlue);
        rbtBlue.setText("Blue");
        rbtBlue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtBlueActionPerformed(evt);
            }
        });

        rbtGreen.setBackground(java.awt.Color.green);
        rbgColor.add(rbtGreen);
        rbtGreen.setText("Green");
        rbtGreen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtGreenActionPerformed(evt);
            }
        });

        rbtBlack.setBackground(java.awt.Color.black);
        rbgColor.add(rbtBlack);
        rbtBlack.setText("Black");
        rbtBlack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtBlackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rbtRed, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rbtBlue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rbtGreen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rbtBlack, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtChooseColor, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(71, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rbtRed)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbtBlue)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbtGreen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbtBlack)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtChooseColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rbtRedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtRedActionPerformed
        // TODO add your handling code here:
        this.txtChooseColor.setText("Red is choose");
        this.txtChooseColor.setBackground(Color.red);
        this.txtChooseColor.setForeground(Color.white);
    }//GEN-LAST:event_rbtRedActionPerformed

    private void rbtBlueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtBlueActionPerformed
        // TODO add your handling code here:
        this.txtChooseColor.setText("Blue is choose");
        this.txtChooseColor.setBackground(Color.blue);
        this.txtChooseColor.setForeground(Color.white);
    }//GEN-LAST:event_rbtBlueActionPerformed

    private void rbtGreenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtGreenActionPerformed
        // TODO add your handling code here:
        this.txtChooseColor.setText("Green is choose");
        this.txtChooseColor.setBackground(Color.GREEN);
        this.txtChooseColor.setForeground(Color.white);
    }//GEN-LAST:event_rbtGreenActionPerformed

    private void rbtBlackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtBlackActionPerformed
        // TODO add your handling code here:
        this.txtChooseColor.setText("Black is choose");
        this.txtChooseColor.setBackground(Color.BLACK);
        this.txtChooseColor.setForeground(Color.white);
    }//GEN-LAST:event_rbtBlackActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChooseColor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChooseColor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChooseColor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChooseColor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChooseColor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.ButtonGroup rbgColor;
    private javax.swing.JRadioButton rbtBlack;
    private javax.swing.JRadioButton rbtBlue;
    private javax.swing.JRadioButton rbtGreen;
    private javax.swing.JRadioButton rbtRed;
    private javax.swing.JTextField txtChooseColor;
    // End of variables declaration//GEN-END:variables

}
