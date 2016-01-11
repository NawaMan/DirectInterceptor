package direct.interceptor.agent;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import direct.interceptor.handler.Param;

final class ParameterPuttersProvider {
	
	public static final ParameterPuttersProvider NULL = new ParameterPuttersProvider(null);
	
	private static final ConcurrentHashMap<Method, ParameterPuttersProvider> providers = new ConcurrentHashMap<>();
	
	public static ParameterPuttersProvider of(
			Method method) {
		if (method == null) {
			return NULL;
		}
		
		ParameterPuttersProvider preovider = providers.computeIfAbsent(method,m->new ParameterPuttersProvider(m));
		return preovider;
	}
	
	
	private final Method method;

	private volatile ParameterPutters putters = null;

	private ParameterPuttersProvider(Method method) {
		this.method = method;
	}

	public ParameterPutters getPutters() {
		if (putters != null) {
			return putters;
		}

		synchronized (this) {
			if (putters == null) {
				List<ParameterPutter> putterList = (method != null) ? preparePutterList() : Collections.emptyList();
				putters = new ParameterPutters(Collections.unmodifiableList(putterList));
			}
		}
		return putters;
	}

	private List<ParameterPutter> preparePutterList() {
		Parameter[] params = method.getParameters();
		List<ParameterPutter> putterList = new ArrayList<>();
		for (int p = 0; p < params.length; p++) {
			Parameter param = params[p];
			Param[] paramAnnos = param.getDeclaredAnnotationsByType(Param.class);
			if (paramAnnos.length == 0) {
				continue;
			}

			Param paramAnno = paramAnnos[0];
			String paramName = paramAnno.value();

			int paramIndex = p;
			ParameterPutter putter = new ParameterPutter(paramName, paramIndex);
			putterList.add(putter);
		}
		return putterList;
	}

}
