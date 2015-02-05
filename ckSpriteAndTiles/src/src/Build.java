package ckSpriteAndTiles.src.src;
//import java.lang.reflect.Array;


public class Build
{
        private int width;
        private int height;
        /*this will hold all the tiles in a list.
         * to understand how they will be placed on the
         * board one must do some math.
         * take the location of the tile in the list
         * (so tile 6 is at location 5)
         * Divide location by height of board and note
         * the remainder. Your location is determined
         * by these numbers. Remainder shows how far up you
         * are in height 0 being the top and 1 being the bottom
         * 2 being the second row, ect. The Other number
         * shows how far to the right you are.
         * 0 is far left and it progresses in a linear way.
         * You can tell if you are at the far right by
         * seeing if The number is equal to the width. if so,
         * you are on the far right side of the board.
         */
        private Tile tList[];//=new Tile[100];

public Build()
{
        /* This constructs a 4 by 4 board
         * places tiles inside tList with bBoard() function
         */
        width=4;
        height=4;
        tList=new Tile[width*height];
        bBoard();
        //tList=bBoard();
        //Tile tList[]=new Tile[(width*height)-1];

}

/* This allows users to construct a board of their own
 * desired size.
 * @ param width = how wide the board is
 * @ param height = how tall the board is
 * places tiles inside tList with bBoard() function
 */

public Build(int width, int height)
{
        this.width=width;
        this.height=height;
        tList=new Tile[width*height];
        bBoard();
        //Tile tList[]=new Tile[(width*height)-1];
        //tList=bBoard();

}

/*
 * places a tile within an array. The tiles
 * are numbered from bottom to top, left to right
 * @called by Constructor
 * @called by user constructor
 */
public void bBoard()
{
        int tot=(width)*(height);
        for(int i=0; i<tot; ++i)
        {
                Tile temp= new Tile();
                //Tile tList[] = null;
                tList[i]=temp;
        }
        //return tList;
}

public Tile getTile(int loc)
{
        return tList[loc];
}

/*
 * This function calculates how far to the right
 * the grid is (0 is all the way to the left)
 * @Called by isLeft() and isRight()
 * @param loc - location of the item to find location of
 * @param hi - height of the board
 */
private double mathInt(int loc, int hi)
{
        double temp=loc/hi;
        Math.floor(temp);
        return temp;
}

/**
 * @called no longer at all
 * @param loc - location of the item to find location of
 * @param hi - height of the board
 * @return 0 is the very top, 1 is the very bottom, 2 is
 * one up from the bottom, ect.
 */
/*private int mathRem(int loc, int hi)
{
        int temp=hi%loc;
        temp=hi-temp;
        return temp;
}*/

/*
 * this checks to see if the location the user has entered
 * is within the possible range based on the grid
 * @returns false if it is within paramaters, true if it is not.
 * @Called by isUp(); isDown(); isLeft(); isRight()
 */
private boolean inLoc(int loc)
{
        int wid=getWidth();
        int hi=getHeight();
        if ((wid*hi)>loc)
        {return true;}
        else
        {return false;}
}
/*
 * tests to see if the height is only one
 * called by isUp() and isDown()
 */
private boolean isShort()
{
        int test=getHeight();
        if (test == 1)
        {return true;}
        else
        {return false;}
}

/*
 * tests to see if the width is only one
 * called by isLeft() and isRight()
 */
private boolean isThin()
{
        int test=getWidth();
        if (test == 1)
        {return true;}
        else
        {return false;}
}

/* This checks to see if it is possible to move left one tile
 * first calls checkLoc() then checks to see if it is the
 * tile is at the far left of the grid
 * after checks the tile one row over
 */
public boolean isLeft(int loc)
{
        int hi=getHeight();
        if (isThin())
        {return false;}
        if (inLoc(loc)!=true)
        {return false;}
        double test=mathInt(loc,hi);
        if (test<1)
        {return false;}
        if(tList[loc-hi].isEmpty()!=true)
        {return false;}
        else
        {return true;}
}
/* This checks to see if it is possible to move right one tile
 * first calls checkLoc() then checks to see if it is the
 * tile is at the far right of the grid
 * after checks the tile one row over
 */
public boolean isRight(int loc)
{
        int wid=getWidth();
        int hi=getHeight();
        if (isThin())
        {return false;}
        if (inLoc(loc)!=true)
        {return false;}
        double test=mathInt(loc,hi);
        //checks to see if farthest right row
        if((wid-1)==test)
        {return false;}
        if(tList[loc+hi].isEmpty())
        {return true;}
        else
        {return false;}
}
/* This checks to see if it is possible to move down one tile
 * first calls checkLoc() then checks to see if it is the
 * tile is at the bottom of the grid
 * after checks the tile one tile down
 */
public boolean isDown(int loc)
{
        int hi=getHeight();
        if (isShort())
        {return false;}
        if (inLoc(loc)!=true)
        {return false;}
        if ((loc-1)==-1)
        {return false;}
        if (loc%hi==0)
        {return false;}
        if(tList[loc-1].isEmpty())
        {return true;}
        else
        {return false;}
}
/*
 * This checks to see if it is possible to move up one tile
 * first calls checkLoc() then checks to see if it is the
 * tile is at the top of the grid
 * after checks the tile one above
 */
public boolean isUp(int loc)
{
        int hi=getHeight();
        int wid=getWidth();
        if (isShort())
        {return false;}
        //I may just put inLoc, isShort and wid*hi and loc==
        if (inLoc(loc)!=true)
        {return false;}
        if (((wid*hi)-1)==loc)
        {return false;}
        if ((loc+1)%hi==0)
        {return false;}
        if(tList[loc+1].isEmpty())
        {return true;}
        else
        {return false;}
}

/*
 * returns width of board
 */
public int getWidth()
{
        return width;
}

/*
 * returns height of board
 */
public int getHeight()
{
        return height;
}

/* sets the width of the board
 *
 */
public void setWidth(int width)
{
        this.width=width;
}

/*sets the height of the board
 *
 */
public void setHeight(int height)
{
        this.height=height;
}

}
