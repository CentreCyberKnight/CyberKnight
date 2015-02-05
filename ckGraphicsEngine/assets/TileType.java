/**
 * 
 */
package ckGraphicsEngine.assets;



/**
 * @author dragonlord
 *
 */

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;


public enum TileType {SUB("SUB"),
			BASE("BASE"),OVER("OVER"),SPRITE("SPRITE"),
			HIGHLIGHT("HIGHTLIGHT"),NONE("NONE");
		

	private final String name;
		//need to add data to these to make print pretty and recognize themselves.
		TileType(String name) {this.name=name;}
		public String toString(){ return this.name;}


		static public TileType getTileType(String name)
		{
			for (TileType t:TileType.values())
			{
				if(t.toString().equals(name))
				{
					return t;
				}
			}
			return TileType.NONE;
		}
		
		
		

	    public static void main( String[] args ) {
	        ByteArrayOutputStream stream = new ByteArrayOutputStream();

	        XMLEncoder encoder = new XMLEncoder( stream );
	        encoder.writeObject( TileType.SPRITE );
	        encoder.writeObject( TileType.OVER );
	        encoder.close();

	        System.out.println( stream );
	    }
}

		
	
/*
public enum TileType {SUB("SUB"),
		BASE("BASE"),OVER("OVER"),SPRITE("SPRITE"),
		HIGHLIGHT("HIGHTLIGHT"),NONE("NONE");
	

private final String name;
	//need to add data to these to make print pretty and recognize themselves.
	TileType(String name) {this.name=name;}
	public String toString(){ return this.name;}

	/**
	 * gets the tiletype that has the string representation of name, NONE otherwise
	 * @param name string represeting the tiletype
	 * @return tiletype for the string
	 */

/*

}*/
