/** Class that prints the Collatz sequence starting from a given number.
 *  @author YOUR NAME HERE
 */
public class Collatz {

    /** Return the next number in the Collatz sequence
     * @param number
     * if number is odd, next number is 3 * number + 1
     * if number is even, next number is n / 2
     */
    public static int nextNumber(int number) {
        if (number % 2 == 0) {
            return number / 2;
        } else {
            return 3 * number + 1;
        }
    }

    public static void main(String[] args) {
        int n = 5;
        System.out.print(n + " ");
        while (n != 1) {
            n = nextNumber(n);
            System.out.print(n + " ");
        }
        System.out.println();
    }
}

