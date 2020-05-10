package object;

/**
 * 
 * [实现了比较器的对象类型]
 * 
 * @author baB_hyf
 * @version [版本号, 2019年9月9日]
 */
public class TwoElephant implements Comparable<TwoElephant>
{
    private Integer two;
    private String elephant;

    public TwoElephant() {
    };

    public TwoElephant(Integer two, String elephant) {
        super();
        this.two = two;
        this.elephant = elephant;
    }

    public Integer getTwo() {
        return two;
    }

    public void setTwo(Integer two) {
        this.two = two;
    }

    public String getElephant() {
        return elephant;
    }

    public void setElephant(String elephant) {
        this.elephant = elephant;
    }

    @Override
    public String toString() {
        return "TwoElephant [two=" + two + ", elephant=" + elephant + "]";
    }

    @Override
    public int compareTo(TwoElephant te) {
        return this.two - te.two;
    }
}
