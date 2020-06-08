package gnu.io.apitests.jca;

import static gnu.io.apitests.ApiTestUtil.assertSuperclass;

import org.junit.jupiter.api.Test;

/**
 * Test conformance to <code>javax.comm.NoSuchPortException</code>.
 */
public class NoSuchPortExceptionTest
{
	@Test
	void superclass() throws ClassNotFoundException
	{
		assertSuperclass("java.lang.Exception");
	}
}
