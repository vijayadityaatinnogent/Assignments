// DeadlockSimulation.java
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class DeadlockSimulation {

    // Shared resources represented by locks
    private final ReentrantLock lock1 = new ReentrantLock();
    private final ReentrantLock lock2 = new ReentrantLock();

    // Method that causes deadlock
    public void causeDeadlock() {
        Thread t1 = new Thread(() -> {
            lock1.lock();  // Thread 1 locks Resource1
            System.out.println("Thread 1: Locked Resource 1");

            try {
                Thread.sleep(100); // Pause so Thread 2 can lock Resource2
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//            System.out.println("Thread 1: Trying to lock Resource 2");
//            lock2.lock();
            System.out.println("Thread 1: seeking if Resource 2 is open to acquire");

            try {
                if (lock2.tryLock(1000, TimeUnit.MICROSECONDS)){
                    System.out.println("Thread 1: Locked Resource 2");
                    lock2.unlock();
                    System.out.println("Thread 1: Releasing lock2 as Thread 2 done its work and released both locks");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Thread 1: Unlocked Resource 1 as it doesn't acquired Resource 2");
            lock1.unlock();
//            System.out.println("Thread 1: released Both Locks and finished work");
        });

        Thread t2 = new Thread(() -> {
            lock2.lock();  // Thread 2 locks Resource2
            System.out.println("Thread 2: Locked Resource 2");

            try {
                Thread.sleep(100); // Pause so Thread 1 can lock Resource1
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Thread 2: Trying to lock Resource 1");
            lock1.lock();  // Waiting for Resource1
            System.out.println("Thread 2: Locked Resource 1");

            lock1.unlock();
            lock2.unlock();
            System.out.println("Thread 2: Unlocked Both resources");
        });

        t1.start();
        t2.start();
    }

    // Fixed version using consistent lock order (prevention)
//    public void preventDeadlock() {
//        Thread t1 = new Thread(() -> lockResourcesInOrder(lock1, lock2, "Thread 1"));
//        Thread t2 = new Thread(() -> lockResourcesInOrder(lock1, lock2, "Thread 2"));
//
//        t1.start();
//        t2.start();
//    }

    // Always lock in same order to avoid deadlock
    private void lockResourcesInOrder(ReentrantLock first, ReentrantLock second, String name) {
        try {
            first.lock();
            System.out.println(name + ": Locked first resource");
            Thread.sleep(100);
            second.lock();
            System.out.println(name + ": Locked second resource");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            second.unlock();
            first.unlock();
        }
    }

    public static void main(String[] args) {
        DeadlockSimulation obj = new DeadlockSimulation();

        System.out.println("\n--- Simulating Deadlock ---");
        obj.causeDeadlock();

        // Wait for the deadlock to appear
        try { Thread.sleep(1000); } catch (InterruptedException e) {}

//        System.out.println("\n--- Preventing Deadlock (Fixed) ---");
//        obj.preventDeadlock();
    }
}
