package direct.interceptor.examples.log;

import direct.interceptor.examples.log.interceptor.Log;
import direct.interceptor.handler.Param;

public class Main {
    
    @Log("Next: == %d ==")
    public int next(@Param("number") int number) {
        return number + 1;
    }

    //@Log("Greet: == %s ==")
    public static String greet(
    		@Param("name") String name) {
        return "Hello " + name + "!!!";
    }
    
    public static void main(String[] args) {
        System.out.println(greet("SubWorld 1"));
        System.out.println(new Main().next(5));
        System.out.println(greet("SubWorld 2"));
        System.out.println(new Main().next(6));
    }
    
}
