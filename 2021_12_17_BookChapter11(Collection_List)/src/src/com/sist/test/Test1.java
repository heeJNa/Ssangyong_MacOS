package src.com.sist.test;

import java.util.Scanner;

public class Test1 {

	private String id;
	private int postNum;

	public Test1() {
		id = "hello";
		postNum = 1234;
	}

	public void idCheck(String id) {
		if (this.id.equalsIgnoreCase(id)) {
			System.out.println("���̵� �ߺ��Ǿ����ϴ�.");
			return;
		} else {
			this.id = id;
		}
	}

	public void postNumCheck(int postNum) {
		if (this.postNum == postNum) {
			System.out.println("���̵� �ߺ��Ǿ����ϴ�.");
			return;
		} else {
			this.postNum = postNum;
		}
	}

	public void gogodan() {
		for (int i = 1; i <= 9; i++) {
			for (int j = 1; j <= 9; j++) {
				System.out.printf("%-2d*%-2d=%-2d\t", j, i, i * j);
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		Test1 t = new Test1();

		Scanner scan = new Scanner(System.in);
		System.out.print("ID�� �Է��Ͻÿ� : ");
		String id = scan.next();
		t.idCheck(id);
		System.out.print("�����ȣ�� �Է��Ͻÿ� : ");
		int postNum = scan.nextInt();
		t.postNumCheck(postNum);
		System.out.println();

		t.gogodan();

	}

}
