import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Distribution Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            int e = Math.min(k, array.length);
            int s = Math.max(0, 0);
            for(int i = s; i < e; i++){
                for(int j = i; j > s; j--){
                    if(array[j] < array[j-1]){
                        swap(array, j-1, j);
                    }
                    else
                        break;
                }
            }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for(int i = 0; i < k; i++){
                int min = i;
                for(int j = i+1; j < k; j++) {
                    min = array[min] > array[j] ? min = j : min;
                }
                swap(array, min, i);
            }
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            k = Math.min(k,array.length);
            mS(array, 0, k, new int[k]);
        }

        private void mS(int[] array, int l, int r, int[] j) {
            int mid = (r + l)/2;
            if(r - l > 1){
                mS(array,l,mid,j);
                mS(array,mid,r,j);
                merge(array, l, mid, r, j);
                System.arraycopy(j, l, array, l, r-l);
            }
        }

        private void merge(int[] array, int left, int mid, int right, int[] js) {
            for(int l = left, r = mid; l < mid || r < right;){
                if((r == right) || (l < mid && array[l] < array[r]))
                {
                    js[l+r-mid] = array[l];
                    l++;
                }
                else if((l == mid) ||  (r < right && array[l] >= array[r])){
                    js[l+r-mid] = array[r];
                    r++;
                }
            }
        }


        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Distribution Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class DistributionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            k = Math.min(k,array.length);
            if(k <= 1)
                return;
            int m = Arrays.stream(array).limit(k).max().getAsInt();
            int[] counts = new int[m+1];
            for(int i = 0; i < k; i++)
                ++counts[array[i]];
            int i = 0;
            for(int e = 0; e < m+1; e++){
                int end = counts[e]+i;
                if(end != i)
                    Arrays.fill(array, i, end, e);
                i = end;
            }


        }

        @Override
        public String toString() {
            return "Distribution Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            int n = Math.min(array.length, k);
            for (int m = n/2; m >= 1; m--)
                sink(array, m, n);
            while (n > 1) {
                e(array, 1, n);
                n -= 1;
                sink(array, 1, n);
            }
        }

        private static void sink(int[] pq, int m, int N) {
            while (2*m <= N) {
                int j = 2*m;
                if (j < N && less(pq, j, j+1)) j++;
                if (!less(pq, m, j)) break;
                e(pq, m, j);
                m = j;
            }
        }

        private static boolean less(int[] pq, int i, int j) {
            return pq[i-1] < pq[j-1];
        }

        private static void e(int[] pq, int i, int j) {
            int swap = pq[i-1];
            pq[i-1] = pq[j-1];
            pq[j-1] = swap;
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            int N = Math.min(k - 1, array.length - 1);
            quick(array, 0, N);
        }

        private static void quick(int[] a, int lo, int hi) {
            if (hi <= lo) {
                return;
            }
            int j = part(a, lo, hi);
            quick(a, lo, j - 1);
            quick(a, j + 1, hi);
        }

        private static int[] toArr(List<Integer> al) {
            int[] returnArray = new int[al.size()];

            for (int i = 0; i < al.size(); i += 1) {
                returnArray[i] = al.get(i);
            }

            return returnArray;
        }

        private static int part(int[] a, int lo, int hi) {
            List<Integer> s = new ArrayList<Integer>();
            List<Integer> e = new ArrayList<Integer>();
            List<Integer> l = new ArrayList<Integer>();
            int pivot = a[lo];
            for (int i = lo; i <= hi; i += 1) {
                if (a[i] < pivot) {
                    s.add(a[i]);
                } else if (a[i] > pivot) {
                    l.add(a[i]);
                } else {
                    e.add(a[i]);
                }
            }
            List<Integer> p = new ArrayList<Integer>();
            p.addAll(s);
            p.addAll(e);
            p.addAll(l);
            int[] pa = toArr(p);
            System.arraycopy(pa, 0, a, lo, pa.length);
            return s.size() + lo;
        }



        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            int b = 32;
            int bpb = 8;
            int W = b / bpb;
            int R = 1 << bpb;
            int mask = R - 1;
            int n = Math.min(a.length, k);
            int[] aux = new int[n];
            for (int d = 0; d < W; d++) {
                int[] count = new int[R+1];
                for (int i = 0; i < n; i++) {
                    int c = (a[i] >> bpb*d) & mask;
                    count[c + 1]++;
                }
                for (int r = 0; r < R; r++)
                    count[r+1] += count[r];
                if (d == W-1) {
                    int shift1 = count[R] - count[R/2];
                    int shift2 = count[R/2];
                    for (int r = 0; r < R/2; r++)
                        count[r] += shift1;
                    for (int r = R/2; r < R; r++)
                        count[r] -= shift2;
                }
                for (int i = 0; i < n; i++) {
                    int c = (a[i] >> bpb*d) & mask;
                    aux[count[c]++] = a[i];
                }
                for (int i = 0; i < n; i++)
                    a[i] = aux[i];
            }

        }

        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            int n = Math.min(a.length, k);
            int[] aux = new int[n];
            sort(a, 0, n-1, 0, aux);
        }

        public void sort(int[] a, int lo, int hi, int d, int[] aux) {
            int bpi = 32;
            int bpb = 8;
            int R = 1 << bpb;
            int MASK = R - 1;
            int[] count = new int[R+1];
            int mask = R - 1;   // 0xFF;
            int shift = bpi - bpb*d - bpb;
            for (int i = lo; i <= hi; i++) {
                int c = (a[i] >> shift) & mask;
                count[c + 1]++;
            }
            for (int r = 0; r < R; r++)
                count[r+1] += count[r];
            for (int i = lo; i <= hi; i++) {
                int c = (a[i] >> shift) & mask;
                aux[count[c]++] = a[i];
            }
            for (int i = lo; i <= hi; i++)
                a[i] = aux[i - lo];
            if (d == 4) return;
            if (count[0] > 0)
                sort(a, lo, lo + count[0] - 1, d+1, aux);
            for (int r = 0; r < R; r++)
                if (count[r+1] > count[r])
                    sort(a, lo + count[r], lo + count[r+1] - 1, d+1, aux);
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
