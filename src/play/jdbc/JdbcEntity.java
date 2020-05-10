package play.jdbc;

public class JdbcEntity {

	private Integer sno;
	private String sname;
	public Integer getSno() {
		return sno;
	}
	public void setSno(Integer sno) {
		this.sno = sno;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sid) {
		this.sname = sid;
	}
	
	@Override
	public String toString() {
		return "JdbcEntity [sno=" + sno + ", sname=" + sname + "]";
	}
	
	

}
