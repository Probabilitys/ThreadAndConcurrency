import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Using a Countdown Latch to Trigger a Coordinated Start
 */

public class CountDownLatchDemo {

    final static int NTHREADS = 3;

    public static void main(String[] args) {
        final CountDownLatch startSignal = new CountDownLatch(1);
        final CountDownLatch doneSignal = new CountDownLatch(NTHREADS);
        Runnable r = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    report("enter run()");
                    startSignal.await();  // wait until told to...
                    report("doing work");  // proceed
                    Thread.sleep((int) (Math.random()*1000));
                    doneSignal.countDown();  // reduce count on which main thread is waiting
                }
                catch (InterruptedException ie)
                {
                    System.err.println(ie);
                }
            }  // end run method

            void report(String s)
            {
                System.out.println(System.currentTimeMillis() +
                    "; " + Thread.currentThread() +
                    "; " + s);
            }
        };  // end runnable definition

        ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);
        for (int i=0; i<NTHREADS; i++)
            executor.execute(r);

        try
        {
            System.out.println("main thread doing something");
            Thread.sleep(1000);
            startSignal.countDown();  // let all thread proceed
            System.out.println("main thread doing something else");
            doneSignal.await(); // wait for all threads to finish
            executor.shutdownNow();
        }
        catch (InterruptedException ie)
        {
            System.err.println(ie);
        }

    }
}
