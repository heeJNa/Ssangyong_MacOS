package src.com.sist.test;

import java.util.Scanner;

public class Test3 {
	
	Scanner scan = new Scanner(System.in);
	
	public void input() {
		int num[] = new int[5];
		System.out.print("5���� ���� �Է� : ");
		for(int i = 0 ; i<num.length;i++) {
			num[i] = scan.nextInt();
		}
		print(num);
	}
	
	public int[] sortAsc(int[] num) {
		for(int i = 0; i<num.length-1; i++) {
			for(int j =i+1; j< num.length;j++) {
				if(num[i]>num[j]) {
					int tmp = num[i];
					num[i] = num[j];
					num[j] = tmp;
				}
			}
		}
		return num;
	}
	
	public int[] sortDesc(int[] num) {
		for(int i = 0; i<num.length-1; i++) {
			for(int j =i+1; j< num.length;j++) {
				if(num[i]<num[j]) {
					int tmp = num[i];
					num[i] = num[j];
					num[j] = tmp;
				}
			}
		}
		return num;
	}
	
	public void print(int[] num) {
		System.out.println("������:");
		for(int i : num) {
			System.out.print(i + " ");
		}
		System.out.println("\n�������� ����: ");
		for(int i : sortAsc(num)) {
			System.out.print(i + " ");
		}
		System.out.println("\n�������� ����:");
		for(int i : sortDesc(num)) {
			System.out.print(i + " ");
		}
	}
	
	public static void main(String[] args) {
		Test3 t = new Test3();
		t.input();
	}
}
