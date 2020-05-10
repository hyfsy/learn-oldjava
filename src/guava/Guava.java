package guava;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.Collections2;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.google.common.collect.Table.Cell;

public class Guava
{

    /**
     * : 双键map table -> rowKey -> columnKey -> value
     * row、column不可以都一样，都一样，value会被后面的替代
     * 
     */
    @Test
    public void twoKeyMap() {
        HashBasedTable<String, String, Integer> table = HashBasedTable.create();
        table.put("java", "a", 83);
        table.put("android", "b", 92);
        table.put("python", "c", 98);
        Set<Cell<String, String, Integer>> cellSet = table.cellSet();
        for (Cell cell : cellSet) {
            System.out.println(cell.getRowKey() + " -> " + cell.getColumnKey() + " -> " + cell.getValue());
        }
    }

    /**
     * : 双向map BiMap : key和value都不能重复
     */
    @Test
    public void biMap() {
        HashBiMap<Object, Object> biMap = HashBiMap.create();
        biMap.put("1", 11);
        biMap.put("2", 21);
        biMap.put("3", 31);
        // key和value翻转
        BiMap<Object, Object> inverse = biMap.inverse();
        Object o = inverse.get(21);
        System.out.println(o);
    }

    /**
     * Multimap : key可以重复
     */
    @Test
    public void mapkeyCanSame() {
        Map<String, String> map = new HashMap<>();
        map.put("java", "a");
        map.put("android", "a");
        map.put("python", "c");
        // 创建可重复key map
        Multimap<String, String> mmap = ArrayListMultimap.create();
        Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, String> next = iterator.next();
            // 遍历插入值
            mmap.put(next.getValue(), next.getKey());
        }
        // 获取set对象
        Set<String> keySet = mmap.keySet();
        for (String key : keySet) {
            // 获取key对应的所有value
            Collection<String> collection = mmap.get(key);
            System.out.println(key + " -> " + collection);
        }
    }

    /**
     * Multiset : 无序，可重复
     */
    @Test
    public void setCanSame() {
        String[] strs = new String[] {"good", "good", "study", "day", "day", "up" };
        HashMultiset<String> multSet = HashMultiset.create();
        // 可以放入重复数据，内部是一个又一个map
        for (String str : strs) {
            multSet.add(str);
        }

        Set<String> elementSet = multSet.elementSet();
        for (String str : elementSet) {
            // 获取每个元素的个数
            System.out.println(str + " : " + multSet.count(str));
        }
    }

    /**
     * 交叉并
     */
    @Test
    public void crossAnd() {
        Set<Integer> set1 = Sets.newHashSet(1, 2, 3);
        Set<Integer> set2 = Sets.newHashSet(3, 4, 5);
        // 交集
        SetView<Integer> intersection = Sets.intersection(set1, set2);
        intersection.forEach(System.out::print);
        System.out.println();
        // 差集（第一个set中的第二个没有的）
        SetView<Integer> difference = Sets.difference(set1, set2);
        difference.forEach(System.out::print);
        System.out.println();
        // 并集
        SetView<Integer> union = Sets.union(set1, set2);
        union.forEach(System.out::print);
        System.out.println();
    }

    /**
     * 验证（14版本才可以使用）
     */

    /**
     * 组合
     */
    @Test
    public void combination() {
        Function<String, String> f1 = new Function<String, String>()
        {
            @Override
            public String apply(String str) {
                return str.length() > 3 ? str.substring(0, 3) : str;
            }
        };

        Function<String, String> f2 = new Function<String, String>()
        {
            @Override
            public String apply(String str) {
                return str.toUpperCase();
            }
        };

        Function<String, String> compose = Functions.compose(f1, f2);
        ArrayList<String> list = Lists.newArrayList("asdf", "tom", "lalala");
        Collection<String> transform = Collections2.transform(list, compose);
        transform.forEach(System.out::println);
    }

    /**
     * 转换类型
     */
    @Test
    public void transfor() {
        // Set<String> set = Sets.newHashSet("20190912","20190913","20190914");
        Set<Long> set = Sets.newHashSet(20190912L, 20190913L, 20190914L);
        Collection<String> transform = Collections2.transform(set, (e) -> new SimpleDateFormat("yyyy-MM-dd").format(e));
        transform.forEach(System.out::println);
    }

    /**
     * 过滤
     */
    @Test
    public void filter() {
        ArrayList<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 0);
        Collection<Integer> filter = Collections2.filter(list, (e) -> e > 5);
        filter.forEach(System.out::println);

    }

    /**
     * 只读,不能修改list
     */
    // @Test
    public void readonly() {

        //三种方法设置只读list
        
        //Arrays
        List<String> list = Arrays.asList("1", "2", "3");
        list.add("4");
        System.out.println(list);

        //Collections
        List<String> list2 = new ArrayList<>();
        List<String> list3 = Collections.unmodifiableList(list2);
        list3.add("1");

        //ImmutableList
        ImmutableList<String> list4 = ImmutableList.of("1", "2", "3", "4", "5");
        list4.add("6");

    }

}
