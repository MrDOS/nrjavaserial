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
 * Test conformance to <code>javax.comm.SerialPortEvent</code>.
 */
public class SerialPortEventTest
{
	@Test
	void superclass() throws ClassNotFoundException
	{
		assertSuperclass("java.util.EventObject");
	}

	/* This field has been made private in RXTX/NRJavaSerial. */
	@Test
	@Disabled
	public void eventType()
	{
		assertFieldSignature("int", PUBLIC);
	}

	@Test
	public void DATA_AVAILABLE()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	public void OUTPUT_BUFFER_EMPTY()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	public void CTS()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	public void DSR()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	public void RI()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	public void CD()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	public void OE()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	public void PE()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	public void FE()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	public void BI()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	public void constructor()
	{
		assertConstructorSignature(
				Arrays.asList("javax.comm.SerialPort", "int", "boolean", "boolean"),
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
