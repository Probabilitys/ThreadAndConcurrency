/**
 * Using a Phaser to Control a One-Shot Action Serving a Variable Number
 * of Parties
 */

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class PhaserDemo
{
    public static void main(String[] args)
    {
        List<Runnable> tasks = new ArrayList<>();
        tasks.add(() -> System.out.printf(
            "%s running at %d %n",
            Thread.currentThread().getName(),
            System.currentTimeMillis()
            )
        );
        tasks.add(() -> System.out.printf(
                        "%s running at %d%n",
                        Thread.currentThread().getName(),
                        System.currentTimeMillis()
                )
        );
        runTasks(tasks);
    }  // end main method

    static void runTasks(List<Runnable> tasks)
    {
        final Phaser phaser = new Phaser(1);  // "1" (register itself)
        // create and start threads
        for (final Runnable task: tasks)
        {
            phaser.register();
            Runnable r = () ->
            {
                try
                {
                    Thread.sleep(50 + (int) (Math.random() * 1000));
                }
                catch (InterruptedException ie)
                {
                    System.out.println("interrupted thread");
                }
                phaser.arriveAndAwaitAdvance();  // await the creation of all tasks
                task.run();
            };
            Executors.newSingleThreadExecutor().execute(r);

        }  // end for loop

        // allow threads to start and deregister self
        phaser.arriveAndDeregister();
    }  // end runTasks method
}
