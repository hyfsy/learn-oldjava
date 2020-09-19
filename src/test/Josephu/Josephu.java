package test.Josephu;

public class Josephu{
	private Child firstChild=null;//定义一个指向链表第一个小孩的引用 不能动 用于生成环形
	private Child temp = null;//当前指针指向的小孩
	private int len=0;//表示共有几个小孩
	private int k=0;//从哪个孩子开始数数
	private int m=0;//每次数多少下
	
	//小孩人数
	void setLen(int len){
		this.len=len;
	}
	
	//从第几个小孩开始
	void setK(int k){
		this.k=k;
	}
	
	//每次数的个数
	void setM(int m){
		this.m=m;
	}
	
	//将小孩转化成链表
	public void createLink(){
		for(int i=1;i<=len;i++){
			if(i==1){//创建第一个小孩
				Child ch=new Child(i);
				this.firstChild=ch;
				this.temp=ch;
			}else{
				if(i==len){//创建最后一个小孩
					Child ch=new Child(i);
					temp.nextChild=ch;
					temp=ch;
					temp.nextChild=this.firstChild;//生成环形
				}else{//继续创建
					Child ch=new Child(i);
					temp.nextChild=ch;
					temp=ch;
				}
			}
		}
	}
	
	public void show(){//遍历小孩链表
		Child temp=this.firstChild;
		do{
			System.out.print(temp.no+" ");
			temp=temp.nextChild;
		}while(temp!=this.firstChild);
		System.out.println("\n");
	}
	
	public void play(){//开始
		Child temp=this.firstChild;
		for(int i=1;i<k;i++){
			temp=temp.nextChild;//得到开始数数的小孩
		}
		
		while(this.len!=1){
			//数m下
			for(int i=1;i<m;i++){
				temp=temp.nextChild;//得到要踢出去的小孩
			}

			Child temp2=temp;
			while(temp2.nextChild!=temp){
				temp2=temp2.nextChild;//得到出圈小孩的前一个小孩
			}
			
			temp2.nextChild=temp.nextChild;//将前一个小孩的next指为指针小孩的next
			System.out.println("依次出圈："+temp.no);
			temp=temp.nextChild;//指针右移
			this.len--;//小孩退出自动减1
		}
		System.out.println("最后出圈："+temp.no);
	}
	
}