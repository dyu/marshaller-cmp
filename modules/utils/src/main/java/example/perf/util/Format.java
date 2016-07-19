package example.perf.util;

/**
 * Created by jackeylv on 2016/6/9.
 */
public class Format {
    /**
     * 将字节数转换成易读的KMGTPE格式
     * @param bytes 待转换的字节数
     * @param si_unit true表示以1000为底，false表示以1024为底。
     * @return 格式化后的KMGTPE数值
     */
    public static String humanReadableByteCount(long bytes, boolean si_unit) {
        int unit = si_unit ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        // 换底公式变形 log_unit^bytes = ln(bytes)/ln(unit)
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si_unit ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si_unit ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static String ops(long op_num, long milliseconds) {
        if (milliseconds <= 0) return "NA";
        return String.format("%d OPS", op_num*1000/milliseconds);
    }

    public static String speed(long bytes, long milliseconds){
        if (milliseconds == 0)
            return "NA";
        double bps = 1000 * bytes / milliseconds;
        return humanReadableByteCount((long) bps, false) + "/s";
    }
}
