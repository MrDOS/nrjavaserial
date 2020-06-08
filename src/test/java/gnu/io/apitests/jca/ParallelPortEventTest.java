package gnu.io.apitests.jca;

import static gnu.io.apitests.ApiTestUtil.NO_EXCEPTIONS;
import static gnu.io.apitests.ApiTestUtil.assertConstructorSignature;
import static gnu.io.apitests.ApiTestUtil.assertFieldSignature;
import static gnu.io.apitests.ApiTestUtil.assertMethodSignature;
import static gnu.io.apitests.ApiTestUtil.assertSuperclass;
import static java.lang.reflect.Modifier.FINAL;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.lang.reflect.Modifier.STATIC;

import java.util.Arrays;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Test conformance to <code>javax.comm.ParallelPortEvent</code>.
 * <p>
 * Note that this test is disabled for NRJavaSerial, because parallel port
 * support was removed. As such, this test is itself untested.
 */
@Disabled
public class ParallelPortEventTest
{
	@Test
	void superclass() throws ClassNotFoundException
	{
		assertSuperclass("java.util.EventObject");
	}

	@Test
	public void eventType()
	{
		assertFieldSignature("int", PUBLIC);
	}

	@Test
	public void PAR_EV_ERROR()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	public void PAR_EV_BUFFER()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	public void constructor()
	{
		assertConstructorSignature(
				Arrays.asList("javax.comm.ParallelPort", "int", "boolean", "boolean"),
				PUBLIC,
				NO_EXCEPTIONS);
	}

	@Test
	public void getEventType()
	{
		assertMethodSignature("int", PUBLIC);
	}

	@Test
	public void getNewValue()
	{
		assertMethodSignature("boolean", PUBLIC);
	}

	@Test
	public void getOldValue()
	{
		assertMethodSignature("boolean", PUBLIC);
	}
}
