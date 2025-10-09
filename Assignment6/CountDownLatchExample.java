import java.util.concurrent.CountDownLatch;
public class CountDownLatchExample
    {
    // CountDownLatchExample.java
        public static void main(String[] args) {
            // Create CountDownLatch for 3 worker threads
            CountDownLatch latch = new CountDownLatch(3);

            System.out.println("Main Thread: Starting workers...");

            // Create and start 3 worker threads
            new Thread(new Worker("Worker-1", 2000, latch)).start();
            new Thread(new Worker("Worker-2", 3000, latch)).start();
            new Thread(new Worker("Worker-3", 1500, latch)).start();

            try {
                // Main thread waits until latch count reaches 0
                latch.await();
                System.out.println("Main Thread: All workers finished. Proceeding...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Worker class implements Runnable
    class Worker implements Runnable {
        private String name;
        private int workTime;
        private CountDownLatch latch;

        public Worker(String name, int workTime, CountDownLatch latch) {
            this.name = name;
            this.workTime = workTime;
            this.latch = latch;
        }

        @Override
        public void run() {
            System.out.println(name + " started its task.");
            try {
                Thread.sleep(workTime);  // Simulate some work
            } catch (InterruptedException e) {
//                e.printStackTrace();
            }
            System.out.println(name + " finished work.");
            latch.countDown();  // Decrease latch count by 1
        }
    }


