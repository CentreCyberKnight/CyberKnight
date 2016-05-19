package ckEditor.CharacterSheetCreator;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CharacterSheetCreator 
{
	// name format: "C_NW_int A_int B , NW is the direction used in blender, int A denotes the row. Int B is the column
	BufferedImage[][] images=null;
	
	
	
	public CharacterSheetCreator(String path, int numRows, int numCols)
	{
		images=new BufferedImage[numRows][numCols];
		
		
		
		for (int r=0; r<numRows;r++)
		{
			for (int c=0;c<numCols;c++)
			{
				try {
					images[r][c]=ImageIO.read(new File(path+r+"_"+c+".png"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		int width=images[0][0].getWidth();
		int height=images[0][0].getHeight();
		
		BufferedImage output=new BufferedImage(width*numCols,height*numRows,BufferedImage.TYPE_INT_ARGB);
		Graphics g=output.getGraphics();
		
		for (int r=0; r<numRows;r++)
		{
			for (int c=0;c<numCols;c++)
			{
				g.drawImage(images[r][c],width*c,r*height,null);
			}
		}
		
		try {
			ImageIO.write(output, "png", new File(path+"characterSheet.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
	
	
	public static void main(String[] args)
	{
		new CharacterSheetCreator("/Render/",4,4);
	}

}
