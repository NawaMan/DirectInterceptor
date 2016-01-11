package direct.interceptor.agent;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import direct.interceptor.handler.Parameters;

public final class ParametersImpl implements Parameters {

	private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

	private final Object[] parameters;
	
	private final ParameterPuttersProvider putterProvider;
	
	private volatile Map<String, Object> parameterMap = null;

	public ParametersImpl(
			Object[] parameters) {
		this(parameters, null);
	}
	public ParametersImpl(
			Object[] parameters,
			ParameterPuttersProvider putterProvider) {
		this.parameters = (parameters != null) ? parameters.clone() : EMPTY_OBJECT_ARRAY;
		this.putterProvider = putterProvider;
	}
	public ParametersImpl(
			List<Object> parameters,
			ParameterPuttersProvider putterProvider) {
		this(parameters.toArray(), putterProvider);
	}

	public Object[] asArray() {
		if (this.parameters == EMPTY_OBJECT_ARRAY) {
			return EMPTY_OBJECT_ARRAY;
		}
		
		return this.parameters.clone();
	}

	public Map<String, Object> asMap() {
		if (parameters.length == 0) {
			return Collections.emptyMap();
		}
		
		if (parameterMap != null) {
			return parameterMap;
		}
		
		synchronized(this) {
			if (this.parameterMap != null) {
				return this.parameterMap;
			}
			
			ParameterPutters putters = putterProvider.getPutters();
			Map<String, Object> paramMap =  putters.map(parameters);
			this.parameterMap = Collections.unmodifiableMap(new HashMap<>(paramMap));
			
			return parameterMap;
		}
	}

	public String format(String format) {
		String formatted = String.format(format, this.parameters);
		return formatted;
	}

}
