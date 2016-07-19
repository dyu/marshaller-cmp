package example.perf.util;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParametersDelegate;

/**
 * Created by jackeylv on 2016/6/27.
 */
public class OptionsHelper {

    public static String getMainClassAndArgs() {
        return System.getProperty("sun.java.command"); // like "org.x.y.Main arg1 arg2"
    }

    public static JCommander jcommander(String[] cmdArgs, MarshallerParameters cfg, String programName) {
        JCommander jCommander = new JCommander();

        jCommander.setAcceptUnknownOptions(false);
        jCommander.setProgramName(programName);
        jCommander.addObject(cfg);

        jCommander.parse(cmdArgs);

        return jCommander;
    }

    public static String usage(Object args) {
        CompositeParameters cp = new CompositeParameters();

        JCommander jCommander = new JCommander();

        jCommander.setAcceptUnknownOptions(true);
        jCommander.addObject(cp);

        StringBuilder sb = new StringBuilder();

        jCommander.usage(sb);

        return sb.toString();
    }

    /** */
    private static class CompositeParameters {
        @ParametersDelegate
        private MarshallerParameters cfg = new MarshallerParameters();
    }
}
