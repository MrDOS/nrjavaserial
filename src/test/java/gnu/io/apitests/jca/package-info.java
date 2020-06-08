/**
 * This package contains tests to ensure that the NRJavaSerial API surface
 * remains in compliance with the Java Communications API. Each test class in
 * this package corresponds to a class or interface originally specified in the
 * <code>javax.comm</code> package, and each method of those test classes
 * corresponds to a method or field of the corresponding class.
 * <p>
 * These tests are intended to be inspect the form of the API, not its
 * functionality. They rely heavily on reflection, and are tied only loosely to
 * the RXTX/NRJavaSerial implementation of the Java Communications API. Type
 * names in tests are given as strings, which is a double-edged sword: it
 * suppresses compiler errors for tests where the implementation is not
 * compliant, intentional or otherwise.
 * <p>
 * See also the {@link gnu.io.apitests.rxtx} package, which contains tests to
 * ensure that NRJavaSerial retains backwards compatibility with the RXTX API.
 */
package gnu.io.apitests.jca;
