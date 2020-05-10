package designmodel.factory;

public class XlsxFile implements ProductFile {

	@Override
	public void product() {
		System.out.println("生产xlsx文件");
	}
	
}
