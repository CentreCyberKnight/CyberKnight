package ckSpriteAndTiles.src.src;
public class Tile
{


        private boolean empty;
        private int width;
        private int height;

        /*
         * default constructor
         * sets tile to empty, pixle width to 32 and pixel
         * height to 16 (theoretically standard but
         * will need to look at more when GUI introducted
         */
        public Tile()
        {
        empty=true;
        width=32;
        height=16;
        }

        /*user constructor
         * should only be needed once
         * GUI introduced
         */
        public Tile(int width, int height)
        {
                empty=true;
                setWidth(width);
                setHeight(height);
        }

        /*
         * returns pixel height of tile
         */
        public int getHeight()
        {
                return height;
        }

        /*
         * Returns false if
         * the object is NOT EMPTY
         */
        public boolean isEmpty()
        {
                return empty;
        }

        /*
         * returns pixel width of tile
         */
        public int getWidth()
        {
                return width;
        }

        /*set pixel height
         *
         */
        public void setHeight(int height)
        {
                this.height=height;
        }

        /*
         * false if
         * the object is NOT EMPTY
         * sets this variable
         */
        public void setEmpty(boolean empty)
        {
                this.empty=empty;
        }
        /*
         * set width
         */
        public void setWidth(int width)
        {
                this.width=width;
        }
}