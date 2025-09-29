import java.util.*;
public class Lists {
    public static void main(String[] args) {
        int[] sizes = {10000, 50000, 100000};

        for (int size : sizes) {
            System.out.println("\n=== Size: " + size + " ===");

            // ArrayList Test
            List<Integer> arrayList = new ArrayList<>();
            testPerformance("ArrayList", arrayList, size);

            // LinkedList Test
            List<Integer> linkedList = new LinkedList<>();
            testPerformance("LinkedList", linkedList, size);
        }
    }

    private static void testPerformance(String type, List<Integer> list, int size) {
        // Insertion
        long start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            list.add(i);  // inserting at end
        }
        long end = System.nanoTime();
        System.out.println(type + " insertion time: " + (end - start) / 1_000_000 + " ms");

        // Deletion
        start = System.nanoTime();
        for (int i = size - 1; i >= 0; i--) {
            list.remove(i); // deleting from end
        }
        end = System.nanoTime();
        System.out.println(type + " deletion time: " + (end - start) / 1_000_000 + " ms");
    }
}
