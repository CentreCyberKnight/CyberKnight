package ckGameEngine;

import static org.junit.Assert.*;

import org.junit.Test;

import ckCommonUtils.CKAreaPositions;

public class CKGridItemSetTest {

	@Test
	public void test() {
		CKAreaPositions pos = new CKAreaPositions(1.1,1.2,1.4,6);
		CKGrid grid = new CKGrid(1,2);
		QuestData qData = new QuestData(2);
		Quest q = new Quest(qData);
		CKGridItemSet dummy = new CKGridItemSet(pos,grid,q);
	//	CKGridItemSet dummy = new CKGridItemSet();
		CKGridItemSet test = (CKGridItemSet) dummy.makeCopy(dummy);
		assertSame(test, dummy);
	}

}
