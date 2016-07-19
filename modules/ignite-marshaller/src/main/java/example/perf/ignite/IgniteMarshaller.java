package example.perf.ignite;

import example.datahelper.DataHelper;
import example.datahelper.ObjectEquals;
import example.perf.util.*;
import org.apache.commons.io.HexDump;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.Ignition;
import org.apache.ignite.binary.BinaryBasicIdMapper;
import org.apache.ignite.binary.BinaryBasicNameMapper;
import org.apache.ignite.configuration.BinaryConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.internal.binary.BinaryMarshaller;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * Created by jackeylv on 2016/6/23.
 */
public class IgniteMarshaller {
    public static void main(String[] args) throws IgniteCheckedException, IllegalAccessException, IOException {
        System.out.println(OptionsHelper.getMainClassAndArgs());
        MarshallerParameters cfg = new MarshallerParameters();
        OptionsHelper.jcommander(args, cfg, "");
        if (cfg.help){
            System.err.println(OptionsHelper.usage(null));
            return;
        }
        System.out.println(cfg);
        System.out.println("Statics result will be store into file "
                + System.getProperty("user.dir") + File.separator + cfg.output_file);
        withIgniteInstance(cfg);
    }

    public static void withIgniteInstance(MarshallerParameters cfg) throws IllegalAccessException, IOException, IgniteCheckedException {
        IgniteConfiguration icfg = new IgniteConfiguration();
        icfg.setIgniteHome(System.getProperty("user.dir"));
        icfg.setWorkDirectory(System.getProperty("user.dir"));

        BinaryConfiguration bCfg = new BinaryConfiguration();
        bCfg.setCompactFooter(true);
        bCfg.setNameMapper(new BinaryBasicNameMapper(false));
        bCfg.setIdMapper(new BinaryBasicIdMapper(false));

        icfg.setMarshaller(new BinaryMarshaller());
        icfg.setBinaryConfiguration(bCfg);
        try (Ignite ignite = Ignition.start(icfg)) {
            BinaryMarshaller marshaller = (BinaryMarshaller) icfg.getMarshaller();

            PrintStream output = cfg.output();
            output.println("Case,InputSize(bytes),AvgConvertingSpeed," +
                    "AvgConvertingSpeed(bps),time_cost(ms),AvgCompressedSize(bytes)");
            for (DataHelper.DataType v : DataHelper.DataType.values()){
                int num_obj = cfg.num;

                System.out.println("Processing Data type " + v.name());
                ArrayList<Object> objectArrayList = DataHelper.buildData(num_obj, v);
                long bytes = DataHelper.CalculateSize(objectArrayList);
                Summary input_summary = new Summary(bytes, num_obj);

                ArrayList<Long> compressed_size = new ArrayList<>(num_obj);

                TimeRecord tr = new TimeRecord();
                tr.reset();
                for (int i = 0; i < num_obj; i++) {
                    marshall(marshaller, objectArrayList.get(i), compressed_size, cfg.unmarshall, cfg.dump&&i==0);
                }
                long time_cost = tr.end();
                Summary summary_compressed = Statics.summary(compressed_size);

                output.println(v.name() + "," + input_summary.avg()
                        +"," + Format.speed(bytes, time_cost)
                        + "," + (time_cost >0 ? 1000*bytes/time_cost : Long.MAX_VALUE ) +"," + time_cost
                        + "," + summary_compressed.avg()
                );
                objectArrayList.clear();
            }
        }
    }

    public static void marshall(BinaryMarshaller binaryMarshaller,
                                Object object,
                                ArrayList<Long> compressed_size,
                                boolean unmarshall,
                                boolean debug) throws IOException, IgniteCheckedException {
            byte[] bytes = binaryMarshaller.marshal(object);
            if (debug && bytes.length>0) {
                HexDump.dump(bytes, 0, System.err, 0);
                System.err.println("bytes: " + bytes.length);
            }
            compressed_size.add((long) bytes.length);

            if (unmarshall){
                Object back = binaryMarshaller.unmarshal(bytes, null);
                ObjectEquals.assertEquals(object, back);
                System.out.println(object);
                System.out.println(back);
            }
    }

}
