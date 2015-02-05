package ckEditor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import ckGraphicsEngine.assets.CKImageAsset;

public class Editor extends JPanel implements MouseListener, MouseMotionListener//, MouseMotionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 569037233705778278L;
	
	/* class data */	
	 //housing for editor

	private int x;//x and y values 
	private int y;
	private int scale=9;
	private int constX=(64*scale);
	private int constY=(32*scale);
	private static int win_width=(64*9);//the default scale;
	private static int win_height=(32*9);//"
	private int frame=0;
	private int row=0;
	private String cursorMode="paint";
	private CKImageAsset image=null;
	private Image bg_image=null;
	private ColorChooser chooser;
	 /*constructor
		 *@param desktop--JDesktopPane--the window to draw to
		 *@return none
		 */
	public Editor(ColorChooser c)
	{
			chooser=c;
			setVisible(true);
			addMouseListener(this);
			addMouseMotionListener(this);
			setPreferredSize(new Dimension(win_width,win_height));
			
			    // Read from a file
			   
			    try {
			    	 File file = new File("ckEditor/images/tools/finalgrid.png");
					bg_image = ImageIO.read(file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	
	

	public void paint(Graphics g)
	{
		//Graphics s=scroll.getGraphics();
		if (bg_image != null)
		{
			
			//g.drawImage(bg_image, 0, 0, null);
			for (int i=0;i<64*scale;i=i+100)
			{
				for(int j=0;j<32*scale;j=j+100)
				{
					g.drawImage(bg_image, i, j, null);
					//s.drawImage(bg_image, i, j, null);
				}
			}
		}
		else
		{
			//g.setColor(bgcolor);
			g.setColor(Color.pink);
			g.fillRect(0,0, 64*scale,32*scale);
			//s.fillRect(0,0, 64*scale,32*scale);
		}
		//g.drawImage(bg_image,0,0,null);
		if (image!=null)
		{
			image.scaleToGraphics(g, 0, 0,frame,row, this,scale);
			
		}
		// image.drawToGraphics(g, win_width-90, win_height-80, 0, this); 
	}
	
	
	

	
	
	
	public Image getImage(String fname)
	{
		Image img=null;
		  try {
				img=ImageIO.read(new File(fname));
			} catch (IOException e) {System.out.println("file not found");}//TODO print("Can't locate file.");}
		return img;
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}


	@Override
	public void mouseExited(MouseEvent arg0) {}

	
	
	@Override
	public void mousePressed(MouseEvent e) 
	{
		if (image!=null)
		{
			if (cursorMode=="copyColor")
			{
				x=e.getX();//whoops. you must get a new x and new y
				y=e.getY();
				System.out.println("mode is set to: color");
				int argb=image.getRGB(x/(scale), y/(scale));
				int alpha=(argb >> 24) & 0xff;
				int red=(argb >> 16) & 0xff;
				int green=(argb >> 8) & 0xff;
				int blue=argb & 0xff;
				//System.out.printf("alpha is: %d\n", alpha);
				//System.out.printf("red is: %d\n", red);
				//System.out.printf("green is: %d\n", green);
				//System.out.printf("blue is: %d\n", blue);
				chooser.setColor(red,green,blue,alpha);
				cursorMode="paint";
			}
			
			else if(cursorMode=="fill")
			{
				x=e.getX();
				y=e.getY();
				try{
				fill(this.image.getPixel(x/(scale), y/(scale), 1),chooser.getColor(),x/(scale), y/(scale),1);
				
				}
				catch(Exception d){System.out.println("catching an error. . ."+d.getMessage());}
				cursorMode="paint";
				
			}
		}
		//print("cursor at ("+x+","+y+")");
	}

//this currently controls the coloring of a pixel when not ragging
	@Override
	public void mouseReleased(MouseEvent e) 
	{
		if(image!=null)
		{
			x = e.getX();
			y = e.getY();
			if (cursorMode=="paint");
			{
				if (x<=constX && y<=constY)
				{	
				chooser.recalcColor();
				image.setPixel(x/(scale), y/(scale), frame, chooser.getColor());
				this.getParent().getParent().repaint();
				repaint();
				}
			
			}
		}
		
	}
	
	/* this does nothing --we sould fix that*/
	@Override
	public void mouseClicked(MouseEvent e) 
	{}
	
	//function calles everytime the cursor is dragged
	@Override
	public void mouseDragged(MouseEvent event) 
	{
		if (image!=null)
		{
		  x = event.getX();//-10 for better accuracy
		  y = event.getY();
          if (x<=constX && y<=constY)
          {
        	  try {
        		  chooser.recalcColor();
        		  image.setPixel(x/(scale), y/(scale), frame, chooser.getColor());
        		  this.getParent().getParent().repaint();
        		  repaint();
        	  } catch (Exception e1) {System.out.println("Cursor out of bounds.");}
          }
         
		}
          //print("cursor at ("+x+","+y+")");
	}

	//not used
	@Override
	public void mouseMoved(MouseEvent e)
	{}

	private void resetConstants()
	{
	 constX=(64*scale);
	 constY=(32*scale);
	}
	
	private void resetSize()
	{
		//this.(constX,constY);
		System.out.println("size: ("+constX+","+constY+")");
		setPreferredSize(new Dimension(constX,constY));
		//scroll.setPreferredSize(new Dimension(64*9,32*9));
		revalidate();
	}
	
	public void zoomIn()
	{
		scale++;
		resetConstants();
		resetSize();
		repaint();
	}
	
	public void zoomOut()
	{
		scale--;
		resetConstants();
		resetSize();
		repaint();
		this.getParent().getParent().repaint();
	}
	
	public void setCursorMode(String s)
	{
		cursorMode=s;
	}
	
	public void setFrame(int f)
	{
		if (f>=0)
		{
		frame=f;
		System.out.println("displaying frame: "+frame);
		repaint();
		}
	}
	
	public void setRow(int r)
	{	
		if (r>=0)
		{
		row=r;
		System.out.println("displaying row: "+row);
		repaint();
		}
	}
	
	public int getFrame()
	{
		return frame;
	}
	
	public int getRow()
	{
		return row;
	}
	
	public CKImageAsset getImageAsset()
	{
		return image;
	}
	
	public void setImageAsset(CKImageAsset asset)
	{
		image=asset;
		repaint();
	}
	
	/**fill algorithm
	 * using recursion
	 * 
	 * @param target      - the RGB color that we wish to change
	 * @param replacement - the Color that we wish to change it to 
	 * @param x           - x coord of pixel
	 * @param y           - y coord of pixel
	 * @param frame       - which frame of the tile to replace
	 */
	public void fill(int target, Color replacement,int x, int y,int frame)
	{
		
		if(		target==replacement.getRGB() ) //don't try to change it to what you have already changed it to)
		{
			//System.out.println("bad call to fill, target and replacement are the same");
			return;
			
		}
		
		//first check the bounds and if the position has already been altered 
		if(x>=this.image.getWidth(0) || y>=this.image.getHeight(0) 
				|| y<0 || x<0 //out of bounds
		)
		{
			//System.out.printf("Out of bounds point (%d,%d)\n",x,y);
			return;
		}

		
		if( this.image.getPixel(x,y,frame)!=target)  //is the pixel not the target
		{
			//System.out.printf("Pixel (%d,%d) is not target(%d,%d)\n",
			//		x,y,this.image.getPixel(x,y,frame),target);
			return;
			
		}
		
		
		
		//System.out.printf("coloring Point (%d,%d)\n",x,y);
		//draw self
		this.image.setPixel(x,y, frame, replacement);
		
	
		
		//draw neighbors
		this.fill(target,replacement,x-1,y,frame);
		this.fill(target,replacement,x,y-1,frame);
		this.fill(target,replacement,x+1,y,frame);
		this.fill(target,replacement,x,y+1,frame);
		
	}
	
	public void setBackground(Image i)
	{
			bg_image=i;
			repaint();
		
	}


}
