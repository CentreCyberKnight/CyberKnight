package ckEditor.treegui;

import java.beans.BeanInfo;
import java.beans.DefaultPersistenceDelegate;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.XMLEncoder;
import ckSatisfies.TrueSatisfies;

/**
 * We should find a way to keep this class from writting any children nodes..
 * Quest can remove and then reattach this node every time it needs to write.
 */
public class CKHiddenNode extends CKGUINode
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6981493904587289329L;

	int id=10;
	
	 /**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id)
	{
		this.id = id;
	}

	static {
	        try {
	            //
	            // In this block will change the attribute type of the children
	            // to transient so it will be not serialized to xml when we use
	            // XMLEncode to convert the bean to xml persistence.
	            //
	            BeanInfo bi = Introspector.getBeanInfo(CKHiddenNode.class);
	            PropertyDescriptor[] pds = bi.getPropertyDescriptors();
	            for (int i = 0; i < pds.length; i++) {
	                PropertyDescriptor propertyDescriptor = pds[i];
	                //System.out.println(  propertyDescriptor.getName());
	                if (propertyDescriptor.getName().equals("children"))
	                {
	                	//System.out.println("setting the children transient");
	                    propertyDescriptor.setValue("transient", Boolean.TRUE);
	                }
	            }
	        } catch (IntrospectionException e) {
	            e.printStackTrace();
	        }
	    }
	/*
	
	 public Vector<Object> getChildren() {return children; }
	 public void setChildren(Vector<Object> obj) { children =obj; }
	 */
	//transient Vector<Object> children;
	
	public CKHiddenNode()
	{
		visible=false;
		this.allowsChildren=true;
		this.setChildRemoveable(false);
		
		
	}
	
	public void secretParent(CKGUINode p)
	{
		parent = p;
	}
	
	
	/*
	private void writeObject(java.io.ObjectOutputStream out)
		    throws IOException
	{
	        System.out.println("from Dog.writeObject: ");  
	        out.writeInt(10);  
	        out.writeInt(15);
	        
	    }  
	  
	    private void readObject(ObjectInputStream in) throws Exception 
	    {  
	    	
	    	
	   int n = (Integer) in.readInt();  
	        int dn = (Integer) in.readInt();
	        System.out.println("from Dog.readObject: ");
	       
	    }  
	*/
	
	public static void main(String[] args)
	{
		CKHiddenNode root = new CKHiddenNode();
		CKHiddenNode middle = new CKHiddenNode();
		
		middle.add(new TrueSatisfies());
		root.add(middle);
		root.add(new TrueSatisfies());
		
		XMLEncoder encoder = new XMLEncoder(System.out);
		
		 String[] propertyNames = new String[] { "id" };
		    encoder.setPersistenceDelegate(CKHiddenNode.class,
		        new DefaultPersistenceDelegate(propertyNames));
		    
		encoder.writeObject(root);
		encoder.close();
	}


	
	
}
