package di.example.log.app;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import direct.interceptor.handler.InterceptionHandler;

public class InterceptorManager implements direct.interceptor.handler.InterceptionManager {

	@Override
	public InterceptionHandler getHandler(Annotation annotation, Class<?> annotationClass, Class<?> originClasss, Method originMethod) {
		if (annotationClass.equals(annotationClass)) {
			return new LogHandler();
		}
		return null;
	}

}
