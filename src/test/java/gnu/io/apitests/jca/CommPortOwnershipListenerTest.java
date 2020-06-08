package gnu.io.apitests.jca;

import static gnu.io.apitests.ApiTestUtil.assertFieldSignature;
import static gnu.io.apitests.ApiTestUtil.assertInterface;
import static gnu.io.apitests.ApiTestUtil.assertMethodSignature;
import static java.lang.reflect.Modifier.ABSTRACT;
import static java.lang.reflect.Modifier.FINAL;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.lang.reflect.Modifier.STATIC;

import org.junit.jupiter.api.Test;

/**
 * Test conformance to <code>javax.comm.CommPortOwnershipListener</code>.
 */
public class CommPortOwnershipListenerTest
{
	@Test
	void interfaces() throws ClassNotFoundException
	{
		assertInterface("java.util.EventListener");
	}

	@Test
	void PORT_OWNED()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	void PORT_UNOWNED()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	void PORT_OWNERSHIP_REQUESTED()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	void ownershipChange()
	{
		assertMethodSignature("int", "void", ABSTRACT | PUBLIC);
	}
}
