package ckGameEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


//This object should inherit all of the functions of the arrayList, so 
//I'm not going to add them

/*When you cast a Scry, you create a CKScryObjectDS
 * The DS is a map connecting Target names (or whatever) to HashMaps that link all the 
 * queries on that target to all the results of those queries.
 * 
 */

public class CKScryObjectDS {
 Map scryReturns = new HashMap<String, CKScryObjectDict>();
 int index =0;
 public CKScryObjectDS(){	 
	 
 }
 
 public void insert(String target, CKScryObjectDict data){

	 scryReturns.put(target, data);
 }
 public ArrayList<String> answerQuery(String query){
	 ArrayList<String> answers = new ArrayList<String>();
	 Iterator it = scryReturns.entrySet().iterator();
	 while (it.hasNext()) {
		 
	        Map.Entry <String, CKScryObjectDict> pair;
	        pair = (Map.Entry)it.next();
	        if (pair.getValue().containsKey(query) == true){
		 	answers.add(pair.getValue().get(query));
	        }
	      
	    }
	 return answers;
	 
	 
 }
 public ArrayList<String> answerTarget(String target){
	 ArrayList<String> answers = new ArrayList<String>();
	 CKScryObjectDict subject = (CKScryObjectDict) scryReturns.get(target);
	        
	        //Not sure if you want the name of the query answered here or not
	        //If you do, here's the code for it, commented out
	        
	        /*
	         * answers.add(subject.getQueryBlind());
			
	         * 
	         */
	 		answers.addAll(subject.getResultBlind());

		 return answers;
	    
	 
	 
 }
 public String answerTargetQuery(String target, String query){
	 CKScryObjectDict subject = (CKScryObjectDict) scryReturns.get(target);
	 return subject.get(query);
	 
 }
 
 public ArrayList<String>  answerAll(){
	 ArrayList<String> answers = new ArrayList<String>();

	 Iterator it = scryReturns.entrySet().iterator();
	 while (it.hasNext()) {
		 
	        Map.Entry <String, CKScryObjectDict> pair;
	        pair = (Map.Entry)it.next();
	        //Not sure if you want the name of the query answered here or not
	        //If you do, here's the code for it, commented out
	        
	        /*
	         * answers.add(pair.getValue().getQueryBlind());
			
	         * 
	         */
		 	answers.addAll(pair.getValue().getResultBlind());
	        
	     	      
	    }
	 return answers;
	 
	 
 }

 
}
