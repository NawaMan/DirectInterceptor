package di.example.log.app;

import org.junit.Test;

import di.example.log.core.intercept.Log;
import direct.interceptor.handler.Param;

public class Main {
    
    @Log("Next: == %d ==")
    private int next(@Param("number") int number) {
        return number + 1;
    }

    //@Log("Greet: == %s ==")
    public static String greet(
    		@Param("name") String name) {
        return "Hello " + name + "!!!";
    }
    
    @Test
    public void test() {
        System.out.println(greet("SubWorld 1"));
        System.out.println(new Main().next(5));
        System.out.println(greet("SubWorld 2"));
        System.out.println(new Main().next(6));
    }
    
    public static void main(String[] args) {
        System.out.println(greet("SubWorld 1"));
        System.out.println(new Main().next(5));
        System.out.println(greet("SubWorld 2"));
        System.out.println(new Main().next(6));
    }
    
}