package ckCommonUtils;

import java.awt.Component;

import javax.swing.Icon;

public interface RequestNewComponentListener
{

	/**
	 * 
	 * @return a new component for use in a tabbed pane
	 */
	Component requestNewComponent(); 
	Icon requestNewIconforComponent(Component comp);
	String requestNewDescriptionForComponent(Component comp);
	void notifyComponentRemoved(Component comp);
}
