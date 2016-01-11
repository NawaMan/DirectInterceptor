package direct.interceptor.handler;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface InterceptionHandlerFinally {

	public <T> void handleFinally(
			long startTime,
			Annotation anno,
			Class<T> clazz,
			Object object,
			Method method,
			Parameters parameters,
			long finishTime,
			Object result,
			Throwable throwable);
	
}
