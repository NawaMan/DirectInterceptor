package direct.interceptor.agent;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class $Util {
	
	private static $Util instance = new $Util();
	
	
	public static final $Util getInstance() {
		return instance;
	}

	public InputStream fromResource(Class<?> seedClass, String name) {
		return seedClass.getResourceAsStream(name);
	}
	
	public List<String> linesFromResource(Class<?> seedClass, String name) {
		return linesFrom(seedClass.getResourceAsStream(name));
	}
	
	public List<String> linesFrom(InputStream inStream) {
		List<String> lines = new ArrayList<>();
		
		InputStreamReader reader = new InputStreamReader(inStream);
		BufferedReader buffer = new BufferedReader(reader);
		
		String line = null;
		while(true) {
			try {
				line = buffer.readLine();
			} catch (IOException e) {
				// This block is intentionally left blank.
			}
			if (line == null) {
				break;
			}
			
			lines.add(line);
			line = null;
		}
		
		closeCarefree(reader);
		closeCarefree(buffer);
		
		return lines;
	}
	
	public void closeCarefree(Closeable closeable) {
		if (closeable == null) {
			return;
		}
		
		try {
			closeable.close();
		} catch(IOException exception) {
			// This block is intentionally left blank.
		}
	}

	public String matchPackageNameStartWith(String packageName) {
		return "^" + packageName.replaceAll("\\.", "\\\\.") + "\\..*";
	}
	
}
