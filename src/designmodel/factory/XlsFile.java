package designmodel.factory;

public class XlsFile implements ProductFile {
	@Override
	public void product() {
		System.out.println("生产xls文件");
	}
}
