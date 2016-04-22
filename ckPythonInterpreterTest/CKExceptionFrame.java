package ckPythonInterpreterTest;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.python.core.Py;
import org.python.core.PyException;

public class CKExceptionFrame extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6752355779863920868L;
	private ImageIcon Icon1 = new ImageIcon("images/fractal3.jpg");
	private ImageIcon Icon2 = new ImageIcon("images/small_trophy.png");
	
	// this looks promising... http://www.jython.org/docs/library/exceptions.html
	
	// 3rd part of CKExceptionFrame out of 6
	public static JComponent getExceptionErrorDisplay(PyException e)
	{
		String text = e.type.toString();
		//we can tell which error
		if(e.match(Py.NameError)) { 	text = " NameError Exception"; 	}
		
		//we can tell what the error says with this..
		//System.out.println("line number   = " + e.value);
        //System.out.println("Error message = " + e.value.__findattr__("message"));
		
		//3 name
		JLabel name = new JLabel(text, JLabel.CENTER);
		
		name.setForeground(Color.red);
		System.out.print(e.value);
		
		return name;
	}
	
	public static JButton getExceptionErrorDisplay2(PyException e)
	{
		String line = " Error on line : " + e.value.__findattr__("lineno");
		
		if (e.value.__findattr__("lineno") == null)
			line = "value is null";
		
		JButton b1 = new JButton(line);
		b1.setBackground(Color.GREEN);
		
		return b1;
	}
	
	public static String getExceptionErrorDisplay3(PyException e)
	{
		String description = " Description: " + e.value.__findattr__("message");
		
		if (e.value.__findattr__("message") == null)
			description = " value is null";

		return description;
	}
	
	
	public CKExceptionFrame(PyException exception)
	{
		
		setTitle("You Have Encountered an Error");
		setLayout(new GridLayout(3,1));
		
		Border compound;
		Border blackLine = BorderFactory.createLineBorder(Color.BLACK,3);
		
		Border loweredBevel = new BevelBorder(BevelBorder.LOWERED);
		Border raisedBevel = new BevelBorder(BevelBorder.RAISED);
		
		compound = BorderFactory.createCompoundBorder(
                raisedBevel, loweredBevel);
		
		compound = BorderFactory.createCompoundBorder(
                blackLine, compound);
		
		Border border1 = new LineBorder(Color.BLACK, 3);
		
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(1,2));
		
		p1.setBorder(compound);
		
		JPanel subPanel1 = new JPanel();
		subPanel1.setLayout(new GridLayout(2,1));
		
		JLabel l1 = new JLabel(Icon1);
		
		l1.setBorder(border1);
		
		p1.add(l1);
		
		JComponent l2 = getExceptionErrorDisplay(exception);
		
		l2.setForeground(Color.RED);
		
		l2.setBorder(compound);
		
		subPanel1.add(l2);
		
		JButton b1 = getExceptionErrorDisplay2(exception);
		
		b1.setBorder(compound);
		
		subPanel1.add(b1);
		
		p1.add(subPanel1);
		
		add(p1);
		
		//------------------------------------------
		
		JPanel p2 = new JPanel();
		p2.setLayout(new BorderLayout());
		
		
		String text1 = "\nRaised when a local or global name" +
				" is not found. This applies only to unqualified names." +
				" The associated value is an error message that " +
				"includes the name that could not be found.";
		
		JTextArea textArea1 = new JTextArea();
		
		textArea1.setText(text1);
		
		textArea1.setLineWrap(true);
		textArea1.setWrapStyleWord(true);
		textArea1.setEditable(false);

		JScrollPane comments1 = new JScrollPane(textArea1);
		
		comments1.setBorder(compound);
		
		p2.add(comments1, BorderLayout.CENTER);
		
		String description = getExceptionErrorDisplay3(exception);
		
		JLabel l3 = new JLabel(description);
		
		l3.setBorder(compound);
		
		p2.add(l3, BorderLayout.NORTH);
		
		add(p2);
		
		//--------------------------------------------
		
		JPanel p3 = new JPanel();
		p3.setLayout(new GridLayout(2,1));
		p3.setBorder(compound);
		
		String text2 = " user comments";
		
		JTextArea textArea2 = new JTextArea();
		
		textArea2.setText(text2);
		
		textArea2.setLineWrap(true);
		textArea2.setWrapStyleWord(true);
		textArea2.setEditable(false);

		JScrollPane comments2 = new JScrollPane(textArea2);
		
		comments2.setBorder(compound);
		
		p3.add(comments2);
		
		JPanel subPanel2 = new JPanel();
		subPanel2.setLayout(new GridLayout(1,2));
		
		JLabel l4 = new JLabel(" accomplishment x", JLabel.CENTER);
		
		l4.setBorder(compound);
		
		subPanel2.add(l4);
		
		JLabel l5 = new JLabel(Icon2);
		
		l5.setBorder(compound);
		
		subPanel2.add(l5);
		
		p3.add(subPanel2);
		
		add(p3);
		
		pack();
		setSize(400,700);
		setVisible(true);		
	}
	

	public static void main(String [] args)
	{
		//need to force JYthon to initialize its values
		//need a better way
		//InteractiveConsole console =  new InteractiveConsole();
		
		
		CKExceptionFrame frame = new CKExceptionFrame(Py.NameError("testing error"));
		
		  frame.setVisible(true);
	      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		
	}
	
	
}
