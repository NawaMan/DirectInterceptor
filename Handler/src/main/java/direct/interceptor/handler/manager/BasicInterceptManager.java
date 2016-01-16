package direct.interceptor.handler.manager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import direct.interceptor.handler.InterceptHandler;
import direct.interceptor.handler.InterceptHandlerSupplier;
import direct.interceptor.handler.InterceptManager;

public class BasicInterceptManager implements InterceptManager {

	final Map<Class<?>, InterceptHandlerSupplier> handlers = new ConcurrentHashMap<>();
	
	public BasicInterceptManager(Map<Class<?>, InterceptHandlerSupplier> handlers) {
		for (Entry<Class<?>, InterceptHandlerSupplier> entry : handlers.entrySet()) {
			Class<?> clss = entry.getKey();
			InterceptHandlerSupplier handler = entry.getValue();
			if (clss == null) {
				continue;
			}
			if (handler == null) {
				continue;
			}
			this.handlers.put(clss, handler);
		}
	}
	
	@Override
	public InterceptHandler getHandler(
			Annotation annotation,
			Class<?> annotationClass,
			Class<?> originClasss,
			Method originMethod) {
		InterceptHandlerSupplier supplier = handlers.get(annotationClass);
		InterceptHandler handler = (supplier != null) ? supplier.getHandler() : null;
		return handler;
	}

}
