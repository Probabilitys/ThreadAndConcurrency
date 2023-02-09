/* Accomplish the conditional communication problem via wait() and notify(). */

/*
The writeable instance field tracks two conditions: the producer waiting on the
consumer to consume a data item and the consumer waiting on the producer to produce
a new data item.
*/

public class ProducerConsumerProblem {
    public static void main(String[] args)
    {
        SharedChar s = new SharedChar();
        new NewProducer(s).start();
        new NewConsumer(s).start();
    }
}

class Shared {
    private char c;
    private volatile boolean writeable = true;

    synchronized void setSharedChar(char c) {
        while(!writeable) {
            try {
                wait();
            }
            catch (InterruptedException ie) { }
        }
        this.c = c;
        writeable = false;
        notify();
    }

    synchronized char getSharedChar() {
        while(writeable) {
            try {
                wait();
            }
            catch (InterruptedException ie) { }
        }
        writeable = true;
        notify();
        return c;
    }
}

/* The producer thread produces data to be consumed by Consumer*/
class Producer extends Thread {
    private final SharedChar s;

    Producer(SharedChar s) {
        this.s = s;
    }

    @Override
    public void run() {
        for(char ch = 'A'; ch <= 'Z'; ch++) {
            synchronized (s) {
                s.setSharedChar(ch);
                System.out.println(ch + " produced by Producer");
            }

        }
    }
}

/* Consumer consumes the produced data by Producer */
class Consumer extends Thread {
    private final SharedChar s;

    Consumer(SharedChar s) {
        this.s = s;
    }

    @Override
    public void run() {
        char ch;
        do {
            synchronized (s) {
                ch = s.getSharedChar();
                System.out.println(ch + " consumed by Consumer");
            }

        }
        while (ch != 'Z');
    }
}

