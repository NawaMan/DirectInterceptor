package direct.interceptor.example;

import direct.interceptor.handler.Param;

public class Main {
    
    @Log("Greet: == %s ==")
    public static String greet(
    		@Param("name") String name) {
        return "Hello " + name + "!!!";
    }
    
}
