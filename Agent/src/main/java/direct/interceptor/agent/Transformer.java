package direct.interceptor.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

class Transformer implements AgentBuilder.Transformer {

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public DynamicType.Builder transform(
            DynamicType.Builder builder,
            TypeDescription typeDescription) {
        return builder
                .method(ElementMatchers.any())
                .intercept(
                        MethodDelegation
                                .to(new Interceptor()));
    }
}