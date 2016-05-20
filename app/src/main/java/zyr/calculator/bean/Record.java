package zyr.calculator.bean;

/**
 * 将查询记录封装成对象
 *
 * @author LTP 2015年11月24日
 */
public class Record {
    /**
     * BMI记录的Id
     */
    private long id;
    /**
     * 身高
     */
    private double height;
    /**
     * 体重
     */
    private double weight;
    /**
     * BMI值
     */
    private double bmi;
    /**
     * 结果(过轻/正常/过重/肥胖/非常肥胖)
     */
    private String result;
    /**
     * 当前时间(毫秒值确保作为数据表删除数据的唯一性)
     */
    private long currentMillis;

    public Record() {
    }

    public Record(double height, double weight, double bmi, String result, long currentMillis) {
        super();
        this.height = height;
        this.weight = weight;
        this.bmi = bmi;
        this.result = result;
        this.currentMillis = currentMillis;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public long getCurrentMillis() {
        return currentMillis;
    }

    public void setCurrentMillis(long currentMillis) {
        this.currentMillis = currentMillis;
    }

    @Override
    public String toString() {
        return "Record [height=" + height + ", weight=" + weight + ", bmi=" + bmi + ", result=" + result
                + ", currentMillis=" + currentMillis + "]";
    }
}
