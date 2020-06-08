/**
 * This package contains tests to ensure that the NRJavaSerial API surface
 * remains backwards-compatible with RXTX extensions to the Java Communications
 * API. Each test class in this package corresponds to a class or interface
 * originally specified in the <code>gnu.io</code> package, and each method of
 * those test classes corresponds to an extension method or field of the
 * corresponding class.
 * <p>
 * These tests are intended to be inspect the form of the API, not its
 * functionality. They rely heavily on reflection.
 * <p>
 * See also the {@link gnu.io.apitests.jca} package, which contains tests to
 * ensure that the NRJavaSerial API surface retains compliance with the Java
 * Communications API.
 */
package gnu.io.apitests.rxtx;
