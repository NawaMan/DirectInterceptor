package direct.interceptor.handler;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface InterceptionHandlerBefore {

	public <T> void handleBefore(
			long startTime,
			Annotation anno,
			Class<T> clazz,
			Object object,
			Method method,
			Parameters parameters);
	
}
