package example.perf.util;

/**
 * Created by jackeylv on 2016/6/23.
 */
public class Summary {
    private long sum;
    private long num;
    private double avg = 0.0;
    public Summary(long sum, long num) {
        if (num < 0){
            throw new IllegalArgumentException("num < 0 is not allowed.");
        }
        this.sum = sum;
        this.num = num;
        if (num > 0){
            avg = sum/num;
        }
    }

    public double avg() {
        return avg;
    }
}
