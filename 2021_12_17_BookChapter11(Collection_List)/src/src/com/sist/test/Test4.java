package src.com.sist.test;

import java.util.Scanner;

public class Test4 {
	
	Scanner scan = new Scanner(System.in);
	private int year;
	private boolean yun;
	
	public void inputYear() {
		System.out.print("�⵵ �Է�:");
		year = scan.nextInt();
		if(year <0) {
			System.out.println("0�̻��� �Է��Ͻÿ�");
			return;
		}
		yunYearCheck(year);
		print();
	}
	
	public void yunYearCheck(int year) {
		if(year%4 ==0 && year%100 != 0 || year %400 ==0) {
			yun = true;
		}else 
			yun = false;
	}
	
	public void print() {
		if(yun) {
			System.out.println(year +"�⵵�� ������ �Դϴ�.");
		}else {
			System.out.println(year +"�⵵�� ������ �ƴմϴ�.");
		}
	}
	
	public static void main(String[] args) {
		Test4 t = new Test4();
		t.inputYear();
	}
}
