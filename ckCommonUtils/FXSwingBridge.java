package ckCommonUtils;

import javafx.event.EventType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import javax.swing.JLabel;

//import javafx.scene.input.MouseEvent;

public class FXSwingBridge
{

	public static java.awt.event.MouseEvent FXMouseEventToSwing(javafx.scene.input.MouseEvent e)
	{
		
		int mouseType = 0;
		if ( e.getEventType() == javafx.scene.input.MouseEvent.MOUSE_CLICKED)
		{
			mouseType =java.awt.event.MouseEvent.MOUSE_CLICKED; 
		}
		else 	if ( e.getEventType() == javafx.scene.input.MouseEvent.MOUSE_MOVED)
		{
			mouseType =java.awt.event.MouseEvent.MOUSE_MOVED; 
		}
 
		//TODO Does not look at modifiers
		
		return new java.awt.event.MouseEvent(new JLabel("dummy Source"),
				//type,long when, int modifiers, int x, int y, int clickCount, boolean popupTrigger, int button
				mouseType,1, 0, (int) e.getX(),(int)e.getY(),
				e.getClickCount(),e.isPopupTrigger());
	}

	
	
	public static javafx.scene.input.MouseEvent SwingMouseEventToFX(
			java.awt.event.MouseEvent e,EventType<MouseEvent> eventType)
	{
	

		MouseButton mouseButton = MouseButton.NONE; 
		if(e.getButton() == java.awt.event.MouseEvent.BUTTON1)
		{
			mouseButton = MouseButton.PRIMARY;
		}
		else if (e.getButton() == java.awt.event.MouseEvent.BUTTON2)
		{
			mouseButton = MouseButton.MIDDLE;
		}
		else if (e.getButton() == java.awt.event.MouseEvent.BUTTON3)
		{
			mouseButton = MouseButton.SECONDARY;
		}
		
		//need the type of event
		
		
		return new javafx.scene.input.MouseEvent(eventType, 
				e.getX(),e.getY(),e.getXOnScreen(),e.getYOnScreen() , 
				mouseButton,e.getClickCount(), 
				(e.getModifiersEx() | java.awt.event.MouseEvent.SHIFT_DOWN_MASK) !=0,
				(e.getModifiersEx() | java.awt.event.MouseEvent.CTRL_DOWN_MASK) !=0,
				(e.getModifiersEx() | java.awt.event.MouseEvent.ALT_DOWN_MASK) !=0,
				(e.getModifiersEx() | java.awt.event.MouseEvent.META_DOWN_MASK) !=0,

				(e.getModifiersEx() | java.awt.event.MouseEvent.BUTTON1_DOWN_MASK) !=0,
				(e.getModifiersEx() | java.awt.event.MouseEvent.BUTTON2_DOWN_MASK) !=0,
				(e.getModifiersEx() | java.awt.event.MouseEvent.BUTTON3_DOWN_MASK) !=0,
				true, //synthesised
				(e.getModifiersEx() | java.awt.event.MouseEvent.MOUSE_DRAGGED) !=0,
				e.isPopupTrigger(), 
				null);
		
	/*	return new java.awt.event.MouseEvent(new JLabel("dummy Source"),
				//type,long when, int modifiers, int x, int y, int clickCount, boolean popupTrigger, int button
				mouseType,1, 0, (int) e.getX(),(int)e.getY(),
				e.getClickCount(),e.isPopupTrigger());*/
	}

	
	
}
