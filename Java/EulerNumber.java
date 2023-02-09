/**
 * Calculating Euler’s Number e
 */

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EulerNumber {
    final static int LASTITER = 17;
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Callable<BigDecimal> callable;
        callable = new Callable<BigDecimal>()
                {
                    @Override
                    public BigDecimal call()
                    {
                        MathContext mc =
                            new MathContext(100, RoundingMode.HALF_UP);
                        BigDecimal result = BigDecimal.ZERO;
                        for (int i = 0; i <= LASTITER; i++)
                        {
                            BigDecimal factorial = factorial(new BigDecimal(i));
                            BigDecimal res = BigDecimal.ONE.divide(factorial, mc);
                            result = result.add(res);
                        }
                        return result;
                    }  // end call function

                    // calculates factorial
                    public BigDecimal factorial(BigDecimal n)
                    {
                        if (n.equals(BigDecimal.ZERO))
                            return BigDecimal.ONE;
                        else
                            return n.multiply(factorial(n.
                                        subtract(BigDecimal.ONE)));
                    }  // end factorial function
                };  // end callable definition

        Future<BigDecimal> taskFuture = executor.submit(callable);
        try
        {
            // avoid this loop in real application
            while (!taskFuture.isDone())
                System.out.println("waiting");
            System.out.println(taskFuture.get());
        }
        catch (ExecutionException ee)
        {
            System.err.println("task threw an exception");
            System.err.println(ee);
        }
        catch (InterruptedException ie)
        {
            System.err.println("interrupted while waiting");
        }  // end try catch

        executor.shutdownNow();
    }
}
