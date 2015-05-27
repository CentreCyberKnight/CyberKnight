package ckGameEngine;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.beans.DefaultPersistenceDelegate;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ckCommonUtils.CKAreaPositions;
import ckCommonUtils.CKPath;
import ckCommonUtils.CKPosition;
import ckCommonUtils.CKXMLAsset;
import ckEditor.CKGridLayerEditor;
import ckGameEngine.CKGridActorOverLay.GridActorOverLayPersistenceDelegate;
import ckGraphicsEngine.BadInstanceIDError;
import ckGraphicsEngine.CK2dGraphicsEngine;
import ckGraphicsEngine.CKCoordinateTranslator;
import ckGraphicsEngine.CKGraphicsPreviewGenerator;
import ckGraphicsEngine.UnknownAnimationError;
import ckGraphicsEngine.layers.CKGridGraphicsLayer;

public class CKGrid implements CKXMLAsset<CKGrid>
{

	public static final int STEP_UP_COST = 2;
	public static final int SLIDE_UP_COST = 2;
	public static final int SLIDE_DOWN_COST = -2;

	public static final int JUMP_UP_COST = 3;
	public static final int JUMP_DOWN_COST = 2;
	public static final int JUMP_OVER_COST = 2;

	public static final int TURN_COST = 1;
	public static final int NUM_OF_DIRECTIONS = 4;

	CKAbstractGridItem[][] positions;
	int width;
	int height;
	String AID = "";
	String name = "";
	String description = "";

	public CKGrid()
	{
		this(1, 1);
	}

	public CKGrid(int x, int y)
	{
		width = 0;
		height = 0;

		resize(x, y);
		/*
		 * positions=new CKAbstractGridItem[x][y]; for(int i=0;i<x;i++) for(int
		 * j=0;j<y;j++) { positions[i][j] = new CKGridItem(); }
		 */
	}

	public void setPosition(CKAbstractGridItem item, int x, int y)
	{
		positions[x][y] = item;
		if (item != null)
		{
			item.setPos(x, y, 0);
		}
	}

	public void setPosition(CKAbstractGridItem item, CKPosition pos)
	{
		setPosition(item, (int) pos.getX(), (int) pos.getY());
	}

	public void addToPosition(CKAbstractGridItem item, int x, int y)
	{
		CKAbstractGridItem dest = positions[x][y];
		if (dest == null)
		{
			setPosition(item, x, y);
			item.onAdd();
		} else
		{
			dest.addOn(item);
		}

	}

	public void addToPosition(CKAbstractGridItem item, CKPosition pos)
	{
		addToPosition(item, (int) pos.getX(), (int) pos.getY());
	}

	public CKAbstractGridItem getPosition(int x, int y)
	{
		return positions[x][y];
	}

	public CKAbstractGridItem getPosition(CKPosition pos)
	{
		return positions[(int) pos.getX()][(int) pos.getY()];
	}

	public CKAbstractGridItem getTopPosition(int x, int y)
	{
		return getPosition(x, y).getTop();
	}

	public CKAbstractGridItem getTopPosition(CKPosition pos)
	{
		return getPosition(pos).getTop();
	}

	/**
	 * returns how many squares the actor can jump over for a given amount of
	 * CP. 0 is no squares,
	 * 
	 * @param actor
	 * @param dir
	 * @param CP
	 * @return
	 */
	public int calcJumpOverDistance(CKGridActor actor, Direction dir, int CP)
	{

		// cost = JUMP_OVER_COST+MATH.pow(JUMP_OVER_COST,squares)
		// inserse would be
		// MATH.floor(MATH.log(cost - JUMP_OVER_COST,JUMP_OVER_COST))
		int max = (int) Math.floor((Math.log(CP - JUMP_OVER_COST) / Math
				.log(JUMP_OVER_COST)));

		int distance = 0;

		CKPosition lastDest = actor.getPos(); // get the last actor's position
		int beginingHeight = (int) actor.getPos().getZ();
		Stack<CKAbstractGridItem> spots = new Stack<>(); // potential spots
															// actor can move

		while (distance < max)
		{
			CKPosition dest = dir.getAheadPosition(lastDest, width, height); // move
																				// toward
																				// the
																				// direction
			if (dest.compareTo(lastDest) == 0)
			{
				break;
			} // within a proximity, then break

			CKAbstractGridItem destItem = getPosition((int) dest.getX(),
					(int) dest.getY());
			if (destItem.getTotalHeight() > beginingHeight)
			{
				break;
			}
			spots.add(destItem);
			distance++;
			lastDest = dest;
		}
		// distance is as far as we could jump, but we still need a target on
		// which we can land

		while (!spots.isEmpty())
		{
			CKAbstractGridItem spot = spots.pop();
			if (spot.getTotalHeight() > beginingHeight - 2
					&& spot.getFinalMoveCost() < 100)
			{
				return distance; // we found a spot.
			}
			distance--;
		}
		return 0;

	}

	/**
	 * This function gonna calculates the required CP for a given jump distance.
	 * 
	 * @param actor
	 * @param dir
	 * @param targetDistane
	 * @return the amount of CP
	 */
	public int calcCPForJumpDistance(CKGridActor actor, Direction dir,
			int targetDistance, int limitCP)
	{
		int cp = 0;
		int distance = 0;
		while (distance != targetDistance)
		{
			cp++;
			distance = this.calcJumpOverDistance(actor, dir, cp);
			if (cp > limitCP)
			{
				return limitCP;
			}
		}
		return cp;
	}

	/**
	 * 
	 * @param actor
	 * @param dir
	 * @param up
	 *            jumping up or jumping down
	 * @return
	 */
	public int costJumpUp(CKGridActor actor, Direction dir, boolean up)
	{
		CKPosition dest = dir.getAheadPosition(actor.getPos(), width, height);
		if (dest.compareTo(actor.getPos()) == 0)
		{// bouncing on edge
			return Integer.MAX_VALUE;
		}
		CKAbstractGridItem destItem = getPosition((int) dest.getX(),
				(int) dest.getY());

		int height = destItem.getTotalHeight();
		int moveCost = destItem.getFinalMoveCost();

		int diffHeight = height - (int) (actor.getPos().getZ());

		// can I land there?
		if (moveCost >= 100)
		{
			return Integer.MAX_VALUE;
		}

		if (up && diffHeight > 0)
		{
			return (int) Math.pow(JUMP_UP_COST, diffHeight);
		} else if (!up && diffHeight < 0)
		{
			return (-JUMP_DOWN_COST) * diffHeight;
		} else
		{
			return Integer.MAX_VALUE;
		}

	}

	/**
	 * 
	 * 
	 * @param shovee
	 * @param dir
	 *            Direction that I am getting shoved
	 * @return
	 */
	public int costSlide(CKGridActor shovee, Direction dir)
	{

		CKPosition dest = dir.getAheadPosition(shovee.getPos(), width, height);
		if (dest.compareTo(shovee.getPos()) == 0)
		{// bouncing on edge
			return Integer.MAX_VALUE;
		}
		CKAbstractGridItem destItem = getTopPosition((int) dest.getX(),
				(int) dest.getY());
		CKAbstractGridItem sourceItem = shovee.getPrev();

		int height = destItem.getTotalHeight();
		int slideCost = destItem.getFinalSlideCost() + shovee.getTotalWeight();
		Direction lowside = sourceItem.getLowSide();

		if (sourceItem.getLowSide() == dir) // check if downhill
		{
			slideCost += SLIDE_DOWN_COST;
		} else if (sourceItem.getLowSide() == dir.getOppositeDirection()) // up
																			// hill
		{
			slideCost += SLIDE_UP_COST;
		}

		if (height > shovee.getPos().getZ() + 1
				|| slideCost == Integer.MAX_VALUE)
		{// can't push up that far
			return Integer.MAX_VALUE;
		} else if (height == sourceItem.getPos().getZ() + 1)
		{
			if (destItem.getLowSide() == dir.getOppositeDirection())
			{
				return slideCost;
			} else
			{
				return Integer.MAX_VALUE;
			}
		} else
		{
			return slideCost;
		}
	}

	public int costStep(CKAbstractGridItem item, Direction dir)
	{

		CKPosition dest = dir.getAheadPosition(item.getPos(), width, height);
		if (dest.compareTo(item.getPos()) == 0)
		{// bouncing on edge
			return Integer.MAX_VALUE;
		}
		CKAbstractGridItem destItem = getPosition((int) dest.getX(),
				(int) dest.getY());

		int height = destItem.getTotalHeight();
		int moveCost = destItem.getFinalMoveCost();

		if (height > item.getPos().getZ() + 1 || moveCost == Integer.MAX_VALUE)
		{// too
			// far
			// to
			// jump
			return Integer.MAX_VALUE;
		} else if (height == item.getPos().getZ() + 1)
		{
			return moveCost + STEP_UP_COST;
		} else if (height >= item.getPos().getZ() - 1)
		{
			return moveCost;
		} else
		// need to drop not step
		{
			return Integer.MAX_VALUE;
		}
	}

	/**
	 * returns the lowest cost to move forward one square
	 * 
	 * @param actor
	 *            - moving person
	 * @param dir
	 *            - direction of movement
	 * @return lowestCost for a move associated with a dir not just the
	 *         lowestCost, but a pair of lowestCost and d
	 */
	public Pair<Integer, String> lowestMoveCost(CKGridActor actor, Direction dir)
	{
		// int slideCost = this.costSlide(item, dir);
		// an actor cannot slide on his own, someone has to push him
		int stepCost = this.costStep(actor, dir);
		int jumpUpCost = this.costJumpUp(actor, dir, true);
		int jumpDownCost = this.costJumpUp(actor, dir, false);

		int[] data = { stepCost, jumpUpCost, jumpDownCost };
		Arrays.sort(data);
		int minCost = data[0];
		if (stepCost == minCost)
		{
			return new Pair<Integer, String>(minCost, "step forward, ");
		} else if (jumpUpCost == minCost)
		{
			return new Pair<Integer, String>(minCost, "jump up, ");
		} else
		{
			return new Pair<Integer, String>(minCost, "jump down, ");
		}
	}

	/*
	 * helper inner class to represent a pair of integer and string
	 */
	public class Pair<Integer, String>
	{
		public int getCost()
		{
			return cost;
		}

		public String getMove()
		{
			return move;
		}

		public final int cost;
		public final String move;

		public Pair(int cost, String move)
		{
			this.cost = cost;
			this.move = move;
		}
	}

	/*
	 * find the cost and action required to move from one direction to another
	 */
	public Pair<Integer, String> costToTurn(Direction currDir, Direction nextDir)
	{
		// TODO Auto-generated method stub
		// given 2 directions, return the cost to turn from one to the other
		int curr = this.convertDirToInt(currDir);
		int next = this.convertDirToInt(nextDir);
		if (Math.abs(curr - next) == 2)
		{
			return new Pair<Integer, String>(2, "turn right, turn right, ");
		} else
		{
			if (curr > next)
			{
				if (curr - next == 1)
				{
					return new Pair<Integer, String>(1, "turn left, ");
				} else
				{
					return new Pair<Integer, String>(1, "turn right, ");
				}
			} else if (curr < next)
			{
				if (curr - next == -1)
				{
					return new Pair<Integer, String>(1, "turn right, ");
				} else
				{
					return new Pair<Integer, String>(1, "turn left, ");
				}
			} else
			{
				return new Pair<Integer, String>(0, "no turn, ");
			}
		}
	}

	/**
	 * updates the grid node information after moving to a position TODO:
	 * shorten this method
	 */
	public void bfs_UpdatePositionCost(GridNode[][][] arrays, CKPosition pos,
			int remaningCP, Direction dir, CKPath parentNode, String action)
	{
		int x = (int) pos.getX();
		int y = (int) pos.getY();
		int z = this.convertDirToInt(dir);
		if (arrays[x][y][z].getPos() == null && !arrays[x][y][z].isVisited)
		{
			arrays[x][y][z].setPos((CKPosition) pos.clone());
			arrays[x][y][z].setRemainingCP(remaningCP);
			arrays[x][y][z].setDir(dir);
			arrays[x][y][z].setParentNode(parentNode);
			arrays[x][y][z].setAction(action);
		} else
		{
			// if pos is visited before, check to see if the remainingCP
			// at this time is higher than the one before
			if (arrays[x][y][z].isVisited)
			{
				if (arrays[x][y][z].getRemainingCP() < remaningCP)
				{
					arrays[x][y][z].setRemainingCP(remaningCP);
					arrays[x][y][z].setVisited(false);
					arrays[x][y][z].setDir(dir);
					arrays[x][y][z].setParentNode(parentNode);
					arrays[x][y][z].setAction(action);
				}
			}
		}
	}

	/*
	 * convert direction to a digit value
	 */
	public int convertDirToInt(Direction dir)
	{
		// TODO Auto-generated method stub
		// figure out the index associated with this direction - dir
		int dx = dir.dx;
		int dy = dir.dy;
		if (dx == 0 && dy == -1)
		{
			return 0;
		} else if (dx == 1 && dy == 0)
		{
			return 1;
		} else if (dx == 0 && dy == 1)
		{
			return 2;
		} else if (dx == -1 && dy == 0)
		{
			return 3;
		}
		return -1;
	}

	static class GridNode
	{
		CKPosition pos;
		int remainingCP;
		boolean isVisited;
		Direction dir;
		CKPath parentNode;
		String action;

		public GridNode()
		{
			this.pos = null;
			this.remainingCP = 0;
			this.isVisited = false;
			this.dir = null;
			this.parentNode = null;
			this.action = "None";
		}

		public CKPosition getPos()
		{
			return pos;
		}

		public void setPos(CKPosition pos)
		{
			this.pos = pos;
		}

		public int getRemainingCP()
		{
			return remainingCP;
		}

		public void setRemainingCP(int remainingCP)
		{
			this.remainingCP = remainingCP;
		}

		public boolean isVisited()
		{
			return isVisited;
		}

		public void setVisited(boolean isVisited)
		{
			this.isVisited = isVisited;
		}

		public Direction getDir()
		{
			return dir;
		}

		public void setDir(Direction dir)
		{
			this.dir = dir;
		}

		public CKPath getParentNode()
		{
			return this.parentNode;
		}

		public void setParentNode(CKPath parentNode)
		{
			this.parentNode = parentNode;
		}

		public String getAction()
		{
			return this.action;
		}

		public void setAction(String action)
		{
			this.action = action;
		}

	}

	/**
	 * pick a node to explore next in arrays node has highest remaining CP and
	 * hasn't been visited before
	 * 
	 * @param arrays
	 * @return the node which has the highest remainingCP
	 */
	public GridNode pickNextNode(GridNode[][][] arrays)
	{

		int cp = 0;
		GridNode node = null;
		for (int i = 0; i < this.width; i++)
		{
			for (int j = 0; j < this.height; j++)
			{
				for (int k = 0; k < this.NUM_OF_DIRECTIONS; k++)
				{
					if (arrays[i][j][k].remainingCP >= cp
							&& !arrays[i][j][k].isVisited
							&& arrays[i][j][k].getPos() != null
							&& arrays[i][j][k].getDir() != null)
					{
						cp = arrays[i][j][k].remainingCP;
						node = arrays[i][j][k];
					}
				}
			}
		}
		return node;
	}

	/**
	 * This function calculates all possible positions a grid item can reach
	 * given an amount of CP limitation: for now, if it takes the jump over
	 * action, it cannot take any other action use a two dimensional array
	 * instead of the queue no need to use any hashtable because two CKPosition
	 * of the same x and y don't yield the same object.
	 * 
	 * Special note: be very careful when using item.getPos(). It's probably
	 * best to make a copy of a CKPosition unless you deliberately want to
	 * change its coordinates.
	 * 
	 * TODO: make this method shorter, by refactoring
	 * 
	 * @param item
	 * @param maxCP
	 * @param dir
	 * @return a list of possible positions the item can reach
	 **/
	public CKAreaPositions allPositionsReachable(CKGridActor item, int maxCP)
	{
		GridNode[][][] arrays = new GridNode[this.width][this.height][this.NUM_OF_DIRECTIONS];
		ArrayList<CKPath> positions = new ArrayList<CKPath>();

		for (int i = 0; i < this.width; i++)
		{
			for (int j = 0; j < this.height; j++)
			{
				for (int k = 0; k < this.NUM_OF_DIRECTIONS; k++)
				{
					arrays[i][j][k] = new GridNode();
				}
			}
		}

		int x = (int) item.getPos().getX();
		int y = (int) item.getPos().getY();
		int z = this.convertDirToInt(item.getDirection());
		arrays[x][y][z].setPos((CKPosition) item.getPos().clone());
		arrays[x][y][z].setRemainingCP(maxCP);
		arrays[x][y][z].setDir(item.getDirection());

		// get the one with highest remaining CP
		GridNode node = this.pickNextNode(arrays);

		while (node != null)
		{
			// get attributes
			CKPosition bestMovePosition = node.getPos();
			int highestRemainingCP = node.getRemainingCP();
			Direction movingDir = node.getDir();

			node.setVisited(true);
			CKPath path = new CKPath(bestMovePosition.getX(),
					bestMovePosition.getY(), bestMovePosition.getZ(),
					bestMovePosition.getDepth(), node.getParentNode(),
					node.getAction());
			positions.add(path);
			// change the position and direction of the item
			item.setPos(bestMovePosition);
			item.setDirection(movingDir);

			// now handle moving forward, either by stepping up or jumping
			// up/down
			CKPosition dest = movingDir.getAheadPosition(item.getPos(), width,
					height);
			int lowestCost = this.lowestMoveCost(item, movingDir).getCost();
			String action = this.lowestMoveCost(item, movingDir).getMove();

			this.bfs_UpdatePositionCost(arrays, dest, highestRemainingCP
					- lowestCost, movingDir, path, action);

			// reachable positions by jump over
			action = "jump over, "; // hacky, manually set the action name
			int jumpOverDistance = this.calcJumpOverDistance(item, movingDir,
					highestRemainingCP);

			if (jumpOverDistance > 0)
			{
				CKPosition currPos = (CKPosition) item.getPos().clone();
				while (jumpOverDistance != 0)
				{
					currPos.setX((int) currPos.getX() + movingDir.dx);
					currPos.setY((int) currPos.getY() + movingDir.dy);
					int usedCP = this.calcCPForJumpDistance(item, movingDir,
							jumpOverDistance, highestRemainingCP);
					this.bfs_UpdatePositionCost(arrays, currPos,
							highestRemainingCP - usedCP, movingDir, path,
							action);
					jumpOverDistance--;
				}
			}

			// now handle turning: right, left, or double-rights
			for (Direction dir : Direction.values())
			{
				if (dir.name() != "NONE"
						&& this.convertDirToInt(dir) != this
								.convertDirToInt(movingDir))
				{
					int costToTurn = this.costToTurn(movingDir, dir).getCost();
					action = this.costToTurn(movingDir, dir).getMove();
					this.bfs_UpdatePositionCost(arrays, bestMovePosition,
							highestRemainingCP - costToTurn, dir, path, action);
				}
			}

			// finding the next node
			node = this.pickNextNode(arrays);
		}

		CKPath[] allPositions = new CKPath[positions.size()];
		positions.toArray(allPositions);
		return new CKAreaPositions(item.getPos().getX(), item.getPos().getY(),
				item.getPos().getZ(), item.getPos().getDepth(), allPositions);
	}

	/**
	 * Assumes that it is OK to move. either from a step, jump, or drop
	 * 
	 * @param item
	 * @param dir
	 * @return the time the actor will reach the destination
	 */

	public int move(CKGridActor item, Direction dir)
	{

		CKPosition dPos = dir.getAheadPosition(item.getPos(), width, height);

		CKAbstractGridItem dest = getTopPosition((int) dPos.getX(),
				(int) dPos.getY());
		dPos.setZ(dest.getTotalHeight());

		if (dPos.compareTo(item.getPos()) == 0)
		{// bouncing on edge
			return 0;
		}
		return move(item, dPos);
	}

	/**
	 * Moves item to top of CKPosition.
	 * 
	 * @param item
	 * @param pos
	 * @return the time the actor will reach the destination
	 */
	public int move(CKGridActor item, CKPosition pos)
	{
		CKAbstractGridItem dest = getPosition((int) pos.getX(),
				(int) pos.getY());
		int endtime = 0;

		if (!CKGameObjectsFacade.unitTest)
		{
			Quest quest = CKGameObjectsFacade.getQuest();

			endtime = item.drawMove(item.getPos(), pos, quest.getStartTime());
		}

		dest.stepOn(item);
		/*
		 * if(! CKGameObjectsFacade.unitTest) { Quest quest =
		 * CKGameObjectsFacade.getQuest();
		 * 
		 * endtime = item.drawMove(pos,item.getPos(),endtime); }
		 */
		return endtime;
	}

	public void setDirection(CKGridActor target, Direction direction)
	{

		if (CKGameObjectsFacade.unitTest == false)
		{// only run pictures if
			// there is a quest.
			Quest quest = CKGameObjectsFacade.getQuest();
			CK2dGraphicsEngine engine = CKGameObjectsFacade.getEngine();
			if (engine != null)
			{

				try
				{
					engine.setAnimation(0, target.getInstanceID(),
							direction.toString(), quest.getStartTime());
				} catch (BadInstanceIDError | UnknownAnimationError e)
				{
					e.printStackTrace();
				}
			}

		}
		target.setDirection(direction);
	}

	/**
	 * @return the width
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * @return the height
	 */
	public int getHeight()
	{
		return height;
	}

	public void resize(int _width, int _height)
	{

		this.width = _width;
		this.height = _height;

		positions = new CKAbstractGridItem[width][height];
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
			{
				positions[i][j] = new CKGridItem();
				positions[i][j].setPos(new CKPosition(i, j));
			}

	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(int width)
	{
		resize(width, this.height);
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(int height)
	{
		resize(this.width, height);
	}

	/**
	 * @return the positions, only for XMLEncode
	 */
	public CKAbstractGridItem[][] getPositions()
	{
		return positions;
	}

	/**
	 * @param positions
	 *            the positions to set, only for XMLDecode
	 */
	public void setPositions(CKAbstractGridItem[][] positions)
	{
		this.positions = positions;
	}

	public void drawPosition(int x, int y, int frame, ImageObserver observer,
			CKCoordinateTranslator translator, Graphics g)
	{
		getPosition(x, y).drawItem(frame, observer, translator, g);
	}

	@Override
	public void writeToStream(OutputStream out)
	{
		XMLEncoder e = new XMLEncoder(new BufferedOutputStream(out));
		e.setPersistenceDelegate(getClass(), new DefaultPersistenceDelegate(
				new String[] { "width", "height" }));

		e.setPersistenceDelegate(CKSharedGridItem.class,
				new DefaultPersistenceDelegate(new String[] { "sharedGI" }));
		e.setPersistenceDelegate(CKGridActorOverLay.class,
				new GridActorOverLayPersistenceDelegate());

		e.writeObject(this);
		e.close();
	}

	/*
	 * class GridPersistenceDelegate extends DefaultPersistenceDelegate {
	 * protected void initialize(Class type, Object oldInstance, Object
	 * newInstance, Encoder out) { super.initialize(type, oldInstance,
	 * newInstance, out);
	 * 
	 * CKArtifact art = (CKArtifact) oldInstance;
	 * 
	 * Iterator<CKSpell> iter = art.getSpells(); while(iter.hasNext()) { CKSpell
	 * inst = iter.next(); out.writeStatement(new
	 * java.beans.Statement(oldInstance, "addSpell", // Could also use
	 * "addElement" here. new Object[]{inst}) ); } } }
	 */

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String getAID()
	{
		return AID;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	@Override
	public void setAID(String a)
	{
		AID = a;
	}

	@Override
	public JComponent getXMLAssetViewer()
	{
		return getXMLAssetViewer(ViewEnum.STATIC);
	}

	@Override
	public JComponent getXMLAssetViewer(ViewEnum v)
	{
		BufferedImage img = CKGraphicsPreviewGenerator.createLayerPreview(
				new CKGridGraphicsLayer(getName(), getDescription(), this,
						height), 200, 200);
		JComponent panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(new JLabel(new ImageIcon(img)));
		panel.add(new JLabel(getDescription(), JLabel.CENTER),
				BorderLayout.NORTH);
		return panel;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CKGridLayerEditor getXMLPropertiesEditor()
	{

		return new CKGridLayerEditor(this);
	}

	public static void main(String[] s)
	{

		/*
		 * CKGridFactory factory = CKGridFactory.getInstance();
		 * 
		 * CKGrid grid = factory.getAsset("asset4401570641036555939");
		 * 
		 * JFrame frame=new JFrame();
		 * 
		 * CKGridLayerEditor e=new CKGridLayerEditor(grid); frame.add(e);
		 * frame.pack(); frame.setVisible(true); frame.addWindowListener(new
		 * WindowAdapter(){ public void windowClosing(WindowEvent e) {
		 * System.exit(0); } } );
		 */

		CKGrid grid = new CKGrid(10, 10);
		// now to place some tiles.

		for (int i = 2; i <= 8; i++)
			for (int j = 2; j <= 8; j++)
			{
				CKAbstractGridItem land = new CKGridItem();
				land.setAssetID("blue");
				land.setMoveCost(1);
				grid.setPosition(land, i, j);
			}

		for (int i = 4; i <= 6; i++)
			for (int j = 4; j <= 6; j++)
			{
				CKAbstractGridItem block = new CKGridItem();
				block.setAssetID("pineBlock");
				block.setMoveCost(2);
				block.setItemHeight(1);
				grid.addToPosition(block, i, j);
			}

		CKAbstractGridItem bigBlock = new CKGridItem();
		bigBlock.setAssetID("stoneBlock");
		bigBlock.setMoveCost(1);
		bigBlock.setItemHeight(2);
		grid.addToPosition(bigBlock, 5, 5);

		grid.writeToStream(System.out);
	}

}