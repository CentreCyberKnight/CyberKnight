package ckDatabase;

import static ckCommonUtils.CKDatabaseTools.DBAT;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.h2.tools.TriggerAdapter;
//import org.h2.api.Trigger;

public class CKDeleteAssetTrigger extends TriggerAdapter
{

	@Override
	public void fire(Connection conn, ResultSet oldRow,
			ResultSet newRow)
			throws SQLException
	{
		
		if(oldRow.next())
		{
			int aid = oldRow.getInt("asset_id");
			PreparedStatement prep = conn.prepareStatement("DELETE FROM "+DBAT+" WHERE asset_id=?;");
			prep.setInt(1, aid);
			prep.execute();
		}

	}

}
