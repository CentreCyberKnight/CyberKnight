package ckGraphicsEngine;

import java.util.Collection;

import ckCommonUtils.CKPosition;
import ckCommonUtils.LogListener;
import ckGameEngine.CKGrid;
import ckGraphicsEngine.CKGraphicsEngine.SelectAreaType;
import ckGraphicsEngine.assets.CKGraphicsAsset;


public interface CKGraphicsEngine
{
	public int startTransaction(boolean block); 
		/**
		 * Starts a new transaction.
		 *  An implementation may decide to ignore the block parameter and block in all cases.
		 * @param block - requests the function block until previous transactions are completed.
		 * @return unique id for a transaction.
		 */

	/**
	 * 	 This function ends a transaction and allows the graphics engine to schedule the sequence.
	 *   @param block - requests that the function blocks until all outstanding actions are completed. 
	 *   @param tid - transaction id
	 */
	public void endTransaction(int tid,boolean block);
	
	/**
	 *  loads a new asset into memory.
	 * @param tid - transaction id
	 * @param AID - asset id
	 * @throws LoadAssetError
	 */
	public void loadAsset(int tid,String AID) throws LoadAssetError;
	
/**
 *   Creates an instance of an asset
 * @param tid - transaction id
 * @param AID - asset id to create an instance of.
 * @param pos - starting position of the instance
 * @param startFrame - time to position the asset
 * @param layerDepth - which layer the instance should appear on
 * @return a unique instance id that should be used to reference an instance.
 * @throws LoadAssetError
 */
	int createInstance(int tid, String AID, CKPosition pos, int startFrame,
			int layerDepth) throws LoadAssetError;  
	
	
	int createUniqueInstance(int tid, CKGraphicsAsset asset, CKPosition pos,
			int startFrame, int layerDepth);
/**
 *  destroys the instance with IID
 * @param tid transaction id
 * @param IID instance id
 * @param startFrame time to destroy the asset
 * @throws BadInstanceIDError
 */
	public void destroy(int tid,int IID, int startFrame) throws BadInstanceIDError;
/**
 *  hides the instance.  If the instance is already hidden, this function does nothing.  If a hide and reveal command have the same start time, the hide will be conducted first.
 * @param tid - transaction id
 * @param IID - instance id
 * @param startFrame - time to start hide the instance
 * @throws BadInstanceIDError 
 */
	public void hide(int tid,int IID, int startFrame) throws BadInstanceIDError;
/**
 *  reveals the instance.  If the instance is not hidden this function does nothing. If the hide and reveal command have the same start_time the hide command will be conducted first.
 * @param tid transaction id
 * @param IID instance id
 * @param startFrame time to reveal the instance
 * @throws BadInstanceIDError
 */
	public void reveal(int tid,int IID,int startFrame) throws BadInstanceIDError;
	/**
	 *  moves the instance with IID from origin Position to the destination.
	 * @param tid transaction id
	 * @param IID instance id
	 * @param startFrame time to start moving
	 * @param orgin Position to move from
	 * @param destination Position to move to
	 * @param speed how many frames should be used to travel from one tile to an adjacent tile.
	 * @return the frame number in which the move will be completed
	 * @throws BadInstanceIDError
	 */	
	public int move(int tid,int IID,int startFrame,
			CKPosition orgin, CKPosition destination,int speed) throws BadInstanceIDError;


	/** 
	 * 
	 *  moves the instance with IID from its present location to the destination.
	 * @param tid transaction id
	 * @param IID instance id
	 * @param startFrame time to start moving
	 * @param destination Position to move to
	 * @param duration how many frames should be used to travel from where the instance is to the destination.
	 * @return the frame number in which the move will be completed
	 * @throws BadInstanceIDError
	 */	

	public int moveTo(int tid, int IID, int startFrame, CKPosition destination, int duration)
	throws BadInstanceIDError;
	
	
	/**
	 *  returns the length of time that the Animation will take to run.
	 * @param tid transaction id
	 * @param AID asset id
	 * @param Animation which animation to recover
	 * @return number of frames the animation takes
	 * @throws LoadAssetError
	 * @throws BadInstanceIDError
	 * @throws UnknownAnimationError
	 */
	public int getAnimationLength(int tid,String AID,String Animation) 
	throws LoadAssetError,BadInstanceIDError,UnknownAnimationError;

	/**
	 *  returns the length of time that the Animation will take to run for the instance with IID.  
	 * @param tid transaction id
	 * @param IID instance id
	 * @param animation which animation to recover
	 * @return number of frames the animation takes
	 * @throws BadInstanceIDError
	 * @throws UnknownAnimationError
	 */
	public int getInstanceAnimationLength(int tid,int IID,String animation) throws BadInstanceIDError,UnknownAnimationError;
/**
 * sets the animation of the instance with IID to new Animation at time start_time.
 * @param tid transaction id
 * @param IID instance id
 * @param animation animation to run
 * @param startFrame time to start using the new animation
 * @throws BadInstanceIDError
 * @throws UnknownAnimationError
 */
	public void setAnimation(int tid,int IID, String animation,int startFrame) throws BadInstanceIDError,UnknownAnimationError;
	
/**
 * This is a convenience function that allows a client to define a set of animations that the instance with IId will perform.  Arrays animations and start_times must be the same length.  The start times must be in increasing order.  Out of order animations will be discarded.   Each new animation will be set as the its start_time comes up. The Final animation will not be replaced (by this function.)
 * @param tid transaction id
 * @param IID instance id
 * @param animations array of animations to execute
 * @param start_times times to execute each animation
 * @throws BadInstanceIDError 
 * @throws UnkonwAnimationError
 * @throws IndexOutOfBoundsException
 */
	public void setAnimationChain(int tid,int IID, String[] animations,int[] start_times) throws BadInstanceIDError,UnknownAnimationError,IndexOutOfBoundsException;

	
/**
 *  replaces the tile at position listed
 * @param tid transaction id
 * @param AID asset id
 * @param position location to place the tile
 * @param layerDepth which layer to place the asset on
 * @param startFrame time to replace the tile
 * @throws LoadAssetError
 */
	public void replaceTile(int tid,String AID,CKPosition position,
			int layerDepth, int startFrame) throws LoadAssetError;

	public enum RelationalLinkType {PUSH,PULL,RELATIVE,NONE};
	
/**
 *   Links the future movements of the instance with IId_child to the movements of iid_Parent based on the RelationalPositionType starting at time start_time.		
 * @param tid         transaction id
 * @param IID_Child   instance id of the child 
 * @param IID_Parent  instance id of the parent
 * @param type        how the link should be conducted
 * @param startFrame  when the link should occur
 * @throws BadInstanceIDError 
 * @throws CircularDependanceError
 */
	public void linkGraphics(int tid,int IID_Child, int IID_Parent, RelationalLinkType type, int startFrame) throws BadInstanceIDError, CircularDependanceError;

	
	/**
	 *     removes a parent linkage for instance with IID.  If there is no link present, this function will silently do nothing.
	 * @param tid transaction id
	 * @param IId instance id
	 * @param startFrame time to remove the link
	 * @throws BadInstanceIDError
	 */
	public void unLink(int tid,int IID,int startFrame) throws BadInstanceIDError;

	
	
	/**
	 *  Instructs the graphics engine to center the view on the instance IId at time start_time.  If the instance moves in the future, the �camera� will continue to center on it.  If the �camera� is not already focused on the instance, it will transition to the instance at speed, transition_speed.  A speed of 0 indicates and instantaneous transition.
	 * @param tid transaction id
	 * @param IID instance id
	 * @param startFrame when the camera will start to move
	 * @param transition_duration how fast the camera should move to instance
	 * @return the frame number in which the move will be completed
	 * @throws BadInstanceIDError
	 * @throws CircularDependanceError
	 */
	public int cameraFollowInstance(int tid,int IID,int startFrame,int transition_duration) throws BadInstanceIDError,CircularDependanceError;

	
	/**
	 * Instructs the graphics engine to center the view on the position,position at time start_time,  If the �camera� is not already focused on the instance, it will transition to the instance at speed, transition_speed.  A speed of 0 indicates and instantaneous transition.
	 * @param tid transaction id
	 * @param position place to point the camera at 
	 * @param startFrame time to start moving the camera
	 * @param transition_duration how fast to move the camera
	 * @return the frame number in which the move will be completed
	 * @throws BadInstanceIDError 
	 */
	public int cameraPointAt(int tid, CKPosition position,int startFrame,int transition_duration) throws BadInstanceIDError;

	/**Loads a Scene with id sid
	 * @param sid - id of the scene to load
	 */
	public void loadScene(String sid);

	
	
	
	/**Loads a dialog message to the GUI and displays it if there are no pending messages.
	 * @param mess
	 */
	public void loadDialogMessage(CKDialogMessage mess);
	
	/**
	 * Loads up a script to answer the dialog messages for use in testing
	 * @param script
	 * @param frames
	 */
	public void loadDialogDebugScript(String script, int frames);
	
	
	/**
	 * Adds a log listener to to the GUI to record all of the messages sent
	 * @param l
	 */
	public void loadDialogLogListener(LogListener l);
	
	
	/**
	 * This method will block until the graphic engine has completed any outstanding actions.
	 */
	public void blockTilActionsComplete();
	
	
	public enum SelectAreaType {TARGET,RAY,CONE,RING,NONE};

	
	/**
	 * Allows the user to select a set positions using a mouse.
	 * @param originLocation - original position from which distances are calculated.
	 * @param minDistance - minimum distance from the origin for valid choices. 
	 * @param maxDistance - maximum distance from the origin for valid choices. 
	 * @param callback - who to report selected positions to.
	 * @param type - Provides alternative selection types. 
	 */
	void selectArea(CKPosition originLocation,double minDistance,double maxDistance,
			                     CKSelectedPositionsListeners callback,	SelectAreaType type);
	
	/**
	 * Allows the user to select a set positions using a mouse.
	 * @param originLocation - original position from which distances are calculated.
	 * @param minDistance - minimum distance from the origin for valid choices. 
	 * @param maxDistance - maximum distance from the origin for valid choices. 
	 * @param callback - who to report selected positions to.
	 * @param offsets - A collection of CKPositions that describe offsets from the selected tile that will be selected.
	 */
	void selectAreaOffsets(CKPosition originLocation,double minDistance,double maxDistance,
			                     CKSelectedPositionsListeners callback,	Collection<CKPosition> offsets);
	
	
	/**	The graphics engine will highlight orginLocation at starttime. 
	 *  If the list of offsets are not null,  the engine will highlight each tile described by the offset plus the orginLocation.
	 *  Otherwise it will just highlight the orginLocation
	 * @param originLocation 
	 * @param startTime
	 * @param offsets
	 */
	void highlightArea(CKPosition originLocation,int startTime,Iterable<CKPosition>offsets);	
			
	
	/**
	 * 	Unhighlights all highlighted locations at startTime.
	 * @param startTime
	 */
	void unhighlight(int startTime);	
	
	
	
	/**
	 * Adds an action that effectively does nothing, but keep the engine in a busy state.
	 * @param tid
	 * @param startTime - what frame the action starts on
	 * @param endTime   - what frame the action completes on
	 */
	void createNullAction(int tid, int startTime,int endTime);
	
	
	public CKGrid getGrid();

	void selectAreaOffsets(CKPosition originLocation,
			Collection<CKPosition> possibles,
			CKSelectedPositionsListeners callback,
			Collection<CKPosition> offsets);

	


}
