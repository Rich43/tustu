package java.net;

import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:java/net/DefaultDatagramSocketImplFactory.class */
class DefaultDatagramSocketImplFactory {
    private static final Class<?> prefixImplClass;
    private static float version;
    private static boolean preferIPv4Stack = false;
    private static final boolean useDualStackImpl;
    private static String exclBindProp;
    private static final boolean exclusiveBind;

    DefaultDatagramSocketImplFactory() {
    }

    static {
        Class<?> cls = null;
        boolean z2 = false;
        boolean z3 = true;
        AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: java.net.DefaultDatagramSocketImplFactory.1
            static final /* synthetic */ boolean $assertionsDisabled;

            static {
                $assertionsDisabled = !DefaultDatagramSocketImplFactory.class.desiredAssertionStatus();
            }

            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                float unused = DefaultDatagramSocketImplFactory.version = 0.0f;
                try {
                    float unused2 = DefaultDatagramSocketImplFactory.version = Float.parseFloat(System.getProperties().getProperty("os.version"));
                    boolean unused3 = DefaultDatagramSocketImplFactory.preferIPv4Stack = Boolean.parseBoolean(System.getProperties().getProperty("java.net.preferIPv4Stack"));
                    String unused4 = DefaultDatagramSocketImplFactory.exclBindProp = System.getProperty("sun.net.useExclusiveBind");
                    return null;
                } catch (NumberFormatException e2) {
                    if ($assertionsDisabled) {
                        return null;
                    }
                    throw new AssertionError(e2);
                }
            }
        });
        if (version >= 6.0d && !preferIPv4Stack) {
            z2 = true;
        }
        if (exclBindProp != null) {
            z3 = exclBindProp.length() == 0 ? true : Boolean.parseBoolean(exclBindProp);
        } else if (version < 6.0d) {
            z3 = false;
        }
        String str = null;
        try {
            str = (String) AccessController.doPrivileged(new GetPropertyAction("impl.prefix", null));
            if (str != null) {
                cls = Class.forName("java.net." + str + "DatagramSocketImpl");
            }
        } catch (Exception e2) {
            System.err.println("Can't find class: java.net." + str + "DatagramSocketImpl: check impl.prefix property");
        }
        prefixImplClass = cls;
        useDualStackImpl = z2;
        exclusiveBind = z3;
    }

    static DatagramSocketImpl createDatagramSocketImpl(boolean z2) throws SocketException {
        if (prefixImplClass != null) {
            try {
                return (DatagramSocketImpl) prefixImplClass.newInstance();
            } catch (Exception e2) {
                throw new SocketException("can't instantiate DatagramSocketImpl");
            }
        }
        if (useDualStackImpl && !z2) {
            return new DualStackPlainDatagramSocketImpl(exclusiveBind);
        }
        return new TwoStacksPlainDatagramSocketImpl(exclusiveBind && !z2);
    }
}
