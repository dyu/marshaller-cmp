package example.perf.protostuff;

/**
 * Created by jackeylv on 2016/7/13.
 */
public class Foo {
    private Object object;

    public Foo(){
        object = null;
    }

    public Foo(Object object){
        this.object = object;
    }

    public Object getObject(){
        return object;
    }

    public void setObject(Object object){
        this.object = object;
    }
}
