package direct.interceptor.handler;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface InterceptHandlerThrowable {

	public <T> void handleThrowable(
			long startTime,
			Annotation anno,
			Class<T> clazz,
			Object object,
			Method method,
			Parameters parameters,
			long finishTime,
			Throwable throwable);
	
}
