package ckDatabase;
import static ckCommonUtils.CKDatabaseTools.DBAT;
import static ckCommonUtils.CKDatabaseTools.DBCascade;
import static ckCommonUtils.CKDatabaseTools.DBcreateTableHeader;
import static ckCommonUtils.CKDatabaseTools.DBmakeCHARVARType;
import static ckCommonUtils.CKDatabaseTools.DBmakeFKIdType;
import static ckCommonUtils.CKDatabaseTools.DBmakePKIdType;
import static ckCommonUtils.CKDatabaseTools.DBmakeTextType;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.python.core.PyException;

import ckPythonInterpreterTest.CKExceptionFrame;

/**
 * 
 * @author Scott Gilday
 *
 */
public class CKExceptionFrameFactoryDB
{
	///static private String LT_TYPE = "graphics_layer_type";
	static public  String PEDBT = "python_error";
	static public String PCDBT = "python_error_cases";
	
	private Connection connection;
	
	/**
	 * Populates the database with required data,  Should only be called 
	 * right after of tables.
	 */
	
	
	public Connection getConnection()
	{
		return connection;
	}
	
	
	
	/**
	 * Returns the layer stored in the database with id lid.
	 * Layers are not unique, so there is no need to keep them unique.
	 * @param aid
	 * @return
	 */
	public CKExceptionFrame getExceptionFrame(PyException e)
	{
		//Connection conn = getConnection();	
		// TODO  finish this
		return null;
	}
	
	/*
	 * Make test database
	 */
	
	

	static public void createTables(Statement stmt)
	{
		String pythonException = DBcreateTableHeader(PEDBT)+
			DBmakePKIdType("error_id",",")+
			DBmakeFKIdType("asset_id",DBAT,",")+
			DBmakeCHARVARType("error_name",256,true,",")+
			DBmakeTextType("error_description",true,");");
		
		String pythonCase = DBcreateTableHeader(PCDBT)+
			DBmakePKIdType("case_id",",")+
			DBmakeFKIdType("error_id",DBAT,DBCascade()+",")+
			DBmakeTextType("case_code",true,",");
			DBmakeTextType("case_description",true,");");	
		
		try
		{
			stmt.executeUpdate(pythonException);
			stmt.executeUpdate(pythonCase);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		
	}
	
	

		

	
	public CKExceptionFrameFactoryDB()
	{
		this(null);
	}

	public CKExceptionFrameFactoryDB(Connection c)
	{
		super();
		if(c == null)
		{ //connect to test database
			connection = CKConnection.getConnection();
		}
		else
		{
			connection=c;
		}
	}


	
	
	/*
	
	public static void addLayerToSceneDB(int lid,int sid, Statement stmt)
	{
		String ins = "INSERT INTO "+DBST_L+" " +
				"(layer_id, scene_id)"+
				"VALUES ("+lid+","+sid+");";
		
		try
		{
			
			//Statement stmt=conn.createStatement();
			stmt.executeUpdate(ins);
		}
		catch(SQLException e)
		{//I could use this to do an update if there is an error?
			e.printStackTrace();
		}			
	}


	public static void removeLayersFromGraphics(int sid, Statement stmt)
	{
		String ins = "DELETE FROM "+DBST_L+" WHERE scene_id="+sid+");";

		try
		{	
			stmt.executeUpdate(ins);
		}
		catch(SQLException e)
		{//I could use this to do an update if there is an error?
			e.printStackTrace();
		}			
	
	
	}
	
	
	public Iterator<CKGraphicsSceneInterface> getAllGraphicsLayers()
	{
		
		String q="SELECT scene_id FROM "+DBST+";";
		
		println(q);
		Connection conn = getConnection();
		try
		{
			Statement stmt= conn.createStatement();
			ResultSet set = stmt.executeQuery(q);
			return new CKGraphicsSceneResultSetIterator(set,this);
			
		} catch (SQLException e)
		{
			e.printStackTrace();
		} 
		
		return null;
	}
	*/
	
}
