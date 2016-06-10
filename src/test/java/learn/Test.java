/**
 * 
 */
package learn;

/**
 * @author ScattLabs
 *
 */
public class Test {
	public static void main(String[] args) {
		int a = 11;
		int b = 4;
		int c = a / b;
		System.out.println(c);
		double d = Math.round(c);
		if (a % 4 > 0) {
			d += 1;
		}

		System.out.println(d);
	}
}
