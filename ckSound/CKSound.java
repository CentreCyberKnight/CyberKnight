 package ckSound;

 

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ckCommonUtils.CKURL;
import ckCommonUtils.CKXMLAsset;
import ckDatabase.XMLDirectories;
import ckEditor.CKSoundPropertiesEditor;


	public class CKSound implements CKXMLAsset<CKSound> 
	{
		//File folder;
		String filename, license , contributer,url_source,name,description; 
		String AID ="";
		
		Clip clip;
		//Double percent;
		double preferredVolume=.5; 

		public String getBaseDir() {
			return XMLDirectories.SOUND_DIR;
		}
		
	
		//make a default constructor
		public CKSound()
		{
			
		}
		
		
		public CKSound(String filename)
		{
			this(filename, "", "", "");
 
		}
		
		public CKSound(String filename, String license, String AID, String contributer)
		{
			this.setFilename(filename);
			this.setLicense(license);
			this.setAID(AID);
			this.setContributer(contributer);
			
		}
			
		
		public long getMicroSecondLength()
		{
			return clip.getMicrosecondLength();
		}
		
		public void play()
		{
			clip.start();
		}
		
		public void loop()
		{
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			
		}
		
		public void stop() 
		{
			clip.stop();
			
		}

		/**
		 * @return the preferredVolume
		 */
		public double getPreferredVolume()
		{
			return preferredVolume;
		}


		/**
		 * @param preferredVolume the preferredVolume to set
		 */
		public void setPreferredVolume(double preferredVolume)
		{
			this.preferredVolume = preferredVolume;
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


		public String getLicense() {
			return license;
		}

		public void setLicense(String license) {
			this.license = license;
		}
		/**
		 * @return the url_source
		 */
		public String getUrl_source()
		{
			return url_source;
		}


		/**
		 * @param url_source the url_source to set
		 */
		public void setUrl_source(String url_source)
		{
			this.url_source = url_source;
		}


		
		/**
		 * @return the description
		 */
		public String getDescription()
		{
			return description;
		}


		/**
		 * @param description the description to set
		 */
		public void setDescription(String description)
		{
			this.description = description;
		}


		@Override
		public String getAID() {
			return AID;
		}




		@Override
		public void setAID(String a) {
			this.AID = a;;	
		}
		
		
		public String getContributer() {
			return contributer;
		}

		public void setContributer(String contributer) {
			this.contributer = contributer;
		}

		public String getFilename() {
			return filename;
		}
		


		public boolean isRunning() {
			return clip.isRunning();
		}


		public void setFilename(String filename) {
			
			this.filename = filename;
			try {
				File folder = new File (new CKURL(filename).getURL().getFile());
				//File folder = new File (new CKURL(getBaseDir()+filename).getURL().getFile());
		        clip = AudioSystem.getClip();
		        //System.out.println("Data:"+folder.getCanonicalPath());
		        clip.open(AudioSystem.getAudioInputStream(folder));//use ckurl instead of new file meaning use whereever it is instead of relative
		        //clip.start();
		    }
		    catch (Exception exc)
		    {
		        exc.printStackTrace(System.out);
		    }
		}

		@Override
		/**
		* Stores this object to an OutputString
		 * @throws IOException
		 */
		public void writeToStream(OutputStream out)
		{
			XMLEncoder e = new XMLEncoder(
					new BufferedOutputStream(out));
			e.writeObject(this);
			e.close();
			
		}
		
		/*@Override
		public static CKGraphicsAsset readFromStream(InputStream in)
		{
			XMLDecoder d = new XMLDecoder(in);
			CKGraphicsAsset node = (CKGraphicsAsset) d.readObject();
			d.close();
			return node;
			
		}
*/

		
		
		static class SliderListener implements ChangeListener 
		{
			CKSound cks;
			
			
			public SliderListener(CKSound cks)
			{
				this.cks = cks;
			}

			@Override
			public void stateChanged(ChangeEvent e)
			{
				float val = ((JSlider) e.getSource()).getValue();
				
				System.out.println(val);
		
				val =  (float) (val/100.0);
		
				System.out.println(val);
				cks.playAtPercent(val);
			
			}
		}

		public void playAtPercent(double val) {
			
			//this.percent = val;
			//if(clip.isRunning())
			//{
		
			try{
				FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				
				float min = gainControl.getMinimum();
				float max = gainControl.getMaximum();
				float range = max-min;
				
				double logval = Math.log10((9*val)+1);
				double intensity = min + (logval*range);
				gainControl.setValue((float) intensity); 
			}
			catch (IllegalArgumentException e)
			{
				System.out.println(e);
			}
			//}
			
			//else
			//{
			//	System.out.println("this is our problem");
			//}
			
		}
		
		
		public static void main(String[] args) 
		{
	
			JFrame frame = new JFrame();
			frame.setSize(480,400);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			CKSound test1 = new CKSound("/SOUNDS/ASSET_STORAGE/rainforest_ambience-GlorySunz-1938133500.wav");
			
			/*JSlider j = new JSlider();
			j.addChangeListener(new SliderListener(test1));
			*/
			SoundControls controls = new SoundControls(test1);
			frame.add(controls);
			frame.setVisible(true);
			
			//test1.play();
			//test1.playAtPercent(.30);

		}


		@Override
		public JComponent getXMLAssetViewer()
		{
			return new CKSoundPropertiesEditor(this,false);
		}


		@Override
		public JComponent getXMLAssetViewer(ckCommonUtils.CKXMLAsset.ViewEnum v)
		{
			return new CKSoundPropertiesEditor(this,false);
		}


		@SuppressWarnings("unchecked")
		@Override
		public  CKSoundPropertiesEditor getXMLPropertiesEditor()
		{
			return new CKSoundPropertiesEditor(this,true);
		}


		public static class SoundControls extends JPanel
		{
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -6132253112131383018L;
			CKSound sound;
			JButton playButton;
			JButton stopButton;
			JSlider slider;
			double volume;// = .5f;
			
			
		
			public SoundControls(CKSound sound)
			{
				volume = sound.getPreferredVolume();
				this.sound = sound;
				setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
				
				JPanel buttons = new JPanel(new FlowLayout());
				buttons.setAlignmentX(LEFT_ALIGNMENT);
				
				playButton = new JButton("Play Sound");
				playButton.addActionListener(new PlaySound());
				buttons.add(playButton);
				
				stopButton = new JButton("Stop Sound");
				stopButton.addActionListener(new StopSound());
				stopButton.setEnabled(false);
				buttons.add(stopButton);

				add(buttons);
				
				slider = new JSlider();
				slider.addChangeListener(new SliderListener());
				slider.setValue((int) (volume*100));
				slider.setAlignmentX(LEFT_ALIGNMENT);
				add(slider);
				
			}
			
			
			public double getVolume()
			{
				return volume;
			}
			
			public void setVolume(double volume2)
			{
				volume = volume2;
				slider.setValue( (int) (volume*100) );
				
				
			}
			
			class SliderListener implements ChangeListener 
			{
				
				@Override
				public void stateChanged(ChangeEvent e)
				{
					float val = ((JSlider) e.getSource()).getValue();
			
					volume=  (float) (val/100.0);
			
					//System.out.println(val);
					sound.playAtPercent(volume);
				
				}
			}
			
			
			class PlaySound implements ActionListener
			{

				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					
					sound.loop();
					sound.playAtPercent(volume);
					playButton.setEnabled(false);
					stopButton.setEnabled(true);
					}
				
			}
			
			class StopSound implements ActionListener
			{

				@Override
				public void actionPerformed(ActionEvent e)
				{
					sound.stop();
					playButton.setEnabled(true);
					stopButton.setEnabled(false);					
				}
				
			}

			
			
			
		}


		

	}
