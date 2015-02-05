package ckPythonInterpreter;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CKPythonSyntaxParser 
{
	Pattern blockStart;
	Pattern blockIndent;
	
	public CKPythonSyntaxParser()
	{
		//store patterns for use later on.
				
		//startVals.push(0);
		/*
		 * Pattern will search for any colon that is not within a string or after a quote
		 * move patterns to the class for speed?
		 *  
		 */
		//String notCommentQuote = "[^#\"\']*"; --reacts badly with the multiples *
		String notCommentQuote = "[^#\"\']?";
		String nestedQuote="(([\"\']).*\\3)?";
		String colonResidual=":\\s*(#.*)*";
		
		String blockStartingString = "^("+notCommentQuote+nestedQuote+")*"+colonResidual;
		blockStart=Pattern.compile(blockStartingString);
		/*
		 *detects lines that are not empty or completely commented out.
		 *use matcher to grab group 1 to determine amount of indent 
		 */
		String blockIndentString="(^\\s*+)[\\S&&[^#]].*+";
		blockIndent=Pattern.compile(blockIndentString);
				
		//System.out.println(blockIndentString);
		//System.out.println(blockStartingString);
		
		
		
	}
	

	public class CSDTriple
	{
		public int first;
		public int second;
		public int depth;
		public CSDTriple(int f,int s,int d)
		{
			first = f;
			second = s;
			depth = d;
		}
		/*int getFrom() { return from;}
		int getTo() {return to; }
		*/
	}
	
	
	public Stack<CSDTriple> ParseCSD(String code)
	{
		String[] lines=code.split("\n");
		
		
		//System.out.println("Entering parseCSD\n");
		
		Stack<CSDTriple> startVals=new Stack<CSDTriple>();
		Stack<CSDTriple> blocks=new Stack<CSDTriple>();
		int presentLine = 0;
		int presentDepth =0;
		boolean recordDepth=true; //can get rid of this later..
		
		startVals.push(new CSDTriple(0,-1,-1));
		
		for(String line:lines)
		{
			//System.out.println("for loop\n");	

			Matcher detectDepth=blockIndent.matcher(line);
			if(detectDepth.find())
			{
				//System.out.println(line);
				int lineDepth = detectDepth.group(1).length();//amount of white space
				if(lineDepth<presentDepth)
				{//this is the mark that we are leaving a group
					while(lineDepth <= startVals.peek().depth)
					{ //could be exiting more than one block
						CSDTriple T= startVals.pop();
						T.second=presentLine;
						blocks.push(T);
					}
					presentDepth=lineDepth; //resetting this
				//System.out.printf("leaving group %d, %d %d\n",T.first,T.second,T.depth);
				}
				else if(lineDepth >presentDepth) //we are entering a block
				{
					if(recordDepth)//correctly prompted with : 
					{
					//System.out.printf("depth change %d to %d\n",presentDepth,lineDepth);
						presentDepth=lineDepth;
						recordDepth=false;
						
					}
					else
					{
						//System.out.printf("syntax error");
						//syntax error?
					}
				}
			
				
				Matcher isStart = blockStart.matcher(line);
				
				
				if(isStart.matches()) //start of block
				{//could still be if stmt block, work on later - MKB TODO
					//System.out.println("mark 2\n");	

					CSDTriple T = new CSDTriple(presentLine,-1,presentDepth);
					startVals.push(T);
					recordDepth=true;
				//System.out.printf("entering group %d, %d %d\n",T.first,T.second,T.depth);
				}
							
			} //end non-empty/uncommented  line	
			//System.out.println("exiting block");
			
		presentLine++;
		//if detecting start-push startVals
		//else if detecting end - Pop startVal and 
		} //for loop
		CSDTriple T= startVals.pop();
		T.second=presentLine;
		blocks.push(T);
		

		return blocks;
		//processCSDStack(blocks,g);
		//process Stack Here
		
	}

	public void printCSDStack(Stack<CSDTriple> CSD)
	{
		System.out.println("Printing out CSD Stack");

		int width = 1;
		int height=1;
		int screenwidth=40;
		while (! CSD.isEmpty())
		{
//			System.out.println("in while loop");
			CSDTriple C = CSD.pop();
			
			System.out.printf("Rect drawn at (%d,%d) with width %d, and height %d\n",C.depth*width, C.first*height, 
					(screenwidth-C.depth)*width, (C.second-C.first)*height);
		}
		//System.out.println("out while loop");
		
	}
	
	
	

	public void processCSDStack(Stack<CSDTriple> CSD,Graphics g,FontMetrics textFont)
	{
		
		//need to calculate the width...
		int screenwidth = 200;
		//need to calculate hight per line.
		//FontMetrics textFont = getFontMetrics( getFont() );
		int height = textFont.getHeight();
		int width = textFont.getMaxAdvance();

		
		while (! CSD.isEmpty())
		{
			//System.out.println("in while lop");
			CSDTriple C = CSD.pop();
			if(g != null)
			{
			g.drawRect(C.depth*width, C.first*height, 
					(screenwidth-C.depth)*width, (C.second-C.first)*height);
			}
		
			//System.out.printf("Rect drawn at (%d,%d) with width %d, and height %d\n ",C.depth*width, C.first*height, 
			//		(screenwidth-C.depth)*width, (C.second-C.first)*height);
			
		}
		//System.out.println("out while lop");
		
	}
	
}
