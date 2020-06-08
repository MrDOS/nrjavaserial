package gnu.io.apitests.jca;

import static gnu.io.apitests.ApiTestUtil.assertInterface;
import static gnu.io.apitests.ApiTestUtil.assertMethodSignature;
import static java.lang.reflect.Modifier.ABSTRACT;
import static java.lang.reflect.Modifier.PUBLIC;

import org.junit.jupiter.api.Test;

/**
 * Test conformance to <code>javax.comm.SerialPortEventListener</code>.
 */
public class SerialPortEventListenerTest
{
	@Test
	void interfaces() throws ClassNotFoundException
	{
		assertInterface("java.util.EventListener");
	}

	@Test
	public void serialEvent()
	{
		assertMethodSignature("javax.comm.SerialPortEvent", "void", ABSTRACT | PUBLIC);
	}
}
