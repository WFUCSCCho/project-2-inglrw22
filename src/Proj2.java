/***********************************************
 * @file Proj2.java
 * @description  Driver program to compare BST vs AVL performance.
 *               Reads N lines from a dataset file (one record per line),
 *               builds sorted and shuffled lists, inserts into trees,
 *               times insert/search operations, and appends CSV results.
 * @author Ravi Ingle
 * @date October 21, 2025
 ***********************************************/

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Proj2 {

    /**
     * Program entry point.
     * Expects two command-line args: <input-file> <number-of-lines>
     *
     * @param args command-line arguments
     * @throws IOException when file I/O fails
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage: java Proj2 <input-file> <number-of-lines>");
            System.exit(1);
        }

        String infile = args[0];
        int n = Integer.parseInt(args[1]);

        // Read up to n non-empty lines from file (skip header if present)
        List<String> original = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(infile))) {
            String line = br.readLine(); // attempt to read first line
            if (line == null) {
                System.err.println("Input file is empty.");
                System.exit(1);
            }

            // If first line looks like a header (contains non-digit words or commas), skip it.
            // This heuristic won't break numeric-only datasets.
            if (looksLikeHeader(line)) {
                // skip header: read next line
                line = br.readLine();
            }

            while (line != null && original.size() < n) {
                line = line.trim();
                if (!line.isEmpty()) original.add(line);
                line = br.readLine();
            }
        }

        if (original.isEmpty()) {
            System.err.println("No data read from file.");
            System.exit(1);
        }

        // Prepare sorted and randomized copies
        List<String> sorted = new ArrayList<>(original);
        Collections.sort(sorted);

        List<String> shuffled = new ArrayList<>(original);
        Collections.shuffle(shuffled);

        // Create the four trees (use generics with String)
        BST<String> bstSorted = new BST<>();
        BST<String> bstShuffled = new BST<>();
        AvlTree<String> avlSorted = new AvlTree<>();
        AvlTree<String> avlShuffled = new AvlTree<>();

        // Time insertions
        long start, end;

        start = System.nanoTime();
        for (String s : sorted) bstSorted.insert(s);
        end = System.nanoTime();
        long bstSortedInsert = end - start;

        start = System.nanoTime();
        for (String s : shuffled) bstShuffled.insert(s);
        end = System.nanoTime();
        long bstShuffledInsert = end - start;

        start = System.nanoTime();
        for (String s : sorted) avlSorted.insert(s);
        end = System.nanoTime();
        long avlSortedInsert = end - start;

        start = System.nanoTime();
        for (String s : shuffled) avlShuffled.insert(s);
        end = System.nanoTime();
        long avlShuffledInsert = end - start;

        // Time searches (use original list as query set)
        start = System.nanoTime();
        for (String s : original) bstSorted.contains(s);
        end = System.nanoTime();
        long bstSortedSearch = end - start;

        start = System.nanoTime();
        for (String s : original) bstShuffled.contains(s);
        end = System.nanoTime();
        long bstShuffledSearch = end - start;

        start = System.nanoTime();
        for (String s : original) avlSorted.contains(s);
        end = System.nanoTime();
        long avlSortedSearch = end - start;

        start = System.nanoTime();
        for (String s : original) avlShuffled.contains(s);
        end = System.nanoTime();
        long avlShuffledSearch = end - start;

        // Print human-readable results
        System.out.println("---------------------------------------------------");
        System.out.println("Lines read: " + original.size());
        System.out.printf("BST (sorted)   : insert = %d ns, search = %d ns%n", bstSortedInsert, bstSortedSearch);
        System.out.printf("BST (shuffled) : insert = %d ns, search = %d ns%n", bstShuffledInsert, bstShuffledSearch);
        System.out.printf("AVL (sorted)   : insert = %d ns, search = %d ns%n", avlSortedInsert, avlSortedSearch);
        System.out.printf("AVL (shuffled) : insert = %d ns, search = %d ns%n", avlShuffledInsert, avlShuffledSearch);
        System.out.println("---------------------------------------------------");

        // Append CSV line to output.txt
        String csv = buildCsvLine(original.size(),
                bstSortedInsert, bstSortedSearch,
                bstShuffledInsert, bstShuffledSearch,
                avlSortedInsert, avlSortedSearch,
                avlShuffledInsert, avlShuffledSearch);

        try (FileWriter fw = new FileWriter("output.txt", true)) {
            fw.write(csv);
        }

        System.out.println("CSV appended to output.txt");
    }

    /**
     * Heuristic: determine whether a line looks like a header (contains a letter or comma).
     * We treat lines that contain letters (not only digits and punctuation) as headers.
     *
     * @param s the line to inspect
     * @return true if it probably is a header
     */
    private static boolean looksLikeHeader(String s) {
        if (s == null) return false;
        // if contains any alphabetic character or comma, treat as header
        for (char c : s.toCharArray()) {
            if (Character.isLetter(c) || c == ',') return true;
        }
        return false;
    }

    /**
     * Compose one CSV line in a stable format:
     * timestamp,rows,bstSortedInsert,bstSortedSearch,bstShuffledInsert,bstShuffledSearch,
     * avlSortedInsert,avlSortedSearch,avlShuffledInsert,avlShuffledSearch
     *
     * @param rows number of rows read
     * @param bsi  BST sorted insert ns
     * @param bss  BST sorted search ns
     * @param bri  BST shuffled insert ns
     * @param brs  BST shuffled search ns
     * @param asi  AVL sorted insert ns
     * @param ass  AVL sorted search ns
     * @param ari  AVL shuffled insert ns
     * @param ars  AVL shuffled search ns
     * @return CSV line ending with newline
     */
    private static String buildCsvLine(int rows, long bsi, long bss, long bri, long brs, long asi, long ass, long ari, long ars) {
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return String.format("%s,%d,%d,%d,%d,%d,%d,%d,%d,%d%n",
                ts, rows, bsi, bss, bri, brs, asi, ass, ari, ars);
    }
}
