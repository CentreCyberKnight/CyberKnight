package ckGameEngine;

import static org.junit.Assert.*;

import org.junit.Test;

public class CKGridActorTest {

	@Test
	public void test() {
		CKGridActor dummy = new CKGridActor("BOB", Direction.NORTHEAST);
		CKBook ate = new CKBook("bookName","chapte",2,"page");
		CKBook sate = new CKBook("boe","chaasdfpte",21,"pasdfage");
	

	//	System.out.println(test.getQuestAbilities());
		System.out.println(dummy.getQuestAbilities());
		CKGridActor test = (CKGridActor) dummy.makeCopy(dummy);

		System.out.println(test.getQuestAbilities());
		System.out.println(dummy.getQuestAbilities());
		
		test.setQuestAbilities(sate);
		dummy.setQuestAbilities(ate);
	
		System.out.println(test.getCyberPoints());
		System.out.println(test.getQuestAbilities());
		System.out.println(dummy.getQuestAbilities());

		assertNotSame(test, dummy);
	}

}
