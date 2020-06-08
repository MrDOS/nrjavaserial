package gnu.io.apitests.jca;

import static gnu.io.apitests.ApiTestUtil.assertInterface;
import static gnu.io.apitests.ApiTestUtil.assertMethodSignature;
import static java.lang.reflect.Modifier.ABSTRACT;
import static java.lang.reflect.Modifier.PUBLIC;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Test conformance to <code>javax.comm.ParallelPortEventListener</code>.
 * <p>
 * Note that this test is disabled for NRJavaSerial, because parallel port
 * support was removed. As such, this test is itself untested.
 */
@Disabled
public class ParallelPortEventListenerTest
{
	@Test
	void interfaces() throws ClassNotFoundException
	{
		assertInterface("java.util.EventListener");
	}

	@Test
	public void parallelEvent()
	{
		assertMethodSignature("javax.comm.ParallelPortEvent", "void", ABSTRACT | PUBLIC);
	}
}
