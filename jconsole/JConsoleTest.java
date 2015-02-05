package jconsole;

import java.awt.GridLayout;

import javax.swing.JFrame;

public class JConsoleTest
{
	public static void main(String[] args)
	{
		JFrame frame = new JFrame("Jython Interactive Console");
		frame.setSize(640, 480);
		frame.setLayout(new GridLayout());
		frame.add(new JConsole());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
