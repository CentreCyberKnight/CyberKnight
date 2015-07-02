package ckSnapInterpreter;

public interface ckSubject {
	
	public void registerObserver(Observer o);
	public void removeObserver(Observer o);
	public void notifyObserver();
}