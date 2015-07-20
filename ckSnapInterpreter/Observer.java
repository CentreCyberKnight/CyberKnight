package ckSnapInterpreter;

@FunctionalInterface
public interface Observer<T> {
	
	public void update(T entity);
	
}