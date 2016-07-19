package example.datahelper;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by jackeylv on 2016/6/29.
 */
public class ObjectSizeDetector {
    public static void main(String[] args) throws IOException {
        DetectorOptions options = options_parser(args);
        if (null == options){
            System.err.println(usage());
            return;
        }
        LinkedList<String> lst_classes = get_classes(options);
        PrintStream out = new PrintStream(new FileOutputStream(options.result_file));
        detect(lst_classes, out);
    }

    private static void detect(LinkedList<String> lst_classes, PrintStream out) {
        LinkedList<String> lst_not_found = new LinkedList<>();
        LinkedList<String> lst_failed_instance = new LinkedList<>();
        ClassIntrospector introspector = new ClassIntrospector();
        for (String clazz :
                lst_classes) {
            try {
                System.err.println("Detecting "+clazz);
                Class t = Class.forName(clazz);
                ObjectInfo objectInfo = introspector.introspect(t.newInstance());
                out.println(String.format("%s:%d", clazz, objectInfo.getDeepSize()));
            } catch (ClassNotFoundException e) {
                //e.printStackTrace();
                lst_not_found.add(clazz);
            } catch (InstantiationException e) {
                //e.printStackTrace();
                lst_failed_instance.add(clazz);
            } catch (IllegalAccessException e) {
                //e.printStackTrace();
                lst_failed_instance.add(clazz);
            }
        }
        if (!lst_not_found.isEmpty()){
            out.println();
            out.println("--------------------------");
            out.println("Classes not found:");
            for (String clazz: lst_not_found)
                out.println(clazz);
        }

        if (!lst_failed_instance.isEmpty()){
            out.println();
            out.println("--------------------------");
            out.println("Classes failed to instantiation:");
            for (String clazz : lst_failed_instance)
                out.println(clazz);
        }
    }

    private static LinkedList<String> get_classes(DetectorOptions options) throws IOException {
        assert null != options;
        LinkedList<String> lst_classes = new LinkedList<>();
        if (null != options.class_names && options.class_names.length() > 0){
            String[] classes = options.class_names.split(";");
            lst_classes.addAll(Arrays.asList(classes));
        }
        if (null != options.class_files){
            try (BufferedReader br = new BufferedReader(new FileReader(options.class_files))){
                for (String line; (line = br.readLine())!=null; ){
                    line = line.trim();
                    if (!line.isEmpty()) {
                        String[] classes = line.split(";");
                        lst_classes.addAll(Arrays.asList(classes));
                    }
                }
            }
        }
        return lst_classes;
    }

    private static String usage() {
        DetectorOptions cp = new DetectorOptions();

        JCommander jCommander = new JCommander();

        jCommander.setAcceptUnknownOptions(true);
        jCommander.addObject(cp);

        StringBuilder sb = new StringBuilder();

        jCommander.usage(sb);

        return sb.toString();
    }

    private static DetectorOptions options_parser(String[] args) {
        JCommander jCommander = new JCommander();
        DetectorOptions options = new DetectorOptions();

        jCommander.setAcceptUnknownOptions(true);
        jCommander.setProgramName(ObjectSizeDetector.class.getSimpleName());
        jCommander.addObject(options);

        jCommander.parse(args);
        return options;
    }

    private static class DetectorOptions {
        @Parameter(names = "-f", description =
                "text file with class names, separated by semicolons or carriage returns.")
        public String class_files = null;

        @Parameter(names = "-c", description = "class names separated by semicolons.")
        public String class_names = null;

        @Parameter(names = "-o", description = "Result output file.")
        public String result_file = "class_size.txt";

        @Override
        public String toString() {
            return "DetectorOptions{" +
                    "class_files='" + class_files + '\'' +
                    ", class_names='" + class_names + '\'' +
                    ", result_file='" + result_file + '\'' +
                    '}';
        }
    }
}
