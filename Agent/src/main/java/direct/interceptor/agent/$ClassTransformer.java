package direct.interceptor.agent;

import java.lang.reflect.Method;

import direct.interceptor.handler.InterceptManager;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

class $ClassTransformer implements AgentBuilder.Transformer {

	private final String option;

	public $ClassTransformer(String option) {
		this.option = option;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public DynamicType.Builder transform(DynamicType.Builder builder, TypeDescription typeDescription) {

		// TODO These should be cached
		ElementMatcher<MethodDescription> matcher = ElementMatchers.isAnnotatedWith(ElementMatchers.nameContains(".intercept."));
		if (option != null) {
			if (option.startsWith("nameContains=")) {
				String contains = option.substring("nameContains=".length());
				matcher = ElementMatchers.isAnnotatedWith(ElementMatchers.nameContains(contains));
			} else if (option.startsWith("nameStartsWith=")) {
				String prefix = option.substring("nameStartsWith=".length());
				matcher = ElementMatchers.isAnnotatedWith(ElementMatchers.nameStartsWith(prefix));
			} else if (option.startsWith("nameEndsWith=")) {
				String prefix = option.substring("nameEndsWith=".length());
				matcher = ElementMatchers.isAnnotatedWith(ElementMatchers.nameEndsWith(prefix));
			} else {
				ElementMatcher<MethodDescription> nameContains = annotationNameContains();
				if (nameContains != null) {
					matcher = nameContains;
				}
			}
		}
		
		// TODO This should be cached.

		InterceptManager handler = null;
		String managerClassName = System.getenv().get("manager");
		if (managerClassName != null) {
			Class<?> managerClass = null;
			try {
				managerClass = Class.forName(managerClassName);
				handler = (InterceptManager)managerClass.newInstance();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
		
		
		Interceptor interceptor = new Interceptor(handler);
		
		return builder.method(matcher).intercept(MethodDelegation.to(interceptor));
	}

	private ElementMatcher<MethodDescription> annotationNameContains() {
		String part = null;
		Class<?> clazz = null;
		try {
			clazz = Class.forName(option);
		} catch (Exception exception) {
		}

		Object instance = null;
		if (clazz != null) {
			try {
				instance = clazz.newInstance();
			} catch (Exception exception) {
			}
		}
		
		if (instance != null) {
			Method method = null;
			if (part == null) {
				try {
					method = clazz.getMethod("nameContains");
				} catch (Exception exception) {
				}
				if ((method != null) && String.class.equals(method.getReturnType())) {
					try {
						part = (String) method.invoke(instance);
					} catch (Exception exception) {
					}
					if (part != null) {
						return ElementMatchers.isAnnotatedWith(ElementMatchers.nameContains(part));
					}
				}
			}

			if (part == null) {
				try {
					method = clazz.getMethod("nameStartsWith");
				} catch (Exception exception) {
				}
				if ((method != null) && String.class.equals(method.getReturnType())) {
					try {
						part = (String) method.invoke(instance);
					} catch (Exception exception) {
					}
					if (part != null) {
						return ElementMatchers.isAnnotatedWith(ElementMatchers.nameStartsWith(part));
					}
				}
			}

			if (part == null) {
				try {
					method = clazz.getMethod("nameEndsWith");
				} catch (Exception exception) {
				}
				if ((method != null) && String.class.equals(method.getReturnType())) {
					try {
						part = (String) method.invoke(instance);
					} catch (Exception exception) {
					}
					if (part != null) {
						return ElementMatchers.isAnnotatedWith(ElementMatchers.nameEndsWith(part));
					}
				}
			}
		}
		
		return null;
	}
}