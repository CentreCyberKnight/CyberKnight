/**
 * 
 */
package ckGameEngine.actions;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;

import ckCommonUtils.CKGridPosition;
import ckCommonUtils.CKPosition;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKGrid;
import ckGameEngine.CKGridActor;
import ckGameEngine.CKSpellCast;
import ckGraphicsEngine.BadInstanceIDError;
import ckGraphicsEngine.FX2dGraphicsEngine;
import ckGraphicsEngine.LoadAssetError;
import ckGraphicsEngine.UnknownAnimationError;
import ckGraphicsEngine.layers.CKGraphicsLayer;
import ckSnapInterpreter.CKQuestRunner;

/**
 * @author dragonlord
 *
 */
public class TeleportAction extends CKQuestAction
{
	


	
	private static final long serialVersionUID = 6459487720903022323L;
	
	String name;
	//static CKGridPosition pos;
	int xVal;
	int yVal;
	int zVal;
	int startFadeOut;
	int endFadeOut;
	int startFadeIn;
	int endFadeIn;
	
	public TeleportAction()
	{
		this("HERO",new CKGridPosition());
	}
	
	public TeleportAction(String name,CKGridPosition pos)
	{
		this.name = name;
		//TeleportAction.pos=pos;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "TeleportAction [name=" + name ;
	}

	
	/* (non-Javadoc)
	 * @see ckGameEngineAlpha.actions.CKQuestCmd#doAction()
	 */
	@Override
	protected void questDoAction(CKSpellCast cast)
	{
		CKGridActor target;
		if(cast!=null) 	{			
			target = getPC(name);			
		}
		else{
			target = getPC(name);						
			}
		
		FX2dGraphicsEngine engine=CKGameObjectsFacade.getEngine();
		
		
		int tid=engine.startTransaction(true);
		
		//spos and epos
		CKPosition spos=target.getPos();
		double zcor=spos.getZ();
		CKPosition epos=new CKPosition(xVal,yVal,zcor,0);
		
		
		try {

			int ID1=engine.FadeMe(tid, target.getAsset(), startFadeOut, endFadeOut, true,spos,CKGraphicsLayer.FRONTHIGHLIGHT_LAYER,target.getDirection().toString());
			engine.loadAsset(tid, "teleport2_Select");
			int spriteID3=engine.createInstance(tid, "teleport2_Select", spos, startFadeOut, CKGraphicsLayer.ENVIRNOMENT_BOUNDRY);
			engine.hide(tid, target.getInstanceID(), startFadeOut);
			CKGrid grid = CKGameObjectsFacade.getQuest().getGrid();
			CKGameObjectsFacade.getQuest().setStartTime(grid.moveInstantly(target,epos,startFadeOut)); 
			engine.destroy(tid, spriteID3, endFadeOut);
			engine.destroy(tid, ID1, endFadeOut);
			//engine.setAnimation(tid,ID1, target.getDirection().toString(), startFadeOut);
		} catch (BadInstanceIDError |LoadAssetError/*| UnknownAnimationError*/ e1) {			
			e1.printStackTrace();}
	
		int ID2=engine.FadeMe(tid, target.getAsset(), startFadeIn, endFadeIn, false, epos, CKGraphicsLayer.FRONTHIGHLIGHT_LAYER,target.getDirection().toString());
		
			/*try {
				engine.setAnimation(tid,ID2, target.getDirection().toString(), startFadeIn);
			} catch (BadInstanceIDError | UnknownAnimationError e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
		try {

			engine.destroy(tid,ID2,endFadeIn);
			engine.reveal(tid, target.getInstanceID(), endFadeIn);
		} catch (BadInstanceIDError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		engine.endTransaction(tid, false);
		
		
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the pos
	 */
	/*public CKGridPosition getPos()
	{
		return pos;
	}*/

	/**
	 * @param pos the pos to set
	 */
	/*public void setPos(CKGridPosition pos)
	{
		//TeleportAction.pos = pos;
		TeleportAction.pos=pos;
	}
	*/
	
	/*
	public JMenuItem GUIEdit()
	{
		JMenu menu = new JMenu("Edit Action");
		JPanel panel = new JPanel();
		panel.add(new JLabel("Actor:"));
		SpinnerListModel model = new SpinnerListModel(getQuest().getActors().getActorNames());
		model.setValue(name);
		JSpinner spinN = new JSpinner(model);
		((JSpinner.DefaultEditor)spinN.getEditor()).getTextField().setColumns(16);
		panel.add(spinN);
		menu.add(panel);	
		
		panel = new JPanel();
		panel.add(new JLabel("X Pos:"));
		SpinnerNumberModel modelX = new SpinnerNumberModel(pos.getX(), 0, 1000,1);
		JSpinner spin = new JSpinner(modelX);
		panel.add(spin);
		menu.add(panel);	

		panel = new JPanel();
		panel.add(new JLabel("Y Pos:"));
		SpinnerNumberModel modelY = new SpinnerNumberModel(pos.getY(), 0, 1000,1);
		spin = new JSpinner(modelY);
		panel.add(spin);
		menu.add(panel);	

		panel = new JPanel();
		panel.add(new JLabel("Z Pos:"));
		SpinnerNumberModel modelZ = new SpinnerNumberModel(pos.getZ(), 0, 1000,1);
		spin = new JSpinner(modelZ);
		panel.add(spin);
		menu.add(panel);	

		panel = new JPanel();
		panel.add(new JLabel("Depth:"));
		SpinnerNumberModel modelDepth = new SpinnerNumberModel(pos.getDepth(), 0, 1000,1);
		spin = new JSpinner(modelDepth);
		panel.add(spin);
		menu.add(panel);	

		
		JMenuItem store = new JMenuItem("Store Edits");
		store.addActionListener(new EditAction(model,modelX,modelY,modelZ,modelDepth));
		menu.add(store);
		
		return menu;
	}
	*/
	
	/**
	 * @return the xVal
	 */
	public int getXVal() {
		return xVal;
	}

	/**
	 * @param xVal the xVal to set
	 */
	public void setXVal(int xVal) {
		this.xVal = xVal;
	}

	/**
	 * @return the yVal
	 */
	public int getYVal() {
		return yVal;
	}

	/**
	 * @param yVal the yVal to set
	 */
	public void setYVal(int yVal) {
		this.yVal = yVal;
	}

	/**
	 * @return the zVal
	 */
	public int getZVal() {
		return zVal;
	}

	/**
	 * @param zVal the zVal to set
	 */
	public void setZVal(int zVal) {
		this.zVal = zVal;
	}
	
	

/*
	class EditAction  implements ActionListener
	{
		SpinnerNumberModel x;
		SpinnerNumberModel y;
		SpinnerNumberModel z;
		SpinnerNumberModel depth;
		SpinnerListModel text;
		
		public EditAction(SpinnerListModel model,SpinnerNumberModel xm,SpinnerNumberModel ym,
				SpinnerNumberModel zm,SpinnerNumberModel dm)
		{
			text=model;
			x=xm;
			y=ym;
			z=zm;
			depth=dm;
		}
	
	

		public void actionPerformed(ActionEvent evt) 
		{
			//pos.setX(x.getNumber().intValue());
			//pos.setY(y.getNumber().intValue());
			xVal=x.getNumber().intValue();
			//pos.setZ(0z.getNumber().intValue());
			yVal=y.getNumber().intValue();
			name=(String) text.getValue();
		}
	}
	
	*/
	//I added everything below from Relative
	
	
	/**
	 * @return the startFadeOut
	 */
	public int getStartFadeOut() {
		return startFadeOut;
	}

	/**
	 * @param startFadeOut the startFadeOut to set
	 */
	public void setStartFadeOut(int startFadeOut) {
		this.startFadeOut = startFadeOut;
	}



	/**
	 * @return the endFadeOut
	 */
	public int getEndFadeOut() {
		return endFadeOut;
	}

	/**
	 * @param endFadeOut the endFadeOut to set
	 */
	public void setEndFadeOut(int endFadeOut) {
		this.endFadeOut = endFadeOut;
	}



	/**
	 * @return the startFadeIn
	 */
	public int getStartFadeIn() {
		return startFadeIn;
	}

	/**
	 * @param startFadeIn the startFadeIn to set
	 */
	public void setStartFadeIn(int startFadeIn) {
		this.startFadeIn = startFadeIn;
	}

	/**
	 * @return the endFadeIn
	 */
	public int getEndFadeIn() {
		return endFadeIn;
	}

	/**
	 * @param endFadeIn the endFadeIn to set
	 */
	public void setEndFadeIn(int endFadeIn) {
		this.endFadeIn = endFadeIn;
	}



	static JPanel []panel;
	static JComboBox<String> []nameBox;
	static SpinnerNumberModel []X; 
	static SpinnerNumberModel []Y;
	static SpinnerNumberModel []SFadeout;
	static SpinnerNumberModel []EFadeout;
	static SpinnerNumberModel []SFadein;
	static SpinnerNumberModel []EFadein;
	
	
	
	@SuppressWarnings("unchecked")
	static private void initPanel(boolean force)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[1]=new JPanel();			

			panel[0].setLayout(new BoxLayout(panel[0], BoxLayout.PAGE_AXIS));
			panel[1].setLayout(new BoxLayout(panel[1], BoxLayout.PAGE_AXIS));

			panel[0].setBorder(BorderFactory.createEmptyBorder(2,2,2,0));
			panel[1].setBorder(BorderFactory.createEmptyBorder(2,2,2,0));
			
			JPanel []tops=new JPanel[2];
			tops[0]=new JPanel();
			tops[1]=new JPanel();
			
			tops[0].setLayout(new BoxLayout(tops[0], BoxLayout.LINE_AXIS));
			tops[0].setBackground(colors[0]);
			tops[1].setBackground(colors[1]);
			tops[1].setLayout(new BoxLayout(tops[1], BoxLayout.LINE_AXIS));			

			//name
			nameBox = new JComboBox[2];
			nameBox[0] = new JComboBox<String>();
			nameBox[1] = new JComboBox<String>();
			tops[0].add(nameBox[0]);		
			tops[1].add(nameBox[1]);		

			//XVal
			X=new SpinnerNumberModel[2];			
			X[0] = new SpinnerNumberModel(1, 0, 1000,1);
			JSpinner spin = new JSpinner(X[0]);
			tops[0].add(spin);
			X[1] = new SpinnerNumberModel(1, 0, 1000,1);
			spin = new JSpinner(X[1]);
			tops[1].add(spin);
			
			tops[0].add(new JLabel("X "));
			tops[1].add(new JLabel("X "));
			
			//YVal
			Y=new SpinnerNumberModel[2];			
			Y[0] = new SpinnerNumberModel(1, 0, 1000,1);
			JSpinner spin2 = new JSpinner(Y[0]);
			tops[0].add(spin2);
			Y[1] = new SpinnerNumberModel(1, 0, 1000,1);
			spin2 = new JSpinner(Y[1]);
			tops[1].add(spin2);
			
			tops[0].add(new JLabel("Y "));
			tops[1].add(new JLabel("Y "));
			
			panel[0].add(tops[0]);
			panel[1].add(tops[1]);
			
			JPanel [] mid = new JPanel[2];
			mid[0] = new JPanel();
			mid[1] = new JPanel();
			
			mid[0].setLayout(new BoxLayout(mid[0], BoxLayout.LINE_AXIS));
			mid[1].setLayout(new BoxLayout(mid[1], BoxLayout.LINE_AXIS));
			mid[0].setBackground(colors[0]);
			mid[1].setBackground(colors[1]);
			
			SFadeout=new SpinnerNumberModel[2];
			SFadeout[0]=new SpinnerNumberModel(1,0,1000,1);
			JSpinner spin3=new JSpinner(SFadeout[0]);
			mid[0].add(spin3);
			SFadeout[1]=new SpinnerNumberModel(1,0,1000,1);
			spin3=new JSpinner(SFadeout[1]);
			mid[1].add(spin3);
			
			mid[0].add(new JLabel("Start Fadeout "));
			mid[1].add(new JLabel("Start Fadeout "));
			
			EFadeout=new SpinnerNumberModel[2];
			EFadeout[0]=new SpinnerNumberModel(1,0,1000,1);
			JSpinner spin4=new JSpinner(EFadeout[0]);
			mid[0].add(spin4);
			EFadeout[1]=new SpinnerNumberModel(1,0,1000,1);
			spin4=new JSpinner(EFadeout[1]);
			mid[1].add(spin4);
			
			mid[0].add(new JLabel("End Fadeout "));
			mid[1].add(new JLabel("End Fadeout "));
			panel[0].add(mid[0]);
			panel[1].add(mid[1]);
			
			
			JPanel []bot=new JPanel[2];
			bot[0]=new JPanel();
			bot[1]=new JPanel();
			
			bot[0].setLayout(new BoxLayout(bot[0], BoxLayout.LINE_AXIS));
			bot[1].setLayout(new BoxLayout(bot[1], BoxLayout.LINE_AXIS));
			bot[0].setBackground(colors[0]);
			bot[1].setBackground(colors[1]);
			
			SFadein=new SpinnerNumberModel[2];
			SFadein[0]=new SpinnerNumberModel(1,0,1000,1);
			JSpinner spin5=new JSpinner(SFadein[0]);
			bot[0].add(spin5);
			SFadein[1]=new SpinnerNumberModel(1,0,1000,1);
			spin5=new JSpinner(SFadein[1]);
			bot[1].add(spin5);
			
			bot[0].add(new JLabel("Start Fadein "));
			bot[1].add(new JLabel("Start Fadein "));
			
			EFadein=new SpinnerNumberModel[2];
			EFadein[0]=new SpinnerNumberModel(1,0,1000,1);
			JSpinner spin6=new JSpinner(EFadein[0]);
			bot[0].add(spin6);
			EFadein[1]=new SpinnerNumberModel(1,0,1000,1);
			spin6=new JSpinner(EFadein[1]);
			bot[1].add(spin6);
			bot[0].add(new JLabel("End Fadein "));
			bot[1].add(new JLabel("End Fadein "));
			panel[0].add(bot[0]);
			panel[1].add(bot[1]);
		}
		
	}
	
	private final static int EDIT=0;
	private final static int RENDER=1;
	private final static Color[] colors={Color.GREEN,Color.WHITE};
	
	private void setPanelValues(int index)
	{
		//System.out.println("setting panel");
		if(panel==null) { initPanel(true);
		System.out.println("Initial XVal is:"+xVal);}
		panel[index].setBackground(colors[index]);
		
		if(getQuest()!=null)
		{
			name = initializeActorBox(nameBox[index],name);
		}
		else
		{

			nameBox[index].addItem("Not Availible");
			nameBox[index].setEnabled(false);
		}
		
		X[index].setValue(xVal);
		System.out.println("New XVal is:"+xVal);
		Y[index].setValue(yVal);
		SFadeout[index].setValue(startFadeOut);
		EFadeout[index].setValue(endFadeOut);
		SFadein[index].setValue(startFadeIn);
		EFadein[index].setValue(endFadeIn);
		//Z[index].setValue(zVal);
	}
	
	
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
		name = (String)nameBox[EDIT].getSelectedItem();
		xVal= X[EDIT].getNumber().intValue();
		System.out.println("XVal is:"+xVal);
		yVal= Y[EDIT].getNumber().intValue();
		startFadeOut=SFadeout[EDIT].getNumber().intValue();
		endFadeOut=EFadeout[EDIT].getNumber().intValue();
		startFadeIn=SFadein[EDIT].getNumber().intValue();
		endFadeIn=EFadein[EDIT].getNumber().intValue();
		writeToStream(System.out);
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
	
	public static void main(String[] args){
		new Thread() {
	        @Override
	        public void run() {
	        	
	            javafx.application.Application.launch(CKQuestRunner.class,"asset3345354808389436014");
	        }
	    }.start();
		
	}



	
	

}


	
	
