public class CreateThread {
    public static void main(String[] args) {

        boolean isDaemon = args.length != 0;

        // create a runnable obj
        Runnable r = () -> {
            Thread thd = Thread.currentThread();
            System.out.printf("%s is %salive and in %s state, priority:%s%n",
                thd.getName(),  // get thread name
                thd.isAlive() ? "" : "not ",  // get alive status
                thd.getState(),  // get thread state
                thd.getPriority());  // get thread priority
        };

        // create a thread and specify the name
        Thread t1 = new Thread(r, "thread 1");
        if (isDaemon)
            t1.setDaemon(true);  // set daemon
        t1.setName("first thread");  // set thread name
        t1.setPriority(Thread.MIN_PRIORITY);  // set thread priority
        t1.start();  // start the thread

        /*
        //-------------------------------interrupt---------------------------------------//
        Thread t2 = new Thread(() -> {
            String name = Thread.currentThread().getName();
            int count = 0;
            while (!Thread.interrupted())
                System.out.println(name + ": " + count++);
        });
        t2.start();
        while (true)
        {
            double n = Math.random();
            System.out.println(n);
            if (n >= 0.499999 && n <= 0.500001)
                break;
        }
        t2.interrupt();
        */

        //-------------------------------sleep---------------------------------------//
        Thread t3 = new Thread(() -> {
            String name = Thread.currentThread().getName();
            int count = 0;
            while (!Thread.interrupted())
                System.out.println(name + ": " + count++);
        });
        t3.start();
        try
        {
            Thread.sleep(100);
        }
        catch (InterruptedException ie)
        {
        }
        t3.interrupt();

    }
}