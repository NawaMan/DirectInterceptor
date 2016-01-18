package di.example.log.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import dssb.util.process.AsynComplete;
import dssb.util.process.Process;
import dssb.util.process.ProcessOutput;

public class TestLog {

	@Test
	public void testWithAgent() throws IOException, InterruptedException {
		String userHome = System.getProperty("user.home");
		
		final String[] command = {
				"/usr/lib/jvm/java-8-oracle/bin/java",
				"-Dfile.encoding=UTF-8",
				"-classpath",
				userHome + "/git/DirectInterceptor/Examples/Log/LogExampleApplication/target/classes:"+
				userHome + "/.m2/repository/net/bytebuddy/byte-buddy/0.7.7/byte-buddy-0.7.7.jar:"+
				userHome + "/git/DirectInterceptor/Handler/target/classes:"+
				userHome + "/git/DirectInterceptor/Examples/Log/LogExampleCore/target/classes:"+
				userHome + "/.m2/repository/junit/junit/4.12/junit-4.12.jar:"+
				userHome + "/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar:"+
				userHome + "/git/DirectInterceptor/Agent/target/classes:"+
				"/usr/lib/jvm/java-8-oracle/lib/tools.jar",
				"di.example.log.app.DynamicMain",
				"--with-agent"
		};
		
		String workingDir = "/home/dssb";
		
		Process process = new Process(workingDir, command);
		process.start();
		
		AtomicReference<ProcessOutput> output = new AtomicReference<ProcessOutput>(null);
		process.whenComplete(new AsynComplete<ProcessOutput, Throwable>() {
			@Override
			public void onComplete(ProcessOutput result, Throwable throwable) {
				if (result != null) {
					output.set(result);
				} else {
					throwable.printStackTrace();
				}
			}
		});
		
		Thread.sleep(1000);
		
		String expectOut =
				"dynamically loading javaagent\n" + 
				"Hello SubWorld 1!!!\n" + 
				"method: di.example.log.app.Main -- private int di.example.log.app.Main.next(int)\n" + 
				"Next: == 5 == = 6\n" + 
				"6\n" + 
				"Hello SubWorld 2!!!\n" + 
				"method: di.example.log.app.Main -- private int di.example.log.app.Main.next(int)\n" + 
				"Next: == 6 == = 7\n" + 
				"7\n" + 
				"";
		
		assertEquals(expectOut, output.get().getOutput());
		assertEquals("", output.get().getError());
		assertEquals(0, output.get().getExitCode());
	}

	@Test
	public void testNoAgent() throws IOException, InterruptedException {
		String userHome = System.getProperty("user.home");
		
		final String[] command = {
				"/usr/lib/jvm/java-8-oracle/bin/java",
				"-Dfile.encoding=UTF-8",
				"-classpath",
				userHome + "/git/DirectInterceptor/Examples/Log/LogExampleApplication/target/classes:"+
				userHome + "/.m2/repository/net/bytebuddy/byte-buddy/0.7.7/byte-buddy-0.7.7.jar:"+
				userHome + "/git/DirectInterceptor/Handler/target/classes:"+
				userHome + "/git/DirectInterceptor/Examples/Log/LogExampleCore/target/classes:"+
				userHome + "/.m2/repository/junit/junit/4.12/junit-4.12.jar:"+
				userHome + "/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar:"+
				userHome + "/git/DirectInterceptor/Agent/target/classes:"+
				"/usr/lib/jvm/java-8-oracle/lib/tools.jar",
				"di.example.log.app.DynamicMain"
		};
		
		String workingDir = "/home/dssb";
		
		Process process = new Process(workingDir, command);
		process.start();
		
		AtomicReference<ProcessOutput> output = new AtomicReference<ProcessOutput>(null);
		process.whenComplete(new AsynComplete<ProcessOutput, Throwable>() {
			@Override
			public void onComplete(ProcessOutput result, Throwable throwable) {
				if (result != null) {
					output.set(result);
				} else {
					throwable.printStackTrace();
				}
			}
		});
		
		Thread.sleep(1000);
		
		String expectOut =
				"Hello SubWorld 1!!!\n" + 
				"6\n" + 
				"Hello SubWorld 2!!!\n" + 
				"7\n" + 
				"";
		
		assertEquals(expectOut, output.get().getOutput());
		assertEquals("", output.get().getError());
		assertEquals(0, output.get().getExitCode());
	}

}
