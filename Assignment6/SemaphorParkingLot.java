import java.util.concurrent.Semaphore;
public class SemaphorParkingLot
{
    // SemaphoreParkingLot.java


        // Only 3 parking spots available
        private static final Semaphore parkingSpots = new Semaphore(3);

        public static void main(String[] args) {

            // Create 6 cars (threads)
            for (int i = 1; i <= 6; i++) {
                Thread car = new Thread(new Car("Car-" + i));
                car.start();
            }
        }

        // Car class simulating parking and leaving
        static class Car implements Runnable {
            private String name;

            public Car(String name) {
                this.name = name;
            }

            @Override
            public void run() {
                try {
                    System.out.println(name + " is trying to park...");

                    // Acquire a parking spot (waits if all are full)
                    parkingSpots.acquire();

                    System.out.println(name + " has parked.");
                    Thread.sleep(2000); // Simulate parking time

                    System.out.println(name + " is leaving the parking lot.");

                    // Release the parking spot
                    parkingSpots.release();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


