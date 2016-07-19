package example.datahelper;

import data.media.GenMediaContent;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by jackeylv on 2016/6/24.
 */
public class DataHelper {

    public enum DataType{
        MEDIA_CONTENT_1,
        MEDIA_CONTENT_2,
        NULL_TYPE,
        INT_TYPE,
        INT_ARRAY,
        INT_RANDOM_ARRAY,
        DOUBLE_TYPE,
        DOUBLE_ARRAY,
        BYTE_TYPE,
        BYTE_ARRAY,
        STRING_TYPE,
        STRING_ARRAY,
        MAP_TYPE,
        LIST_TYPE,
        LIST_NULL_TYPE, // null elements are not supported in protostuff, assertion error when checking after unmarshall
        SET_TYPE,
        SET_NULL_TYPE,
        BIG_DECIMAL,
        CHAR_ARRAY,
        JAVA_UTIL_DATE,

    }
    private static final int array_size = 256;
    private static int[] fixed_long_intArray = new int[array_size];
    static {
        Random rnd = new Random(3721);
        for (int i = 0; i < fixed_long_intArray.length; i++) {
            fixed_long_intArray[i] = rnd.nextInt();
        }
    }

    public static ArrayList<Object> buildData(int num, DataType type) {
        ArrayList<Object> data = null;
        int size = array_size;
        for (int i = 0; i < num; i++) {
            Object o = null;
            switch (type) {
                case MEDIA_CONTENT_1:
                    o = GenMediaContent.build(1);
                    break;
                case MEDIA_CONTENT_2:
                    o = GenMediaContent.build(2);
                    break;
                case NULL_TYPE:
                    o = null;
                    break;
                case INT_TYPE:
                    o = new Integer(1);
                    break;
                case INT_ARRAY: {
                    int[] arr = new int[size];
                    for (int j = 0; j < size; j++)
                        arr[j] = j;
                    o = arr;
                    break;
                }
                case INT_RANDOM_ARRAY:{
                    o = Arrays.copyOf(fixed_long_intArray, fixed_long_intArray.length);
                    break;
                }
                case DOUBLE_TYPE:
                    o = new Double(Math.E);
                    break;
                case DOUBLE_ARRAY: {
                    double[] arr = new double[size];
                    for (int j = 0; j < size; j++)
                        arr[j] = Math.PI;
                    o =arr;
                    break;
                }
                case BYTE_TYPE:
                    o = (byte)1;
                    break;
                case BYTE_ARRAY: {
                    byte[] arr = new byte[size];
                    for (int j = 0; j < size; j++) {
                        arr[j] = (byte)(j%Byte.MAX_VALUE);
                    }
                    o = arr;
                    break;
                }
                case STRING_TYPE:
                    o = new String("hello kitty.");
                    break;
                case STRING_ARRAY: {
                    String[] arr = new String[size];
                    for (int j = 0; j < size; j++) {
                        arr[j] = new String("A quick fox jump over the lazy dog.");
                    }
                    o = arr;
                    break;
                }
                case MAP_TYPE: {
                    Map<String, String> map = new HashMap<>();
                    String value = "value.oooo";
                    for (int j = 0; j < size; j++) {
                        map.put("key" + j, value);
                    }
                    o = map;
                    break;
                }
                case LIST_TYPE: {
                    List<String> lst = new LinkedList<>();
                    for (int j = 0; j < size; j++) {
                        lst.add("key-" + j + "-key");
                    }
                    o = lst;
                    break;
                }
                case LIST_NULL_TYPE: {
                    List lst = new LinkedList();
                    lst.add("Hello");
                    lst.add("123");
                    lst.add(null);
                    lst.add("end");
                    o = lst;
                    break;
                }
                case SET_TYPE:
                    Set<String> stringSet = new HashSet<>();
                    for (int j = 0; j < size; j++) {
                        stringSet.add("value"+j);
                    }
                    o = stringSet;
                    break;
                case SET_NULL_TYPE: {
                    Set set = new HashSet();
                    set.add("Hello");
                    set.add(null);
                    set.add(1);
                    set.add("end");
                    break;
                }
                case BIG_DECIMAL: {
                    o = new BigDecimal(
                            new char[]{
                            '1','2','3','4','5','6','7','8','9','0', '1','2','3','4','5','6','7','8','9','0',
                            '1','2','3','4','5','6','7','8','9','0', '1','2','3','4','5','6','7','8','9','0',
                            },
                            0, 40);
                    break;
                }
                case CHAR_ARRAY: {
                    o = new char[]{'1', '2','3','4','5','6'};
                    break;
                }
                case JAVA_UTIL_DATE: {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(2016, 1, 1);
                    o = calendar.getTime();
                    break;
                }
                default:
                    break;
            }
            if (null == data)
                data = new ArrayList<>(num);
            data.add(o);
        }
        return data;
    }

    /**
     * 计算数组中所有元素的大小(bytes)
     * @param objectArrayList
     * @return
     * @throws IllegalAccessException
     */
    public static long CalculateSize(ArrayList<Object> objectArrayList) throws IllegalAccessException {
        long sum = 0;
        ClassIntrospector introspector = new ClassIntrospector();
        for (Object o : objectArrayList){
            if (null != o) {
                ObjectInfo objectInfo = introspector.introspect(o);
//            if (0 == sum)
//                System.err.println(objectInfo);
                sum += objectInfo.getDeepSize();
            }
        }
        return sum;
    }
}
