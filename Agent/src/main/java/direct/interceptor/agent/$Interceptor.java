package direct.interceptor.agent;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import direct.interceptor.handler.InterceptHandler;
import direct.interceptor.handler.InterceptHandlerBefore;
import direct.interceptor.handler.InterceptHandlerFinally;
import direct.interceptor.handler.InterceptHandlerResult;
import direct.interceptor.handler.InterceptHandlerThrowable;
import direct.interceptor.handler.InterceptManager;
import direct.interceptor.handler.manager.RootInterceptManager;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;

@SuppressWarnings("rawtypes")
public class $Interceptor {
	
	private final InterceptManager manager;
	
	$Interceptor(InterceptManager manager) {
		this.manager = manager;
	}
	

	class In<I> {
		final Annotation anno;
		final InterceptHandler handler;
		final ParametersImpl parameters;

		public In(Annotation anno, InterceptHandler handler, ParametersImpl parameters) {
			super();
			this.anno = anno;
			this.handler = handler;
			this.parameters = parameters;
		}
	}

	@RuntimeType
	public Object intercept(@SuperCall Callable<?> callable, @This(optional = true) Object object,
			@AllArguments Object[] allArguments, @Origin Method method, @Origin Class<?> clazz) throws Throwable {
		long startTime = System.currentTimeMillis();
		long finishTime = startTime;

		Object result = null;
		Throwable throwable = null;

		List<In> beforeList = null;
		List<In> resultList = null;
		List<In> throwList = null;
		List<In> finallyList = null;

		try {
			Class<?> dClss = method.getDeclaringClass();
			String className = dClss.getCanonicalName();
			System.out.println("method: " + className + " -- " + method);

			Annotation[] annos = new Annotation[0];

			try {
				annos = method.getDeclaredAnnotations();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				// Must cache this 
				for (int i = 0; i < annos.length; i++) {
					Annotation anno = annos[i];

					// OK, I admit, this is really not a good way ... but WTH.
					String annoFullName = anno.toString().replaceAll("@([^\\(]+)\\(.*$", "$1");
					Class<?> annoClss = Class.forName(annoFullName);
					InterceptHandler handler = null;
					
					if (manager != null) {
						handler = manager.getHandler(anno, annoClss, clazz, method);
					} else {
						handler = RootInterceptManager.getHandler(anno, annoClss, clazz, method);
					}
					
					if (handler == null) {
						String annoName = annoClss.getSimpleName();
						String hdlrClssName =  System.getenv().get(annoName + "Handler");
						Class<?> hdlrClass = null;
						if(hdlrClssName != null) {
							hdlrClass = Class.forName(hdlrClssName);
						} else {
							Method mthd = anno.getClass().getDeclaredMethod("handler", new Class[0]);
							hdlrClass = (Class<?>)mthd.invoke(anno);
						}
						handler = (InterceptHandler)hdlrClass.newInstance();
					}
					ParameterPuttersProvider provider = ParameterPuttersProvider.of(method);
					ParametersImpl parameters = new ParametersImpl(allArguments, provider);

					In in = null;
					if (handler instanceof InterceptHandlerBefore) {
						if (beforeList == null) {
							beforeList = new ArrayList<>();
						}
						if (in == null) {
							in = new In(anno, handler, parameters);
						}
						beforeList.add(new In(anno, handler, parameters));
					}
					if (handler instanceof InterceptHandlerResult) {
						if (resultList == null) {
							resultList = new ArrayList<>();
						}
						if (in == null) {
							in = new In(anno, handler, parameters);
						}
						resultList.add(in);
					}
					if (handler instanceof InterceptHandlerThrowable) {
						if (throwList == null) {
							throwList = new ArrayList<>();
						}
						if (in == null) {
							in = new In(anno, handler, parameters);
						}
						throwList.add(in);
					}
					if (handler instanceof InterceptHandlerFinally) {
						if (finallyList == null) {
							finallyList = new ArrayList<>();
						}
						if (in == null) {
							in = new In(anno, handler, parameters);
						}
						finallyList.add(in);
					}
				}
			} catch (Exception e) {
			}

			if (beforeList != null) {
				for (In in : beforeList) {
					if (in.handler instanceof InterceptHandlerBefore) {
						InterceptHandlerBefore handler = ((InterceptHandlerBefore) in.handler);
						handler.handleBefore(startTime, in.anno, clazz, object, method, in.parameters);
					}
				}
			}

			result = callable.call();

		} catch (Throwable t) {
			throwable = t;
			throw throwable;
		} finally {
			finishTime = System.currentTimeMillis();

			if ((throwable != null) && (resultList != null)) {
				for (In in : resultList) {
					if (in.handler instanceof InterceptHandlerResult) {
						InterceptHandlerResult handler = ((InterceptHandlerResult) in.handler);
						handler.handleResult(startTime, in.anno, clazz, object, method, in.parameters, finishTime,
								result);
					}
				}
			}
			if ((throwable == null) && (throwList != null)) {
				for (In in : throwList) {
					if (in.handler instanceof InterceptHandlerThrowable) {
						InterceptHandlerThrowable handler = ((InterceptHandlerThrowable) in.handler);
						handler.handleThrowable(startTime, in.anno, clazz, object, method, in.parameters, finishTime,
								throwable);
					}
				}
			}
			if (finallyList != null) {
				for (In in : finallyList) {
					if (in.handler instanceof InterceptHandlerFinally) {
						InterceptHandlerFinally handler = ((InterceptHandlerFinally) in.handler);
						handler.handleFinally(startTime, in.anno, clazz, object, method, in.parameters, finishTime,
								result, throwable);
					}
				}
			}
		}
		return result;
	}
}