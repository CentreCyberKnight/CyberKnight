/**
 * 
 */
package ckGameEngine.actions;

import java.awt.Component;
import java.awt.event.ItemListener;

import javafx.application.Platform;
import javafx.scene.web.WebEngine;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTree;

import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import ckEditor.treegui.CKGuiRoot;
import ckEditor.treegui.CKTreeGui;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKSpellCast;

/**
 * @author dragonlord
 *
 */
public class CKTutorialAction extends CKGameAction 

{
	
	private static final long serialVersionUID = -2687596818371869914L;

	private String mess;
	
	
	public CKTutorialAction()
	{
		//FIXME-need default text
		this("Let's Chase Bad Guys!");
		
	}
	
	
	public CKTutorialAction(String mess)
	{
		super();
		this.mess=mess;
	}
	


	
	/**
	 * @return the mess
	 */
	public String getMess()
	{
		return mess;
	}


	/**
	 * @param mess the mess to set
	 */
	public void setMess(String mess)
	{
		this.mess = mess;
	}
	
	public void notifyDone()
	{
		notifyListener();
	}

	/* (non-Javadoc)
	 * @see ckGameEngine.actions.CKGameAction#doAction(ckGameEngine.actions.CKGameActionListener)
	 */
	@Override
	public void doAction(CKGameActionListenerInterface L,CKSpellCast cast)
	{
		
		replaceListener(L);
		CKTutorialAction me = this;
		Platform.runLater(new Runnable(){
			
			@Override public void run()
			{
				WebEngine webEngine = CKGameObjectsFacade.getWebEngine();
				
				JSObject jsobj = (JSObject) webEngine.executeScript("window");
				//send me to javascript
				jsobj.setMember("tutorialDone", me);
							
				//start tutorial
				try
				{
//					webEngine.executeScript("ide.setCyberSnap()");
					//webEngine.executeScript("ide.runTutorial("+mess+")");
					//System.out.println(mess);
					webEngine.executeScript( "var tutorial = new tutorial_Morph(ide," + mess + ", world)");
					//webEngine.executeScript( "console.log("+mess+")");
					//webEngine.executeScript("tutorial.openIn(world)");
				}
				catch(JSException e)
				{
					System.err.println("JavaScript" + e.getMessage());
				}
					
			}
		});
		
			
		//do nothing.
		//tutorial will run tutorialDone.notifyListener();
		
	}


	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Tutorial is"+ mess;
	}
	
	

	static JPanel []panel;
	static JTextArea []messBox;
	
	
	static private void initPanel(boolean force)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[1]=new JPanel();
			
			messBox=new JTextArea[2];
			messBox[0]=new JTextArea(5,30);
			messBox[1]=new JTextArea(5,30);
			panel[0].add(messBox[0]);
			panel[1].add(messBox[1]);
			
			messBox[0].setLineWrap(true);
			messBox[0].setWrapStyleWord(true);
			messBox[1].setLineWrap(true);
			messBox[1].setWrapStyleWord(true);
			
			//panel[0].add(bot[0]);
			//panel[1].add(bot[1]);
			
		}
		
	}


	private void setPanelValues(int index)
	{
		
		if(panel==null) { initPanel(true); }
		
		messBox[index].setText(mess);
	
	}
	
	
	static ItemListener guiListener=null;
	
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
	 */
	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row)
	{
		setPanelValues(EDIT);		
		return panel[EDIT];	
	}

	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#storeComponentValues()
	 */
	@Override
	public void storeComponentValues()
	{
		mess = messBox[EDIT].getText();
	}

	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus)
	{
		setPanelValues(RENDER);
		return panel[RENDER];
	}


	 public static void main(String[] args)
	 {
			JFrame frame = new JFrame("CyberKnight Tutorial Editor");
			CKGuiRoot root = new CKGuiRoot();
		
			
			//root.add(new BookSatisfies("Fire","bolt",13,NumericalCostType.EQ) );
			root.add(new CKTutorialAction());
			frame.add(new CKTreeGui(root));
			//frame.add(new CKGameActionBuilder());
			frame.pack();
			frame.setVisible(true);
			frame.setSize(600,600);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 
	 }
	
}
