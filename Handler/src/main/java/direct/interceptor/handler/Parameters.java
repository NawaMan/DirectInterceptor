package direct.interceptor.handler;


import java.util.Map;

public interface Parameters {

	public Object[] asArray();

	public Map<String, Object> asMap();

	public String format(String format);
}
