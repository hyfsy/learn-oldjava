package test.Josephu;

import java.util.Scanner;

public class TestController{
	public static void main(String[] args){
		Josephu josephu=new Josephu();
		System.out.print("请输入参加小孩的个数：");
		Scanner len=new Scanner(System.in);
		josephu.setLen(len.nextInt());
		josephu.createLink();
		System.out.print("请输入参加第一个小孩的编号：");
		Scanner k=new Scanner(System.in);
		josephu.setK(k.nextInt());
		System.out.print("请输入每次游戏的个数：");
		Scanner m=new Scanner(System.in);
		josephu.setM(m.nextInt());
		josephu.show();
		josephu.play();
	}
}