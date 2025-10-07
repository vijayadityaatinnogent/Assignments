import java.util.*;

public class ProducerConsumer {
    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter buffer capacity: ");
        int capacity = sc.nextInt();

        PC pc = new PC();

        Thread t1 = new Thread(() -> {
            try {
                pc.producer(capacity);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                pc.consumer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        sc.close();
    }
}

class PC {
    LinkedList<Integer> buffer = new LinkedList<>();
    int value = 0;

    public void producer(int cap) throws InterruptedException {
        while (true) {
            synchronized (this) {
                // Wait if buffer is full
                while (buffer.size() == cap) {
                    System.out.println("Producer -> Buffer is full, waiting...\n");
                    wait();
                }

                // Produce until buffer is FULL
                while (buffer.size() < cap) {
                    buffer.add(value);
                    System.out.println("Produced: " + value + " | Buffer size: " +
                            buffer.size() + "/" + cap);
                    value++;
                }

                System.out.println("Producer -> Buffer FULL! Notifying consumer...\n");
                notify();
            }
        }
    }

    public void consumer() throws InterruptedException {
        while (true) {
            synchronized (this) {
                // Wait if buffer is empty
                while (buffer.isEmpty()) {
                    System.out.println("Consumer -> Buffer is empty, waiting...\n");
                    wait();
                }

                // Consume until buffer is EMPTY
                while (!buffer.isEmpty()) {
                    int current_value = buffer.removeFirst();
                    System.out.println("Consumed: " + current_value + " | Remaining: " +
                            buffer.size());
                }

                System.out.println("Consumer -> All consumed! Notifying producer...\n");
                notify();
            }
        }
    }
}