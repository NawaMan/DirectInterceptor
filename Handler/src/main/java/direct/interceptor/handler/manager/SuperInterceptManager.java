package direct.interceptor.handler.manager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import direct.interceptor.handler.InterceptHandler;
import direct.interceptor.handler.InterceptManager;

public class SuperInterceptManager implements InterceptManager {

	private List<InterceptManager> managers = new ArrayList<>();
	
	public void addManager(InterceptManager manager) {
		if (manager == null) {
			return;
		}
		this.managers.add(manager);
	}
	
	@Override
	public InterceptHandler getHandler(
			Annotation annotation,
			Class<?> annotationClass,
			Class<?> originClasss,
			Method originMethod) {
		for (InterceptManager manager : managers) {
			InterceptHandler handler = manager.getHandler(annotation, annotationClass, originClasss, originMethod);
			if (handler != null) {
				return handler;
			}
		}
		return null;
	}

}
