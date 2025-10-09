import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockDemo {

    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private static final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    private static int sharedData = 0; // shared resource

    public static void main(String[] args) {
        // Creating reader threads
        Thread reader1 = new Thread(ReadWriteLockDemo::readData, "Reader-1");
        Thread reader2 = new Thread(ReadWriteLockDemo::readData, "Reader-2");

        // Creating writer threads
        Thread writer1 = new Thread(ReadWriteLockDemo::writeData, "Writer-1");
        Thread writer2 = new Thread(ReadWriteLockDemo::writeData, "Writer-2");

        // Start threads
        reader1.start();
        reader2.start();
        writer1.start();
        writer2.start();
    }

    private static void readData() {
        for (int i = 0; i < 3; i++) {
            readLock.lock();  // acquire read lock
            try {
                System.out.println(Thread.currentThread().getName() + " is reading data: " + sharedData);
                Thread.sleep(200); // simulate read time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                readLock.unlock(); // release read lock
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void writeData() {
        for (int i = 0; i < 2; i++) {
            writeLock.lock();  // acquire write lock
            try {
                sharedData++;
                System.out.println(Thread.currentThread().getName() + " is writing data: " + sharedData);
                Thread.sleep(300); // simulate write time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                writeLock.unlock(); // release write lock
            }

            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}