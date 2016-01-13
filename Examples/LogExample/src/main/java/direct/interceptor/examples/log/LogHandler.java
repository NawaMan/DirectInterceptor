package direct.interceptor.examples.log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import direct.interceptor.examples.log.interceptor.Log;
import direct.interceptor.handler.InterceptionHandler;
import direct.interceptor.handler.InterceptionHandlerFinally;
import direct.interceptor.handler.Parameters;

public class LogHandler implements InterceptionHandler, InterceptionHandlerFinally {

	@Override
	public <T> void handleFinally(long startTime, Annotation anno, Class<T> clazz, Object object, Method method,
			Parameters parameters, long finishTime, Object result, Throwable throwable) {
		Log log = (Log) anno;
		String format = log.value();
		String logMsg = startTime +" to " + finishTime + "(" + (finishTime - startTime) + ")" + ": " + String.format(format, parameters.asArray());
		if (throwable != null) {
			System.out.println(logMsg + " throws " + throwable);
		} else {
			System.out.println(logMsg + " = " + result);
		}
		
	}
}
