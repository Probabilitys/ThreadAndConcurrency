/**
 * Producer-consumer problem
 * Achieving Synchronization in Terms of Locks and Conditions
 */

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionDemo
{
    public static void main(String[] args)
    {
        SharedChar s = new SharedChar();
        new NewProducer(s).start();
        new NewConsumer(s).start();
    }
}

class SharedChar
{
    private char c;

    private volatile boolean available;

    private final Lock lock;

    private final Condition condition;

    SharedChar()
    {
        available = false;
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }

    Lock getLock()
    {
        return lock;
    }

    char getSharedChar()
    {
        lock.lock();
        try
        {
            while (!available)
            {
                try
                {
                    condition.await();
                }
                catch (InterruptedException ie)
                {
                    ie.printStackTrace();
                }

            }
            available = false;
            condition.signal();
        }
        finally
        {
            lock.unlock();
            return c;
        }
    }

    void setSharedChar(char c)
    {
        lock.lock();
        try
        {
            while (available)
            {
                try
                {
                    condition.await();
                }
                catch (InterruptedException ie)
                {
                    ie.printStackTrace();
                }

            }
            this.c = c;
            available = true;
            condition.signal();
        }
        finally
        {
            lock.unlock();
        }
    }
}  // end SharedChar class


// Producer class
class NewProducer extends Thread
{
    private final Lock l;
    private final SharedChar s;

    NewProducer(SharedChar s)
    {
        this.s = s;
        l = s.getLock();
    }

    @Override
    public void run()
    {
        for (char ch = 'A'; ch <= 'Z'; ch++)
        {
            l.lock();
            s.setSharedChar(ch);
            System.out.println(ch + " produced by producer.");
            l.unlock();
        }
    }
}

// Consumer class
class NewConsumer extends Thread
{
    private final Lock l;
    private final SharedChar s;
    NewConsumer(SharedChar s)
    {
        this.s = s;
        l = s.getLock();
    }
    @Override
    public void run()
    {
        char ch;
        do
        {
            l.lock();
            ch = s.getSharedChar();
            System.out.println(ch + " consumed by consumer.");
            l.unlock();
        }
        while (ch != 'Z');
    }
}

