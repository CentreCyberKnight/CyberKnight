package ckSnapInterpreter;

import java.util.ArrayList;

import javafx.application.Platform;

public class Observable<T>
{

	private T data;
	ArrayList<Observer<T>> listeners=new ArrayList<Observer<T>>();

	public Observable()
	{
		data = null;
	}

	// Data for the current Player
	public T getData()
	{
		return data;
	}

	//will always do this on the FX thread?
	
	public void setData(T newData)
	{
		data = newData;
		notifyListeners();
	}

	public ArrayList<Observer<T>> getListeners()
	{
		return listeners;
	}

	public void setListeners(ArrayList<Observer<T>> newListeners)
	{
		listeners = newListeners;
	}

	public void registerObserver(Observer<T> o)
	{

		listeners.add(o);
	}

	public void removeObserver(Observer<T> o)
	{

		listeners.remove(o);
	}

	public void notifyListeners()
	{

		if(Platform.isFxApplicationThread())
		{
			notifyListenersHelper();		
		}
		else//schedule this to run on the FXthread
		{
			Platform.runLater(new Runnable()
			{

				@Override
				public void run()
				{
					notifyListenersHelper();					
				}
			});
		}
	}

		
		protected void notifyListenersHelper()
		{
			for (Observer<T> observer : listeners)
		{	
			observer.update(data);
		}
		System.out.println("Notifying all registered Player observers");
	}

}