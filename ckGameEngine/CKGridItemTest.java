package ckGameEngine;

import static org.junit.Assert.*;

import org.junit.Test;

import ckCommonUtils.CKAreaPositions;

public class CKGridItemTest {

	@Test
	public void test() {

		CKGridItem test = (CKGridItem) dummy.makeCopy(dummy);
		assertSame(test, dummy);
	}

}
