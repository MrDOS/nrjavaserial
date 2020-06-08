package gnu.io.apitests.jca;

import static gnu.io.apitests.ApiTestUtil.assertFieldSignature;
import static gnu.io.apitests.ApiTestUtil.assertSuperclass;
import static java.lang.reflect.Modifier.PUBLIC;

import org.junit.jupiter.api.Test;

/**
 * Test conformance to <code>javax.comm.PortInUseException</code>.
 */
public class PortInUseExceptionTest
{
	@Test
	void superclass()
	{
		assertSuperclass("java.lang.Exception");
	}

	@Test
	void currentOwner()
	{
		assertFieldSignature("java.lang.String", PUBLIC);
	}
}
