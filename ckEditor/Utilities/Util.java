package ckEditor.Utilities;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

import ckEditor.ImageAssetInformationPanel;

public class Util 
{

	public static void promptDialog(ImageAssetInformationPanel i)
	{
		JDialog d=new JDialog();
		d.setTitle("Please fill in asset information");
		d.setContentPane(i);
		d.pack();
		d.setResizable(false);
		d.setVisible(true);
		
	}
	

	
	public static String getFile()
	{
		JFileChooser fileChooser=new JFileChooser("./");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int result=fileChooser.showOpenDialog(fileChooser);//has open button instead of save
		if (result==JFileChooser.CANCEL_OPTION)
			{return null;}
		return fileChooser.getSelectedFile().getAbsolutePath();
	}
	
	public static Image getImage(String fname)
	{
		Image img=null;
		  try {
				img=ImageIO.read(new File(fname));
			} catch (IOException e) {System.out.println("grid image not there");}
		return img;
	}
}
