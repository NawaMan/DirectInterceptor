package direct.interceptor.handler.manager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import direct.interceptor.handler.InterceptHandler;
import direct.interceptor.handler.InterceptManager;

public class RootInterceptManager { 
	
	static InterceptManager manager = new SuperInterceptManager();
	
	public static void addManager(InterceptManager manager) {
		if (manager == null) {
			return;
		}
		if (!(RootInterceptManager.manager instanceof SuperInterceptManager)) {
			return;
		}
		
		((SuperInterceptManager)RootInterceptManager.manager).addManager(manager);
	}
	
	public static InterceptHandler getHandler(
			Annotation annotation,
			Class<?> annotationClass,
			Class<?> originClasss,
			Method originMethod) {
		InterceptHandler handler = manager.getHandler(annotation, annotationClass, originClasss, originMethod);
		return handler;
	}
	
}
