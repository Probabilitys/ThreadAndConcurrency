/**
 * Demonstrates Timer and TimerTask
 */

 import java.util.Timer;
 import java.util.TimerTask;

public class TimerDemo {
    public static void main(String[] args) {

        /**
         * demostrates One-Shot Execution
         */
        TimerTask task = new TimerTask()
                        {
                            @Override
                            public void run() {
                                System.out.println("a one-shot execution");
                                System.exit(0);
                            }
                        };
        Timer timer = new Timer();
        timer.schedule(task, 2000);  // Execute one-shot timer task after
                                          // 2-second delay.

        /**
         * demonstrates repeated execution at regular
         * intervals of a timer task.
         */
        TimerTask intervalTask = new TimerTask()
                            {
                                @Override
                                public void run() {
                                    System.out.println(System.currentTimeMillis());
                                }
                            };
        Timer intervalTimer = new Timer();
        timer.schedule(intervalTask, 0, 1000);
    }
}
