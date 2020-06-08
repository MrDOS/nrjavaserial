package gnu.io.apitests.jca;

import static gnu.io.apitests.ApiTestUtil.NO_EXCEPTIONS;
import static gnu.io.apitests.ApiTestUtil.assertConstructorSignature;
import static gnu.io.apitests.ApiTestUtil.assertFieldSignature;
import static gnu.io.apitests.ApiTestUtil.assertMethodSignature;
import static java.lang.reflect.Modifier.FINAL;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.lang.reflect.Modifier.STATIC;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Test conformance to <code>javax.comm.CommPortIdentifier</code>.
 */
public class CommPortIdentifierTest
{
	@Test
	void PORT_SERIAL()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	void PORT_PARALLEL()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	/* FIXME: The RXTX/NRJavaSerial is only package-visible, and receives an
	 * `RXTXPort` instance as the second argument (instead of `CommPort`). */
	@Test
	@Disabled
	void constructor()
	{
		assertConstructorSignature(
				Arrays.asList("java.lang.String", "javax.comm.CommPort", "int", "javax.comm.CommDriver"),
				PUBLIC,
				NO_EXCEPTIONS);
	}

	@Test
	void getPortIdentifiers()
	{
		assertMethodSignature("java.util.Enumeration", STATIC | PUBLIC);
	}

	@Test
	void getPortIdentifier$1()
	{
		assertMethodSignature(
				Arrays.asList("java.lang.String"),
				"javax.comm.CommPortIdentifier",
				STATIC | PUBLIC,
				Collections.singleton("javax.comm.NoSuchPortException"));
	}

	@Test
	void getPortIdentifier$2()
	{
		assertMethodSignature(
				Arrays.asList("javax.comm.CommPort"),
				"javax.comm.CommPortIdentifier",
				STATIC | PUBLIC,
				Collections.singleton("javax.comm.NoSuchPortException"));
	}

	@Test
	void addPortName()
	{
		assertMethodSignature(
				Arrays.asList("java.lang.String", "int", "javax.comm.CommDriver"),
				"void",
				STATIC | PUBLIC,
				NO_EXCEPTIONS);
	}

	/* FIXME: RXTX/NRJavaSerial returns `RXTXPort`, not `CommPort`. */
	@Test
	@Disabled
	void open$1()
	{
		assertMethodSignature(
				Arrays.asList("java.lang.String", "int"),
				"javax.comm.CommPort",
				PUBLIC,
				Collections.singleton("javax.comm.PortInUseException"));
	}

	@Test
	void open$2()
	{
		assertMethodSignature(
				Arrays.asList("java.io.FileDescriptor"),
				"javax.comm.CommPort",
				PUBLIC,
				Collections.singleton("javax.comm.UnsupportedCommOperationException"));
	}

	@Test
	void getName()
	{
		assertMethodSignature("java.lang.String", PUBLIC);
	}

	@Test
	void getPortType()
	{
		assertMethodSignature("int", PUBLIC);
	}

	@Test
	void getCurrentOwner()
	{
		assertMethodSignature("java.lang.String", PUBLIC);
	}

	@Test
	void isCurrentlyOwned()
	{
		assertMethodSignature("boolean", PUBLIC);
	}

	@Test
	void addPortOwnershipListener()
	{
		assertMethodSignature("javax.comm.CommPortOwnershipListener", "void", PUBLIC);
	}

	@Test
	void removePortOwnershipListener()
	{
		assertMethodSignature("javax.comm.CommPortOwnershipListener", "void", PUBLIC);
	}
}
