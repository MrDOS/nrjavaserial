package gnu.io.apitests.jca;

import static gnu.io.apitests.ApiTestUtil.NO_EXCEPTIONS;
import static gnu.io.apitests.ApiTestUtil.assertMethodSignature;
import static java.lang.reflect.Modifier.ABSTRACT;
import static java.lang.reflect.Modifier.PUBLIC;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

/**
 * Test conformance to <code>javax.comm.CommDriver</code>.
 */
public class CommDriverTest
{
	@Test
	void initialize()
	{
		assertMethodSignature("void", ABSTRACT | PUBLIC);
	}

	@Test
	void getCommPort()
	{
		assertMethodSignature(
				Arrays.asList("java.lang.String", "int"),
				"javax.comm.CommPort",
				ABSTRACT | PUBLIC,
				NO_EXCEPTIONS);
	}
}
