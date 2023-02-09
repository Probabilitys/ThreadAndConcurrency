/**
 * Using a Cyclic Barrier to Decompose a Task into Subtasks
 */

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {
    public static void main(String[] args) {
        float[][] matrix = new float[3][3];
        int counter = 0;
        for (int row = 0; row < matrix.length; row++)
            for (int col = 0; col < matrix[0].length; col++)
                matrix[row][col] = counter++;
        dump(matrix);
        System.out.println();
        Solver solver = new Solver(matrix);
        System.out.println();
        dump(matrix);
    }  // end main method

    /**
     * display the matrix
     * @param matrix
     */
    static void dump(float[][] matrix)
    {
        for (int row = 0; row < matrix.length; row++)
        {
            for (int col = 0; col < matrix[0].length; col++)
                System.out.print(matrix[row][col] + " ");
            System.out.println();
        }
    }
}

class Solver
{
    int N;
    float[][] data;
    CyclicBarrier barrier;

    class Worker implements Runnable
    {
        int myRow;
        boolean done = false;

        Worker(int row)
        {
            myRow = row;
        }

        boolean done()
        {
            return done;
        }

        void proceedRow(int myRow)
        {
            System.out.println("Proceeding row: " + myRow);
            for (int i = 0; i < N; i++)
                data[myRow][i] *= 10;
            done = true;
        }

        @Override
        public void run()
        {
            while (!done())
            {
                proceedRow(myRow);
                try
                {
                    barrier.await();
                }
                catch (InterruptedException ie)
                {
                    return;
                }
                catch (BrokenBarrierException bbe)
                {
                    return;
                }
            }
        }
    }  // end class Worker

    // Solver Constructor
    public Solver(float[][] matrix)
    {
        data = matrix;
        N = matrix.length;
        barrier = new CyclicBarrier(
            N,
            new Runnable()
            {
                @Override
                public void run()
                {
                    mergeRows();
                }
            }
        );
        for (int i = 0; i < N; i++)
            new Thread(new Worker(i)).start();
        waitUntilDone();
    }  // end constructor

    void mergeRows()
    {
        System.out.println("merging");
        synchronized("abc")
        {
            "abc".notify();
        }
    }

    void waitUntilDone()
    {
        synchronized ("abc")
        {
            try
            {
                System.out.println("main thread waiting");
                "abc".wait();
                System.out.println("main thread notified");
            }
            catch (InterruptedException ie)
            {
                System.out.println("main thread interrupted");
            }
        }

    }

}
