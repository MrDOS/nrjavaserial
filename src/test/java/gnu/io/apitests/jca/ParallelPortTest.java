package gnu.io.apitests.jca;

import static gnu.io.apitests.ApiTestUtil.NO_EXCEPTIONS;
import static gnu.io.apitests.ApiTestUtil.NO_PARAMETERS;
import static gnu.io.apitests.ApiTestUtil.assertConstructorSignature;
import static gnu.io.apitests.ApiTestUtil.assertFieldSignature;
import static gnu.io.apitests.ApiTestUtil.assertMethodSignature;
import static gnu.io.apitests.ApiTestUtil.assertSuperclass;
import static java.lang.reflect.Modifier.ABSTRACT;
import static java.lang.reflect.Modifier.FINAL;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.lang.reflect.Modifier.STATIC;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Test conformance to <code>javax.comm.ParallelPort</code>.
 * <p>
 * Note that this test is disabled for NRJavaSerial, because parallel port
 * support was removed. As such, this test is itself untested.
 */
@Disabled
public class ParallelPortTest
{
	@Test
	void superclass()
	{
		assertSuperclass("javax.comm.CommPort");
	}

	@Test
	void LPT_MODE_ANY()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	void LPT_MODE_SPP()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	void LPT_MODE_PS2()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	void LPT_MODE_EPP()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	void LPT_MODE_ECP()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	void LPT_MODE_NIBBLE()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	void constructor()
	{
		assertConstructorSignature(
				NO_PARAMETERS,
				PUBLIC,
				NO_EXCEPTIONS);
	}

	@Test
	void addEventListener()
	{
		assertMethodSignature(
				Arrays.asList("javax.comm.ParallelPortEventListener"),
				"void",
				ABSTRACT | PUBLIC,
				Collections.singleton("javax.comm.TooManyListenersException"));
	}

	@Test
	void removeEventListener()
	{
		assertMethodSignature("void", ABSTRACT | PUBLIC);
	}

	@Test
	void notifyOnError()
	{
		assertMethodSignature("boolean", "void", ABSTRACT | PUBLIC);
	}

	@Test
	void notifyOnBuffer()
	{
		assertMethodSignature("boolean", "void", ABSTRACT | PUBLIC);
	}

	@Test
	void getOutputBufferFree()
	{
		assertMethodSignature("int", ABSTRACT | PUBLIC);
	}

	@Test
	void isPaperOut()
	{
		assertMethodSignature("boolean", ABSTRACT | PUBLIC);
	}

	@Test
	void isPrinterBusy()
	{
		assertMethodSignature("boolean", ABSTRACT | PUBLIC);
	}

	@Test
	void isPrinterSelected()
	{
		assertMethodSignature("boolean", ABSTRACT | PUBLIC);
	}

	@Test
	void isPrinterTimedOut()
	{
		assertMethodSignature("boolean", ABSTRACT | PUBLIC);
	}

	@Test
	void isPrinterError()
	{
		assertMethodSignature("boolean", ABSTRACT | PUBLIC);
	}

	@Test
	void restart()
	{
		assertMethodSignature("void", ABSTRACT | PUBLIC);
	}

	@Test
	void suspend()
	{
		assertMethodSignature("void", ABSTRACT | PUBLIC);
	}

	@Test
	void getMode()
	{
		assertMethodSignature("int", ABSTRACT | PUBLIC);
	}

	@Test
	void setMode()
	{
		assertMethodSignature(
				Arrays.asList("int"),
				"boolean",
				ABSTRACT | PUBLIC,
				Collections.singleton("javax.comm.UnsupportedCommOperationException"));
	}
}
