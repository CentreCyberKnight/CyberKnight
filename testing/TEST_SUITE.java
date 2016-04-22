package testing;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
	TestQuestCases.class,
	TestActorJumpAndShove.class,
	TestActorJumpOver.class,
	TestActorMovement.class})
	//TestPythonMoveForward.class
public class TEST_SUITE
{
}
