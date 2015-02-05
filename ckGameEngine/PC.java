package ckGameEngine;
/**
 * @author CyberKnight Graphics Team
 * Nathan, Tina, Phillip
 *
 */

//TODO refactor this into two (or three classes)
@Deprecated
public class PC extends PlaceHolder implements Comparable<PC> 
{

	@Override
	public int compareTo(PC o)
	{
		// TODO Auto-generated method stub
		return 0;
	}
/*	//character data - true between quests
	int assetId;                        //picture
	String name;
	String backstory;
	CKBook abilites;
	int priority; //should be part of the abilities..
	HashMap <CKPage,CKArtifact >EquippedList;

	CKCharacter characterData;
	
	//true only in a quest...?
	private int tId;                
	int cyberPoints;              
	
	PCTurnController controller;
	int turnNumber;
	Direction direction;
	
	EffectList effects;
	
	int instanceId;

	//perhaps shouldn't be here anyway?
	Grid grid;
	CK2dGraphicsEngine engine;
	Quest quest;


	*//**
	 * This creates a default PC at a specific location
	 * @param pos
	 * @ 
	 * @calls None
	 * @calledBy
	 *//*
	public PC(CKGridPosition pos, String assetID) 
	{
		this(new CKCharacter(assetID), Direction.NORTHEAST, pos);
	}

	


	*//**
	 * PC constructor that sets species, actionPoints, direction, position, and checkOutPosition
	 * @param species
	 * @param cyberPoints
	 * @param direction
	 * @param position
	 * @calls checkOutPosition
	 * @calledBy 
	 *//*
	public PC(CKCharacter characterData,  Direction direction, CKPosition position) 
	{
		this.characterData = characterData;
		CKBook abilities = characterData.getAbilties();
		this.direction = direction;
		this.controller = new PCNullController(this);
		this.cyberPoints = abilities.getChapter(MAX_CP).getValue();
		
//		characterData.getAssetID();
		
		//these should not be set here....
		this.engine=CKGameObjectsFacade.getEngine();
		this.quest=CKGameObjectsFacade.getQuest();
		this.grid = quest.getGrid();
		
		
		this.abilites=new CKBook();
		CKChapter chap = new CKChapter("move", 1);
		chap.addPage(new CKPage("forward"));
		chap.addPage(new CKPage("left"));
		chap.addPage(new CKPage("right"));
		abilites.addChapter(chap);
		CKChapter chap2 = new CKChapter(CKPlayerObjectsFacade.CPPERROUND,1);
		abilites.addChapter(chap2);
		
		
		CKGridPosition pos = quest.getGrid().getPositionFromList((int)position.getX(),(int)position.getY());
		if ( ! checkOutPosition(pos) )
		{
			System.err.println("Someone is at that position already"+pos);
			return;
		}
		try {
			//System.out.println(engine);
			engine.loadAsset(tId, characterData.getAssetID());
			System.out.println("id is "+characterData.getAssetID());
			this.instanceId = engine.createInstance(tId, characterData.getAssetID(),position,30,CKGraphicsLayer.SPRITE_LAYER);
			setDirection(direction);
		} catch (LoadAssetError e) {
				
		}

	} 
	
	public PC(String assetID, Direction direction, CKPosition pos)
	{
		this(new CKCharacter(assetID),direction,pos);
	}
	
	
	
*//**
 * Go through my list of triggers and if they do not get the job done,
 * Go through the global triggers to see if there is anything solved.
 * @param boss
 * @param cast
 *//*
	public void targetSpell(CKGameActionListenerInterface boss, CKSpellCast cast)
	{
		TriggerResult result = characterData.getTriggers().doTriggers( 
				boss,false, cast);
		
		if(result ==TriggerResult.UNSATISFIED)
		{
			CKGameObjectsFacade.targetSpell(boss,cast);
		}
		//FIXME - need failover for doTriggerlist
	}


	public CKBook getAbilties()
	{
		return characterData.getAbilties();
		
	}
	
	
	public Quest getQuest() {
		return quest;
	}


	
	public void setController(PCTurnController controller) {
		this.controller = controller;
	}


	*//**
	 * @return the assetId
	 *//*
	public String getAssetId() {
		return characterData.getAssetID();
	}

	*//**
	 * @param assetId the assetId to set
	 *//*
	
	public void setAssetId(int assetId) {
		this.assetId = assetId;
	}

	*//**
	 * @return the instanceId
	 *//*
	public int getInstanceId() {
		return instanceId;
	}

	*//**
	 * @param instanceId the instanceId to set
	 *//*
	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}


	*//**
	 * @return the grid
	 *//*
	public Grid getGrid() {
		return grid;
	}

	*//**
	 * @param grid the grid to set
	 *//*
	public void setGrid(Grid grid) {
		this.grid = grid;
	}
	
	
	
	public int getTurnNumber()
	{
		return turnNumber;
	}

	
	*//**
	 * @return the actionPoints
	 * @calledBy
	 *//*
	public int getCyberPoints() {
		return cyberPoints;
	}

	*//**
	 * @param actionPoints the actionPoints to set
	 *//*
	public void setCyberPoints(int cyberPoints)
	{
		this.cyberPoints = cyberPoints;
	}

	*//**
	 * @return the direction the PC is facing
	 * @calledBy
	 *//*
	public Direction getDirection() 
	{
		return direction;
	}
	

	*//**
	 * @param the direction the PC is to be set:
	 * @calledBy
	 *  0 for north, 1 for east and proceeds clockwise
	 *//*
	public void setDirection(Direction direction) 
	{
		this.direction = direction;
		try{
			engine.setAnimation(tId, instanceId, this.direction.toString(), quest.getStartTime());
		} catch (BadInstanceIDError e) {
			e.printStackTrace();
		} catch (UnknownAnimationError e) {
			e.printStackTrace();
		}
		quest.setStartTime(quest.getStartTime()+30);
	}
	
	public void turnLeft()
	{
		setDirection(this.direction.getLeftNeightbor());
	}
	
	public void turnRight()
	{
		setDirection(this.direction.getRightNeightbor());
	}
	
	
	*//**
	 * @return the priority
	 *//*
	public int getPriority()
	{
		int CPUsed = 5; //TODO store this value somewhere, TurnController?
		 double priority = (10+CPUsed) /characterData.getAbilties().getChapter(CKPropertyStrings.SPEED).getValue();
		 		
		return (int) Math.ceil(priority);
	}


	*//**
	 * @param priority the priority to set
	 *//*
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public boolean isDead()
	{
		return this.getCyberPoints()<=0;
	}

	*//**
	 * moves PC one position infront of it
	 * @calls getNextPosition, private_move
	 * @calledBy
	 *//*
		
	public int move(int numberOfSquares)
	{
		return private_move(this.direction.getAhead(this.getPosition(), numberOfSquares, grid));
	}
	
	public int move(Direction direction, int numberOfSquares)
	{
		this.direction=direction;
		return private_move(this.getNextPosition());
	}
	
	 *//** moves pc to the location specified by the x and y
	 * @param x coord 
	 * @param y coord
	 *//*
	
	 * Warning! Uncommenting this may conflict code
	public void move(int x, int y)	
	{
		CKGridPosition pos=Grid.getPositionFromList(x, y);
		private_move(pos);
	}
	
	
	*//**
	 * moves PC to new Position
	 * @param loc to move to
	 * @calls private_move
	 * @calledBY
	 *//*
	
	public int  move(CKGridPosition pos)
	{
		//System.out.println("Calls private");
		return private_move(pos);
	}
	
	

	
	*//**
	 * private function to move
	 * @param loc
	 * @calls isPossible, switchTile
	 * @calledBy move, move(CKPosition)
	 *//*
	private int private_move(CKGridPosition pos)
	{
		//TODO when you run into a wall if moving more than 2 spaces needs to break
		CKGridPosition startPos=this.getPosition();
		if(startPos==pos)
		{
			System.out.println("You can't do it, you turn away in shame and spend your turn crying ");
			setDirection(this.direction.getOppositeDirection(direction));
			return quest.getStartTime();
		}
		//Direction dir=getDirectionTo(pos);
		//setDirection(dir);
		turn(pos);
		int startTime=0;
		while(!(isNextTo(pos)))
		{
			if(isPossible(getNextPosition()))
			{
				switchTile(getNextPosition());
			}
			else
			{
				try {
					startTime=engine.move(tId,instanceId,quest.getStartTime(),startPos,grid.getPositionFromList((int)this.getPosition().getX(),(int)this.getPosition().getY()),10);
				} catch (BadInstanceIDError e) {
					e.printStackTrace();
				}
				//TODO camera shake
				return startTime;
			}
			//Uncomment this if you want to traverse anything but a straight line
			//turn(dir);
		}
		if(isPossible(pos) )
		{
			switchTile(pos);
			try {
				startTime=engine.move(tId,instanceId,quest.getStartTime(),startPos,grid.getPositionFromList((int)this.getPosition().getX(),(int)this.getPosition().getY()),10);
			} catch (BadInstanceIDError e) {
				e.printStackTrace();
				System.out.println("Bad instance Error");
			}
			return startTime;
		}
		try {
			startTime=engine.move(tId,instanceId,quest.getStartTime(),startPos,grid.getPositionFromList((int)this.getPosition().getX(),(int)this.getPosition().getY()),10);
		} catch (BadInstanceIDError e) {
			e.printStackTrace();
		}
		return startTime;
	}
	
	*//**
	 * @return CKPosition in front of PC
	 * @calls getPositionFromList
	 * @calledBy move
	 *//*
	public CKGridPosition getNextPosition()
	{
		CKGridPosition next = direction.getAhead(this.getPosition(), grid);
		return next;
	}
	
	
	*//**
	 * turns the PC to face the tile it is going to move onto
	 * @param poc
	 * @calls getPosition
	 * @called by private_move
	 *//*
	public void turn(CKPosition pos)
	{
		Direction dir = getDirectionTo(pos);
		this.direction=dir;
		try{
			engine.setAnimation(tId, instanceId, this.direction.toString(), quest.getStartTime());
		} catch (BadInstanceIDError e) {
			e.printStackTrace();
		} catch (UnknownAnimationError e) {
			e.printStackTrace();
		}
	}
	
	*//**
	 *	gets sprite's direction to given Position
	 *	takes into consideration that pos may not be on sprite's axis
	 *	renders closest direction. example: if pos is 5w and 1n, returns west
	 *	we can use this to move to a Position, even if not on axis
	 *	get direction that leads closest to pos, move one tile that dir
	 * 	recalculate dir, that should render shortest route
	 * 	@param pos
	 * 	@calls getDirection
	 *  @calledBy 
	 * 	@return direction to face
	 *//*
	private Direction getDirectionTo(CKPosition pos)
	{
		return Direction.getDirectionTo(this.getPosition(), pos);
	}
	
	*//**
	 * checks to see if it is possible to move to the loc, based on action points and loc has a tenant 
	 * @param loc
	 * @return true if a move is possible, false if it is not
	 * @calls getPlaceHolder, isNextTo
	 * @calledBy private_move
	 *//*
	private boolean isPossible(CKGridPosition pos)
	{
		//TODO 
		//check to see if char has enough move points
		//does height difference hinder?
		//is loc checked out?
		if(pos.getPlaceHolder()==null)
		{
			return true;
		}
		return false;
	}
	
	*//**
	 * check to see the tile the PC is about to move to is next to it's current tile
	 * NOTE: make sure this is checked after the character turns or it will not work
	 * @param pos to move to 
	 * @calls getNextPosition
	 * @calledBy 
	 *//*
	private boolean isNextTo(CKGridPosition pos)
	{
		if(this.getNextPosition().equals(pos))
		{
			return true;
		}
		return false;
	}


	
	*//**
	 * switches the pos the PC is at to the loc
	 * @param loc
	 * @calls updateEffects, checkInPosition, checkOutLocation, useActionPoints
	 * @calledBy private_move
	 *//*
	private void switchTile(CKGridPosition pos)
	{
		//TODO update effects, add/remove any here once implemented
		//updateEffects();
		this.checkInPosition();
		checkOutPosition(pos);
	}
	
	*//**
	 * updates effects currently in PC's effect list
	 * @calls useActionPoints
	 * @called by private_move
	 *//*
	@SuppressWarnings("unused")
	private void updateEffects()
	{
		//effects.sortEffects();
		//effects.deleteExpired();
		//int damage = effects.damageCountdown();
		//useActionPoints(effects.damageCountdown());
	}
	
	*//**
	 * takes away action points which have been used
	 * @param numberOfPoints to take away
	 * @calledBy private_move
	 *//*
	public void useCyberPoints(int numberOfPoints)
	{
		cyberPoints-=numberOfPoints;
		System.out.println("Sprite uses/loses " + numberOfPoints + " action points, leaving "
				+ cyberPoints + " remaning");
	}


	@Override
	public int compareTo(PC that) {
		if(this.getPriority()==that.getPriority())
			return 0;
		else if(this.getPriority()>that.getPriority())
			return 1;
		return -1;
	}



	public void setTurnNumber(int i) {
		this.turnNumber=i;
	}




	public CKTeam getTeam()
	{
		
		return characterData.getTeam();
	}




	public String getName()
	{
		return characterData.getName();
	}




	public CKCharacter getCharacterData()
	{
		return characterData;
	}




	public PCTurnController getTurnController()
	{
		return controller;
	}

	*/
}
