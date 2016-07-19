package example.perf.util;

import java.util.Collection;

/**
 * Created by jackeylv on 2016/6/9.
 */
public class Statics {
    /**
     * 输出统一的时间信息
     * @param operation 操作类型
     * @param op_num 操作次数
     * @param cost_time 总耗时
     * @param bytes 数据大小
     * @return
     */
    public static String collect(String operation, long op_num, long cost_time, long bytes) {
        return String.format("op %s, data(bytes) %d, times %d, cost(ms) %d, %s",
                operation, bytes, op_num, cost_time, Format.ops(op_num, cost_time));
    }

    public static Summary summary(Collection<Long> collection){
        long sum = 0;
        long num = 0;
        for (Long l : collection){
            sum += l;
            num += 1;
        }
        Summary summary = new Summary(sum, num);
        return summary;
    }
}
