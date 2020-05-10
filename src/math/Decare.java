package math;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 *  [笛卡尔乘积] 
 * @author baB_hyf
 * @version [版本号, 2019年10月6日]
 */
public class Decare {

	public static void main(String[] args) {
		List<List<String>> list = new ArrayList<List<String>>();
        List<String> listSub1 = new ArrayList<String>();
        List<String> listSub2 = new ArrayList<String>();
        List<String> listSub3 = new ArrayList<String>();
        List<String> listSub4 = new ArrayList<String>();
        listSub1.add("1");
        listSub1.add("2");
        listSub2.add("3");
        listSub2.add("4");
        listSub3.add("a");
        listSub3.add("b");
        listSub4.add("c");
        listSub4.add("d");
        list.add(listSub1);
        list.add(listSub2);
        list.add(listSub3);
        list.add(listSub4);
        List<List<String>> result = new ArrayList<List<String>>();
        descartes(list, result, 0, new ArrayList<String>());
        for(List<String> sl:result) {
        	System.out.println(sl);
        }
	}
	
	
	/**
     * <p>
     * Discription:笛卡尔乘积算法
     * 把一个List{[1,2],[3,4],[a,b]}转化成List{[1,3,a],[1,3,b],[1,4
     * ,a],[1,4,b],[2,3,a],[2,3,b],[2,4,a],[2,4,b]}数组输出
     * </p>
     * 
     * @param dimvalue原List
     * @param result通过乘积转化后的数组
     * @param layer
     *            中间参数
     * @param curList
     *            中间参数
     */
    private static void descartes(List<List<String>> dimvalue,
            List<List<String>> result, int layer, List<String> curList) {
        if (layer < dimvalue.size() - 1) {
            if (dimvalue.get(layer).size() == 0) {
                Decare.descartes(dimvalue, result, layer + 1, curList);
            } else {
                for (int i = 0; i < dimvalue.get(layer).size(); i++) {
                    List<String> list = new ArrayList<String>(curList);
                    list.add(dimvalue.get(layer).get(i));
                    Decare.descartes(dimvalue, result, layer + 1, list);
                }
            }
        } else if (layer == dimvalue.size() - 1) {
            if (dimvalue.get(layer).size() == 0) {
                result.add(curList);
            } else {
                for (int i = 0; i < dimvalue.get(layer).size(); i++) {
                    List<String> list = new ArrayList<String>(curList);
                    list.add(dimvalue.get(layer).get(i));
                    result.add(list);
                }
            }
        }
    }
	

}
