    // ParallelStreamVsExecutor.java
import java.util.concurrent.*;
import java.util.*;
import java.util.stream.*;

    public class ParallelStreamVsExecutor {

        public static void main(String[] args) throws InterruptedException, ExecutionException {
            int n = 10_000_000; // 10 million numbers

            System.out.println("Comparing performance between Parallel Stream and ExecutorService...\n");

            // Measure Parallel Stream time
            long start1 = System.currentTimeMillis();
            long sum1 = IntStream.rangeClosed(1, n)
                    .parallel() // parallel processing
                    .asLongStream()
                    .sum();
            long end1 = System.currentTimeMillis();
            System.out.println("Parallel Stream Sum: " + sum1);
            System.out.println("Time taken by Parallel Stream: " + (end1 - start1) + " ms\n");

            // Measure ExecutorService time
            long start2 = System.currentTimeMillis();
            int numThreads = 4; // number of worker threads
            ExecutorService executor = Executors.newFixedThreadPool(numThreads);

            List<Future<Long>> results = new ArrayList<>();
            int chunkSize = n / numThreads;

            for (int i = 0; i < numThreads; i++) {
                int start = i * chunkSize + 1;
                int end = (i == numThreads - 1) ? n : (i + 1) * chunkSize;
                results.add(executor.submit(new SumTask(start, end)));
            }

            long total = 0;
            for (Future<Long> f : results) {
                total += f.get();
            }

            long end2 = System.currentTimeMillis();
            System.out.println("ExecutorService Sum: " + total);
            System.out.println("Time taken by ExecutorService: " + (end2 - start2) + " ms");

            executor.shutdown();
        }
    }

    // Callable class for partial sum calculation
    class SumTask implements Callable<Long> {
        private int start, end;

        public SumTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public Long call() {
            long sum = 0;
            for (int i = start; i <= end; i++) {
                sum += i;
            }
            return sum;
        }
    }


