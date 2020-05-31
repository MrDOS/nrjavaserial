/*-------------------------------------------------------------------------
|   RXTX License v 2.1 - LGPL v 2.1 + Linking Over Controlled Interface.
|   RXTX is a native interface to serial ports in java.
|   Copyright 1998 Kevin Hester, kevinh@acm.org
|   Copyright 2000-2008 Trent Jarvi tjarvi@qbang.org and others who
|   actually wrote it.  See individual source files for more information.
|
|   A copy of the LGPL v 2.1 may be found at
|   http://www.gnu.org/licenses/lgpl.txt on March 4th 2007.  A copy is
|   here for your convenience.
|
|   This library is free software; you can redistribute it and/or
|   modify it under the terms of the GNU Lesser General Public
|   License as published by the Free Software Foundation; either
|   version 2.1 of the License, or (at your option) any later version.
|
|   This library is distributed in the hope that it will be useful,
|   but WITHOUT ANY WARRANTY; without even the implied warranty of
|   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
|   Lesser General Public License for more details.
|
|   An executable that contains no derivative of any portion of RXTX, but
|   is designed to work with RXTX by being dynamically linked with it,
|   is considered a "work that uses the Library" subject to the terms and
|   conditions of the GNU Lesser General Public License.
|
|   The following has been added to the RXTX License to remove
|   any confusion about linking to RXTX.   We want to allow in part what
|   section 5, paragraph 2 of the LGPL does not permit in the special
|   case of linking over a controlled interface.  The intent is to add a
|   Java Specification Request or standards body defined interface in the
|   future as another exception but one is not currently available.
|
|   http://www.fsf.org/licenses/gpl-faq.html#LinkingOverControlledInterface
|
|   As a special exception, the copyright holders of RXTX give you
|   permission to link RXTX with independent modules that communicate with
|   RXTX solely through the Sun Microsytems CommAPI interface version 2,
|   regardless of the license terms of these independent modules, and to copy
|   and distribute the resulting combined work under terms of your choice,
|   provided that every copy of the combined work is accompanied by a complete
|   copy of the source code of RXTX (the version of RXTX used to produce the
|   combined work), being distributed under the terms of the GNU Lesser General
|   Public License plus this exception.  An independent module is a
|   module which is not derived from or based on RXTX.
|
|   Note that people who make modified versions of RXTX are not obligated
|   to grant this special exception for their modified versions; it is
|   their choice whether to do so.  The GNU Lesser General Public License
|   gives permission to release a modified version without this exception; this
|   exception also makes it possible to release a modified version which
|   carries forward this exception.
|
|   You should have received a copy of the GNU Lesser General Public
|   License along with this library; if not, write to the Free
|   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
|   All trademarks belong to their respective owners.
--------------------------------------------------------------------------*/
/* Martin Pool <mbp@linuxcare.com> added support for explicitly-specified
 * lists of ports, October 2000. */
/* Joseph Goldstone <joseph@lp.com> reorganized to support registered ports,
 * known ports, and scanned ports, July 2001 */

package gnu.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

/**
 * RXTX/NRJavaSerial implementation of {@link CommDriver}. Performs internal
 * intialization and port retrieval tasks.
 */
public class RXTXCommDriver implements CommDriver
{
	private static final String EXTENSIONS_PROPERTIES_FILENAME = "gnu.io.rxtx.properties";
	private static final String CONFIGURATION_SERIAL_KEY = "gnu.io.rxtx.SerialPorts";
	private static final String CONFIGURATION_PARALLEL_KEY = "gnu.io.rxtx.ParallelPorts";

	private static final String[] LINUX_SERIAL_PORT_PREFIXES = new String[] {
			"ttyS", // linux Serial Ports
			"ttySA", // for the IPAQs
			"ttyUSB", // for USB frobs
			"ttyAMA", // Raspberry Pi
			"rfcomm", // bluetooth serial device
			"ttyircomm", // linux IrCommdevices (IrDA serial emu)
			"ttyACM", // linux CDC ACM devices
			"DyIO", // NRDyIO
			"Bootloader", // NRDyIO bootloader
			"BowlerDevice", // Generic Bowler Device
			"DeltaDoodle", // DeltaDoodle Printer
			"dyio"// linux CDC ACM devices
	};

	private static final String[] LINUX_ALL_SERIAL_PORT_PREFIXES = new String[] {
			"comx", // linux COMMX synchronous serial card
			"holter", // custom card for heart monitoring
			"modem", // linux symbolic link to modem.
			"rfcomm", // bluetooth serial device
			"ttyircomm", // linux IrCommdevices (IrDA serial emu)
			"ttycosa0c", // linux COSA/SRP synchronous serial card
			"ttycosa1c", // linux COSA/SRP synchronous serial card
			"ttyACM", // linux CDC ACM devices
			"DyIO", // linux CDC ACM devices
			"Bootloader", // linux CDC ACM devices
			"BowlerDevice", // Generic Bowler Device
			"DeltaDoodle", // DeltaDoodle Printer
			"dyio", // linux CDC ACM devices
			"ttyC", // linux cyclades cards
			"ttyCH", // linux Chase Research AT/PCI-Fast serial card
			"ttyD", // linux Digiboard serial card
			"ttyE", // linux Stallion serial card
			"ttyF", // linux Computone IntelliPort serial card
			"ttyH", // linux Chase serial card
			"ttyI", // linux virtual modems
			"ttyL", // linux SDL RISCom serial card
			"ttyM", // linux PAM Software's multimodem boards
			// linux ISI serial card
			"ttyMX", // linux Moxa Smart IO cards
			"ttyP", // linux Hayes ESP serial card
			"ttyR", // linux comtrol cards
			// linux Specialix RIO serial card
			"ttyS", // linux Serial Ports
			"ttySI", // linux SmartIO serial card
			"ttySR", // linux Specialix RIO serial card 257+
			"ttyT", // linux Technology Concepts serial card
			"ttyUSB", // linux USB serial converters
			"ttyV", // linux Comtrol VS-1000 serial controller
			"ttyW", // linux specialix cards
			"ttyX" // linux SpecialX serial card
	};

	private static final String[] QNX_SERIAL_PORT_PREFIXES = new String[] {
			"ser"
	};

	private static final String[] IRIX_SERIAL_PORT_PREFIXES = new String[] {
			"ttyc", // irix raw character devices
			"ttyd", // irix basic serial ports
			"ttyf", // irix serial ports with hardware flow
			"ttym", // irix modems
			"ttyq", // irix pseudo ttys
			"tty4d", // irix RS422
			"tty4f", // irix RS422 with HSKo/HSki
			"midi", // irix serial midi
			"us" // irix mapped interface
	};

	private static final String[] FREEBSD_SERIAL_PORT_PREFIXES = new String[] {
			"ttyd", // general purpose serial ports
			"cuaa", // dialout serial ports
			"ttyA", // Specialix SI/XIO dialin ports
			"cuaA", // Specialix SI/XIO dialout ports
			"ttyu", // general purpose usb serial ports
			"cuau", // dialout usb serial ports
			"ttyU", // Specialix SI/XIO usb dialin ports
			"cuaU", // Specialix SI/XIO usb dialout ports
			"ttyD", // Digiboard - 16 dialin ports
			"cuaD", // Digiboard - 16 dialout ports
			"ttyE", // Stallion EasyIO (stl) dialin ports
			"cuaE", // Stallion EasyIO (stl) dialout ports
			"ttyF", // Stallion Brumby (stli) dialin ports
			"cuaF", // Stallion Brumby (stli) dialout ports
			"ttyR", // Rocketport dialin ports
			"cuaR", // Rocketport dialout ports
			"stl" // Stallion EasyIO board or Brumby N
	};

	private static final String[] NETBSD_SERIAL_PORT_PREFIXES = new String[] {
			"tty0" // netbsd serial ports
	};

	private static final String[] SOLARIS_SERIAL_PORT_PREFIXES = new String[] {
			"term/",
			"cua/"
	};

	private static final String[] HPUX_SERIAL_PORT_PREFIXES = new String[] {
			"tty0p", // HP-UX serial ports
			"tty1p" // HP-UX serial ports
	};

	private static final String[] UNIXWARE_SERIAL_PORT_PREFIXES = new String[] {
			"tty00s", // UW7/OU8 serial ports
			"tty01s",
			"tty02s",
			"tty03s"
	};

	private static final String[] OPENSERVER_SERIAL_PORT_PREFIXES = new String[] {
			"tty1A", // OSR5 serial ports
			"tty2A",
			"tty3A",
			"tty4A",
			"tty5A",
			"tty6A",
			"tty7A",
			"tty8A",
			"tty9A",
			"tty10A",
			"tty11A",
			"tty12A",
			"tty13A",
			"tty14A",
			"tty15A",
			"tty16A",
			"ttyu1A", // OSR5 USB-serial ports
			"ttyu2A",
			"ttyu3A",
			"ttyu4A",
			"ttyu5A",
			"ttyu6A",
			"ttyu7A",
			"ttyu8A",
			"ttyu9A",
			"ttyu10A",
			"ttyu11A",
			"ttyu12A",
			"ttyu13A",
			"ttyu14A",
			"ttyu15A",
			"ttyu16A"
	};

	private static final String[] DIGITAL_UNIX_SERIAL_PORT_PREFIXES = new String[] {
			"tty0" // Digital Unix serial ports
	};

	private static final String[] BEOS_SERIAL_PORT_PREFIXES = new String[] {
			"serial" // BeOS serial ports
	};

	private static final String[] MACOS_SERIAL_PORT_PREFIXES = new String[] {
			// Keyspan USA-28X adapter, USB port 1
			"cu.KeyUSA28X191.",
			// Keyspan USA-28X adapter, USB port 1
			"tty.KeyUSA28X191.",
			// Keyspan USA-28X adapter, USB port 2
			"cu.KeyUSA28X181.",
			// Keyspan USA-28X adapter, USB port 2
			"tty.KeyUSA28X181.",
			// Keyspan USA-19 adapter
			"cu.KeyUSA19181.",
			// Keyspan USA-19 adapter
			"tty.KeyUSA19181."
	};

	private static final String[] WINDOWS_SERIAL_PORT_PREFIXES = new String[] {
			"COM", // win32 serial ports
//			"//./COM" // win32 serial ports
	};

	private static final String[] LINUX_PARALLEL_PORT_PREFIXES = new String[] {
			"lp" // linux printer port
	};

	private static final String[] FREEBSD_PARALLEL_PORT_PREFIXES = new String[] {
			"lpt" // linux printer port
	};

	private static final String[] WINDOWS_PARALLEL_PORT_PREFIXES = new String[] {
			"LPT" // linux printer port
	};

	private static final String[] NO_PORT_PREFIXES = new String[0];
	private static final String[] NO_PORTS = new String[0];

	private static final int WINDOWS_MAX_SERIAL_PORTS = 256;
	private static final int WINDOWS_MAX_PARALLEL_PORTS = 256;

	private static Set<String> PORTS = new HashSet<String>();

	private static final boolean DEBUG = false;
	private static final boolean DEVEL = false;
	private static final boolean NO_VERSION_OUTPUT = "true".equals(System.getProperty("gnu.io.rxtx.NoVersionOutput"));

	static
	{
		if (RXTXCommDriver.DEBUG)
		{
			System.out.println("RXTXCommDriver {}");
		}

		/* Force the natives to be loaded. */
		SerialManager.getInstance();

		/*
		 * Perform a crude check to make sure people don't mix
		 * versions of the Jar and native lib
		 *
		 * Mixing the libs can create a nightmare.
		 *
		 * It could be possible to move this over to RXTXVersion
		 * but All we want to do is warn people when first loading
		 * the Library.
		 */
		String jarVersion = RXTXVersion.getVersion();
		String libVersion;
		try
		{
			libVersion = RXTXVersion.nativeGetVersion();
		}
		catch (UnsatisfiedLinkError e)
		{
			/* For RXTX prior to 2.1.7. */
			libVersion = RXTXCommDriver.nativeGetVersion();
		}
		if (RXTXCommDriver.DEVEL)
		{
			if (!RXTXCommDriver.NO_VERSION_OUTPUT)
			{
				System.out.println("Stable Library");
				System.out.println("=========================================");
				System.out.println("Native lib Version = " + libVersion);
				System.out.println("Java lib Version   = " + jarVersion);
			}
		}

		if (!jarVersion.equals(libVersion))
		{
//			System.out.println("WARNING:  RXTX Version mismatch\n\tJar version = " + jarVersion + "\n\tnative lib Version = " + libVersion);
		}
		else if (RXTXCommDriver.DEBUG)
		{
			System.out.println("RXTXCommDriver:\n\tJar version = " + jarVersion
					+ "\n\tnative lib Version = " + libVersion);
		}
	}

	/** Get the Serial port prefixes for the running OS */
	private String deviceDirectory;
	private static final String OS_NAME = System.getProperty("os.name");

	private native boolean registerKnownPorts(int PortType);

	private native boolean isPortPrefixValid(String dev);

	private native boolean testRead(String dev, int type);

	private native String getDeviceDirectory();

	/**
	 * Get the version of the native library.
	 * 
	 * This was deprecated in favour of {@link RXTXVersion#nativeGetVersion()}
	 * in RXTX 2.1.7.
	 * 
	 * @return the version of the native library
	 */
	@Deprecated
	private static native String nativeGetVersion();

	@Deprecated
	public static String nativeGetVersionWrapper() throws UnsatisfiedLinkError
	{
		return RXTXCommDriver.nativeGetVersion();
	}

	public Set<String> getPortIdentifiers()
	{
		RXTXCommDriver.PORTS = new HashSet<String>();
		this.registerScannedPorts(CommPortIdentifier.PORT_SERIAL);
		Enumeration<CommPortIdentifier> ids;
		try
		{
			ids = CommPortIdentifier.getPortIdentifiers();
		}
		catch (UnsatisfiedLinkError e)
		{
			e.printStackTrace();
			return null;
		}

		if (ids == null)
		{
			return Collections.emptySet();
		}

		while (ids.hasMoreElements())
		{
			CommPortIdentifier com = ids.nextElement();
			switch (com.getPortType()) {
			case CommPortIdentifier.PORT_SERIAL:
				/* Hide call-up and Bluetooth ports on macOS. They're rarely
				 * what the user is after, and if they really do want them,
				 * they can discover them for themselves. */
				if (com.getName().matches("^/.+/cu\\..+$")
						|| com.getName().matches("^/.+/tty\\.Bluetooth.+$"))
				{
					continue;
				}

				/* TODO: Under what circumstance is only a substring of
				 * `CommPortIdentifier.getName()` found in the set
				 * `RXTXCommDriver.ports`? At this point, the set should
				 * contain only whatever has been added to it by
				 * `registerValidPorts()` – which is also what informed the
				 * return value of `CommPortIdentifier.getPortIdentifiers()`.
				 * I suspect this whole loop could be removed in favour of just
				 * blindly calling:
				 *
				 *     RXTXCommDriver.ports.add(com.getName());
				 *
				 * `RXTXCommDriver.ports` is a set, after all, so a duplicate
				 * insertion is of no consequence. */
				boolean inList = false;
				for (String port : RXTXCommDriver.PORTS)
				{
					if (com.getName().contains(port))
					{
						inList = true;
					}
				}
				if (!inList)
				{
					RXTXCommDriver.PORTS.add(com.getName());
				}
			}
		}
		return RXTXCommDriver.PORTS;
	}

	/**
	 * Look for ports conforming to the Solaris/SunOS port naming convention
	 * (<code>/dev/type/[0-9a-z]</code>; e.g., <code>/dev/term/a</code>).
	 *
	 * @param portDir  the directory containing port devices (e.g.,
	 *                 <code>/dev/cua/</code>)
	 * @param portType the type of port (e.g.,
	 *                 {@link CommPortIdentifier.PORT_SERIAL})
	 */
	private void checkSolaris(String portDir, int portType)
	{
		for (char p = 'a'; p <= 'z'; ++p)
		{
			String portName = portDir + p;
			if (this.testRead(portName, portType))
			{
				CommPortIdentifier.addPortName(portName, portType, this);
				RXTXCommDriver.PORTS.add(portName);
			}
		}
		/* Check 0-9, too (Solaris USB). */
		for (char p = '0'; p <= '9'; ++p)
		{
			String portName = portDir + p;
			if (this.testRead(portName, portType))
			{
				CommPortIdentifier.addPortName(portName, portType, this);
				RXTXCommDriver.PORTS.add(portName);
			}
		}
	}

	private void registerValidPorts(
			String candidateDeviceNames[],
			String validPortPrefixes[],
			int portType,
			boolean performTestRead)
	{
		/* On Windows, port names are read directly from the registry. If we've
		 * not been explicitly instructed to test that we can access each
		 * possible port, we can reasonably trust the names and register them
		 * without any further hassle. */
		if (RXTXCommDriver.OS_NAME.toLowerCase().contains("windows") && !performTestRead)
		{
			for (String name : candidateDeviceNames)
			{
				CommPortIdentifier.addPortName(name, portType, this);
			}
			return;
		}

		if (RXTXCommDriver.DEBUG)
		{
			System.out.println("Entering registerValidPorts()");
			System.out.println(" Candidate devices:");
			for (String name : candidateDeviceNames)
			{
				System.out.println("  " + name);
			}
			System.out.println(" valid port prefixes:");
			for (String prefix : validPortPrefixes)
			{
				System.out.println("  " + prefix);
			}
		}

		if (candidateDeviceNames == null || validPortPrefixes == null)
		{
			return;
		}

		/* We want to check each device name (e.g., “sda2”, “tty29”, “ttyS0”)
		 * to see whether it matches one of the platform-specific prefixes for
		 * valid serial ports (e.g., “ttyS”, “ttyUSB”). Prefixes must check
		 * only the non-numeric part of the device name, but they must check
		 * _all_ of the non-numeric part of the device name. For example, for
		 * the prefix “/dev/ttyS”:
		 *
		 *     /dev/ttyR[0-9]*  != /dev/ttyS[0-9]*
		 *     /dev/ttySI[0-9]* != /dev/ttyS[0-9]*
		 *     /dev/ttyS[0-9]*  == /dev/ttyS[0-9]*
		 *
		 * To efficiently perform this check, we'll concatenate all of the
		 * valid prefixes into a regex of the form:
		 *
		 *     (prefixA|prefixB|prefixC)[0-9]*
		 *
		 * ...then check all of the device names against that pattern. */
		Pattern validPortPattern = Pattern.compile("(" + String.join("|", validPortPrefixes) + ")[0-9]*");

		for (String name : candidateDeviceNames)
		{
			if (!validPortPattern.matcher(name).matches())
			{
				continue;
			}

			String portName;
			if (RXTXCommDriver.OS_NAME.toLowerCase().contains("windows"))
			{
				/* Windows serial/parallel port exist in every directory.
				 * COMn/LPTn can be opened relative to anywhere. */
				portName = name;
			}
			else
			{
				portName = this.deviceDirectory + name;
			}

			if (RXTXCommDriver.OS_NAME.equals("Solaris") || RXTXCommDriver.OS_NAME.equals("SunOS"))
			{
				this.checkSolaris(portName, portType);
			}
			else if (this.testRead(portName, portType))
			{
				CommPortIdentifier.addPortName(portName, portType, this);
				RXTXCommDriver.PORTS.add(portName);
			}
		}

		if (RXTXCommDriver.DEBUG)
		{
			System.out.println("Leaving registerValidPorts()");
		}
	}

	/*
	* initialize() will be called by the CommPortIdentifier's static
	* initializer. The responsibility of this method is:
	* 1) Ensure that that the hardware is present.
	* 2) Load any required native libraries.
	* 3) Register the port names with the CommPortIdentifier.
	*
	*  From the NullDriver.java CommAPI sample.
	*
	* added printerport stuff
	* Holger Lehmann
	* July 12, 1999
	* IBM
	*
	* Added ttyM for Moxa boards
	* Removed obsolete device cuaa
	* Peter Bennett
	* January 02, 2000
	* Bencom
	*
	*/

	/**
	 * Perform any internal initialization necessary to communicate with serial
	 * ports, and detect which ports available on the system.
	 * <p>
	 * Ports are detected in three different ways, each one preferable to
	 * whatever follows it. Detection stops after any one of these methods is
	 * successful.
	 * <ol>
	 * <li>First, a configuration file is checked for static definition of port
	 * names. (See {@link RXTXCommDriver#registerSpecifiedPorts(int)} for
	 * details.)</li>
	 * <li>Then, the native code is interrogated for a list of port names. This
	 * is where port detection occurs on macOS, and it happens via
	 * <a href="https://developer.apple.com/documentation/iokit">IOKit</a> in
	 * a manner very similar to
	 * <a href=
	 * "https://developer.apple.com/documentation/iokit/communicating_with_a_modem_on_a_serial_port">
	 * the modem communication example</a>. On all other platforms, this step
	 * is a no-op.</li>
	 * <li>Finally, port names are guessed based on platform-appropriate
	 * values. All files in the platform-specific device directory (e.g.,
	 * <code>/dev</code>) are checked to see if they match the expected name
	 * of a serial device on that system.</li>
	 * </ol>
	 */
	@Override
	public void initialize()
	{
		this.deviceDirectory = this.getDeviceDirectory();

		/* First try to register ports specified in the properties
		 * file. If that doesn't exist, then scan for ports. */

		if (this.registerSpecifiedPorts(CommPortIdentifier.PORT_SERIAL))
		{
			return;
		}
		if (this.registerKnownPorts(CommPortIdentifier.PORT_SERIAL))
		{
			return;
		}
		this.registerScannedPorts(CommPortIdentifier.PORT_SERIAL);
	}

	private void addSpecifiedPorts(String names, int portType)
	{
		for (String name : names.split(System.getProperty("path.separator", ":")))
		{
			if (name.length() == 0)
			{
				continue;
			}

			if (this.testRead(name, portType))
			{
				CommPortIdentifier.addPortName(name, portType, this);
			}
		}
	}

	/**
	 * Register ports specified in an external, global configuration file.
	 * <p>
	 * Ports are specified in the properties file named
	 * <code>gnu.io.rxtx.properties</code> located in the Java extension
	 * directory.
	 * <p>
	 * Serial and parallel ports must be specified in the keys
	 * <code>gnu.io.rxtx.SerialPorts</code> and
	 * <code>gnu.io.rxtx.ParallelPorts</code>, respectively, as a
	 * <code>path.separator</code>-separated lists of paths.
	 * <p>
	 * For example:
	 *
	 * <pre>
	 * gnu.io.rxtx.SerialPorts=/dev/ttyS0:/dev/ttyS1:
	 * gnu.io.rxtx.ParallelPorts=/dev/lp0:
	 * </pre>
	 *
	 * Note that the return value of this method only indicates whether
	 * configuration for the specified port type was found; not whether any
	 * ports were found in that configuration. This method will return true for
	 * a configuration file consisting of:
	 *
	 * <pre>
	 * gnu.io.rxtx.SerialPorts=
	 * </pre>
	 *
	 * @param portType the type of port specifications to load (either
	 *                 {@link CommPortIdentifier.PORT_SERIAL} or
	 *                 {@link CommPortIdentifier.PORT_PARALLEL})
	 * @return whether the properties file and any configuration for the
	 *         specified type of port was found
	 * @see <a href="https://jonskeet.uk/java/extensions.html">
	 *      https://jonskeet.uk/java/extensions.html</a> for details on
	 *      configuring the extension directory
	 */
	private boolean registerSpecifiedPorts(int portType)
	{
		String extDirs = System.getProperty("java.ext.dirs");
		if (extDirs == null)
		{
			if (RXTXCommDriver.DEBUG)
			{
				System.out.println("No runtime extension directories specified. No ports were registered from "
						+ "configuration file.");
			}
			return false;
		}

		Properties properties = new Properties();
		for (String extDir : extDirs.split(System.getProperty("path.separator", ":")))
		{
			if (extDir.length() == 0)
			{
				continue;
			}

			Path propertiesPath = Paths.get(extDir, RXTXCommDriver.EXTENSIONS_PROPERTIES_FILENAME);
			try (FileInputStream propertiesStream = new FileInputStream(propertiesPath.toFile()))
			{
				properties.load(propertiesStream);
			}
			catch (FileNotFoundException e)
			{
				/* Not a problem: we don't really expect to find it. */
			}
			catch (IOException e)
			{
				if (RXTXCommDriver.DEBUG)
				{
					System.out.println("Error opening properties " + propertiesPath + ": " + e.getMessage());
				}
			}
		}

		if (properties.size() == 0)
		{
			if (RXTXCommDriver.DEBUG)
			{
				System.out.println("The configuration file " + RXTXCommDriver.EXTENSIONS_PROPERTIES_FILENAME + " "
						+ "could not be found in any of the extension directories. No ports were registered from file "
						+ "configuration.");
			}
			return false;
		}

		if (RXTXCommDriver.DEBUG)
		{
			System.out.println("checking for system-known ports of type " + portType);
			System.out.println("checking registry for ports of type " + portType);
		}

		String key;
		switch (portType) {
		case CommPortIdentifier.PORT_SERIAL:
			key = RXTXCommDriver.CONFIGURATION_SERIAL_KEY;
			break;
		case CommPortIdentifier.PORT_PARALLEL:
			key = RXTXCommDriver.CONFIGURATION_PARALLEL_KEY;
			break;
		default:
			if (RXTXCommDriver.DEBUG)
			{
				System.out.println("Unknown port type " + portType + ".");
			}
			return false;
		}

		String ports = properties.getProperty(key);
		if (ports != null)
		{
			this.addSpecifiedPorts(ports, portType);
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Inspect all entries in the deviceDirectory; if they look like they
	 * should be serial ports on this OS and they can be opened, then register
	 * them.
	 */
	private void registerScannedPorts(int portType)
	{
		this.deviceDirectory = this.getDeviceDirectory();
		if (RXTXCommDriver.DEBUG)
		{
			System.out.println("Scanning device directory " + this.deviceDirectory
					+ " for ports of type " + portType + ".");
		}

		String[] candidateDeviceNames = RXTXCommDriver.NO_PORTS;
		boolean performTestRead = true;

		if (RXTXCommDriver.OS_NAME.toLowerCase().contains("windows"))
		{
			if (RXTXCommDriver.isClassPresent("com.sun.jna.platform.win32.Advapi32Util"))
			{
				/* On windows, we try to get the serial port list from the
				 * registry. */
				try
				{
					candidateDeviceNames = this.windowsGetSerialPortsFromRegistry();
					/* We trust the registry; we can register the returned port
					 * names directly without performing a test read. */
					performTestRead = false;
				}
				catch (Throwable ex)
				{
					if (RXTXCommDriver.DEBUG)
					{
						System.out.println("Error reading the registry to get port list: " + ex.getMessage());
					}
				}
			}
			else
			{
				/* If we don't have access to JNA, fall back to a full scan of
				 * possible ports. */
				candidateDeviceNames = new String[RXTXCommDriver.WINDOWS_MAX_SERIAL_PORTS
						+ RXTXCommDriver.WINDOWS_MAX_PARALLEL_PORTS];
				for (int i = 0; i < RXTXCommDriver.WINDOWS_MAX_SERIAL_PORTS; ++i)
				{
					candidateDeviceNames[i] = "COM" + i;
				}
				for (int i = 0; i < RXTXCommDriver.WINDOWS_MAX_PARALLEL_PORTS; ++i)
				{
					candidateDeviceNames[RXTXCommDriver.WINDOWS_MAX_SERIAL_PORTS + i] = "LPT" + i;
				}
			}
		}
		else if (RXTXCommDriver.OS_NAME.equals("Solaris") || RXTXCommDriver.OS_NAME.equals("SunOS"))
		{
			/* Solaris ports can be organized in a few different ways. They
			 * could be `/dev/term/[a-z]`, `/dev/term[0-9]`, `/dev/cua/[a-z]`,
			 * or `/dev/cua[a-z]`. The `/dev/cua` devices are “call-up”
			 * devices, akin to `/dev/cu\..*` devices on macOS. We only look
			 * for things in `/dev/term`. */
			candidateDeviceNames = Paths.get(this.deviceDirectory, "term").toFile().list();
		}
		else
		{
			candidateDeviceNames = new File(this.deviceDirectory).list();
		}

		if (candidateDeviceNames == null || candidateDeviceNames.length == 0)
		{
			if (RXTXCommDriver.DEBUG)
			{
				System.out.println("No device files to check.");
			}
			return;
		}

		String[] candidatePortPrefixes = RXTXCommDriver.NO_PORT_PREFIXES;
		switch (portType) {
		case CommPortIdentifier.PORT_SERIAL:
			if (RXTXCommDriver.DEBUG)
			{
				System.out.println("Scanning for serial ports for OS " + RXTXCommDriver.OS_NAME + ".");
			}

			/* There are _many_ possible ports that can be used
			 * on Linux. See below in the fake Linux-all-ports
			 * case for a list. You may add additional ports
			 * here but be warned that too many will significantly
			 * slow down port enumeration. Linux 2.6 has udev
			 * support which should be faster as only ports the
			 * kernel finds should be exposed in /dev
			 *
			 * See also how to override port enumeration and
			 * specifying port in INSTALL.
			 *
			 * taj */

			if (RXTXCommDriver.OS_NAME.equals("Linux"))
			{
				candidatePortPrefixes = RXTXCommDriver.LINUX_SERIAL_PORT_PREFIXES;
			}
			else if (RXTXCommDriver.OS_NAME.equals("Linux-all-ports"))
			{
				/* If you want to enumerate all ports ~5000 possible, then
				 * replace the above with this. */
				candidatePortPrefixes = RXTXCommDriver.LINUX_ALL_SERIAL_PORT_PREFIXES;
			}
			else if (RXTXCommDriver.OS_NAME.toLowerCase().contains("qnx"))
			{
				candidatePortPrefixes = RXTXCommDriver.QNX_SERIAL_PORT_PREFIXES;
			}
			else if (RXTXCommDriver.OS_NAME.equals("Irix"))
			{
				candidatePortPrefixes = RXTXCommDriver.IRIX_SERIAL_PORT_PREFIXES;
			}
			else if (RXTXCommDriver.OS_NAME.equals("FreeBSD")) // FIXME this is probably wrong
			{
				candidatePortPrefixes = RXTXCommDriver.FREEBSD_SERIAL_PORT_PREFIXES;
			}
			else if (RXTXCommDriver.OS_NAME.equals("NetBSD")) // FIXME this is probably wrong
			{
				candidatePortPrefixes = RXTXCommDriver.NETBSD_SERIAL_PORT_PREFIXES;
			}
			else if (RXTXCommDriver.OS_NAME.equals("Solaris") || RXTXCommDriver.OS_NAME.equals("SunOS"))
			{
				candidatePortPrefixes = RXTXCommDriver.SOLARIS_SERIAL_PORT_PREFIXES;
			}
			else if (RXTXCommDriver.OS_NAME.equals("HP-UX"))
			{
				candidatePortPrefixes = RXTXCommDriver.HPUX_SERIAL_PORT_PREFIXES;
			}
			else if (RXTXCommDriver.OS_NAME.equals("UnixWare") || RXTXCommDriver.OS_NAME.equals("OpenUNIX"))
			{
				candidatePortPrefixes = RXTXCommDriver.UNIXWARE_SERIAL_PORT_PREFIXES;
			}
			else if (RXTXCommDriver.OS_NAME.equals("OpenServer"))
			{
				candidatePortPrefixes = RXTXCommDriver.OPENSERVER_SERIAL_PORT_PREFIXES;
			}
			else if (RXTXCommDriver.OS_NAME.equals("Compaq's Digital UNIX") || RXTXCommDriver.OS_NAME.equals("OSF1"))
			{
				candidatePortPrefixes = RXTXCommDriver.DIGITAL_UNIX_SERIAL_PORT_PREFIXES;
			}
			else if (RXTXCommDriver.OS_NAME.equals("BeOS"))
			{
				candidatePortPrefixes = RXTXCommDriver.BEOS_SERIAL_PORT_PREFIXES;
			}
			else if (RXTXCommDriver.OS_NAME.equals("Mac OS X"))
			{
				/* Under normal circumstances, this is not called: macOS serial
				 * ports are detected by the native code in
				 * `registerKnownPorts()`. */
				candidatePortPrefixes = RXTXCommDriver.MACOS_SERIAL_PORT_PREFIXES;
			}
			else if (RXTXCommDriver.OS_NAME.toLowerCase().contains("windows"))
			{
				candidatePortPrefixes = RXTXCommDriver.WINDOWS_SERIAL_PORT_PREFIXES;
			}
			else if (RXTXCommDriver.DEBUG)
			{
				/* TODO: Replace with real logging. */
				System.out.println("RXTXCommDriver.registerScannedPorts(): No valid serial port name prefixes are "
						+ "known for " + RXTXCommDriver.OS_NAME + ".");
			}
			break;

		case CommPortIdentifier.PORT_PARALLEL:
			if (RXTXCommDriver.DEBUG)
			{
				System.out.println("Scanning for parallel ports for OS " + RXTXCommDriver.OS_NAME + ".");
			}

			/**
			 * Get the Parallel port prefixes for the running os
			 * Holger Lehmann
			 * July 12, 1999
			 * IBM
			 */
			if (RXTXCommDriver.OS_NAME.equals("Linux")
			/* || osName.equals("NetBSD") FIXME
			 * || osName.equals("HP-UX") FIXME
			 * || osName.equals("Irix") FIXME
			 * || osName.equals("BeOS") FIXME
			 * || osName.equals("Compaq's Digital UNIX") FIXME */
			)
			{
				candidatePortPrefixes = RXTXCommDriver.LINUX_PARALLEL_PORT_PREFIXES;
			}
			else if (RXTXCommDriver.OS_NAME.equals("FreeBSD"))
			{
				candidatePortPrefixes = RXTXCommDriver.FREEBSD_PARALLEL_PORT_PREFIXES;
			}
			else if (RXTXCommDriver.OS_NAME.toLowerCase().indexOf("windows") != -1)
			{
				candidatePortPrefixes = RXTXCommDriver.WINDOWS_PARALLEL_PORT_PREFIXES;
			}
			else /* printer support is green */
			{
				candidatePortPrefixes = RXTXCommDriver.NO_PORT_PREFIXES;
			}
			break;
		default:
			if (RXTXCommDriver.DEBUG)
			{
				System.out.println("Unknown port type " + portType + ".");
			}
		}
		this.registerValidPorts(candidateDeviceNames, candidatePortPrefixes, portType, performTestRead);
	}

	/**
	 * @return an array containing the serial ports, taken from registry
	 *         HKEY_LOCAL_MACHINE\HARDWARE\DEVICEMAP\SERIALCOMM
	 * @throws Exception in case something goes wrong with the registry
	 */
	private String[] windowsGetSerialPortsFromRegistry() throws Exception
	{
		return Advapi32Util.registryGetValues(WinReg.HKEY_LOCAL_MACHINE, "HARDWARE\\DEVICEMAP\\SERIALCOMM")
				.values()
				.stream()
				.filter(Objects::nonNull)
				.map(Object::toString)
				.filter(p -> p.startsWith("COM"))
				.toArray(String[]::new);
	}

	/**
	 * Function to test if a given class is currently present
	 *
	 * @param className The desired class name
	 * @return true, if class found, false otherwise
	 */
	private static boolean isClassPresent(final String className)
	{
		try
		{
			Class.forName(className);
			return true;
		}
		catch (Throwable ex)
		{
			// Class or one of its dependencies is not present.
			return false;
		}
	}

	/* From the NullDriver.java CommAPI sample. */
	/**
	 * This is called by {@link CommPortIdentifier#open(String, int)}.
	 * <code>portName</code> is the string that was registered earlier via
	 * {@link CommPortIdentifier#addPortName(String, int, CommDriver)}.
	 * returns an object that extends either SerialPort or ParallelPort.
	 *
	 * @param portName The name of the port the OS recognizes
	 * @param portType CommPortIdentifier.PORT_SERIAL or PORT_PARALLEL
	 * @return the {@link RXTXPort} port
	 */
	@Override
	public CommPort getCommPort(String portName, int portType)
	{
		try
		{
			switch (portType) {
			case CommPortIdentifier.PORT_SERIAL:
				if (!RXTXCommDriver.OS_NAME.toLowerCase().contains("windows"))
				{
					return new RXTXPort(portName);
				}
				else
				{
					return new RXTXPort(this.deviceDirectory + portName);
				}
			default:
				if (RXTXCommDriver.DEBUG)
				{
					System.out.println("Unknown port type  " + portType + "!");
				}
			}
		}
		catch (PortInUseException e)
		{
			if (RXTXCommDriver.DEBUG)
			{
				System.out.println("Port " + portName + " in use by another application!");
			}
		}
		return null;
	}

	/* Yikes. Trying to call println from C for odd reasons */
	@Deprecated
	public void Report(String arg)
	{
		System.out.println(arg);
	}
}
