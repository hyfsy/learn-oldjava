package util;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeCastUtil {
	// 转换unicode(\\u)编码为string
	public static String unicodeToString(String str) {
		Pattern compile = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
		Matcher matcher = compile.matcher(str);
		char ch;
		while (matcher.find()) {
			String group = matcher.group(2);// ad3s
			ch = (char) Integer.parseInt(group, 16);
			String group2 = matcher.group(1);// \\uad3s
			str = str.replace(group2, ch + "");
		}
		return str;
	}

	// 转换string为unicode编码
	public static String stringToUnicode(String s) {
		try {
			StringBuffer out = new StringBuffer("");
			// 直接获取字符串的unicode二进制
			byte[] bytes = s.getBytes("unicode");
			// 然后将其byte转换成对应的16进制表示即可
			for (int i = 0; i < bytes.length - 1; i += 2) {
				out.append("\\u");
				String str = Integer.toHexString(bytes[i + 1] & 0xff);
				for (int j = str.length(); j < 2; j++) {
					out.append("0");
				}
				String str1 = Integer.toHexString(bytes[i] & 0xff);
				out.append(str1);
				out.append(str);
			}
			return out.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 转换string为utf8编码
	//有 % 和 无% 两种格式，默认 无% 格式
	public static String stringToUtf8(String s) {
		if (s == null || s.equals("")) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		try {
			char c;
			for (int i = 0; i < s.length(); i++) {
				c = s.charAt(i);
				if (c >= 0 && c <= 255) {
					sb.append(c);
				} else {
					byte[] b;
					b = Character.toString(c).getBytes("utf-8");
					for (int j = 0; j < b.length; j++) {
						int k = b[j];
						// 转换为unsigned integer 无符号integer
						/*
						 * if (k < 0) k += 256;
						 */
						k = k < 0 ? k + 256 : k;
						// 返回整数参数的字符串表示形式 作为十六进制（base16）中的无符号整数
						// 该值以十六进制（base16）转换为ASCII数字的字符串
						sb.append(Integer.toHexString(k).toUpperCase());

						// url转置形式
						// sb.append("%" +Integer.toHexString(k).toUpperCase());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	// 转换utf8编码为string
	public static String Utf8ToString(String s) {
		if (s == null || s.equals("")) {
			return null;
		}
		try {
			s = s.toUpperCase();
			int total = s.length() / 2;
			// 标识字节长度
			int pos = 0;
			byte[] buffer = new byte[total];
			for (int i = 0; i < total; i++) {
				int start = i * 2;
				// 将字符串参数解析为第二个参数指定的基数中的有符号整数。
				buffer[i] = (byte) Integer.parseInt(s.substring(start, start + 2), 16);
				pos++;
			}
			// 通过使用指定的字符集解码指定的字节子阵列来构造一个新的字符串。
			// 新字符串的长度是字符集的函数，因此可能不等于子数组的长度。
			return new String(buffer, 0, pos, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
	}

}
