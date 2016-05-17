package ckCommonUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
/**
 * This class will take several iterators of type E and will treat them as one iterator
 * This class is !not thread safe.  Please do not use it as such
 * @author Michael K. Bradshaw
 *
 * @param <E>
 */
public class JoinedIterator<E> implements Iterator<E>
{
	
	ArrayList<Iterator<E>> iters;
	Iterator<Iterator<E>> currentIterator;
	Iterator<E> current;
	
	@SafeVarargs
	public JoinedIterator(Iterator<E> ... iterators)
	{
		iters=new ArrayList<Iterator<E>>(iterators.length); 
		
		for(Iterator<E> i:iterators)
		{
			iters.add(i);
		}
		
		currentIterator=iters.iterator();
		current = currentIterator.next();
	}
	
	
	
	
	
	@Override
	public boolean hasNext()
	{
		if(current.hasNext()) { return true; }
		else if (currentIterator.hasNext())
		{
			current=currentIterator.next();
			return this.hasNext();
		}
		else
		{
			return false;
		}
	}

	@Override
	public E next()
	{
		try
		{
			return current.next();
		}
		catch (NoSuchElementException E)
		{
			//could throw exception, but I would not be able to correct anyway.
			current=currentIterator.next();
			return this.next();
		}
	}
	
	@Override
	public void remove()
	{
		current.remove();
		
	}

}
