package ckEditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import ckGraphicsEngine.assets.CKImageAsset;

public class PreviewPanel extends JPanel 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4715669878135745478L;
	Image bg_img;//64,32
	Color bg_color;
	CKImageAsset tile;
	
	PreviewPanel(Image i,Color c,CKImageAsset a)
	{
		setPreferredSize(new Dimension(64,32));
		bg_img=i;
		bg_color=c;
		tile=a;
		setVisible(true);
	}
	
	public void paint(Graphics g)
	{
		if (bg_img !=null)
		{
			g.drawImage(bg_img,0,0,null);
		}
		else
		{
			g.setColor(bg_color);
			g.fillRect(0,0, 64, 32);
		}
		if (tile!=null)
		{
			//tile.scaleToGraphics(g, 0, 0,0, this, 1);
		}
	}
}
