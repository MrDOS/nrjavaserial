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
 * Test conformance to <code>javax.comm.SerialPort</code>.
 */
public class SerialPortTest
{
	@Test
	void superclass()
	{
		assertSuperclass("javax.comm.CommPort");
	}

	@Test
	void DATABITS_5()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	void DATABITS_6()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	void DATABITS_7()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	void DATABITS_8()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	void STOPBITS_1()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	void STOPBITS_2()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	void STOPBITS_1_5()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	void PARITY_NONE()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	void PARITY_ODD()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	void PARITY_EVEN()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	void PARITY_MARK()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	void PARITY_SPACE()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	void FLOWCONTROL_NONE()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	void FLOWCONTROL_RTSCTS_IN()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	void FLOWCONTROL_RTSCTS_OUT()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	void FLOWCONTROL_XONXOFF_IN()
	{
		assertFieldSignature("int", FINAL | STATIC | PUBLIC);
	}

	@Test
	void FLOWCONTROL_XONXOFF_OUT()
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
	void getBaudRate()
	{
		assertMethodSignature("int", ABSTRACT | PUBLIC);
	}

	@Test
	void getDataBits()
	{
		assertMethodSignature("int", ABSTRACT | PUBLIC);
	}

	@Test
	void getStopBits()
	{
		assertMethodSignature("int", ABSTRACT | PUBLIC);
	}

	@Test
	void getParity()
	{
		assertMethodSignature("int", ABSTRACT | PUBLIC);
	}

	@Test
	void sendBreak()
	{
		assertMethodSignature("int", "void", ABSTRACT | PUBLIC);
	}

	@Test
	void setFlowControlMode()
	{
		assertMethodSignature(
				Arrays.asList("int"),
				"void",
				ABSTRACT | PUBLIC,
				Collections.singleton("javax.comm.UnsupportedCommOperationException"));
	}

	@Test
	void getFlowControlMode()
	{
		assertMethodSignature("int", ABSTRACT | PUBLIC);
	}

	/* This method has been removed from RXTX/NRJavaSerial. */
	@Test
	@Disabled
	void setRcvFifoTrigger()
	{
		assertMethodSignature("int", "void", PUBLIC);
	}

	@Test
	void setSerialPortParams()
	{
		assertMethodSignature(
				Arrays.asList("int", "int", "int", "int"),
				"void",
				ABSTRACT | PUBLIC,
				Collections.singleton("javax.comm.UnsupportedCommOperationException"));
	}

	@Test
	void setDTR()
	{
		assertMethodSignature("boolean", "void", ABSTRACT | PUBLIC);
	}

	@Test
	void isDTR()
	{
		assertMethodSignature("boolean", ABSTRACT | PUBLIC);
	}

	@Test
	void setRTS()
	{
		assertMethodSignature("boolean", "void", ABSTRACT | PUBLIC);
	}

	@Test
	void isRTS()
	{
		assertMethodSignature("boolean", ABSTRACT | PUBLIC);
	}

	@Test
	void isCTS()
	{
		assertMethodSignature("boolean", ABSTRACT | PUBLIC);
	}

	@Test
	void isDSR()
	{
		assertMethodSignature("boolean", ABSTRACT | PUBLIC);
	}

	@Test
	void isRI()
	{
		assertMethodSignature("boolean", ABSTRACT | PUBLIC);
	}

	@Test
	void isCD()
	{
		assertMethodSignature("boolean", ABSTRACT | PUBLIC);
	}

	@Test
	void addEventListener()
	{
		assertMethodSignature(
				Arrays.asList("javax.comm.SerialPortEventListener"),
				"void",
				ABSTRACT | PUBLIC,
				Collections.singleton("java.util.TooManyListenersException"));
	}

	@Test
	void removeEventListener()
	{
		assertMethodSignature("void", ABSTRACT | PUBLIC);
	}

	@Test
	void notifyOnDataAvailable()
	{
		assertMethodSignature("boolean", "void", ABSTRACT | PUBLIC);
	}

	@Test
	void notifyOnOutputEmpty()
	{
		assertMethodSignature("boolean", "void", ABSTRACT | PUBLIC);
	}

	@Test
	void notifyOnCTS()
	{
		assertMethodSignature("boolean", "void", ABSTRACT | PUBLIC);
	}

	@Test
	void notifyOnDSR()
	{
		assertMethodSignature("boolean", "void", ABSTRACT | PUBLIC);
	}

	@Test
	void notifyOnRingIndicator()
	{
		assertMethodSignature("boolean", "void", ABSTRACT | PUBLIC);
	}

	@Test
	void notifyOnCarrierDetect()
	{
		assertMethodSignature("boolean", "void", ABSTRACT | PUBLIC);
	}

	@Test
	void notifyOnOverrunError()
	{
		assertMethodSignature("boolean", "void", ABSTRACT | PUBLIC);
	}

	@Test
	void notifyOnParityError()
	{
		assertMethodSignature("boolean", "void", ABSTRACT | PUBLIC);
	}

	@Test
	void notifyOnFramingError()
	{
		assertMethodSignature("boolean", "void", ABSTRACT | PUBLIC);
	}

	@Test
	void notifyOnBreakInterrupt()
	{
		assertMethodSignature("boolean", "void", ABSTRACT | PUBLIC);
	}
}
