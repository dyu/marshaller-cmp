package example.perf.util;

/**
 * Created by jackeylv on 2016/6/9.
 */
public class TimeRecord {
    private long start_time;
    private long cost_time;

    /**
     * reset the time record.
     * @return the current time in milliseconds.
     */
    public long reset() {
        start_time = System.currentTimeMillis();
        cost_time = 0;
        return start_time;
    }

    /**
     * return cost time in milliseconds
     * @return cost time in milliseconds
     */
    public long end() {
        cost_time = System.currentTimeMillis() - start_time;
        return cost_time;
    }
}
