package direct.interceptor.handler;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface InterceptionManager {
	
	public InterceptionHandler getHandler(
			Annotation annotation,
			Class<?> annotationClass,
			Class<?> originClasss,
			Method originMethod);
	
}
