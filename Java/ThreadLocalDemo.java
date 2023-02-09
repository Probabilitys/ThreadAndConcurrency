/**
 * Demostrates how to use ThreadLocal to associate different user IDs with two
 * threads.
 */

public class ThreadLocalDemo {

    private static volatile ThreadLocal<String> userID =
        new ThreadLocal<String>();

    public static void main(String[] args) {
        Runnable r = new Runnable()
                    {
                        @Override
                        public void run() {
                            String name = Thread.currentThread().getName();
                            if (name.equals("A"))
                                userID.set("one");
                            else
                                userID.set("two");
                            System.out.println(name + " " + userID.get());
                        }
                    };

        Thread thdA = new Thread(r);
        thdA.setName("A");
        Thread thdB = new Thread(r);
        thdB.setName("B");
        thdA.start();
        thdB.start();
    }
}
