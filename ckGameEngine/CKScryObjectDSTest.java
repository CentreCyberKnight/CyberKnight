package ckGameEngine;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class CKScryObjectDSTest {

	@Test
	public void test() {
		
		CKScryObjectDict oDict = new CKScryObjectDict();
		oDict.addData("Big", "Guy");
		oDict.addData("Bitg", "Guys");
		oDict.addData("Bsig", "Guyt");


		CKScryObjectDS oDS = new CKScryObjectDS();
		oDS.insert("Bane", oDict);
		

		ArrayList<String> bob = oDS.answerQuery("Bitg");
		System.out.println(bob);
	   assertNotNull(bob);
	   
	   ArrayList<String> dob = oDS.answerTarget("Bane");
		System.out.println(dob);
	   assertNotNull(dob);
	   
	   
	   String dot = oDS.answerTargetQuery("Bane", "Big");
		System.out.println(dot);
	 assertNotNull(dot);
	
	 ArrayList<String>  dog = oDS.answerAll();
		System.out.println(dog);
	   assertNotNull(dog);
	}

}
