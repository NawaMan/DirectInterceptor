package direct.interceptor.example;

import direct.interceptor.handler.Param;

public class SubMain extends Main {
    
    @Log("Next: == %d ==")
    public int next(@Param("number") int number) {
        return number + 1;
    }
    
    public static void main(String[] args) {
        System.out.println(greet("SubWorld"));
        System.out.println(new SubMain().next(5));
        System.out.println(new SubMain().next(6));
        System.out.println(new SubMain().next(7));
        System.out.println(new SubMain().next(8));
        System.out.println(new SubMain().next(9));
        /*
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("1:" + Arrays.toString(Log.class.getAnnotations()));
        System.out.println("2:" + Arrays.toString(Log.class.getDeclaredAnnotations()));
        System.out.println("3:" + Arrays.toString(Log.class.getAnnotatedInterfaces()));
        System.out.println(Log.class.getAnnotatedSuperclass());
        */
    }
    
}