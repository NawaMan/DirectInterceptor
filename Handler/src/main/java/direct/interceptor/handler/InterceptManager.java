package direct.interceptor.handler;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface InterceptManager {
	
	public InterceptHandler getHandler(
			Annotation annotation,
			Class<?> annotationClass,
			Class<?> originClasss,
			Method originMethod);
	
}
