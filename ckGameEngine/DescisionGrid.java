package ckGameEngine;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import ckCommonUtils.CKPosition;
import ckCommonUtils.InterpolationTools;
import ckDatabase.AimDescriptionFactory;
import ckGameEngine.CKGrid.GridNode;
import ckGameEngine.DescisionGrid.CharacterActionDescription;
import ckGameEngine.DescisionGrid.CharacterActionReport;
import ckGameEngine.DescisionGrid.DecisionNode;
import static ckCommonUtils.StreamOperators.*;
import static ckCommonUtils.InterpolationTools.*;
public class DescisionGrid
{

	static public class CharacterActionDescription
	{
	
		public final String action;
		public final String targetType;
		public final int[] costs;
	
		public final boolean combo; // can I use this with a walk action?
		public final boolean interpolate; // can I use CP values between those given?
	
		public final boolean aggregate; // if true add values of this type, else max
		public final int catagory;
		
		public final BiFunction<CharacterActionDescription, DecisionNode, double[]> evalActionConsumer;
	
		/**
		 * @param action
		 * @param targetType
		 * @param costs
		 * @param combo
		 * @param interpolate
		 * @param aggregate
		 * @param evalActionConsumer
		 */
		public CharacterActionDescription(
				String action,
				String targetType,
				int[] costs,
				boolean combo,
				boolean interpolate,
				boolean aggregate,
				int catagory,
				BiFunction<CharacterActionDescription, DecisionNode, double[]> evalActionConsumer)
		{
			this.action = action;
			this.targetType = targetType;
			this.costs = costs;
			this.combo = combo;
			this.interpolate = interpolate;
			this.aggregate = aggregate;
			this.catagory=catagory;
			this.evalActionConsumer = evalActionConsumer;
		}
	
		public CharacterActionReport evalAction(DecisionNode n)
		{
			return new CharacterActionReport(this,
					this.evalActionConsumer.apply(this, n));
		}
	
		public int[] getCosts()
		{
			return costs;
		}
	
	}

	static public class CharacterActionReport
	{
		final CharacterActionDescription descr;

		// the utility values based on the evaluation of the descr.
		final double[] values;

		/**
		 * @param descr
		 * @param values
		 */
		public CharacterActionReport(CharacterActionDescription descr,
				double[] values)
		{
			super();
			this.descr = descr;
			this.values = values;
		}
		
		public double evaluate(int CP,boolean moved)
		{
			if(moved && ! descr.combo) return 0;
			
			int before = -1; //or at
			int after = -1;
					
			for(int i=0;i<descr.costs.length;i++)
			{
				if(descr.costs[i] <=CP)
				{
					before=i;
				}
				else // descr.costs[i] > CP
				{
					after = i;
					break;
				}
			}
			if(before== -1) { return 0;}
			if(after==-1 || !descr.interpolate || descr.costs[before]==CP)
			{  //before was the largest value--no interpolation allowed
				return values[before];
			}
			else //I need to interpolate.
			{
				double percent = calcPercentBetween(descr.costs[before],CP,descr.costs[after]);
				double ret = calcLinearIterpolation(percent,values[before],values[after]);
				return ret;
			}
			
		}

		/**
		 * Returns the CAR with the most value this object or the parameter car.
		 * @param car
		 * @param cp
		 * @param moved
		 * @return
		 */
		public CharacterActionReport takeMax(CharacterActionReport car,
					int cp,boolean moved)
		{
			if(evaluate(cp,moved) >= car.evaluate(cp, moved))
			{
				return this;
			}
			else
			{
				return car;
			}
		}
		
		public CharacterActionReport add(CharacterActionReport car)
		{
			return add(car,descr.aggregate);
		}
		public CharacterActionReport add(CharacterActionReport car,boolean aggregate)
		{

			if (descr != car.descr)
			{
				return null;
			}

			if (aggregate)
			{
				double[] v = new double[values.length];
				for (int i = 0; i < v.length; i++)
				{
					v[i] = values[i] + car.values[i];
				}
				return new CharacterActionReport(descr, v);
			} 
			else	// return max value
			{
				if (values[values.length - 1] > car.values[values.length - 1])
				{
					return this;
				} else
				{
					return car;
				}
			}
		}

	}

	static public class DecisionNode
	{
		// will be working with copies, I don't want to damage them.
		public final CKPosition position;
		public final Direction direction;
		
		public double utility=0;
		HashSet<CharacterActionDescription> actions = new HashSet<>();
		HashMap<String, CharacterActionReport> reports = new HashMap<>();
		HashMap<String, CharacterActionReport> sources = new HashMap<>();
		public HashMap<Integer,CharacterActionReport> cmap;
	
		/**
		 * @param position
		 * @param direction
		 */
		public DecisionNode(CKPosition position, Direction direction)
		{
			super();
			this.position = position;
			this.direction = direction;
		}
	
		public void clear()
		{
			actions.clear();
		}
	
		public void addAction(CharacterActionDescription action)
		{
			actions.add(action);
		}
	
		public void evalActions()
		{
			for (CharacterActionDescription cad : actions)
			{
				CharacterActionReport report = cad.evalAction(this);
				if (reports.containsKey(cad.action))
				{
					reports.compute(cad.action, (s, car) -> car.add(report));
				} else
				{
					reports.put(cad.action, report);
				}
			}
		}
	
		public void addSource(CharacterActionReport report)
		{
			CharacterActionDescription cad = report.descr;
			if (sources.containsKey(cad.action))
			{
				sources.compute(cad.action, (s, car) -> car.add(report));
			} else
			{
				sources.put(cad.action, report);
			}
		}
	
		public Stream<CharacterActionReport> streamReports()
		{
			return reports.values().stream();
		}
	
		public Stream<CharacterActionReport> streamSources()
		{
			return sources.values().stream();
		}
	
		
		public double generateNodeValue(int cp,boolean moved)
		{
			cmap = new HashMap<>();
			streamSources().forEach(car->
			{
					
				int catagory = car.descr.catagory;
				cmap.merge(catagory, car, (oldItem,newItem) ->
					oldItem.takeMax(newItem,cp,moved));
			});
			
			utility = cmap.values().stream().mapToDouble(car->car.evaluate(cp,moved)).sum();
			return utility;

		}
		
		/*
		 * public void forEachAction(BiConsumer<? super String, ? super Double>
		 * action) { actions.forEach(action); }
		 * 
		 * public void evalActions(BiFunction<?super String,? super Double,?
		 * extends Double> action) { actions.replaceAll(action);
		 * 
		 * }
		 */
	
	}

	protected CKGrid grid;
	HashSet<DecisionNode> dirtyOrigin = new HashSet<>();
	HashSet<DecisionNode> dirtySource = new HashSet<>();

	// protected ArrayList<CKPosition> fixedTargets;

	DecisionNode[][][] nodes;

	/**
	 * @param grid
	 */
	public DescisionGrid(CKGrid grid)
	{
		this.grid = grid;
		nodes = new DecisionNode[grid.width][grid.height][Direction.values().length];
		// initialize all grid nodes
		for (int i = 0; i < grid.width; i++)
			for (int j = 0; j < grid.height; j++)
				for (int k = 0; k < Direction.values().length; k++)
					nodes[i][j][k] = new DecisionNode(new CKPosition(i, j),
							Direction.values()[k]);

	}

	public void updateGrid(Collection<CKPosition> targets,
			CharacterActionDescription [] actions)
	{
		dirtyOrigin.forEach(o->o.clear());
		dirtySource.forEach(o->o.clear());
		dirtyOrigin.clear();
		dirtySource.clear();
		
		createTargetReachability(targets,actions);
		dirtyOrigin.forEach(o->o.evalActions());
		
		//now create sources for all of these...
		AimDescriptionFactory factory = AimDescriptionFactory.getInstance();
		dirtyOrigin.forEach(node->node.streamReports()
				.forEach(car->
				{
					AimDescription aim = factory.getAsset(car.descr.targetType);
					int max = (int) (Math.floor(aim.getMaxDistance()+.5));
					int min = (int) (Math.ceil(aim.getMinDistance()));
					
					CKPosition pos = node.position;
					int x = (int) pos.getX();
					int y = (int) pos.getY();
					
					forXYBoundedDonut(x,y,min,max,grid.width, grid.height,
							(xp,yp)->createSourceNode(xp,yp,node.direction,car));					
				}));
		//now collapse the None direction
		dirtySource.stream()
			.filter(node->node.direction==Direction.NONE)
			.forEach(node->node.streamSources()
					.forEach(car->
					{
						Direction.stream().filter(d->Direction.NONE != d)
						.forEach(direction->
						{
							CKPosition pos = node.position;
							createSourceNode((int) pos.getX(),
									(int) pos.getY(),
									direction,car);
						});
					})
					);
					
		//All "real directions" are ready for evaluation.		
					
					
					
		
	}

	
	public void generateNodeValues(int cp,boolean moved)
	{
		
		dirtySource.stream()
			.filter(node->node.direction!=Direction.NONE)
			.forEach(node->node.generateNodeValue(cp, moved));
	}
	
	public void generateNodeValues(GridNode[][][][] motion,int maxCP)
	{
		dirtySource.stream()
		.filter(node->node.direction!=Direction.NONE)
		.filter(node->
		{
			GridNode m = motion[(int) node.position.getX()][(int) node.position.getY()]
					[node.direction.ordinal()][0];
			if(m.isVisited())
			{
				//check that there is a value in motion.  If not set utility to 0.
				node.utility=0.0;
			}
			return m.isVisited();

		})
		.forEach(node->
		{
			GridNode m = motion[(int) node.position.getX()][(int) node.position.getY()]
					[node.direction.ordinal()][0];

			int cp = maxCP - m.remainingCP;
			boolean moved = maxCP !=cp;
			node.generateNodeValue(cp, moved);
			//calculate cp and moved variables
			//
			
			
		});
		
		
	}
	
	public void PrettyPrintNodeValues(Direction dir)
	{
		for (int y = 0; y < grid.height; y++)
		{
			for (int x = 0; x < grid.width; x++)
			{
					double utility =nodes[x][y][dir.ordinal()].utility;
					System.out.printf("%6.1f",utility);
					/*System.out.print(x);
					System.out.print(',');
					System.out.print(y);
					System.out.print("->");
					System.out.print(utility);
					System.out.print('\n');*/
					
			}
			System.out.print('\n');
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void createSourceNode(int x, int y, Direction direction,
			CharacterActionReport car)
	{
		DecisionNode n = nodes[x][y][direction.ordinal()];
		//System.out.println("Source for "+x+","+y+"Direction"+direction);
		n.addSource(car);
		dirtySource.add(n);

	}


	public void createTargetReachability(Collection<CKPosition> targets,
	// HashMap<String, String> actions)
			CharacterActionDescription[] actions)
	{
		// for each action
		// for each target
		// mark it out!

		AimDescriptionFactory factory = AimDescriptionFactory.getInstance();
		for (CharacterActionDescription cad : actions)
		{
			AimDescription aim = factory.getAsset(cad.targetType);
			if (aim.getDirection() == Direction.NONE)
			{
				CKPosition[] inverse = aim.getInverse(Direction.NONE);
				markTargets(targets, inverse, Direction.NONE, cad);
			}
			Direction.stream().filter(d -> d != Direction.NONE)
					.forEach(dir -> {
						CKPosition[] inverse = aim.getInverse(dir);
						markTargets(targets, inverse, dir, cad);
					});
		}

	}

	protected DecisionNode getNode(CKPosition pos, Direction dir)
	{
		return nodes[(int) pos.getX()][(int) pos.getY()][dir.ordinal()];
	}

	protected void markTargets(Collection<CKPosition> targets,
			CKPosition[] inverse, Direction dir, CharacterActionDescription cad)
	{
		for (CKPosition invP : inverse)
			for (CKPosition pos : targets)
			{
				CKPosition oPos = invP.add(pos);
				DecisionNode node = getNode(oPos, dir);
				node.addAction(cad);
				dirtyOrigin.add(node);
			}

	}

}
