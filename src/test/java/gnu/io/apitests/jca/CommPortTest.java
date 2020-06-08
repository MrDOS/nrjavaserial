package gnu.io.apitests.jca;

import static gnu.io.apitests.ApiTestUtil.NO_PARAMETERS;
import static gnu.io.apitests.ApiTestUtil.assertFieldSignature;
import static gnu.io.apitests.ApiTestUtil.assertMethodSignature;
import static java.lang.reflect.Modifier.ABSTRACT;
import static java.lang.reflect.Modifier.PROTECTED;
import static java.lang.reflect.Modifier.PUBLIC;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

/**
 * Test conformance to <code>javax.comm.CommPort</code>.
 */
public class CommPortTest
{
	@Test
	void name()
	{
		assertFieldSignature("java.lang.String", PROTECTED);
	}

	@Test
	void getName()
	{
		assertMethodSignature("java.lang.String", PUBLIC);
	}

	@Test
	void getInputStream()
	{
		assertMethodSignature(
				NO_PARAMETERS,
				"java.io.InputStream",
				ABSTRACT | PUBLIC,
				Collections.singleton("java.io.IOException"));
	}

	@Test
	void getOutputStream()
	{
		assertMethodSignature(
				NO_PARAMETERS,
				"java.io.OutputStream",
				ABSTRACT | PUBLIC,
				Collections.singleton("java.io.IOException"));
	}

	@Test
	void close()
	{
		assertMethodSignature("void", PUBLIC);
	}

	@Test
	void enableReceiveThreshold()
	{
		assertMethodSignature(
				Arrays.asList("int"),
				"void",
				ABSTRACT | PUBLIC,
				Collections.singleton("javax.comm.UnsupportedCommOperationException"));
	}

	@Test
	void disableReceiveThreshold()
	{
		assertMethodSignature("void", ABSTRACT | PUBLIC);
	}

	@Test
	void isReceiveThresholdEnabled()
	{
		assertMethodSignature("boolean", ABSTRACT | PUBLIC);
	}

	@Test
	void getReceiveThreshold()
	{
		assertMethodSignature("int", ABSTRACT | PUBLIC);
	}

	@Test
	void enableReceiveTimeout()
	{
		assertMethodSignature(
				Arrays.asList("int"),
				"void",
				ABSTRACT | PUBLIC,
				Collections.singleton("javax.comm.UnsupportedCommOperationException"));
	}

	@Test
	void disableReceiveTimeout()
	{
		assertMethodSignature("void", ABSTRACT | PUBLIC);
	}

	@Test
	void isReceiveTimeoutEnabled()
	{
		assertMethodSignature("boolean", ABSTRACT | PUBLIC);
	}

	@Test
	void getReceiveTimeout()
	{
		assertMethodSignature("int", ABSTRACT | PUBLIC);
	}

	@Test
	void enableReceiveFraming()
	{
		assertMethodSignature(
				Arrays.asList("int"),
				"void",
				ABSTRACT | PUBLIC,
				Collections.singleton("javax.comm.UnsupportedCommOperationException"));
	}

	@Test
	void disableReceiveFraming()
	{
		assertMethodSignature("void", ABSTRACT | PUBLIC);
	}

	@Test
	void isReceiveFramingEnabled()
	{
		assertMethodSignature("boolean", ABSTRACT | PUBLIC);
	}

	@Test
	void getReceiveFramingByte()
	{
		assertMethodSignature("int", ABSTRACT | PUBLIC);
	}

	@Test
	void setInputBufferSize()
	{
		assertMethodSignature("int", "void", ABSTRACT | PUBLIC);
	}

	@Test
	void getInputBufferSize()
	{
		assertMethodSignature("int", ABSTRACT | PUBLIC);
	}

	@Test
	void setOutputBufferSize()
	{
		assertMethodSignature("int", "void", ABSTRACT | PUBLIC);
	}

	@Test
	void getOutputBufferSize()
	{
		assertMethodSignature("int", ABSTRACT | PUBLIC);
	}
}
