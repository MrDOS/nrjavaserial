package gnu.io.apitests.jca;

import static gnu.io.apitests.ApiTestUtil.NO_EXCEPTIONS;
import static gnu.io.apitests.ApiTestUtil.NO_PARAMETERS;
import static gnu.io.apitests.ApiTestUtil.assertConstructorSignature;
import static gnu.io.apitests.ApiTestUtil.assertSuperclass;
import static java.lang.reflect.Modifier.PUBLIC;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

/**
 * Test conformance to
 * <code>javax.comm.UnsupportedCommOperationException</code>.
 */
public class UnsupportedCommOperationExceptionTest
{
	@Test
	void superclass() throws ClassNotFoundException
	{
		assertSuperclass("java.lang.Exception");
	}

	@Test
	void constructor$1()
	{
		assertConstructorSignature(Arrays.asList("java.lang.String"), PUBLIC,
				NO_EXCEPTIONS);
	}

	@Test
	void constructor$2()
	{
		assertConstructorSignature(NO_PARAMETERS, PUBLIC, NO_EXCEPTIONS);
	}
}
