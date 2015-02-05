package ckSpriteAndTiles.src.src;
public class Sprite
{
/*current idea is to use number
 * to determine which unit is on square
 * for example: 0 for controllable character
 * 1 for enemy
 * 2 for obstacle (space uninhabitable)
 * but we will see
 */
        private int type;

        /*the default is currently
         * an obstacle because I believe
         * it will be used the most
         */
        public Sprite()
        {
                type=2;
        }

        /*allows creation of characters
         * or enemies, 0 being characters
         * and 1 being an enemy
         */
        public Sprite(int type)
        {
                this.type=type;
        }

        /*@return the type of Sprite
         */
        public int getType()
        {
                return type;
        }

        /*change the type of sprite
         * 0 being controllable
         * 1 being enemy
         */
        public void setType(int type)
        {
                this.type=type;
        }
}