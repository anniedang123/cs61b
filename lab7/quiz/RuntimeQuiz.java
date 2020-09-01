package quiz;

public class RuntimeQuiz {
    /** Functions describing runtime **/
    public enum Runtime { // Assuming n is the length of the input
        CONSTANT,     // 1
        LOG_N,        // log(n)
        LINEAR,       // n
        LINEARITHMIC, // n * log(n)
        QUADRATIC,    // n^2
        CUBIC,        // n^3
        EXPONENTIAL,  // a^n where a is any constant
        UNDEFINED     // no such bound exists
    }

    /**
     * Fill out the missing Runtime values according to the
     * asymptotic behavior of each method. Give the corresponding tightest
     * big omega, big O, and big theta runtimes in terms on N where N is 
     * the length of the input array.
     */
    public static Runtime f1_omega_runtime = Runtime.LINEAR;
    public static Runtime f1_o_runtime = Runtime.LINEAR;
    public static Runtime f1_theta_runtime = Runtime.LINEAR;
    public void f1(int[] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < Integer.MAX_VALUE; j++) {
                System.out.println("Hi!");
            }
        }
    }

    public static Runtime f2_omega_runtime = Runtime.QUADRATIC;
    public static Runtime f2_o_runtime = Runtime.QUADRATIC;
    public static Runtime f2_theta_runtime = Runtime.QUADRATIC;
    public int f2(int n) {
        if (n <= 1) return n;
        f1(new int[n]);
        return n + n * f2(n - 1) + n * n * f2(1);
    }

    public static Runtime f3_omega_runtime = Runtime.LINEAR;
    public static Runtime f3_o_runtime = Runtime.LINEAR;
    public static Runtime f3_theta_runtime =  Runtime.LINEAR;
    /* When f3 is first called, start will be 0 and end will be the length of the array - 1 */
    public int f3(char[] array, int start, int end) {
        if (array.length <= 1 || end <= start) return 1;
        int mid = start + ((end - start) / 2);
        return f3(array, start, mid) + f3(array, mid + 1, end);
    }

    public static Runtime f4_omega_runtime = Runtime.LINEARITHMIC;
    public static Runtime f4_o_runtime = Runtime.LINEARITHMIC;
    public static Runtime f4_theta_runtime = Runtime.LINEARITHMIC;
    /* When f4 is first called, start will be 0 and end will be the length of the array - 1 */
    public int f4(char[] array, int start, int end) {
        if (array.length <= 1 || end <= start) return 1;
        int counter = 0;
        for (int i = start; i < end; i++) {
            if (array[i] == 'a') counter++;
        }
        int mid = start + ((end - start) / 2);
        return counter + f4(array, start, mid) + f4(array, mid + 1, end);
    }

    public static Runtime f5_omega_runtime = Runtime.LOG_N;
    public static Runtime f5_o_runtime = Runtime.LOG_N;
    public static Runtime f5_theta_runtime = Runtime.LOG_N;
    public void f5(int n) {
        int[] array = {1, 2, 3};
        while (n > 0) {
            f1(array);
            n = n / 2;
        }
    }

    public static Runtime f6_omega_runtime = Runtime.CONSTANT;
    public static Runtime f6_o_runtime = Runtime.LINEAR;
    public static Runtime f6_theta_runtime = Runtime.UNDEFINED;
    public void f6(int[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i] == array[i-1]) {
                System.out.println("Sarah is the potatoest");
                return;
            }
        }
    }

    public static Runtime f7_omega_runtime = Runtime.LINEAR;
    public static Runtime f7_o_runtime = Runtime.EXPONENTIAL;
    public static Runtime f7_theta_runtime = Runtime.UNDEFINED;
    /* When f7 is first called, start will be 0 and end will be the length of the array - 1 */
    public int f7(int[] array, int start, int end) {
        if (array.length <= 1 || end <= start) {
            return 1;
        } else if (array[start] <= array[end]) {
            return f7(array, start + 1, end - 1);
        } else {
            int tmp = array[start];
            array[start] = array[end];
            array[end] = tmp;
            return f7(array, start + 1, end) + f7(array, start, end - 1)
                    + f7(array, start + 1, end - 1);
        }
    }
}
