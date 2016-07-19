package example.datahelper;

import org.junit.Assert;
import org.junit.internal.ExactComparisonCriteria;

/**
 * Created by jackeylv on 2016/7/13.
 */
public class ObjectEquals {

    public static void assertEquals(Object object1, Object object2){
        // null conditions
        if (null == object1 && null == object2)
            return;
        if (null == object1 || null == object2)
            Assert.fail("object1 = " + object1 + ", object2 = " + object2);

        // primitive type conditions
        if (isPrimitive(object1) || isPrimitive(object2)){
            Assert.assertEquals(object1, object2);
        }

        // array conditions
        if (isArray(object1) || isArray(object2)) {
            new ExactComparisonCriteria().arrayEquals(null, object1, object2);
            return;
        }

        // default condition 如果Collection中元素是数组，将会失败
        Assert.assertEquals(object1, object2);
    }

    private static boolean isPrimitive(Object object) {
        return object != null && object.getClass().isPrimitive();
    }

    public static boolean isArray(Object object){
        return object!=null && object.getClass().isArray();
    }
}
