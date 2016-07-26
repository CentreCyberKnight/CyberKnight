package ckPythonInterpreter;


import prefuse.data.Edge;
import prefuse.data.Node;
import algviz.languages.Language;
import algviz.renderers.RendererValueUpdater;
import algviz.shadow.ShadowState;
import algviz.visualizer.StateGraphView;
import edu.vt.cs.algviz.xml.Field;
import edu.vt.cs.algviz.xml.FieldType;
import edu.vt.cs.algviz.xml.Memalloc;
import edu.vt.cs.algviz.xml.Memfree;
import edu.vt.cs.algviz.xml.Pointerto;
//import edu.vt.cs.algviz.xml.Message;
import edu.vt.cs.algviz.xml.Putfield;
import edu.vt.cs.algviz.xml.Showas;
import edu.vt.cs.algviz.xml.Typedef;
import edu.vt.cs.algviz.xml.types.MemallocTypeType;


public class CKAlgViz extends StateGraphView
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -538499277787402923L;
	ShadowState shadow;
	
	

	public CKAlgViz(int size)
	{
		super(size);
		//TODO need to create a python class to do formatting, for now use java 
		//Language.Settings.selectLanguage("Python");
		Language.Settings.selectLanguage("Java");
		shadow = new ShadowState();
		loadGraph(shadow.getGraph());
		// Register the Layout Manager
        shadow.addChangeListener(getLayoutManager());

        shadow.addChangeListener(new ShadowState.ChangeAdapter() 
        {
            public void nodeChanged(Node node, int offset)
            {
            	System.out.println("Node Changed\n");
                RendererValueUpdater.updateRenderer(node);
            }

            public void nodeAdded(Node node) 
            {
            	System.out.println("Node Added\n");
                RendererValueUpdater.updateRenderer(node);
            }

            public void edgeAdded(Edge edge) 
            {
            	System.out.println("EdgeAdded\n");
                // XXX place code from algviz.shadow.ShadowState.updateOutgoingEdge here
            }
        });
		
		
	}

public String getFieldType(FieldType ft)
{
    if (ft.getInt8() != null)
        return "int 8";

    if (ft.getUint8() != null)
        return "Unsigned int 8";

    if (ft.getInt16() != null)
        return "int 16";

    if (ft.getUint16() != null)
        return "unsigned int 16";

    if (ft.getInt32() != null)
        return "int 32";

    if (ft.getUint32() != null)
        return "unsgined int 32";

    if (ft.getPointerto() != null) {
        if (ft.getPointerto().getValue() == null)
            return "null pointer";
        else
            return "non Null Pointer";
    }

    if (ft.getArrayof() != null)
        if (ft.getArrayof().getValue() == null)
        {
        	
        	
        	
        	
        	return "array of null?";
        }
            
        else
            return "array of stuff";

    return "unknown type";
}
/*	
private void insertStringType()
{
	 Field f[] = new Field[4];
	 for(int i=0;i<4;i++)
	 {
		 f[i]=new Field();
	 }
	 f[0].setName("value");
	 f[0].setSize(4);
	 f[0].setOffset(0);
	 
	 ///making  pointer to arrray of chars type
	 //char type
	 FieldType FChar=new FieldType();
	 FChar.setChar(new edu.vt.cs.algviz.xml.Char());
	 //array of char		 
	 edu.vt.cs.algviz.xml.Arrayof A = new edu.vt.cs.algviz.xml.Arrayof();
	 A.setFieldType(FChar);
	 //array of type
	 FieldType arrT=new FieldType();
	 arrT.setArrayof(A);
	 //pointer to array
	 Pointerto pt=new Pointerto();
	 pt.setFieldType(arrT);

	 FieldType ft = new FieldType();
	 ft.setPointerto(pt);
	 
	 f[0].setFieldType(ft);
	 
	 //
	 f[1].setName("offset");
	 f[1].setSize(4);
	 f[1].setOffset(4);
	 ft = new FieldType();
	 ft.setInt32(new edu.vt.cs.algviz.xml.Int32());
	 f[1].setFieldType(ft);
	 
	 
	 f[2].setName("count");
	 f[2].setSize(4);
	 f[2].setOffset(8);
	 f[2].setFieldType(ft);
	 
	 f[3].setName("hash");
	 f[3].setSize(4);
	 f[3].setOffset(12);
	 f[3].setFieldType(ft);
	 
	sendTypeDefMessage(f, "java.lang.String", 16);		 
	 
	 
}
*/
	public boolean sendPythonVariable()
	{
		//variables have identifier, type and value
		
		//identifier is an array of chars
		
		 Field f[] = new Field[2];
		 for(int i=0;i<2;i++)
		 {
			 f[i]=new Field();
		 }
		 
		 ///making  pointer to array of chars type
		 //char type
		 FieldType FChar=new FieldType();
		 FChar.setChar(new edu.vt.cs.algviz.xml.Char());
		 //array of char		 
		 edu.vt.cs.algviz.xml.Arrayof A = new edu.vt.cs.algviz.xml.Arrayof();
		 A.setFieldType(FChar);
		 //array of type
		 FieldType arrT=new FieldType();
		 arrT.setArrayof(A);
		 //pointer to array
		 Pointerto pt=new Pointerto();
		 pt.setFieldType(arrT);
		
		 FieldType ft = new FieldType();
		 ft.setPointerto(pt);

		 f[0].setName("identifier");
		 f[0].setSize(4);
		 f[0].setOffset(0);
		 f[0].setFieldType(ft);
		 //type and value....
		 //pointer to type
		 
		 //char
		 FieldType FChar2=new FieldType();
		 FChar2.setChar(new edu.vt.cs.algviz.xml.Char());
		// FChar2.setInt32(new edu.vt.cs.algviz.xml.Int32());
		 //pointer to char
		 Pointerto pt2=new Pointerto();
		 pt2.setFieldType(FChar2);
		 //character field type
		 FieldType ft2 = new FieldType();
		 ft2.setPointerto(pt2);
		 

		 f[1].setName("value");
		 f[1].setSize(4);
		 f[1].setOffset(4);
		 f[1].setFieldType(ft2);
		 
		 
		sendTypeDefMessage(f, "Variable", 8);		 
		  
		 
		return true;
	}
	
	public boolean createVariableInstance(String memory)
	{
		 edu.vt.cs.algviz.xml.Struct struct = new edu.vt.cs.algviz.xml.Struct();
		 struct.setName("Variable");
		 FieldType ft = new FieldType();
		 ft.setStruct(struct);
		 sendMallocMessage("Variable", memory, 8, 
//				 MemallocTypeType.GLOBAL, ft);
				 MemallocTypeType.STACK,ft);
		 System.out.println("adding a variable at "+memory);
		
		return true;
	}
	
	public void allocateList(String address,int length)
	{
		FieldType FChar=new FieldType();
		FChar.setChar(new edu.vt.cs.algviz.xml.Char());
		 //pointer to char
		Pointerto charptr=new Pointerto();
		charptr.setFieldType(FChar);
		//field of pointer
		FieldType cpField =new FieldType();
		cpField.setPointerto(charptr);
				 
		 edu.vt.cs.algviz.xml.Arrayof A = new edu.vt.cs.algviz.xml.Arrayof();
		 A.setFieldType(cpField);
		 A.setLength(length);
		 //array of type
		 FieldType arrT=new FieldType();
		 arrT.setArrayof(A);
		 	 
		 sendMallocMessage("List", address, length*4, 
				 //MemallocTypeType.GLOBAL, arrT);
				 MemallocTypeType.HEAP, arrT);
		
		
	}
	
	 public void createStrInstance(String S,String address)
	 {
		//alloc the memory
		 FieldType FChar=new FieldType();
		 FChar.setChar(new edu.vt.cs.algviz.xml.Char());
		 
		 edu.vt.cs.algviz.xml.Arrayof A = new edu.vt.cs.algviz.xml.Arrayof();
		 A.setFieldType(FChar);
		 A.setLength(S.length());
		 //array of type
		 FieldType arrT=new FieldType();
		 arrT.setArrayof(A);
		 	 
		 sendMallocMessage("String", address, S.length()*2, 
				 //MemallocTypeType.GLOBAL, arrT);
				 MemallocTypeType.HEAP, arrT);
		 //put all of the characters in place
		 for( int i =0;i<S.length();i++)
		 {
			 edu.vt.cs.algviz.xml.Char C = new edu.vt.cs.algviz.xml.Char();
			 C.setValue(S.charAt(i)+"");
			 

			 FieldType ft=new FieldType();
			 ft.setChar(C);
			 
			sendPutFieldMessage(address, ft, i*2);
		 }
	 
	 }
	
	 
	 
	 public boolean createIntInstance(int val, String address)
	 {
		 //create message
		 edu.vt.cs.algviz.xml.Int32 C = new edu.vt.cs.algviz.xml.Int32();
		 C.setValue(val);
		 
		 FieldType ft=new FieldType();
		 ft.setInt32(C);
		
		//allocate memory
		 sendMallocMessage("Int", address, 4, 
				 //MemallocTypeType.GLOBAL, arrT);
				 MemallocTypeType.HEAP,ft );

		 //send value
		 sendPutFieldMessage(address, ft, 0);
		
		
		return true;
	 }
	 
	 
	 public boolean createFloatInstance(float val, String address)
	 {
		 //create message
		 edu.vt.cs.algviz.xml.Float C = new edu.vt.cs.algviz.xml.Float();
		 C.setValue(val);
		 
		 FieldType ft=new FieldType();
		 ft.setFloat(C);
		
		//allocate memory
		 sendMallocMessage("Float", address, 4, 
				 //MemallocTypeType.GLOBAL, arrT);
				 MemallocTypeType.HEAP,ft );

		 //send value
		 sendPutFieldMessage(address, ft, 0);
		
		
		return true;
	 }
	 
 
	 
	 
	 
	 
	 
	public boolean setPointer(String fromLoc,String toLoc,int offset)
	{//doesn't check on type so any type should work...
		FieldType FCharZ=new FieldType();
		FCharZ.setChar(new edu.vt.cs.algviz.xml.Char());
		 //pointer to array
		Pointerto pt4=new Pointerto();
		pt4.setFieldType(FCharZ);
    	pt4.setValue(toLoc);
		 		 
		 FieldType ftcharptr = new FieldType();
		 ftcharptr.setPointerto(pt4);		
		 System.out.println("trying to set pointers"+fromLoc+ " "+toLoc);
		 sendPutFieldMessage(fromLoc, ftcharptr, offset);
		return true;
	}
	
	
	
	public boolean sendMallocMessage(String name,String address,long size,
			 						MemallocTypeType type,FieldType fType)
	{
		Memalloc mem = new Memalloc();
		mem.setName(name);
		mem.setAddress(address);
		mem.setType(type);
		mem.setSize(size);
		mem.setFieldType(fType);

		//System.out.printf("%s,%s,%d,\n",name,address,size);
		//need to take apart to see how it works...
		//System.out.println(getFieldType(fType));
		
		//fType.
		
		//send it to the shadow
		boolean change = shadow.process( mem);
		//
		if(change)
		{
			System.out.printf("recording malloc chages\n");
			layoutNewNodes();
		}
		return change;
	}

	
	public boolean sendPutFieldMessage(String address,FieldType fieldType,
										long offset)
	{
		Putfield put =new Putfield();
		put.setAddress(address);
		put.setFieldType(fieldType);
		put.setOffset(offset);
		
		//send it to the shadow
		boolean change = shadow.process( put);
		//
		if(change)
		{
		System.out.printf("Layout new nodes for putfield\n");
			layoutNewNodes();
		}
		else
		{
			System.out.printf("No change detected for put message\n");
		}
		return change;
		
	}	
	
	public boolean sendShowasMessage(String address,FieldType fieldType)
	{
		Showas show =new Showas();
		show.setAddress(address);
		show.setFieldType(fieldType);
		
		//send it to the shadow
		boolean change = shadow.process(show);
		//
		if(change)
		{
			layoutNewNodes();
		}
		return change;
		
	}	
	
	
	public boolean sendMemFreeMessage(String address,long size)
	{
		Memfree mem =new Memfree();
		mem.setAddress(address);
		mem.setSize(size);
	
		//send it to the shadow
		boolean change = shadow.process(mem);
		//
		if(change)
		{
			layoutNewNodes();
		}
		return change;
		
	}	

	
	public boolean sendTypeDefMessage(Field[] fields, String name,
									  long size)
	{
		Typedef def =new Typedef();
		def.setField(fields);
		def.setName(name);
		def.setSize(size);
	
		//send it to the shadow
		boolean change = shadow.process(def);
		//
		if(change)
		{
			layoutNewNodes();
		}
		return change;
		
	}	


}