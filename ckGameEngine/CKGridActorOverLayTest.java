package ckGameEngine;

import static org.junit.Assert.assertSame;

import org.junit.Test;

public class CKGridActorOverLayTest {

	@Test
	public void test() {
		CKGridActorOverLay dummy = new CKGridActorOverLay();
		CKGridActorOverLay test = (CKGridActorOverLay) dummy.makeCopy(dummy);
		assertSame(test, dummy);
		}

}
