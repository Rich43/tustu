package sun.corba;

import com.sun.corba.se.impl.io.ValueUtility;
import java.lang.reflect.Method;
import sun.misc.JavaOISAccess;
import sun.misc.Unsafe;

/* loaded from: rt.jar:sun/corba/SharedSecrets.class */
public class SharedSecrets {
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static JavaCorbaAccess javaCorbaAccess;
    private static final Method getJavaOISAccessMethod;
    private static JavaOISAccess javaOISAccess;

    static {
        try {
            getJavaOISAccessMethod = Class.forName("sun.misc.SharedSecrets").getMethod("getJavaOISAccess", new Class[0]);
        } catch (Exception e2) {
            throw new ExceptionInInitializerError(e2);
        }
    }

    public static JavaOISAccess getJavaOISAccess() {
        if (javaOISAccess == null) {
            try {
                javaOISAccess = (JavaOISAccess) getJavaOISAccessMethod.invoke(null, new Object[0]);
            } catch (Exception e2) {
                throw new ExceptionInInitializerError(e2);
            }
        }
        return javaOISAccess;
    }

    public static JavaCorbaAccess getJavaCorbaAccess() {
        if (javaCorbaAccess == null) {
            unsafe.ensureClassInitialized(ValueUtility.class);
        }
        return javaCorbaAccess;
    }

    public static void setJavaCorbaAccess(JavaCorbaAccess javaCorbaAccess2) {
        javaCorbaAccess = javaCorbaAccess2;
    }
}
