///*
// * NodePropertyDialog.java
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import ckEditor.treegui.CKTreeGui;

//
///**
// *
// * @author  Greg
// */
public class NodePropertyDialog extends JDialog {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	NateNode node;
//    
//    /** Creates new form  NodePropertyDialog */
   public NodePropertyDialog(Frame parent, NateNode node) {
	   
       super(parent, true);
       this.node = node;
	   initComponents();
       System.out.println("\n" + node.toString());
       setTitle("Node: " + node.toString());
       //this.replyTextField.setValue(node.getReply());
       this.replyTextField.setText(node.getReply());
       this.nameTextField.setText(node.getSpeaker());
       
   }
    
    @SuppressWarnings("deprecation")
	private void initComponents() {
        jButton1 = new JButton();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        replyTextField =  new JTextArea();
        nameTextField = new JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Node Properties");
        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonHandler(evt);
            }
        });

        jLabel1.setText("Question:");
        jLabel2.setText("Name of Speaker:");

    	getContentPane().setLayout(new BorderLayout());//setLayout(new GridLayout(4,2));
        resize(new Dimension(500,500));
        replyTextField.setLineWrap(true);
        replyTextField.setWrapStyleWord(true);
        nameTextField.setLineWrap(true);
        nameTextField.setWrapStyleWord(true);
        JPanel nameAndText = new JPanel();
        nameAndText.setLayout(new GridLayout(2,2));
        nameAndText. add(jLabel2);
        nameAndText.add(jLabel1);
        nameAndText.add(nameTextField);
        nameAndText.add(replyTextField);
        add(nameAndText, BorderLayout.NORTH);
        add(new StartAndEndNodesCheckbox(node, jButton1), BorderLayout.SOUTH);
        //add(new JLabel("Checkboxes for start&end here"));//
        //add(jButton1);

//        treeRoot.add(node.getSingleParent());//(new CKQuestFactory()).getQuest(1));
		add(new CKTreeGui(node.getSingleParent()), BorderLayout.CENTER);
		//frame.add(new CKGameActionBuilder());

        
        //getContentPane().setLayout(layout);
        
       // pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonHandler(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonHandler
        node.setReply((String)this.replyTextField.getText());
        node.setSpeaker((String)this.nameTextField.getText());
        dispose();
        repaint();
    }//GEN-LAST:event_okButtonHandler
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
   // private JFormattedTextField capFormattedTextField;
    private JButton jButton1;
    private JLabel jLabel1;
    private JTextArea replyTextField;
    private JLabel jLabel2;
    private JTextArea nameTextField;
    // End of variables declaration//GEN-END:variables
    
}
