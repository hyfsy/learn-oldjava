package math;

import java.util.Scanner;

public class bs {
	public static void main(String[] args) {
		int num1, num2, x, y, m;
		Scanner s = new Scanner(System.in);
		num1 = s.nextInt();
		num2 = s.nextInt();
		x = num1;
		y = num2;
		if (x < y) {
			m = x;
			x = y;
			y = m;
		}
		m = x % y;
		while (m > 0) {
			x = y;
			y = m;
			m = x % y;
		}
		System.out.println("num1=" + num1 + " num2=" + num2);
		System.out.println("最大公约数：" + y);
		System.out.println("最小公倍数：" + num1 * num2 / y);
	}
}
