package ckCommonUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * This class is user to store static functions that are useful in creating and using databases associated with Cyberknight
 * to use this in another class place this at the top of your java file
 * 
 * import static ckCommonUtils.CKDatabaseTools.*;
 * @author bradshaw
 *
 */
final public class CKDatabaseTools
{
	final public static String getTableStringField(String table,
			int id,String idField,
			String field,Statement stmt) throws SQLException
	{
		String Q = "SELECT "+field+" FROM "+table+" " +
				"WHERE "+idField+"="+id+";";
		ResultSet set=stmt.executeQuery(Q);
		if(set.next())
		{
			return set.getString(field);
		}
		return null;
	}
	
	
	final public static void setTableStringField(
			String table,String value,
			int id,String idField,
			String field,Statement stmt) throws SQLException
	{
		String Q = "UPDATE "+table+ 
		" SET "+field+"=\""+value+
		"\" WHERE "+idField+"="+id+";";
	
		stmt.executeUpdate(Q);
	}
	
	final static public boolean hasTable(String table,Statement stmt)
	{
		String t = table.toUpperCase();
		String q =// "SHOW TABLES;";
		"SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '";
		q += t+"';";
		ResultSet set;
		try
		{
			set = stmt.executeQuery(q);
			while(set.next())
			{
				String tbname = set.getString("TABLE_NAME"); 
				//System.out.println("Table is "+tbname);
				if( tbname.equalsIgnoreCase(t)) return true;
			} 
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	static public String DBcreateTableHeader(String t)
	{
		return "DROP TABLE IF EXISTS "+t+";" +
				" CREATE TABLE "+t+"(";
	}
	
	
	
	static public String DBmakeIdType(String name,String end)
	{
		return name+" INTEGER "+DBnn(" AUTO_INCREMENT "+end);
	}

	static public String DBmakePKIdType(String name,String end)
	{
		return name+" INTEGER AUTO_INCREMENT PRIMARY KEY"+end;
	}
	
	
	static public String DBmakeSharedPKIdType(String name,String end)
	{
		return name+" INTEGER PRIMARY KEY"+end;
	}
	
	static public String DBmakeFKIdType(String name,String references, String end) 
	{
		return name+" INTEGER NOT NULL REFERENCES "+references+end+"\n";
	}

	static private String DBmakeType(String name,boolean notnull,String end,String typeName)
	{
		String res = name+ " "+typeName;
		if(notnull) { res+=DBnn(end);}
		else { res +=end;}
		return res;
	}
	
	
	static public String DBmakeIntType(String name, boolean notnull,String end)
	{ 
		return DBmakeType(name,notnull,end,"INTEGER");
	}

	static public String DBmakeDoubleType(String name, boolean notnull,String end)
	{ 
		return DBmakeType(name,notnull,end,"DOUBLE PRECISION");
	}
	

	static public String DBmakeBooleanType(String name, boolean notnull,String end)
	{ 
		return DBmakeType(name,notnull,end,"BOOLEAN");
	}
	
	
	static public String DBmakeCHARVARType(String name,int len, boolean notnull ,String end)
	{
		String res = name+ " VARCHAR("+len+")";
		if(notnull) { res+=DBnn(end);}
		else { res +=end;}
			//println("DBmakeIntType"+res);
		return res;
			
	}
	
	static public String DBmakeTextType(String name, boolean notnull ,String end)
	{
		String res = name+ " TEXT";
		if(notnull) { res+=DBnn(end);}
		else { res +=end;}
			//println("DBmakeIntType"+res);
		return res;
			
	}
	
	
	
	final static public String DBCascade() {return " ON DELETE CASCADE";}
	final static public String DBnn(String end) { return " NOT NULL"+end+"\n"; }
	static public String DBpk(String name,String end) { return "PRIMARY KEY("+name+")"+end+"\n"; }
	static public String DBfk(String name,String references, String end) 
	{ 
		return "FOREIGN KEY("+name+") REFERENCES "+references+end+"\n";
	}
	static public String DBAT = "graphics_asset";
	
	//todo call these from classes
	static public String NULLA= "CKNullAsset";
	static public String IMAGET = "gk_g_asset_image";
	static public String IMAGEA= "CKImageAsset";
	
	
}
