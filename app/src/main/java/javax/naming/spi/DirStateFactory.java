package javax.naming.spi;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;

/* loaded from: rt.jar:javax/naming/spi/DirStateFactory.class */
public interface DirStateFactory extends StateFactory {
    Result getStateToBind(Object obj, Name name, Context context, Hashtable<?, ?> hashtable, Attributes attributes) throws NamingException;

    /* loaded from: rt.jar:javax/naming/spi/DirStateFactory$Result.class */
    public static class Result {
        private Object obj;
        private Attributes attrs;

        public Result(Object obj, Attributes attributes) {
            this.obj = obj;
            this.attrs = attributes;
        }

        public Object getObject() {
            return this.obj;
        }

        public Attributes getAttributes() {
            return this.attrs;
        }
    }
}
