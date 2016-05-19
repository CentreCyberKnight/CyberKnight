package ckPythonInterpreterTest;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
/*import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
*/
import java.io.IOException;
//import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.text.EditorKit;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

//import algviz.shadow.ShadowState;
import algviz.visualizer.StateGraphView;
import ckPythonInterpreter.CKAlgViz;
import ckPythonInterpreter.CKPythonConsoleExtended;
import ckPythonInterpreter.CKPythonEditorPane;
import ckPythonInterpreter.CKUniqueEditor;
import edu.vt.cs.algviz.xml.Field;
import edu.vt.cs.algviz.xml.FieldType;
import edu.vt.cs.algviz.xml.Memalloc;
import edu.vt.cs.algviz.xml.Message;
import edu.vt.cs.algviz.xml.Messages;
import edu.vt.cs.algviz.xml.Pointerto;
import edu.vt.cs.algviz.xml.types.MemallocTypeType;
import jsyntaxpane.DefaultSyntaxKit;


public class CKAlgVizTest 
{ 
	CKPythonEditorPane editor;	
	CKPythonConsoleExtended console;
	JButton runButton;
	JTabbedPane tabpane;
	CKAlgViz algviz;
	
	
	 public CKAlgVizTest() 
	 {
	        JFrame f = new JFrame("Ausome editor of power!");//CKSytaxPaneTest.class.getName());
	        final Container c = f.getContentPane();
	        c.setLayout(new BorderLayout());
	        
	        
	        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	        split.setRightComponent(createEditorPane());
	        split.setLeftComponent(createTabPane());
	        //c.add(createEditorPane(),BorderLayout.CENTER);
	        
	        c.add(split,BorderLayout.CENTER);
	    	c.doLayout();
	                
	        f.setSize(1000, 600);
	        f.setVisible(true);
	        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	        
	        
	        String cmd =
	        	"from ckPythonInterpreter.CKUniqueEditor import *\n" +
	        	//"import ckPythonInterpreter.CKUniqueEditor\n" +
	        	"ed=getUniqueEditor()\n" +
	        	"ed.setText('It Worked')\n";
	        editor.setText(cmd);
	        	
	        	
	        

	    }
	
	 
	 
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
		 
		 algviz.sendTypeDefMessage(f, "java.lang.String", 16);		 
		 
		 
		 
		 
	 }
	 
	 private void allocFillString(String S,String address)
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
		 
		 
		 
		 algviz.sendMallocMessage("char[]", address, S.length()*2, 
				 //MemallocTypeType.GLOBAL, arrT);
				 MemallocTypeType.GLOBAL, arrT);
		 //put all of the characters in place
		 for( int i =0;i<S.length();i++)
		 {
			 edu.vt.cs.algviz.xml.Char C = new edu.vt.cs.algviz.xml.Char();
			 C.setValue(S.charAt(i)+"");
			 

			 FieldType ft=new FieldType();
			 ft.setChar(C);
			 
			algviz.sendPutFieldMessage(address, ft, i*2);
		 }
	 
	 }
	 

	 

	    @SuppressWarnings("unused")
		private void loadNewTrace(Reader reader)
	    {
	    	
	    	Enumeration<Message> msgQueue;
	       try 
	       {
	            BufferedReader xmlStream;
	            xmlStream = new BufferedReader(reader);
	            Messages msg = (Messages) Unmarshaller.unmarshal(Messages.class,
	                    xmlStream);
	            System.out.println("loaded " + msg.getMessageCount() + " messages");
	            msgQueue = msg.enumerateMessage();
	            
	            
	            
	            
	            Message msg1 = msgQueue.nextElement();
	            //first is the array malloc
	            Object v = msg1.getChoiceValue();
	            Memalloc m = (Memalloc) v;
	            
	            algviz.sendMallocMessage(m.getName(),m.getAddress(),m.getSize(),
	   				 MemallocTypeType.GLOBAL,m.getFieldType());

	            
	            
	            
	        } catch (MarshalException e1) {
	            e1.printStackTrace();
	        } catch (ValidationException e1) {
	            e1.printStackTrace();
	        }
	        
	        //be clever!
	        
	      
	        
	        
	        
	        
	    }


	 
	 
	 
	 
	 @SuppressWarnings("unused")
	private StateGraphView  createAlgViz2()
	 {
		 algviz = new CKAlgViz(600);
		 String address="70000000";
		 
		/* try
		{
			loadNewTrace( new FileReader(new File("C:/Users/dragonlord/Documents/Arrays.xml" )));
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 */
		if(false)
		{
		 FieldType ft = new FieldType();
		 ft.setChar(new edu.vt.cs.algviz.xml.Char() );

		 algviz.sendMallocMessage("", address, 2,
				 MemallocTypeType.GLOBAL, ft);

		 edu.vt.cs.algviz.xml.Char C = new edu.vt.cs.algviz.xml.Char();
		 C.setValue("S");
		
		 
		 FieldType ft2=new FieldType();
		 ft2.setChar(C);
		
		 algviz.sendPutFieldMessage(address, ft2, 0);
	 
		 }
		 else
		 {
			 
			 
			 
			 
			 
			 
		 allocFillString("Pickles",address);
		 System.out.println("Here we go again");
		 }
		 return algviz;
		 
		 
	 }
	 

	 
	 
	 private StateGraphView createAlgViz3()
	 {
		 algviz = new CKAlgViz(400);
		//now to create a test
		 //identify variable type
		 algviz.sendPythonVariable();
		 
		 //create a new variable
		 String varAddress = "4000";
		 
		 edu.vt.cs.algviz.xml.Struct struct = new edu.vt.cs.algviz.xml.Struct();
		 struct.setName("Variable");
		 FieldType ft = new FieldType();
		 ft.setStruct(struct);
		 algviz.sendMallocMessage("Variable", varAddress, 8, 
			 MemallocTypeType.GLOBAL,ft);
		 System.out.println("added variable");
		 //now to populate
		 
		 String address="4008";		 
		 allocFillString("bob",address);
		 System.out.println("added string");
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
		 pt.setValue(address);
		 		 
		 FieldType ft2 = new FieldType();
		 ft2.setPointerto(pt);		
		 
		 
 
		 
		 //set pointer to string
		 algviz.sendPutFieldMessage(varAddress,ft2 , 0);
		 //algviz.sendPutFieldMessage(varAddress,ft2 , 0);
		 System.out.println("added edge");
		 boolean makechar=true;
		 String valueAddress = "4010";
		 if(makechar)
		 {
		 //create a char...
			 
		//alloc the memory
			 FieldType FChar2=new FieldType();
			 FChar2.setChar(new edu.vt.cs.algviz.xml.Char());
			 
			 
			 algviz.sendMallocMessage("char", valueAddress, 2, 
					 //MemallocTypeType.GLOBAL, arrT);
					 MemallocTypeType.GLOBAL, FChar2);
			 edu.vt.cs.algviz.xml.Char C = new edu.vt.cs.algviz.xml.Char();
			 C.setValue("Z");
			 FieldType ft3=new FieldType();
			 ft3.setChar(C);
				 
			algviz.sendPutFieldMessage(valueAddress, ft3,0);
			System.out.println("1st sent to boss");
			
			
			edu.vt.cs.algviz.xml.Char C2 = new edu.vt.cs.algviz.xml.Char();
			 C2.setValue("Z");
			 FieldType ft32=new FieldType();
			 ft32.setChar(C);
				 
			algviz.sendPutFieldMessage(valueAddress, ft32,0);
			System.out.println("2nd sent to boss");
			
			//now link to the char
			
			
			//char type
			FieldType FCharZ=new FieldType();
			 FCharZ.setChar(new edu.vt.cs.algviz.xml.Char());
			 //pointer to array
			 Pointerto pt4=new Pointerto();
			 pt4.setFieldType(FCharZ);
			 pt4.setValue(valueAddress);
			 		 
			 FieldType ftcharptr = new FieldType();
			 ftcharptr.setPointerto(pt4);		
			 algviz.sendPutFieldMessage(varAddress, ftcharptr, 4);
		}
		 else
		 {
			 //set pointer to char?
			 FieldType FChar2=new FieldType();
			 FChar2.setChar(new edu.vt.cs.algviz.xml.Char());
			// FChar2.setInt32(new edu.vt.cs.algviz.xml.Int32());
			 //pointer to char
			 Pointerto pt2=new Pointerto();
			 pt2.setFieldType(FChar2);
			 //chrater field type
			 FieldType ft21 = new FieldType();
			 ft21.setPointerto(pt2);
		 }
		//algviz.sendPutFieldMessage(varAddress,C,4);
		 
		 return algviz;
		 
		 
	 }
	 
	 
	 @SuppressWarnings("unused")
	private StateGraphView createAlgViz()
	 {
		 algviz = new CKAlgViz(400);
		//now to create a test
		insertStringType();
		edu.vt.cs.algviz.xml.Struct struct = new edu.vt.cs.algviz.xml.Struct();
		struct.setName("java.lang.String");
		FieldType ft = new FieldType();
		ft.setStruct(struct);
		algviz.sendMallocMessage("java.lang.String", "9000094", 16, 
				MemallocTypeType.GLOBAL,ft);
				
		
		allocFillString("Pickles","90000a4");
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
		 pt.setValue("90000a4");
		 
		 
		 FieldType ft2 = new FieldType();
		 ft2.setPointerto(pt);		
		
		
		 algviz.sendPutFieldMessage("9000094",ft2, 0);
		
		
		System.out.println("go to town\n");
		
    	
		

		 return algviz;
		 
	 }
	 
	 private JTabbedPane createTabPane() 
	 {
		 JTabbedPane tab = new JTabbedPane();
		 tab.setPreferredSize(new Dimension(600,600));
		 
		 tab.addTab("Console", createConsolePane());
		 tab.setMnemonicAt(0, KeyEvent.VK_F1);
		 
		 tab.addTab("scribbles",createURLPage("http://vault.hanover.edu/~bradshaw/"));
		 tab.setMnemonicAt(1, KeyEvent.VK_F2);
		 
		 tab.addTab("documentation",createURLPage("http://docs.python.org/"));
		 tab.setMnemonicAt(2, KeyEvent.VK_F3);

		 tab.addTab("vizualizer",createAlgViz3());
		 tab.setMnemonicAt(3, KeyEvent.VK_F4);

		 
		 tab.setSelectedIndex(3);
		 
		 return tab;
	}

	 
	 
	 private JComponent wrapInScrolls(JComponent j)
	 {
		 JScrollPane scroll = new JScrollPane(j);
		 scroll.setVerticalScrollBarPolicy(
	                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

	     return scroll;
		 
	 }
	 
	 private JComponent createURLPage(String url)
	 {
		 
		 JEditorPane editorPane = new JEditorPane();
		 editorPane.setEditable(false);
		 try {
			 java.net.URL data = new URL(url);
			 editorPane.setPage(data);
		     } catch (IOException e) {
		         System.err.println("Attempted to read a bad URL: " + url);
		     }
		     
		 return wrapInScrolls(editorPane);
		 
	 }
	 
	private JComponent createConsolePane()
	 {
		 
			console = new CKPythonConsoleExtended();
			console.setPreferredSize(new Dimension(400,600));
	        return wrapInScrolls(console);
	        
	 }
	 
	 
	 
	 
	 private JPanel createEditorPane()
	 {
		 JPanel pane=new JPanel();
		 pane.setLayout(new BorderLayout());

		 //create unique editor
		 editor = CKUniqueEditor.getUniqueEditor();
		 pane.add(editor.getScrollablePane());
	           		    
		 //TOOL BARS
		 //toolbar is part of the editor kit--awesome!
		 JToolBar jToolBar1 = new javax.swing.JToolBar();
		 jToolBar1.setRollover(true);
		 jToolBar1.setFocusable(false);
		 	
		 EditorKit kit = editor.getEditorKit();
		 if (kit instanceof DefaultSyntaxKit) 
		 {
			 DefaultSyntaxKit defaultSyntaxKit = (DefaultSyntaxKit) kit;
			 defaultSyntaxKit.addToolBarActions(editor, jToolBar1);
		 }	
		 jToolBar1.validate();
		 pane.add(jToolBar1, BorderLayout.PAGE_START);
		 
	
	    runButton = new JButton("Run Code");
	    ButtonHandler handler = new ButtonHandler();
	    runButton.addActionListener(handler);
	    pane.add(runButton,BorderLayout.PAGE_END);
	        
		 
		 
	    return pane;
	       
		 
	 }
	 
	 
	 
	 
	 	private class ButtonHandler implements ActionListener
	 	{
	 		public void actionPerformed(ActionEvent event)
	 		{
	 			
	 			//get code from editor
	 			String code =editor.getText();
	 			//System.out.println("got to here with the code "+code);
	 			//send code to console to run
	 			console.runNewCode(code);
	 		}
	 		
	 	}

		/**
		 * @param args
		 */
		public static void main(String[] args) {
			
			java.awt.EventQueue.invokeLater(new Runnable() 
	    	{

	        @Override
	        	public void run() 
	        	{
	            	new CKAlgVizTest();//.setVisible(true);
	        	}
	    	}
	    	);
			

		}

}
