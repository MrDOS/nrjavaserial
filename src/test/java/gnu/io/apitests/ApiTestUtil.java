package gnu.io.apitests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class contains utility methods for testing the signatures of classes,
 * fields, constructors, and methods. Test coverage of these elements of the
 * public API helps avoid introducing regressions during refactoring.
 *
 * <h3>Caller name and class as implicit parameters</h3>
 *
 * To keep the number of parameters of the assertion methods provided by this
 * class to a minimum, two implicit parameters are derived from the calling
 * method: its name, and the name of the class which contains it. For field and
 * method tests, the name of the calling method is taken to be the name of the
 * field or method under test; and for all tests, the name of the class
 * containing the calling method is taken as the name of the class under test.
 * <p>
 * To give a concrete example:
 * 
 * <pre>
 * class Bar
 * {
 * 	&#64;Test
 * 	void foo()
 * 	{
 * 		assertMethodSignature("boolean", Modifiers.PUBLIC);
 * 	}
 * }
 * </pre>
 * 
 * In this case, the test asserts that some other class <code>Bar</code> has a
 * public method named <code>foo</code> which returns a boolean value.
 *
 * <h3>Name and package filtering</h3>
 *
 * <ul>
 * <li>Want to have tests for class <code>Bar</code> in class
 * <code>BarTest</code>.</li>
 * <li>Want to have implementation vs. tests in different packages.</li>
 * <li>Want tests to refer to upstream/reference packages
 * (<code>javax.comm.*</code>).</li>
 * <li>Want to be able to test overloaded methods.</li>
 * <li>Filtering:
 * <ul>
 * <li><code>Test</code> suffix on test classes ignored.</li>
 * <li>Only simple test class name used; implementation class named derived
 * from concatenation with a constant package name.</li>
 * <li>Instances of reference package in the names of expected types
 * substituted with implementation package before comparison
 * (<code>javax.comm.Foo</code> → <code>gnu.io.Foo</code>).</li>
 * <li>Test method names stripped of <code>$nnn</code> suffixes
 * (<code>foo$1()</code> → <code>foo()</code>, <code>foo$2()</code> →
 * <code>foo()</code> as well).</li>
 * </ul>
 * </li>
 * </ul>
 */
public class ApiTestUtil
{
	/**
	 * Can be used to improve clarity of a test of a constructor or method
	 * which takes no parameters.
	 */
	public static final List<String> NO_PARAMETERS = Collections.emptyList();
	/**
	 * Can be used to improve clarity of a test of a constructor or method
	 * which throws no exceptions.
	 */
	public static final Set<String> NO_EXCEPTIONS = Collections.emptySet();

	/**
	 * The package containing the reference implementation against which we're
	 * comparing.
	 */
	private static final String REFERENCE_PACKAGE = "javax.comm";
	/** The package containing the implementation we're testing. */
	private static final String IMPLEMENTATION_PACKAGE = System.getProperty("JCA_IMPLEMENTATION_PACKAGE", "gnu.io");

	/**
	 * Synchronized and native methods are an implementation detail, so those
	 * modifiers should be ignored.
	 */
	static final int MODIFIER_MASK = ~(Modifier.NATIVE | Modifier.SYNCHRONIZED);

	public static void assertSuperclass(String expectedSuperclassType)
	{
		ApiTestTarget target = ApiTestUtil.getTestTarget();

		expectedSuperclassType = ApiTestUtil.filterClassName(expectedSuperclassType);
		String superclassType = target.clazz.getSuperclass().getName();
		assertEquals(expectedSuperclassType, superclassType, "Unexpected superclass");
	}

	public static void assertInterface(String expectedInterfaceType)
	{
		ApiTestUtil.assertInterfaces(Collections.singleton(expectedInterfaceType));
	}

	public static void assertInterfaces(Set<String> expectedInterfaceTypes)
	{
		ApiTestTarget target = ApiTestUtil.getTestTarget();

		expectedInterfaceTypes = expectedInterfaceTypes.stream()
				.map(ApiTestUtil::filterClassName)
				.collect(Collectors.toSet());
		Set<String> interfaceTypes = Arrays.stream(target.clazz.getInterfaces())
				.map(Class::getName)
				.collect(Collectors.toSet());
		assertEquals(expectedInterfaceTypes, interfaceTypes, "Unexpected interfaces");
	}

	/**
	 * Locate a class field by name and confirm that its signature matches
	 * expectations.
	 *
	 * @param className         the fully-qualified name of the class
	 *                          containing the field
	 * @param fieldName         the name of the field
	 * @param expectedType      the types of the field
	 * @param expectedModifiers the exact modifiers expected of the field
	 */
	public static void assertFieldSignature(String expectedType,
			int expectedModifiers)
	{
		ApiTestTarget target = ApiTestUtil.getTestTarget();

		Field field;
		try
		{
			field = target.clazz.getDeclaredField(target.memberName);
		}
		catch (NoSuchFieldException e)
		{
			fail(String.format("The field \"%s.%s\" does not exist",
					target.clazz.getName(),
					target.memberName),
					e);
			return;
		}
		catch (SecurityException e)
		{
			fail(String.format("Encountered a security exception accessing the field \"%s.%s\"",
					target.clazz.getName(),
					target.memberName),
					e);
			return;
		}

		String type = field.getType().getName();
		assertEquals(expectedType, type, "Unexpected field type");

		int modifiers = field.getModifiers();
		assertEquals(expectedModifiers, modifiers,
				String.format("Expected the field to be \"%s\", but it is \"%s\"",
						Modifier.toString(expectedModifiers),
						Modifier.toString(modifiers)));
	}

	/**
	 * Locate a class constructor by name and confirm that its signature
	 * matches expectations.
	 *
	 * @param className              the fully-qualified name of the class
	 *                               containing the method
	 * @param parameterTypes         the types of the constructor parameters
	 * @param expectedModifiers      the exact modifiers expected of the
	 *                               constructors
	 * @param expectedExceptionTypes the types the constructor is expected to
	 *                               throw
	 */
	public static void assertConstructorSignature(List<String> parameterTypes, int expectedModifiers,
			Set<String> expectedExceptionTypes)
	{
		ApiTestTarget target = ApiTestUtil.getTestTarget();

		Class<?>[] typedParameterTypes;
		try
		{
			typedParameterTypes = ApiTestUtil.classNamesToClasses(parameterTypes);
		}
		catch (ClassNotFoundException e)
		{
			fail(
					String.format("Could not locate or load the class of one of the parameters: %s", e.getMessage()),
					e);
			return;
		}

		Constructor<?> constructor;
		try
		{
			constructor = target.clazz.getConstructor(typedParameterTypes);
		}
		catch (NoSuchMethodException e)
		{
			fail(String.format("The constructor \"%s(%s)\" does not exist",
					target.clazz.getName(),
					String.join(", ", parameterTypes)),
					e);
			return;
		}
		catch (SecurityException e)
		{
			fail(String.format("Encountered a security exception accessing the constructor \"%s(%s)\"",
					target.clazz.getName(),
					String.join(", ", parameterTypes)),
					e);
			return;
		}

		int modifiers = constructor.getModifiers() & ApiTestUtil.MODIFIER_MASK;
		assertEquals(expectedModifiers, modifiers,
				String.format("Expected the constructor to be \"%s\", but it is \"%s\"",
						Modifier.toString(expectedModifiers),
						Modifier.toString(modifiers)));

		expectedExceptionTypes = expectedExceptionTypes.stream()
				.map(ApiTestUtil::filterClassName)
				.collect(Collectors.toSet());
		Set<String> exceptionTypes = Arrays.stream(constructor.getExceptionTypes())
				.map(Class::getName)
				.collect(Collectors.toSet());
		assertEquals(expectedExceptionTypes, exceptionTypes, "Unexpected exception types");
	}

	/**
	 * Locate a parameter-less, exception-less class method by the name of the
	 * caller and confirm that its signature matches expectations.
	 *
	 * @param expectedReturnType the return type of the method
	 * @param expectedModifiers  the exact modifiers expected of the method
	 */
	public static void assertMethodSignature(String expectedReturnType, int expectedModifiers)
	{
		ApiTestUtil.assertMethodSignature(
				ApiTestUtil.NO_PARAMETERS,
				expectedReturnType,
				expectedModifiers,
				ApiTestUtil.NO_EXCEPTIONS);
	}

	/**
	 * Locate a unary, exception-less class method by the name of the caller
	 * and confirm that its signature matches expectations.
	 *
	 * @param expectedReturnType the return type of the method
	 * @param expectedModifiers  the exact modifiers expected of the method
	 */
	public static void assertMethodSignature(String parameterType, String expectedReturnType, int expectedModifiers)
	{
		ApiTestUtil.assertMethodSignature(
				Collections.singletonList(parameterType),
				expectedReturnType,
				expectedModifiers,
				ApiTestUtil.NO_EXCEPTIONS);
	}

	/**
	 * Locate a class method by the name of the caller and confirm that its
	 * signature matches expectations.
	 *
	 * @param parameterTypes         the types of the method parameters
	 * @param expectedReturnType     the return type of the method
	 * @param expectedModifiers      the exact modifiers expected of the method
	 * @param expectedExceptionTypes the types the method is expected to throw
	 */
	public static void assertMethodSignature(List<String> parameterTypes,
			String expectedReturnType, int expectedModifiers, Set<String> expectedExceptionTypes)
	{
		ApiTestTarget target = ApiTestUtil.getTestTarget();

		Class<?>[] typedParameterTypes;
		try
		{
			typedParameterTypes = ApiTestUtil.classNamesToClasses(parameterTypes);
		}
		catch (ClassNotFoundException e)
		{
			fail(
					String.format("Could not locate or load the class of one of the parameters: %s", e.getMessage()),
					e);
			return;
		}

		Method method;
		try
		{
			method = target.clazz.getDeclaredMethod(target.memberName, typedParameterTypes);
		}
		catch (NoSuchMethodException e)
		{
			fail(String.format("The method \"%s.%s(%s)\" does not exist",
					target.clazz.getName(),
					target.memberName,
					String.join(", ", parameterTypes)),
					e);
			return;
		}
		catch (SecurityException e)
		{
			fail(String.format("Encountered a security exception accessing the method \"%s.%s(%s)\"",
					target.clazz.getName(),
					target.memberName,
					String.join(", ", parameterTypes)),
					e);
			return;
		}

		expectedReturnType = ApiTestUtil.filterClassName(expectedReturnType);
		String returnType = method.getReturnType().getName();
		assertEquals(expectedReturnType, returnType, "Unexpected return type");

		int modifiers = method.getModifiers() & ApiTestUtil.MODIFIER_MASK;
		assertEquals(expectedModifiers, modifiers,
				String.format("Expected the method to be \"%s\", but it is \"%s\"",
						Modifier.toString(expectedModifiers),
						Modifier.toString(modifiers)));

		expectedExceptionTypes = expectedExceptionTypes.stream()
				.map(ApiTestUtil::filterClassName)
				.collect(Collectors.toSet());
		Set<String> exceptionTypes = Arrays.stream(method.getExceptionTypes())
				.map(Class::getName)
				.collect(Collectors.toSet());
		assertEquals(expectedExceptionTypes, exceptionTypes, "Unexpected exception types");
	}

	private static class ApiTestTarget
	{
		final Class<?> clazz;
		final String memberName;

		public ApiTestTarget(StackTraceElement caller) throws ClassNotFoundException
		{
			String className = caller.getClassName();
			className = ApiTestUtil.IMPLEMENTATION_PACKAGE + className.substring(className.lastIndexOf('.'));
			className = className.replaceAll("Test$", "");

			String memberName = caller.getMethodName();
			memberName = memberName.replaceAll("\\$\\d+$", "");

			this.clazz = ApiTestUtil.classForName(className);
			this.memberName = memberName;
		}
	}

	private static ApiTestTarget getTestTarget()
	{
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		/* trace[0] will be Thread.currentThread().getStackTrace(), trace[1]
		 * will be this method, and trace[2] will be the method which asked us
		 * to find its out-of-class caller; so the first reasonable possibility
		 * is trace[3]. */
		for (int i = 3; i < trace.length; ++i)
		{
			if (trace[i].getClassName().equals(ApiTestUtil.class.getName()))
			{
				continue;
			}

			try
			{
				return new ApiTestTarget(trace[i]);
			}
			catch (ClassNotFoundException e)
			{
				fail(String.format("Could not locate or load the class \"%s\"", trace[i].getClassName()), e);
				return null;
			}
		}
		fail("Internal test utility failure: couldn't identify the caller");
		return null;
	}

	private static String filterClassName(String className)
	{
		if (className.startsWith(ApiTestUtil.REFERENCE_PACKAGE + "."))
		{
			return ApiTestUtil.IMPLEMENTATION_PACKAGE + className.substring(ApiTestUtil.REFERENCE_PACKAGE.length());
		}
		else
		{
			return className;
		}
	}

	private static Class<?> classForName(String className) throws ClassNotFoundException
	{
		switch (className) {
		case "boolean":
			return boolean.class;
		case "byte":
			return byte.class;
		case "char":
			return char.class;
		case "double":
			return double.class;
		case "float":
			return float.class;
		case "int":
			return int.class;
		case "long":
			return long.class;
		case "short":
			return short.class;
		case "void":
			return void.class;
		}

		className = ApiTestUtil.filterClassName(className);
		return Class.forName(className);
	}

	private static Class<?>[] classNamesToClasses(List<String> classNames) throws ClassNotFoundException
	{
		Class<?>[] classes = new Class<?>[classNames.size()];
		int i = 0;
		/* for-each loop instead of classNames.stream().map(...) because you
		 * can't throw from within a map operation. */
		for (String className : classNames)
		{
			classes[i++] = ApiTestUtil.classForName(className);
		}
		return classes;
	}
}
