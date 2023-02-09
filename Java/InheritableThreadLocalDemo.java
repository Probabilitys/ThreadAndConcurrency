/**
 * Demostrates how to use InheritableThreadLocal to pass a parent thread’s
 * Integer object to a child thread.
 */

public class InheritableThreadLocalDemo {
    private static final InheritableThreadLocal<Integer> intVal =
        new InheritableThreadLocal<>();

    public static void main(String[] args) {
        Runnable rP = () ->
                    {
                        intVal.set(Integer.valueOf(10));

                        Runnable rC = () ->
                                {
                                    Thread thd = Thread.currentThread();
                                    String name = thd.getName();
                                    System.out.printf("%s %d%n", name, intVal.get());
                                };
                        Thread thdChild = new Thread(rC);
                        thdChild.setName("child");
                        thdChild.start();
                    };
        new Thread(rP).start();
    }
}
