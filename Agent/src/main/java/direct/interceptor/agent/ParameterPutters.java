package direct.interceptor.agent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ParameterPutters {
	
	private final List<ParameterPutter> putters;
	
	public ParameterPutters(List<ParameterPutter> putters) {
		this.putters = (putters != null)
				? Collections.unmodifiableList(new ArrayList<>(putters))
				: Collections.emptyList();
	}
	
	public Map<String, Object> map(
			Object[] parameterArray) {
		Map<String, Object> parameterMap = new HashMap<>();
		for (ParameterPutter putter : putters) {
			putter.put(parameterMap, parameterArray);
		}
		return parameterMap;
	}
	
}
