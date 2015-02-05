/**
 * 
 */
package ckGameEngine.actions;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;

import ckCommonUtils.CKEntitySelectedListener;
import ckEditor.DataPickers.CKXMLFilteredAssetPicker;
import ckEditor.treegui.CKGuiRoot;
import ckEditor.treegui.CKTreeGui;
import ckGameEngine.CKGameObjectsFacade;
import ckGameEngine.CKSpellCast;
import ckGameEngine.Quest;
import ckGraphicsEngine.CK2dGraphicsEngine;
import ckSound.CKSound;
import ckSound.CKSoundFactory;

/**
 * @author dragonlord
 *
 */
public class CKSoundEffect extends CKGameAction implements ActionListener
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4161611870406084407L;
	private String soundAID="";
	private CKSound soundClip = null;
	private double volume=1.0f;
	
	
	private boolean playMultiple=true;
	private boolean playDuration=false;
	private boolean playBackGroundStart = false;
	private boolean playBackGroundStop=false;
	
	private int multiples = 1;
	private int duration=1;
			

	
	
	public CKSoundEffect() {}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Sound Effect";
	}




	



	@Override
	public void doAction(CKGameActionListenerInterface L, CKSpellCast cast)
	{
		replaceListener(L);

		CK2dGraphicsEngine engine = CKGameObjectsFacade.getEngine();
		Quest quest = CKGameObjectsFacade.getQuest();
		if(playBackGroundStart || playBackGroundStop)
		{
			//todo get preffered volume
			int SID = quest.getBackGroundSoundSID(soundAID);
			if(playBackGroundStart)
			{
				//engine.playSound(quest.getStartTime(), SID, volume);
				engine.playSound(quest.getStartTime(), SID);
			}
			else
			{
				engine.stopSound(quest.getStartTime(), SID);
			}
					
		}
		else
		{
			int duration = this.duration;

			if (this.playMultiple) // calculate length
			{
				long length = CKSoundFactory.getInstance()
						.getAsset(this.soundAID).getMicroSecondLength();
				double time = multiples * length / 1000000;
				// CKSoundFactory.getInstance().getAsset(this.soundAID).getMicroSecondLength();
				duration = (int) (time * engine.getFps());
			}
			int SID = engine.createSoundInstance(0, soundAID);
			//engine.playSound(quest.getStartTime(), quest.getStartTime()
			//		+ duration, SID, volume);
			engine.playSound(quest.getStartTime(), quest.getStartTime()
					+ duration, SID);
		}		
		
		notifyListener();
		
	}


	public void loadSound()
	{
		if(soundAID.length()>0 && soundClip==null)
		{
			soundClip = CKSoundFactory.getInstance().getAsset(soundAID);
		}
	}
	
	public String getSoundName()
	{
		loadSound();
		
		if(soundClip==null) { return "Please pick sound"; } 
		
		return soundClip.getName(); 
		
	}
	
	

	
	/**
	 * @return the volume
	 */
	public double getVolume()
	{
		return volume;
	}


	/**
	 * @param volume the volume to set
	 */
	public void setVolume(double volume)
	{
		this.volume = volume;
	}


	/**
	 * @return the soundAID
	 */
	public String getSoundAID()
	{
		return soundAID;
	}


	/**
	 * @param soundAID the soundAID to set
	 */
	public void setSoundAID(String soundAID)
	{
		this.soundAID = soundAID;
	}


	/**
	 * @return the soundClip
	 */
	public CKSound getSoundClip()
	{
		return soundClip;
	}


	/**
	 * @param soundClip the soundClip to set
	 */
	public void setSoundClip(CKSound soundClip)
	{
		this.soundClip = soundClip;
	}


	/**
	 * @return the playMultiple
	 */
	public boolean isPlayMultiple()
	{
		return playMultiple;
	}


	/**
	 * @param playMultiple the playMultiple to set
	 */
	public void setPlayMultiple(boolean playMultiple)
	{
		this.playMultiple = playMultiple;
	}


	/**
	 * @return the multiples
	 */
	public int getMultiples()
	{
		return multiples;
	}


	/**
	 * @param multiples the multiples to set
	 */
	public void setMultiples(int multiples)
	{
		this.multiples = multiples;
	}


	/**
	 * @return the duration
	 */
	public int getDuration()
	{
		return duration;
	}


	/**
	 * @param duration the duration to set
	 */
	public void setDuration(int duration)
	{
		this.duration = duration;
	}




	/**
	 * @return the playDuration
	 */
	public boolean isPlayDuration()
	{
		return playDuration;
	}


	/**
	 * @param playDuration the playDuration to set
	 */
	public void setPlayDuration(boolean playDuration)
	{
		this.playDuration = playDuration;
	}


	/**
	 * @return the playBackGroundStart
	 */
	public boolean isPlayBackGroundStart()
	{
		return playBackGroundStart;
	}


	/**
	 * @param playBackGroundStart the playBackGroundStart to set
	 */
	public void setPlayBackGroundStart(boolean playBackGroundStart)
	{
		this.playBackGroundStart = playBackGroundStart;
	}


	/**
	 * @return the playBackGroundStop
	 */
	public boolean isPlayBackGroundStop()
	{
		return playBackGroundStop;
	}


	/**
	 * @param playBackGroundStop the playBackGroundStop to set
	 */
	public void setPlayBackGroundStop(boolean playBackGroundStop)
	{
		this.playBackGroundStop = playBackGroundStop;
	}




	static JPanel []panel;
	
	static JLabel [] soundName;
	static SpinnerNumberModel [] multipleSpinner;
	static SpinnerNumberModel [] durationSpinner;
	
	static JCheckBox [] multipleBox;
	static JCheckBox [] durationBox;
	static JCheckBox [] startBox;
	static JCheckBox [] endBox;
	
	static JButton [] playSoundButton;
	static JButton [] pickSoundButton;
	

	private void initPanel(boolean force)
	{
		if(panel==null || force)
		{
			panel=new JPanel[2];
			panel[0]=new JPanel();
			panel[0].setLayout(new BoxLayout(panel[0],BoxLayout.Y_AXIS));
			panel[1]=new JPanel();			
			panel[1].setLayout(new BoxLayout(panel[1],BoxLayout.Y_AXIS));


			JPanel []name = new JPanel[2]; 
			name[0] = new JPanel();
			name[1] = new JPanel();
			

			
			
			soundName = new JLabel[2];
			soundName[0] = new JLabel();
			soundName[1] = new JLabel();

			name[0].add(new JLabel("Play Sound:"));
			name[0].add(soundName[0]);			
			name[1].add(new JLabel("Play Sound:"));
			name[1].add(soundName[1]);
			panel[0].add(name[0]);
			panel[1].add(name[1]);
			
			JPanel []top = new JPanel[2]; 
			top[0] = new JPanel();
			top[1] = new JPanel();
			
			
			multipleBox = new JCheckBox[2];
			multipleBox[0] = new JCheckBox("Multiple of Clip");
			multipleBox[1] = new JCheckBox("Mulitple of Clip");
			top[0].add(multipleBox[0]);
			top[1].add(multipleBox[1]);
			
			

			
			multipleSpinner = new SpinnerNumberModel[2];
			multipleSpinner[0] = new SpinnerNumberModel(1,1,100,1);
			multipleSpinner[1] = new SpinnerNumberModel(1,1,100,1);
			
			JSpinner spin = new JSpinner(multipleSpinner[0]);
			top[0].add(spin);

			spin = new JSpinner(multipleSpinner[1]);
			top[1].add(spin);
			
	

			/*---*/
			
			
			JPanel []bot = new JPanel[2]; 
			bot[0] = new JPanel();
			bot[1] = new JPanel();
			
			durationBox = new JCheckBox[2];
			durationBox[0] = new JCheckBox("Set Duration");
			durationBox[1] = new JCheckBox("Set Duration");
			bot[0].add(durationBox[0]);
			bot[1].add(durationBox[1]);
			
			
			


			
			durationSpinner = new SpinnerNumberModel[2];
			durationSpinner[0] = new SpinnerNumberModel(1,1,100,1);
			durationSpinner[1] = new SpinnerNumberModel(1,1,100,1);
			
			JSpinner spin2 = new JSpinner(durationSpinner[0]);
			bot[0].add(spin2);

			spin2 = new JSpinner(durationSpinner[1]);
			bot[1].add(spin2);
			
			
			panel[0].add(top[0]);
			panel[1].add(top[1]);
			panel[0].add(bot[0]);
			panel[1].add(bot[1]);
			
			JPanel [] bg = new JPanel[2];
			bg[0] = new JPanel();
			bg[1] = new JPanel();
			
			bg[0].add(new JLabel("Background Sound:"));
			bg[1].add(new JLabel("Background Sound:"));
			startBox = new JCheckBox[2];
			startBox[0] = new JCheckBox("Start");
			startBox[1] = new JCheckBox("Start");
			bg[0].add(startBox[0]);
			bg[1].add(startBox[1]);

			endBox = new JCheckBox[2];
			endBox[0] = new JCheckBox("Stop");
			endBox[1] = new JCheckBox("Stop");
			bg[0].add(endBox[0]);
			bg[1].add(endBox[1]);

			panel[0].add(bg[0]);
			panel[1].add(bg[1]);
			
			ButtonGroup group = new ButtonGroup();
			group.add(multipleBox[0]);
			group.add(durationBox[0]);
			group.add(startBox[0]);
			group.add(endBox[0]);
			
			ButtonGroup group2 = new ButtonGroup();
			group2.add(multipleBox[1]);
			group2.add(durationBox[1]);
			group2.add(startBox[1]);
			group2.add(endBox[1]);

			
			pickSoundButton = new JButton[2];
			pickSoundButton[0] = new JButton("Pick Sound");
			pickSoundButton[1] = new JButton("Pick Sound");
			
			
			playSoundButton = new JButton[2];
			playSoundButton[0] = new JButton("Play Sound");
			playSoundButton[1] = new JButton("Play Sound");
			
			
			
			JPanel pick[] = new JPanel[2];
			pick[0] = new JPanel();
			pick[1] = new JPanel();
			
			pick[0].add(playSoundButton[0]);
			
			pick[0].add(pickSoundButton[0]);
			pick[1].add(playSoundButton[1]);

			pick[1].add(pickSoundButton[1]);
			
			panel[0].add(pick[0]);
			panel[1].add(pick[1]);
			
			
					
		}
		
	}
	
	
	private final static int EDIT=0;
	private final static int RENDER=1;
	private final static Color[] colors={Color.GREEN,Color.WHITE};
	
	private void setPanelValues(int index)
	{
		//System.out.println("setting panel");
		if(panel==null) { initPanel(true); }
		panel[index].setBackground(colors[index]);

		multipleBox[index].setSelected(playMultiple);
		durationBox[index].setSelected(playDuration);
		startBox[index].setSelected(playBackGroundStart);
		endBox[index].setSelected(playBackGroundStop);
		
		
		multipleSpinner[index].setValue(multiples);
		durationSpinner[index].setValue(duration);
		
		
		//need to set volume and name stuff
		soundName[index].setText(getSoundName());
		
	 }
	
	
	static CKSoundEffect listener = null;
	static PlaySoundListener playListener = null;
	//protected void resetValues()
	
	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
	 */
	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row)
	{
		pickSoundButton[EDIT].removeActionListener(listener);
		playSoundButton[EDIT].removeActionListener(playListener);
		setPanelValues(EDIT);
		
		listener=this;
		playListener = new PlaySoundListener();
		
		pickSoundButton[EDIT].addActionListener(listener);
		playSoundButton[EDIT].addActionListener(playListener);
		
		return panel[EDIT];	
	}

	/* (non-Javadoc)
	 * @see ckGraphics.treegui.CKGUINode#storeComponentValues()
	 */
	@Override
	public void storeComponentValues()
	{
		playMultiple = multipleBox[EDIT].isSelected();
		playDuration = durationBox[EDIT].isSelected();
		playBackGroundStart = startBox[EDIT].isSelected();
		playBackGroundStop = endBox[EDIT].isSelected();
		duration = (Integer) durationSpinner[EDIT].getValue();
		multiples = (Integer) multipleSpinner[EDIT].getValue();

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
			JFrame frame = new JFrame("CyberKnight SAT Editor");
			CKGuiRoot root = new CKGuiRoot();
		
			
			//root.add(new BookSatisfies("Fire","bolt",13,NumericalCostType.EQ) );
			root.add(new CKSoundEffect());root.add(new CKSoundEffect());
			frame.add(new CKTreeGui(root));
			//frame.add(new CKGameActionBuilder());
			frame.pack();
			frame.setVisible(true);
			frame.setSize(600,600);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 
	 }

		
		class PickerListener implements CKEntitySelectedListener<CKSound>
		{
			JFrame frame;
			public PickerListener(JFrame f) {frame=f;}
			
			@Override
			public void entitySelected(CKSound a)
			{
				//CKAssetEditor.this.entitySelected(a);
				soundAID = a.getAID();
				volume = a.getPreferredVolume();
				soundName[0].setText(a.getName());
				soundName[1].setText(a.getName());
				
				frame.setVisible(false); //you can't see me!
				frame.dispose(); //Destroy the JFrame object
			}

		}
		
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		JFrame frame = new JFrame();
		CKXMLFilteredAssetPicker<CKSound,CKSoundFactory> picker = 
				new CKXMLFilteredAssetPicker<CKSound,CKSoundFactory>(CKSoundFactory.getInstance());		
		picker.addSelectedListener(new PickerListener(frame));
		frame.add(picker);
		frame.pack();
		frame.setVisible(true);
		
	}
	
	
	class PlaySoundListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			loadSound();
			if(soundClip!=null)
			{
				JFrame frame = new JFrame();
				frame.add(soundClip.getXMLAssetViewer());
				frame.pack();
				frame.setVisible(true);				
			}			
		}
		
	}
	
}
