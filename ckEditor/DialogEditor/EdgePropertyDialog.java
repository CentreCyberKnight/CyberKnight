///*
// * EdgePropertyDialog.java
// *
// * Created on March 22, 2007, 2:23 PM
// * Copyright 2007 Grotto Networking
// */
//
package ckEditor.DialogEditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ckEditor.treegui.CKTreeGui;

//
///**
// *
// * @author  Greg
// */
public class EdgePropertyDialog extends JDialog {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	NateLink edge;
//    
//    /** Creates new form EdgePropertyDialog */
   public EdgePropertyDialog(Frame parent, NateLink edge) {
       super(parent, true);
       System.out.println(edge + "Here in Edge Property Dialog");
       this.edge = edge;
	   initComponents();

       setTitle("Edge: " + edge.toString());
       this.wtFormattedTextField.setValue(edge.getWeight());
       this.replyTextField.setValue(edge.getDialogOption()); 

   }
    
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
	private void initComponents() {
        jButton1 = new JButton();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        wtFormattedTextField = new javax.swing.JFormattedTextField();
        replyTextField = new javax.swing.JFormattedTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Edge Properties");
        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonHandler(evt);
            }
        });

        jLabel1.setText("Reply:");

        jLabel2.setText("Weight ");
        


    	getContentPane().setLayout(new BorderLayout());
        resize(new Dimension(500,500));
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(2,2));
        textPanel.add(jLabel1);
        textPanel.add(replyTextField);
        textPanel.add(jLabel2);
        textPanel.add(wtFormattedTextField);
        add(textPanel, BorderLayout.NORTH);
        //add(new JLabel("Switch button for edge reversal?"));
        add(jButton1, BorderLayout.SOUTH);

        //CKGuiRoot treeRoot = new CKGuiRoot();
        //treeRoot.add((new CKQuestFactory()).getQuest(1));
        //add(new CKTree2(treeRoot), BorderLayout.CENTER);
        add(new CKTreeGui(edge.getSingleParent()), BorderLayout.CENTER);
		//frame.add(new CKGameActionBuilder());
        

        
        //getContentPane().setLayout(layout);
        
       // pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonHandler(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonHandler
        edge.setWeight((Double)this.wtFormattedTextField.getValue());
        edge.setDialogOption((String)this.replyTextField.getText());
        dispose();
        repaint();
    }//GEN-LAST:event_okButtonHandler
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
   // private JFormattedTextField capFormattedTextField;
    private JButton jButton1;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JFormattedTextField wtFormattedTextField;
    private JFormattedTextField replyTextField;
    // End of variables declaration//GEN-END:variables
    
}
