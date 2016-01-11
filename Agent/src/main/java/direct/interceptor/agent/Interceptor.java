package direct.interceptor.agent;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import direct.interceptor.handler.InterceptionHandler;
import direct.interceptor.handler.InterceptionHandlerBefore;
import direct.interceptor.handler.InterceptionHandlerFinally;
import direct.interceptor.handler.InterceptionHandlerResult;
import direct.interceptor.handler.InterceptionHandlerThrowable;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;

@SuppressWarnings("rawtypes")
public class Interceptor {

	class In<I> {
		final Annotation anno;
		final InterceptionHandler handler;
		final ParametersImpl parameters;

		public In(Annotation anno, InterceptionHandler handler, ParametersImpl parameters) {
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
			if (!method.getDeclaringClass().getCanonicalName().startsWith("java.")
					&& !method.getDeclaringClass().getCanonicalName().startsWith("sun.")) {
				Annotation[] annos = new Annotation[0];

				try {
					annos = method.getDeclaredAnnotations();
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					for (int i = 0; i < annos.length; i++) {
						Annotation anno = annos[i];
						boolean isSo = false;
						try {
							for (Annotation a : anno.annotationType().getDeclaredAnnotations()) {
								if (a.annotationType().getSimpleName().equals("InterceptorAnnotation")) {
									isSo = true;
									break;
								}
							}
						} catch (Exception e) {
						}

						if (isSo) {
							@SuppressWarnings("unchecked")
							InterceptionHandler handler = ((Class<InterceptionHandler>) (anno.getClass()
									.getDeclaredMethod("handler", new Class[0]).invoke(anno))).newInstance();
							ParameterPuttersProvider provider = ParameterPuttersProvider.of(method);
							ParametersImpl parameters = new ParametersImpl(allArguments, provider);

							In in = null;
							if (handler instanceof InterceptionHandlerBefore) {
								if (beforeList == null) {
									beforeList = new ArrayList<>();
								}
								if (in == null) {
									in = new In(anno, handler, parameters);
								}
								beforeList.add(new In(anno, handler, parameters));
							}
							if (handler instanceof InterceptionHandlerResult) {
								if (resultList == null) {
									resultList = new ArrayList<>();
								}
								if (in == null) {
									in = new In(anno, handler, parameters);
								}
								resultList.add(in);
							}
							if (handler instanceof InterceptionHandlerThrowable) {
								if (throwList == null) {
									throwList = new ArrayList<>();
								}
								if (in == null) {
									in = new In(anno, handler, parameters);
								}
								throwList.add(in);
							}
							if (handler instanceof InterceptionHandlerFinally) {
								if (finallyList == null) {
									finallyList = new ArrayList<>();
								}
								if (in == null) {
									in = new In(anno, handler, parameters);
								}
								finallyList.add(in);
							}
						}
					}
				} catch (Exception e) {
				}
			}

			if (beforeList != null) {
				for (In in : beforeList) {
					if (in.handler instanceof InterceptionHandlerBefore) {
						InterceptionHandlerBefore handler = ((InterceptionHandlerBefore) in.handler);
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
					if (in.handler instanceof InterceptionHandlerResult) {
						InterceptionHandlerResult handler = ((InterceptionHandlerResult) in.handler);
						handler.handleResult(startTime, in.anno, clazz, object, method, in.parameters, finishTime,
								result);
					}
				}
			}
			if ((throwable == null) && (throwList != null)) {
				for (In in : throwList) {
					if (in.handler instanceof InterceptionHandlerThrowable) {
						InterceptionHandlerThrowable handler = ((InterceptionHandlerThrowable) in.handler);
						handler.handleThrowable(startTime, in.anno, clazz, object, method, in.parameters, finishTime,
								throwable);
					}
				}
			}
			if (finallyList != null) {
				for (In in : finallyList) {
					if (in.handler instanceof InterceptionHandlerFinally) {
						InterceptionHandlerFinally handler = ((InterceptionHandlerFinally) in.handler);
						handler.handleFinally(startTime, in.anno, clazz, object, method, in.parameters, finishTime,
								result, throwable);
					}
				}
			}
		}
		return result;
	}
}