package designmodel.factory;

public class FileFactory {
	public static ProductFile getFile(String type) {
		if ("xlsx".equals(type)) {
			return new XlsxFile();
		} else if ("xls".equals(type)) {
			return new XlsFile();
		} else {
			return null;
		}
	}
}
