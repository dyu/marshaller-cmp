package example.perf.protostuff;


import example.datahelper.ObjectEquals;
import example.perf.util.*;
import example.datahelper.DataHelper;
import io.protostuff.*;
import io.protostuff.runtime.DefaultIdStrategy;
import io.protostuff.runtime.Delegate;
import io.protostuff.runtime.IdStrategy;
import io.protostuff.runtime.RuntimeSchema;
import org.apache.commons.io.HexDump;

import java.io.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class ProtoStuffMarshaller {
    static final int BUFFER_SIZE = Integer.getInteger("protostuff.buffer_size", 
            LinkedBuffer.DEFAULT_BUFFER_SIZE);
    
    public static void main(String[] args) throws IOException, IllegalAccessException {
        MarshallerParameters cfg = new MarshallerParameters();
        OptionsHelper.jcommander(args, cfg, "");
        if (cfg.help){
            System.err.println(OptionsHelper.usage(null));
            return;
        }
        System.out.println(cfg);
        System.out.println("Statics result will be store into file "
                + System.getProperty("user.dir") + File.separator + cfg.output_file);

        DefaultIdStrategy idStrategy = new DefaultIdStrategy();
        idStrategy.registerDelegate(DATE_DELEGATE);

        RunWithProtoStuff(cfg, idStrategy);
    }

    private static void RunWithProtoStuff(MarshallerParameters cfg, IdStrategy idStrategy) throws IllegalAccessException, IOException {
        PrintStream output = cfg.output();
        output.println("Case,InputSize(bytes),AvgConvertingSpeed," +
                "AvgConvertingSpeed(bps),time_cost(ms),AvgCompressedSize(bytes)");
        for (DataHelper.DataType v : DataHelper.DataType.values()){
            int n = cfg.num;
            System.out.println("Processing data type " + v.name());
            ArrayList<Object> lst = DataHelper.buildData(n, v);
            long bytes = DataHelper.CalculateSize(lst);
            Summary input_summary = new Summary(bytes, n);
            ArrayList<Long> compressed_size = new ArrayList<>(n);
            TimeRecord tr = new TimeRecord();
            tr.reset();
            marshal(lst, compressed_size, cfg.unmarshall, cfg.dump, idStrategy);
            long time_cost = tr.end();
            Summary summary_compressed = Statics.summary(compressed_size);

            output.println(v.name() + "," + input_summary.avg()
                    +"," + Format.speed(bytes, time_cost)
                    + "," + (time_cost >0 ? 1000*bytes/time_cost : Long.MAX_VALUE ) +"," + time_cost
                    + "," + summary_compressed.avg()
            );
        }
    }

    private static void marshal(ArrayList<Object> objectArrayList,
                                ArrayList<Long> compressed_size,
                                boolean unmarshall, boolean dump, IdStrategy idStrategy) throws IOException {
        int num = 0;
        Schema<Foo> schema = RuntimeSchema.getSchema(Foo.class, idStrategy);
        final LinkedBuffer buffer = LinkedBuffer.allocate(BUFFER_SIZE);
        byte[] bytes;
        for (Object o: objectArrayList) {
            Foo foo = new Foo(o);
            bytes = ProtostuffIOUtil.toByteArray(foo, schema, buffer.clear());
            //byte[] compressed = compressDeflate(bytes);
            //bytes = compressed;

            compressed_size.add((long) bytes.length);
            if (dump && num == 0 && bytes.length>0) {
                HexDump.dump(bytes, 0, System.err, 0);
                System.err.println("Bytes: " + bytes.length);
            }

            num++;

            if (unmarshall){
                //byte[] deCompressed = deCompressDeflate(bytes);
                //bytes = deCompressed;
                Foo ignore = schema.newMessage();
                ProtostuffIOUtil.mergeFrom(bytes, ignore, schema);

                ObjectEquals.assertEquals(foo.getObject(), ((Foo) ignore).getObject());
                System.out.println(foo.getObject());
                System.out.println(((Foo) ignore).getObject());
            }
        }
    }



    public static final Delegate<java.sql.Date> DATE_DELEGATE = new Delegate<Date>() {
        @Override
        public WireFormat.FieldType getFieldType() {
            return WireFormat.FieldType.FIXED64;
        }

        @Override
        public Date readFrom(Input input) throws IOException {
            return new Date(input.readFixed64());
        }

        @Override
        public void writeTo(Output output, int number, Date value, boolean repeated) throws IOException {
            output.writeFixed64(number, value.getTime(), repeated);
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number, boolean repeated) throws IOException {
            output.writeFixed64(number, input.readFixed64(), repeated);
        }

        @Override
        public Class<?> typeClass() {
            return java.sql.Date.class;
        }
    };

    protected static byte[] compressDeflate(byte[] data)
    {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream(500);
            DeflaterOutputStream compresser = new DeflaterOutputStream(bout);
            compresser.write(data, 0, data.length);
            compresser.finish();
            compresser.flush();
            return bout.toByteArray();
        }
        catch (IOException ex) {
            AssertionError ae = new AssertionError("IOException while writing to ByteArrayOutputStream!");
            ae.initCause(ex);
            throw ae;
        }
    }


    private static byte[] deCompressDeflate(byte[] in) {
        ByteArrayInputStream stream = new ByteArrayInputStream(in);
        InflaterInputStream zip = new InflaterInputStream(stream);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte b[] = new byte[4092];
        try {
            int n;
            while ((n = zip.read(b)) >= 0) {
                out.write(b, 0, n);
            }
            zip.close();
            out.close();
            return out.toByteArray();
        }
        catch (Exception e) {
            AssertionError ae = new AssertionError("IOException while writing to ByteArrayInputStream!");
            ae.initCause(e);
            throw ae;
        }
    }
}