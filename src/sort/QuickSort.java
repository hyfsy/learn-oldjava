package sort;

public class QuickSort {//快速排序

	public QuickSort() {
		int a[] = { 49, 38, 65, 97, 76, 13, 27, 49, 78, 34, 12, 64, 5, 4, 62, 99, 98, 54, 56, 17, 18, 23, 34, 15, 35,
				25, 53, 51 };
		
		sort(a);//排序入口
		
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i] + " ");
		}
	}

	public void sort(int[] a) {
		if (a.length > 0) {
			quickSort(a, 0, a.length - 1);
		}
	}

	public void quickSort(int[] a, int start, int end) {
		if (start < end) {
			int middle = middle(a, start, end);// 排序并获取中间值
			quickSort(a, start, middle - 1);// 递归排列中间值的前面
			quickSort(a, middle + 1, end);// 递归排列中间值的后面
		}
	}

	// 排序并获得中间值
	public int middle(int[] a, int start, int end) {
		int temp = a[start];// 每次从第一个开始
		while (start < end) {// 执行一遍排序
			while (start < end && a[end] >= temp) {// 主要判断后面的 判断end的值是否比中间值大
				end--;// 大的话，将指针前移
			}
			a[start] = a[end];// 指针到达start 或 比中间值小交换两值
			while (start < end && a[start] <= temp) {
				start++;
			}
			a[end] = a[start];
		}
		a[start] = temp;// 循环结束将当前中间值赋值
		return start;
	}

	public static void main(String[] args) {
		QuickSort quickSort = new QuickSort();
	}

}
