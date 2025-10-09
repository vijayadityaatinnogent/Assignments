import java.io.*;
import java.util.*;
import java.util.concurrent.*;
public class ExecutorServiceCallableExample

{
    // ExecutorServiceCallableExample.java



        public static void main(String[] args) {
            // Directory path (you can change this path to test)
            File folder = new File("D:\\Innogent\\Assignment4"); // folder containing text files
            if (!folder.exists() || !folder.isDirectory()) {
                System.out.println("Directory not found!");
                return;
            }

            // Create a fixed thread pool with 3 threads
            ExecutorService executor = Executors.newFixedThreadPool(3);

            // List to hold Future objects
            List<Future<Integer>> results = new ArrayList<>();

            // Submit a Callable task for each text file
            for (File file : folder.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    LineCounter task = new LineCounter(file);
                    Future<Integer> future = executor.submit(task);
                    results.add(future);
                }
            }

            int totalLines = 0;
            // Collect results from all tasks
            for (Future<Integer> future : results) {
                try {
                    totalLines += future.get(); // wait for result
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("Error reading file: " + e.getMessage());
                }
            }

            // Display total count
            System.out.println("Total number of lines in all text files: " + totalLines);

            // Shut down the thread pool
            executor.shutdown();
        }
    }

    // Callable class to count lines in a file
    class LineCounter implements Callable<Integer> {
        private File file;

        public LineCounter(File file) {
            this.file = file;
        }

        @Override
        public Integer call() {
            int lineCount = 0;
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                while (reader.readLine() != null) {
                    lineCount++;
                }
                System.out.println(Thread.currentThread().getName() +
                        " counted " + lineCount + " lines in " + file.getName());
            } catch (IOException e) {
                System.out.println("Error reading " + file.getName() + ": " + e.getMessage());
            }
            return lineCount;
        }
    }


