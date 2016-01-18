package di.example.log.app;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.sun.tools.attach.VirtualMachine;

import di.example.log.core.intercept.Log;
import direct.interceptor.handler.InterceptManager;
import direct.interceptor.handler.Param;
import direct.interceptor.handler.manager.BasicInterceptManager;
import direct.interceptor.handler.manager.RootInterceptManager;

/**
 * @author dssb
 *
 */
public class DynamicMain {
	
	private static final String jarFilePath = "/home/dssb/git/DirectInterceptor/Agent/target/Agent-0.0.1-SNAPSHOT-jar-with-dependencies.jar";
	

    public static void loadAgent() {
        System.out.println("dynamically loading javaagent");
        String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
        int p = nameOfRunningVM.indexOf('@');
        String pid = nameOfRunningVM.substring(0, p);

        try {
            VirtualMachine vm = VirtualMachine.attach(pid);
            vm.loadAgent(jarFilePath, "");
            vm.detach();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


	@Log("Next: == %d ==")
	private int next(@Param("number") int number) {
		return number + 1;
	}

	// @Log("Greet: == %s ==")
	public static String greet(@Param("name") String name) {
		return "Hello " + name + "!!!";
	}
	
	public static void main(String ... args) {
		List<String> list = Arrays.asList(args);
		if (list.contains("--with-agent")) {
			loadAgent();
		}
		
		InterceptManager manager = new BasicInterceptManager(Collections.singletonMap(Log.class, ()-> new LogHandler()));
		RootInterceptManager.addManager(manager);
		Main.main(args);
/*
		System.out.println(greet("SubWorld 1"));
		System.out.println(new DynamicMain().next(5));
		System.out.println(greet("SubWorld 2"));
		System.out.println(new DynamicMain().next(6));*/
	}
	
	
}
