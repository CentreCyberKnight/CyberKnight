package ckCommonUtils;

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
		
		return new java.awt.event.MouseEvent(null,
				//type,long when, int modifiers, int x, int y, int clickCount, boolean popupTrigger, int button
				mouseType,1, 0, (int) e.getX(),(int)e.getY(),
				e.getClickCount(),e.isPopupTrigger());
	}
	
	
	
}
