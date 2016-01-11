package direct.interceptor.examples.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@InterceptorAnnotation
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD})
public @interface Log {
	
	public String value() default "LOG!!!";
	
	public Class<?> handler() default LogHandler.class;
	
}
