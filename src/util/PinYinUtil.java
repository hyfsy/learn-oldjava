package util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinYinUtil
{

    /**
     * 获取汉字的拼音
     * 
     * @param str
     * @return
     */
    public static String getPinYin(String str) {
        char[] arr = str.toCharArray();
        // 创建转换对象
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        // 转换类型（大写、小写）
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        // 定义中文声调的输出格式
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);
        StringBuffer sb = new StringBuffer();

        try {
            for (int i = 0; i < arr.length; i++) {
                // 判断为汉字
                if (Character.toString(arr[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    // 获取生成数组的拼音（只有一个）
                    String pinyin = PinyinHelper.toHanyuPinyinStringArray(arr[i], format)[0];
                    sb.append(pinyin).append(" ");
                }
                else {
                    sb.append(Character.toString(arr[i])).append(" ");
                }
            }
        }
        catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return sb.toString().trim();
    }
}
