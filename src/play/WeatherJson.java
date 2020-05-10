package play;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WeatherJson {

	
	public static void main(String[] args) {
		weatherControl();
	}

	public static void weatherControl() {
		boolean flag = true;
		while (flag) {
			String areaId = "";
			System.out.println("======请选择地区======\n1：苏州\n2：无锡\n3：常州\n4：连云港\n5：南京\nexit：退出");
			Scanner scan = new Scanner(System.in);
			String s = scan.nextLine();
			switch (s.trim()) {
			case "1":
				areaId = "101190401";
				break;
			case "2":
				areaId = "101190201";
				break;
			case "3":
				areaId = "101191101";
				break;
			case "4":
				areaId = "101191001";
				break;
			case "5":
				areaId = "101190101";
				break;
			case "exit":
				flag = false;
				break;
			default:
				System.out.println("请输入正确的地区！");
				break;
			}
			if (!"".equals(areaId)) {
				try {
					getWeather(areaId);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}

	public static void getWeather(String areaId)throws Exception {
		String link = "http://t.weather.sojson.com/api/weather/city/" + areaId;// 获取链接
		
		StringBuilder json;
		try {
			json = getJson(link);// 得到json数据
		} catch (Exception e) {
			throw new RuntimeException("无法获取天气状况，请确认网络连接状态。。。");
		}
		
		Map<String, Object> weather=null;
		try {
			weather = transformJson(json);// 转换json为map
		} catch (Exception e) {
			throw new RuntimeException("json出问题了，请联系管理员！！！");
		}
		
		printWeather(weather);// 打印天气信息

	}

	// 通过连接返回json字符串
	public static StringBuilder getJson(String link) throws Exception {
		URL url;
		StringBuilder json = null;

		url = new URL(link);
		URLConnection openConnection = url.openConnection();
		InputStream inputStream = openConnection.getInputStream();
		BufferedInputStream bis = new BufferedInputStream(inputStream);

		json = new StringBuilder();
		byte[] bytes = new byte[1024];
		while (bis.read(bytes) != -1) {
			json.append(new String(bytes));
		}
		inputStream.close();
		bis.close();

		return json;
	}

	// 将json转换成map
	public static Map<String, Object> transformJson(StringBuilder json)throws Exception {

		String json2 = json.substring(0, json.length() - 29);// json有问题
		//System.out.println(json2);

		// 开始分割json
		Map<String, Object> map = new HashMap<>();
		Map<String, String> cityInfoMap = new HashMap<>();
		Map<String, Object> dataMap = new HashMap<>();
		Map<String, String> yesterdayMap = new HashMap<>();
		Map<String, Object> forecastMap = new HashMap<>();

		// 分割time
		String[] time = json2.toString().split("\"cityInfo\":");
		String[] splitE = splitElement(time[0]);
		map.put(splitE[0], splitE[1]);
		// 分割cityInfo
		String[] cityInfo = time[1].split("},\"date\":");
		cityInfoMap = splitChild(cityInfo[0]);
		map.put("cityInfo", cityInfoMap);
		// 分割date
		String[] date = cityInfo[1].split("\"message\":");
		map.put("date", date[0].substring(date[0].indexOf("\"") + 1, date[0].lastIndexOf("\"")));
		// 分割message
		String[] message = date[1].split("\"status\":");
		map.put("message", message[0].substring(message[0].indexOf("\"") + 1, message[0].lastIndexOf("\"")));
		// 分割status
		String[] status = message[1].split("\"data\":");
		map.put("status", status[0].substring(0, status[0].lastIndexOf(",")));

		String split11 = null;
		// 分割data
		for (int i = 0; i < 6; i++) {
			String[] split2 = status[1].split(",", 2);
			String[] splitElement = splitElement(split2[0]);
			dataMap.put(splitElement[0], splitElement[1]);
			status[1] = split2[1];
			split11 = status[1];
		}

		String[] split10 = split11.split("},", 2);
		// 分割yesterday
		String[] split12 = split10[0].split(":\\{");
		yesterdayMap = splitChild(split12[1]);
		dataMap.put(split12[0].substring(split12[0].indexOf("\"") + 1, split12[0].lastIndexOf("\"")), yesterdayMap);

		// 分割forecast
		String[] split13 = split10[1].split(":\\[", 2);
		String[] split14 = split13[1].split("},");

		int count = 0;// 未来预报的天数
		// for(int i=0;i<split14.length;i++) {
		for (int i = 0; i < split14.length; i++) {
			Map<String, String> splitChild = splitChild(split14[i]);
			forecastMap.put("day+" + String.valueOf(count), splitChild);
			count++;
		}

		dataMap.put(split13[0].substring(split13[0].indexOf("\"") + 1, split13[0].lastIndexOf("\"")), forecastMap);
		map.put("data", dataMap);
		return map;
	}

	// 打印天气状况
	@SuppressWarnings("unchecked")
	public static void printWeather(Map<String, Object> map) {
		Map<String, String> cityInfo = (Map<String, String>) map.get("cityInfo");// 城市信息
		Map<String, Map<String, Object>> data = (Map<String, Map<String, Object>>) map.get("data");
		Map<String, Object> forecast = data.get("forecast");// 从今天起的15天信息
		Map<String, String> now = (Map<String, String>) forecast.get("day+0");// 今日信息

		System.out.println();
		System.out.println("===============天气===============");
		System.out.println();
		System.out.println("当前时间：" + map.get("time") + " " + now.get("week"));
		System.out.println("城市：" + cityInfo.get("city"));
		System.out.println("城市湿度：" + data.get("shidu"));
		System.out.println("污染程度：" + data.get("quality"));
		System.out.println();
		System.out.println("今日天气：" + now.get("type") + " " + now.get("fx") + " " + now.get("fl"));// 天气状况 风向 风级
		System.out.println("当前温度：" + data.get("wendu"));
		System.out.println("最高温：" + now.get("high"));
		System.out.println("最低温：" + now.get("low"));
		System.out.println("空气指数：" + now.get("aqi"));
		System.out.println("日升时间：" + now.get("sunrise"));
		System.out.println("日落时间：" + now.get("sunset"));
		System.out.println("小贴士：" + now.get("notice"));
		System.out.println();
		System.out.println("=================================");
		System.out.println();
	}

	// 分割单元数据 如 ： "1":"1" or "1":1 ,
	public static String[] splitElement(String s) {
		String[] kv = s.split(":", 2);
		for (int i = 0; i < kv.length; i++) {
			if (kv[i].indexOf("\"") == -1) {
				continue;
			} else {
				kv[i] = kv[i].substring(kv[i].indexOf("\"") + 1, kv[i].lastIndexOf("\""));
			}
		}
		return kv;
	}

	// 传入一个成员数据，返还map 如： {...} ！内部无其他子数据
	public static Map<String, String> splitChild(String s) {
		Map<String, String> map = new HashMap<>();
		String[] e = s.split(",");
		for (int i = 0; i < e.length; i++) {
			String[] kv = e[i].split(":", 2);
			for (int j = 0; j < kv.length; j++) {
				if (kv[j].indexOf("\"") == -1) {// 判断键值对的值是否为 整型
					continue;
				} else {
					kv[j] = kv[j].substring(kv[j].indexOf("\"") + 1, kv[j].lastIndexOf("\""));
				}
			}
			map.put(kv[0], kv[1]);
		}
		return map;
	}

}
