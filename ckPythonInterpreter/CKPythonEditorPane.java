package ckPythonInterpreter;

import javax.swing.JEditorPane;

import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
//import jsyntaxpane.DefaultSyntaxKit;
//import jsyntaxpane.actions.ActionUtils;
import jsyntaxpane.syntaxkits.PythonSyntaxKit;

//import java.awt.Color;
//import java.awt.Font;
//import java.awt.FontMetrics;
import java.awt.Color;
import java.awt.Graphics;
//import java.util.Stack;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
import ckPythonInterpreter.CKPythonSyntaxParser;
import java.lang.String;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import java.awt.FontMetrics;

public class CKPythonEditorPane extends JEditorPane
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1696129151766416620L;
	/**
	 * 
	 */

	
	JScrollPane scrPane;
	CKPythonSyntaxParser parser;
	private enum CSD_STATE_TYPE {NONE,BARS,BRICS};
	@SuppressWarnings("unused")
	private CSD_STATE_TYPE csdState;
	Object hiliteRef=null;

	public CKPythonEditorPane()
	{
		super();
		scrPane = new JScrollPane(this);
		
		//this might be overkill, should be a way to only load what is necessary.
		//DefaultSyntaxKit.initKit();
		//this.setContentType("text/python");
		//only load what is necessary
		this.setEditorKit(new PythonSyntaxKit());
		
		//add more abilities to the editor, using a file?
		csdState=CSD_STATE_TYPE.NONE;
		System.out.println("Got into editor pane");
		parser = new CKPythonSyntaxParser();
		
	}
	
	/**
	 * Returns scrollbars w/ linenumbers for the editorpane created
	 * 
	 * Not the best design, but the jSyntaxPane code will not run properly unless the editor is inside of scroll bars
	 * If you do not use this scrollpane, the editor object can be presented without problems.
	 * @return JScrollPane
	 */
	public JScrollPane getScrollablePane()
	{
		return scrPane;
		
	}
	
	class HLPainter extends DefaultHighlighter.DefaultHighlightPainter {
		public HLPainter(Color color)
		{
		super(color);
		}
		}
	
	//highlight lines without specifying the color
	public void HiLight (int startline,int endline)
	{
		Color color=Color.lightGray;
		HiLight(startline,endline,color);
	}
	
	//highlight lines between start and end line in the specified color
    public void HiLight (int startline,int endline, Color color)
    {
		Highlighter hL=getHighlighter();
	    Highlighter.HighlightPainter hp=new HLPainter(color);
    	int j=0;
    	int s=0;
    	int e=0;
    	int new_line=0;
    	//int z=0;
    	String all=getText();
    	//replace alternative end-line signs with "\n"
    	all=all.replaceAll("\r\n","\n");
    	//remove any existing highlights
    	this.RemoveHL();
    	//runs to the last line to be highlighted, marking the first line to be highlighted
    	for (j=0;j<endline-1;j++)
    	{
        	new_line=all.indexOf("\n",new_line);
        	if (new_line==-1)
        	{
        		e=all.length();
        		break;
        	}
        	//if start line the set to variable
    		if (j==startline-2)
    		{
    			s=new_line;
    		}
    		e=new_line;
    		new_line=new_line+1;
    	}	
    	try 
    	{
    		//sets object hiliteRef to highlighted lines
    		hiliteRef=hL.addHighlight(s,e,hp);
    	} 
    	catch (BadLocationException ex) 
    	{
    		//System.err.println("HUH??");
    	}
    }
    
    public void RemoveHL()
    {
    	//if a line or lines is highlighted
    	if (hiliteRef!=null)
    	{
    	Highlighter hL=getHighlighter();
    	//remove highlight
    	hL.removeHighlight(hiliteRef);
    	}
    }
    
    
    
    //inputs Python code as a string and outputs Python code as string
    //issues- does not highlight false statements and refuses to remove the highlight on the last line
    public String HLcode(String code)
    {
    	String blockIndentString="(^\\s*+)[\\S&&[^#]].*+";
		Pattern blockIndent=Pattern.compile(blockIndentString);
    	int i=0;
    	//import necessary libraries and set up editor
    	String new_code="from time import *\n"
    		+"from ckPythonInterpreter.CKUniqueEditor import *\n"
    		+"ed=getUniqueEditor()\n"
    		+"ed.RemoveHL()\n";
    	//split Python code into lines
    	String[] seg=code.split("\n");
    	//set variable to indicated indents
    	boolean in=false;
    	int len=seg.length;
    	//set variable to hold white space
    	String wspace;
    	//set variables to remember the previous line for the next step
		String oldpar="";
		String oldpar2="";
		//set time delay and remove strings for convenience
		String time="sleep(.5)\n";
		String remove="ed.RemoveHL()\n";
    	for (i=0;i<len;i++)
    	{
    		//number lines
    		String par=Integer.toString(i+1);
    		String par2=Integer.toString(i+2);
    		//determine white space in front of the line
    		Matcher detectDepth=blockIndent.matcher(seg[i]);
			if(detectDepth.find())// if not a comment
			{
				wspace=detectDepth.group(1);
				//trim white space to check for control structures
				String check=seg[i].trim();
				if ((check.startsWith("def"))||(check.startsWith("if"))||(check.startsWith("else"))||(check.startsWith("for"))||(check.startsWith("while")))
    				{
					new_code=new_code+seg[i]+"\n";
					//indicate control structure
					in=true;
					//save the line number for instructions for the next line
					oldpar=par;
					oldpar2=par2;
    				}
				else
    			{
					//if previous line was a control structure
					if (in==true)
					{
						//highlight control structure after it us processed
						new_code=new_code+wspace+remove+wspace+"ed.HiLight("+oldpar+","+oldpar2+")\n"+wspace+time+wspace+remove;	
						//reset variable
						in=false;
					}
					//if command is return
					if (check.startsWith("return"))// ||(i==len-1)????
					{
						//highlight and remove highlight before else the commands will not be processed
						new_code=new_code+wspace+"ed.HiLight("+par+","+par2+")\n"+wspace+time+wspace+remove+seg[i]+"\n";
					}
					//normal case
					else
					{
						//highlight before the line and remove highlight after
						new_code=new_code+wspace+"ed.HiLight("+par+","+par2+")\n"+seg[i]+"\n"+wspace+time+wspace+remove;
					}
    			}
			}
			//if line is a comment
			else
			{
				new_code+=seg[i]+"\n";
			}
    	}
    	//attempt to insure that the last line gets highlighted
    	///int ends=len+1;
    	///new_code=new_code+"ed.HiLight("+len+","+ends+")";
		System.out.printf("%s\n",new_code);
		//return updated code
    	return new_code;
    }
    
	public void paintComponent(Graphics g)
	//public void paint(Graphics g)
	{
		//System.out.println("Entering paintComponent\n");
		//draw Text next
		super.paintComponent(g);		
		//g.setColor(Color.BLACK);
		//g.drawRect(50,50,100,100);
		//draw CSD first
		String data = getText();
		parser.processCSDStack(parser.ParseCSD(data),g,getFontMetrics(getFont()));
		//ParseCSD(g,lines);
		

	//	super.paint(g);
	}
		
	

}
	
	
	
	
	

