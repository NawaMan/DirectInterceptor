package di.example.log.app;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import di.example.log.core.intercept.Log;
import direct.interceptor.handler.InterceptHandler;
import direct.interceptor.handler.InterceptHandlerFinally;
import direct.interceptor.handler.Parameters;

public class LogHandler implements InterceptHandler, InterceptHandlerFinally {

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