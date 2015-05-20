package ckGameEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


//The Hashmap linking Queries and results

public class CKScryObjectDict {
	Map queryResults = new HashMap();

	public CKScryObjectDict(){
	}
	public void addData(String query, String results){
		queryResults.put(query, results);
	}
	public Map getMap(){
		return this.queryResults;
	}
	public String get(String query){
		return (String) this.queryResults.get(query);
	}
	public ArrayList<String> getQueryBlind(){
		ArrayList<String> returned = new ArrayList<String>();

		Iterator it = queryResults.entrySet().iterator();
		 while (it.hasNext()) {

	        Map.Entry pair = (Map.Entry)it.next();
	        returned.add( (String) pair.getKey());
		 }
	   return returned;
	}
	public ArrayList<String> getResultBlind(){
		ArrayList<String> returned = new ArrayList<String>();

		Iterator it = queryResults.entrySet().iterator();
		 while (it.hasNext()) {

	        Map.Entry pair = (Map.Entry)it.next();
	        returned.add( (String) pair.getValue());
		 }
	   return returned;
	}
	public boolean containsKey(String key){
		return this.queryResults.containsKey(key);
	}
	public boolean containsValue(String value){
		return this.queryResults.containsValue(value);
	}
}
