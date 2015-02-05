package ckEditor;

//http://www.java2s.com/Code/Java/Database-SQL-JDBC/DatabaseBrowser.htm
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import ckDatabase.CKConnection;

public class DatabaseBrowser extends JPanel implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Object objectToRetrieve;
	
  protected CKConnection connection;

  protected JComboBox catalogBox;

  protected JComboBox schemaBox;

  protected JComboBox tableBox;

  protected JTable table = new JTable();

  public static void main(String[] args) throws Exception {
 
    //DatabaseBrowser db = new DatabaseBrowser();
  }

  public DatabaseBrowser() throws Exception 
  {
    
    connection =new CKConnection();
   
    add(getSelectionPanel(), BorderLayout.NORTH);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    table.addMouseListener(this);
    refreshTable();
    add(new JScrollPane(table), BorderLayout.CENTER);
    JButton open=new JButton("Open");
    open.addActionListener(new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent arg0) {
			dispose();
			
		}
    	
    });
    add(open,BorderLayout.SOUTH);
    
  }

  protected JPanel getSelectionPanel() {
    JPanel panel = new JPanel();
    panel.add(new JLabel("Catalog"));
    panel.add(new JLabel("Schema"));
    panel.add(new JLabel("Table"));

    catalogBox = new JComboBox();
    populateCatalogBox();
    panel.add(catalogBox);
    schemaBox = new JComboBox();
    populateSchemaBox();
    panel.add(schemaBox);
    tableBox = new JComboBox();
    populateTableBox();
    panel.add(tableBox);

    catalogBox.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent event) {
        String newCatalog = (String) (catalogBox.getSelectedItem());
        try {
          CKConnection.getConnection().setCatalog(newCatalog);
        } catch (Exception e) {
        }
        populateSchemaBox();
        populateTableBox();
        refreshTable();
      }
    });

    schemaBox.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent event) {
        populateTableBox();
        refreshTable();
      }
    });

    tableBox.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent event) {
        refreshTable();
      }
    });
    return panel;
  }

  protected void populateCatalogBox() {
    try {
      DatabaseMetaData dmd = CKConnection.getConnection().getMetaData();
      ResultSet rset = dmd.getCatalogs();
      Vector<String> values = new Vector<String>();
      while (rset.next()) {
        values.addElement(rset.getString(1));
      }
      rset.close();
      catalogBox.setModel(new DefaultComboBoxModel(values));
      catalogBox.setSelectedItem(CKConnection.getConnection().getCatalog());
      catalogBox.setEnabled(values.size() > 0);
    } catch (Exception e) {
      catalogBox.setEnabled(false);
    }
  }

  protected void populateSchemaBox() {
    try {
      DatabaseMetaData dmd = CKConnection.getConnection().getMetaData();
      ResultSet rset = dmd.getSchemas();
      Vector<String> values = new Vector<String>();
      while (rset.next()) {
        values.addElement(rset.getString(1));
      }
      rset.close();
      schemaBox.setModel(new DefaultComboBoxModel(values));
      schemaBox.setEnabled(values.size() > 0);
    } catch (Exception e) {
      schemaBox.setEnabled(false);
    }
  }

  protected void populateTableBox() {
    try {
      String[] types = { "TABLE" };
      String catalog = CKConnection.getConnection().getCatalog();
      String schema = (String) (schemaBox.getSelectedItem());
      DatabaseMetaData dmd = CKConnection.getConnection().getMetaData();
      ResultSet rset = dmd.getTables(catalog, schema, null, types);
      Vector<String> values = new Vector<String>();
      while (rset.next()) {
        values.addElement(rset.getString(3));
      }
      rset.close();
      tableBox.setModel(new DefaultComboBoxModel(values));
      tableBox.setEnabled(values.size() > 0);
    } catch (Exception e) {
      tableBox.setEnabled(false);
    }
  }

  protected void refreshTable() {
    String schema = (schemaBox.isEnabled() ? schemaBox.getSelectedItem().toString() : null);
    String tableName = (String) tableBox.getSelectedItem();
    if (tableName == null) {
      table.setModel(new DefaultTableModel());
      return;
    }
    String selectTable = (schema == null ? "" : schema + ".") + tableName;
    if (selectTable.indexOf(' ') > 0) {
      selectTable = "\"" + selectTable + "\"";
    }
    try {
      Statement stmt = CKConnection.getConnection().createStatement();
      ResultSet rset = stmt.executeQuery("SELECT * FROM " + selectTable);
      table.setModel(new ResultSetTableModel(rset));
    } catch (Exception e) {
    }
  }

@Override
public void mouseClicked(MouseEvent e) {
		int r=table.getSelectedRow();
		int aid=(Integer) table.getValueAt(r, 0);//we always want col 0
		//objectToRetrieve=CKGraphicsLayerFactoryDB.getGraphicsLayerFromDB(aid);		
		
		//MKB objectToRetrieve=(new CKGraphicsAssetFactoryDB()).getGraphicsAssetFromDB(aid);
		objectToRetrieve = null; 
		assert(false);
}

@Override
public void mouseEntered(MouseEvent e) {}

@Override
public void mouseExited(MouseEvent e) {}

@Override
public void mousePressed(MouseEvent e) {}

@Override
public void mouseReleased(MouseEvent e) {}


public Object getRetrievedObject()
{
	return objectToRetrieve;
}

public void dispose()
{
	SwingUtilities.getWindowAncestor(this).dispose();
}
}

class ResultSetTableModel extends AbstractTableModel {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

protected Vector<String> columnHeaders;

  protected Vector<Vector<Object>> tableData;

  public ResultSetTableModel(ResultSet rset) throws SQLException {
    Vector<Object> rowData;
    ResultSetMetaData rsmd = rset.getMetaData();
    int count = rsmd.getColumnCount();
    columnHeaders = new Vector<String>(count);
    tableData = new Vector<Vector<Object>>();
    for (int i = 1; i <= count; i++) {
      columnHeaders.addElement(rsmd.getColumnName(i));
    }
    while (rset.next()) {
      rowData = new Vector<Object>(count);
      for (int i = 1; i <= count; i++) {
        rowData.addElement(rset.getObject(i));
      }
      tableData.addElement(rowData);
    }
  }

  public int getColumnCount() {
    return columnHeaders.size();
  }

  public int getRowCount() {
    return tableData.size();
  }

  public Object getValueAt(int row, int column) {
    Vector<Object> rowData = (Vector<Object>) (tableData.elementAt(row));
    return rowData.elementAt(column);
  }

  public boolean isCellEditable(int row, int column) {
    return false;
  }

  public String getColumnName(int column) {
    return (String) (columnHeaders.elementAt(column));
  }

}