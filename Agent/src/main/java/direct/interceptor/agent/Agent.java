package direct.interceptor.agent;

import static net.bytebuddy.matcher.ElementMatchers.nameMatches;
import static net.bytebuddy.matcher.ElementMatchers.not;

import java.lang.instrument.Instrumentation;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatcher.Junction;

public class Agent {

	private static final $Util util = $Util.getInstance();

	// Due to some problem with error handling in ByteBuddy 0.7.7,
	// There is error message that is deem to be no danger so we will ignore it.
	private static final Set<String> workaroundMessages;
	static {
		List<String> messages = util.linesFromResource(Agent.class, "workaround-messages-list.txt");
		workaroundMessages = Collections.unmodifiableSet(new HashSet<>(messages));
	}

	
	private static final List<String> ignoredPackages;
	static {
		List<String> packages = util.linesFromResource(Agent.class, "ignored-packages-list.txt");
		ignoredPackages = Collections.unmodifiableList(packages);
	}

	
	private static void onError(String errMsg, Throwable throwable) {
		// TODO Should propose to ByteBuddy that this case more accurately
		// detectable
		// as this case it is quite common.
		// (unless super complex matcher was done)
		if (IllegalStateException.class.isInstance(throwable)) {
			String message = throwable.getMessage();
			if (workaroundMessages.contains(message)) {
				return;
			}
		}
		System.out.println("Error - " + errMsg + ", " + throwable.getMessage());
		throwable.printStackTrace();
	}

	/**
	 * The premain method which will be run before the main method (dah!). It
	 * perform the registration for class transformation.
	 * 
	 * @param agentArgument
	 *            the agent argument.
	 * @param instrumentation
	 *            the instrumentation object.
	 */
	@SuppressWarnings({ "unchecked" })
	public static void premain(String agentArgument, Instrumentation instrumentation) {
		try {
			$AgentListener         listener        = theListener();
			$ClassTransformer      transformer     = new $ClassTransformer(agentArgument);
			Junction<NamedElement> ignoredPackages = theIgnoredPackages();
			Junction<NamedElement> exceptTypes     = not(ignoredPackages).and(notAnnotation()).and(notInterface());

			new AgentBuilder.Default()
					.withListener(listener)
					.type(exceptTypes)
					.transform(transformer)
					.installOn(instrumentation);
		} catch (RuntimeException e) {
			System.out.println("Exception instrumenting code : " + e);
			e.printStackTrace();
		}
	}
	
	private static $AgentListener theListener() {
		$AgentListener listener = new $AgentListener() {
			@Override
			public void onError(String errMsg, Throwable throwable) {
				Agent.onError(errMsg, throwable);
			}
		};
		return listener;
	}
	
	private static Junction<NamedElement> theIgnoredPackages() {
		Junction<NamedElement> ignored = null;
		for (String exceptPackage : ignoredPackages) {
			String regEx = util.matchPackageNameStartWith(exceptPackage);
			Junction<NamedElement> except = nameMatches(regEx);
			ignored = (ignored == null) ? except : ignored.or(except);
		}
		return ignored;
	}

	@SuppressWarnings("rawtypes")
	private static ElementMatcher notAnnotation() {
		return new ElementMatcher() {
			@Override
			public boolean matches(Object target) {
				TypeDescription type = (TypeDescription) target;
				return !type.isAnnotation();
			}

		};
	}

	@SuppressWarnings("rawtypes")
	private static ElementMatcher notInterface() {
		return new ElementMatcher() {
			@Override
			public boolean matches(Object target) {
				TypeDescription type = (TypeDescription) target;
				return !type.isInterface();
			}

		};
	}
}
