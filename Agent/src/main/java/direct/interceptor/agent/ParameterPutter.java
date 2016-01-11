package direct.interceptor.agent;

import java.util.Map;

class ParameterPutter {
	
	private final String name;
	private final int index;
	
	public ParameterPutter(
			String name,
			int index) {
		this.name = name;
		this.index = index;
	}
	
	public void put(Map<String, Object> map, Object[] args) {
		Object value = args[this.index];
		map.put(this.name, value);
	}
	
	public String toString() {
		return "ParamPutter:" + name + "->" + index;
	}
	
}
