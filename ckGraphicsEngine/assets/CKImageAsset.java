package ckGraphicsEngine.assets;

import static ckGraphicsEngine.CKGraphicsConstants.BASE_HEIGHT;
import static ckGraphicsEngine.CKGraphicsConstants.BASE_WIDTH;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import ckCommonUtils.CKURL;
import ckDatabase.CKGraphicsAssetFactoryXML;





/**
 * Represents the animated images in the CK system that are used in the game system.  Stores an image for each row/frame pair.
 * @author bradshaw
 * Will need to divide this into a lightweight version for the running game
 */
public class CKImageAsset extends CKGraphicsAsset
{
	

	private int width; //might be different from image size
	private int height;
	private int frames; //how many tiles are in this file
	private int rows;   //how many rows are stored in this asset
	
	private int x_offset;
	private int y_offset;
	
	private TileType drawType;
	private String filename;
	
	private BufferedImage image;
	private javafx.scene.image.Image fxImage;
	
	private String license="Please choose";
	private String contributer="must update";
	private String url_source = "good to have";
	
	
	/**
	 * This is here for XMLEncode/XMLDecode
	 */
	public CKImageAsset()
	{
		this("blank",10,10,1,1,TileType.NONE);
	}
			
	

	/**
	 * creates an ImageAsset 
	 */
	public CKImageAsset(String aid,String desc,
			int frames,int rows,TileType ty,String f)
	{
		this(aid,desc,10,10,frames,rows,ty,f);
	}

	/**
	 * creates an ImageAsset 
	 * @param width   width of image
	 * @param height  height of image
	 */
	public CKImageAsset(String aid,String desc,int width,int height,
			int frames,int rows,TileType ty,String f)
	{
		super(aid,desc);
		this.width=width;
		this.height=height;
		this.frames=frames;
		this.rows=rows;
		drawType=ty;
		filename=f;
		setDirty();

//		image = new BufferedImage(width*frames,height*rows,BufferedImage.TYPE_INT_ARGB);
		readImage();
		calculateOffsets();
	}


	
	public CKImageAsset(String desc, int width,int height,int frames,int rows,TileType ty)
	{
		this("",desc,width,height,frames,rows,ty,"");
	}
	
	protected CKImageAsset(BufferedImage img,String aid,String desc,
			int width,int height,
			int frames,int rows,TileType ty,
			String fname)
	{
		super(aid,desc);
	    image=img;
		filename = fname;		
		this.width=width;
		this.height=height;
		drawType=ty;
		this.frames=frames;
		this.rows = rows;

		setDirty();
		calculateOffsets();
	}

	
		
	/**
	 * calculates the offsets based on tile width and tile type
	 * @return
	 */
	private void calculateOffsets()
	{
		x_offset = (BASE_WIDTH/2)-(width/2);
		y_offset =0;
		switch (drawType)
		{
		case SUB:
			y_offset = (BASE_HEIGHT/2);
			break;
		case HIGHLIGHT:		
		case OVER:
			y_offset =BASE_HEIGHT - height;
			break;
		case SPRITE:
			y_offset =(3*(BASE_HEIGHT/4)) - height;
			break;
		case BASE:
		
			y_offset = 0;
			break;
		default:
//			System.err.println("Unsupported Tile Type");
//			System.exit(0);
		}
		//System.out.println(getAID()+"  offsets are "+x_offset+", "+y_offset+" for "+width+","+height);
				
	}
	
	
	//FIXME FX stuff reading image in both formats for now.
	protected void readImage()
	{
		//super(width*frames,height,BufferedImage.TYPE_4BYTE_ABGR);
		if(filename.length()>0)
		{
		CKURL url;
		try
		{
			url = new CKURL(filename);

			image= ImageIO.read(url.getInputStream());
			//lets set the width and height to something appropriate?
			height = getRawImageHeight()/rows;
			width = getRawImageWidth()/frames;
			
			//FX fix
			fxImage =new  javafx.scene.image.Image(url.getInputStream());
			
			calculateOffsets();
			
			return;
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
		}
		}
		image = new BufferedImage(width*frames,height*rows,BufferedImage.TYPE_INT_ARGB);
		fxImage = new WritableImage(width*frames,height*rows);
		
		//now fill in the tile with something...
		int val = Color.black.getRGB();
		for (int i=0;i<width;i++)
			for(int j=0;j<height;j++)
			{
				image.setRGB(i,j,val);
			}
		calculateOffsets();
		
	}

	
	public void store(Boolean  force) throws IOException
	{
		if(force || isDirty())
		{
			//FIXME  need unique file name if empty
			CKURL url = new CKURL(filename);
			if( ! ImageIO.write((BufferedImage)image, "png", url.getOutputStream() ) )
				{		
				throw new IOException("unable to store Image to disk");
				}	
		}
	}
	
	public int getPixel(int x, int y, int frame)
	{
		int pos = frame %frames;
		int calcx = x+width*pos;
		return image.getRGB(calcx, y);		
	}
	
	public void setPixel(int x, int y,int frame,int color)
	{
	    setDirty();
		int pos = frame %frames;
		int calcx = x + width*pos;
		image.setRGB(calcx,y,color);
	}
	
	public void setPixel(int x, int y, int frame, Color color)
	{
		int alpha =color.getAlpha();
		int int_color = color.getRGB();
	    int newcolor = int_color | (alpha & 0xff000000);
	    setPixel(x,y,frame,newcolor);
	}

	
	public TileType getTileType()
	{
		return drawType;
	}
	
	
	@Override
	public void getDrawBounds(int frame, int row, Point off, Point bounds)
	{
		off.x=x_offset;
		off.y=y_offset;
		bounds.x=off.x+width;
		bounds.y=off.y+height;
		
	}

	
	/**
	 * draws the graphics to the screen based on the frame number of the animation. 
	 * @param g  graphics to draw the tile to.
	 * @param x  location to place tile
	 * @param y  location to place tile
	 * @param row which row to display
	 * @param frame  what frame is the game in, if there is more than one tile this will tell us which tile to find
	 * @param observer
	 */
	public void drawToGraphics(Graphics g,int x,int y, int frame,int row,ImageObserver observer)
	{
		int pos = frame %frames;
		int calcx = width*pos;
		int calcy=height *(row %rows);
		x = x +x_offset;
		y = y +y_offset;
		g.drawImage((Image)image, x,y,x+width,y+height,
				calcx,calcy,calcx+width,calcy+height,observer);
		//System.out.println("tiles at "+x+","+y);
	}

	
	/**
	 * draws the graphics to the screen based on the frame number of the animation. 
	 * @param g  graphics to draw the tile to.
	 * @param x  location to place tile
	 * @param y  location to place tile
	 * @param row which row to display
	 * @param frame  what frame is the game in, if there is more than one tile this will tell us which tile to find
	 * @param observer
	 */
	public void drawToGraphics(GraphicsContext g,int x,int y, int frame,int row,ImageObserver observer)
	{
		int pos = frame %frames;
		int calcx = width*pos;
		int calcy=height *(row %rows);
		x = x +x_offset;
		y = y +y_offset;
		g.drawImage(fxImage,
				calcx,calcy,width,height,
				
				 x, y,width,height
				);
		
		//System.out.println("tiles at "+x+","+y);
	}

	
	
	
	
	

	@Override
	public void drawPreviewToGraphics(Graphics g, int screenx, int screeny,
			ImageObserver observer)
	{
		g.drawImage((Image)image, screenx,screeny,observer);
	}
	
	@Override
	public void drawPreviewToGraphics(GraphicsContext g, int screenx, int screeny,
			ImageObserver observer)
	{
		g.drawImage(fxImage, screenx,screeny);
	}

	public void scaleToGraphics(Graphics g,int x, int y, int frame, int row, ImageObserver observer, double scale)
	{
		int pos = frame %frames;
		int calcx = width*pos;
		int calcy=height *(row %rows);
		x = x +x_offset;
		y = y +y_offset;
	
		
		g.drawImage((Image)image,
              x, y,(int)( x+width*scale), (int)(y+height*scale),     /* dst rectangle */
               calcx,calcy, calcx+width,calcy+height, /* src area of image */ 
                observer);
		
	}

	@Override
	public void drawPreviewRowToGraphics(Graphics g, int x, int y,
			int row, ImageObserver observer)
	{
		int true_width = width*frames;
		int calcy=height *(row %rows);
		g.drawImage((Image)image, x,y,x+true_width,y+height,
				0,calcy,true_width,calcy+height,observer);
	}


	@Override
	public void drawPreviewFrameToGraphics(Graphics g, int x, int y,
			int frame, ImageObserver observer)
	{
		int pos = frame %frames;
		int calcx = width*pos;
		int true_height=height *rows;
		g.drawImage((Image)image, x,y,x+width,y+true_height,
				calcx,0,calcx+width,true_height,observer);
	}
	
	@Override
	public void drawPreviewRowToGraphics(GraphicsContext g, int x, int y,
			int row, ImageObserver observer)
	{
		int true_width = width*frames;
		int calcy=height *(row %rows);
		g.drawImage(fxImage,0,calcy,true_width,height,
				x,y,true_width,height
				);
	}


	@Override
	public void drawPreviewFrameToGraphics(GraphicsContext g, int x, int y,
			int frame, ImageObserver observer)
	{
		int pos = frame %frames;
		int calcx = width*pos;
		int true_height=height *rows;
		g.drawImage(fxImage, 
				calcx,0,width,true_height,
				x,y,width,true_height);
	}
	
	
	
	public int getRGB(int x, int y)
	{
		return image.getRGB(x,y);
	}
	
	public CKGraphicsAsset createShallowCopy()
	{
			
		CKImageAsset ret = new CKImageAsset(getDescription(),width,height,
				                               frames,rows,drawType);
	
		//need a new fileName...
		ret.filename = "Copy-Of-"+filename;
		//need to make a copy of image
		Graphics g = ret.image.getGraphics();
		g.drawImage(image, 0, 0, width*frames, height*rows,
				 0, 0, width*frames, height*rows,null);

			
		return ret;
	}


	public static void main(String[] args)
	{
		//CKImageAsset A1 = //new CKImageAsset( "person","a1",6,4,TileType.SPRITE,  "images/big_person.png");
		CKGraphicsAsset A1=CKGraphicsAssetFactoryXML.getInstance().getGraphicsAsset("hero");	
	
		JFrame frame = new JFrame();

		CKAssetViewer view=new CKAssetViewer(1,A1,null,true);
		frame.add(view);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		
		  XMLEncoder encoder = new XMLEncoder( stream );
	        encoder.writeObject( TileType.SPRITE );
	        encoder.writeObject( TileType.OVER );
	        encoder.writeObject(A1);
	        encoder.close();

	        System.out.println( stream );
		
		
		
	//		factory.writeAssetToXMLDirectory(A1);
		
	
/*
		
		try
		{
//			water.store(true);

	      

		
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	*/	
		
		
		
	}


/*
 * Setters and getters	
 */
	

	@Override
	public int getFrames(int row)
	{
		return frames;
	}


	/**
	 * @return the license
	 */
	public String getLicense()
	{
		return license;
	}



	/**
	 * @param license the license to set
	 */
	public void setLicense(String license)
	{
		this.license = license;
	}



	/**
	 * @return the contributer
	 */
	public String getContributer()
	{
		return contributer;
	}



	/**
	 * @param contributer the contributer to set
	 */
	public void setContributer(String contributer)
	{
		this.contributer = contributer;
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



	@Override
	public int getRows()
	{
		return rows;
	}


	@Override
	public int getHeight(int row)
	{
		return height;
	}


	@Override
	public int getWidth(int row)
	{
		return width;
	}

	
	
	/**
	 * @return the width
	 */
	public int getWidth()
	{
		return width;
	}


	/**
	 * @param width the width to set
	 */
/*
	public void setWidth(int width)
	{
	//	this.width = width;
		calculateOffsets();
	}
*/

	/**
	 * @return the height
	 */
	public int getHeight()
	{
		return height;
	}


	/**
	 * @param height the height to set
	 */
	/*
	public void setHeight(int height)
	{
		//this.height = height;
		calculateOffsets();
	}
*?

	/**
	 * @return the frames
	 */
	public int getFrames()
	{
		return frames;
	}


	/**
	 * @param frames the frames to set
	 */
	public void setFrames(int frames)
	{
		this.frames = frames;
		width = getRawImageWidth()/frames;
		calculateOffsets();
	}


	/**
	 * @return the drawType
	 */
	public TileType getDrawType()
	{
		return drawType;
	}


	/**
	 * @param drawType the drawType to set
	 */
	public void setDrawType(TileType drawType)
	{
		this.drawType = drawType;
		calculateOffsets();
	}


	/**
	 * @return the filename
	 */
	public String getFilename()
	{
		return filename;
	}


	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename)
	{
		this.filename = filename;
		readImage();
	}


	/**
	 * @param rows the rows to set
	 */
	public void setRows(int rows)
	{
		this.rows = rows;
		height = getRawImageHeight()/rows;
		calculateOffsets();
	}
	
	public int getRawImageWidth()
	{
		return image.getWidth();
	}

	public int getRawImageHeight()
	{
		return image.getHeight();
	}

	

	/*
	 * MouseMap functions    
	 */

	

	public static boolean isBaseUpperLeft(int x,int y,int width,int height)
    {
        return(width*y+height*x<height*width/2);
    }
    
    public static boolean isBaseUpperRight(int x,int y,int width,int height)
    {
        return(width*y+height*(width-x)<height*width/2);
    }
    
    public static boolean isBaseLowerLeft(int x,int y,int width,int height)
    {
        return(width*(height-y)+height*x<height*width/2);
    }
    
    public static boolean isBaseLowerRight(int x,int y,int width,int height)
    {
        return(width*(height-y)+height*(width-x)<height*width/2);
    }
    

    public static CKImageAsset createMouseMap(int width,int height,String fname)
    {
        CKImageAsset img = new CKImageAsset("MouseMap",width,height,1,1,TileType.BASE);
        img.filename=fname;
        for(int y=0;y<height;y++)
            for(int x=0;x<width;x++)
            {
                if(CKImageAsset.isBaseUpperLeft(x,y,width,height))
                { //upper left
                    img.setPixel(x,y,0,Color.red.getRGB());
                }
                else if(CKImageAsset.isBaseUpperRight(x,y,width,height))
                {//upper right
                    img.setPixel(x,y,0,Color.green.getRGB());                   
                }
                else if(CKImageAsset.isBaseLowerLeft(x,y,width,height))
                { //lower left
                    img.setPixel(x,y,0,Color.blue.getRGB());
                }
                else if(CKImageAsset.isBaseLowerRight(x,y,width,height))
                {//lower right
                    img.setPixel(x,y,0,Color.yellow.getRGB());                  
                }
                else
                {//center
                    img.setPixel(x,y,0,Color.white.getRGB());                   
                }
            }
         return img;
    }
		
	
	
	
	
}
