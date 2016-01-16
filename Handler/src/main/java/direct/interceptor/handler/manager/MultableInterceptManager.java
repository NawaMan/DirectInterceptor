package direct.interceptor.handler.manager;

import java.util.Map;

import direct.interceptor.handler.InterceptHandlerSupplier;

public class MultableInterceptManager extends BasicInterceptManager {

	public MultableInterceptManager(Map<Class<?>, InterceptHandlerSupplier> handlers) {
		super(handlers);
	}
	
	public void put(Class<?> clss, InterceptHandlerSupplier handler) {
		handlers.put(clss, handler);
	}

}
