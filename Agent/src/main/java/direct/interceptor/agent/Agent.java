package direct.interceptor.agent;

import static net.bytebuddy.matcher.ElementMatchers.nameMatches;
import static net.bytebuddy.matcher.ElementMatchers.not;

import java.lang.instrument.Instrumentation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.matcher.ElementMatcher.Junction;

public class Agent {

	// Due to some problem with error handling in ByteBuddy 0.7.7,
	// There is error message that is deem to be no danger so we will ignore it.
	private static final Set<String> workaroundMessages;

	static {
		Set<String> messages = new HashSet<>();
		messages.add("Injecting classes into the bootstrap class loader was not enabled");
		messages.add("Cannot define method 'equals' with the given signature as an annotation type method");
		workaroundMessages = Collections.unmodifiableSet(messages);
	}

	private static final String exceptTypesRegex = "^(java\\.|sun\\.|direct\\.interceptor\\.agent|direct\\.interceptor\\.handler).*";

	private static void onError(String errMsg, Throwable throwable) {
		// TODO Should propose to have this case more accurately detectable
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

	public static void premain(String agentArgument, Instrumentation instrumentation) {
		try {
			Listener listener = new Listener() {
				@Override
				public void onError(String errMsg, Throwable throwable) {
					Agent.onError(errMsg, throwable);
				}
			};

			Transformer transformer = new Transformer();

			Junction<NamedElement> exceptTypes = not(nameMatches(exceptTypesRegex));

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
}