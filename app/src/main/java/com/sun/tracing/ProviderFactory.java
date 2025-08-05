package com.sun.tracing;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.HashSet;
import sun.security.action.GetPropertyAction;
import sun.tracing.MultiplexProviderFactory;
import sun.tracing.NullProviderFactory;
import sun.tracing.PrintStreamProviderFactory;
import sun.tracing.dtrace.DTraceProviderFactory;

/* loaded from: rt.jar:com/sun/tracing/ProviderFactory.class */
public abstract class ProviderFactory {
    public abstract <T extends Provider> T createProvider(Class<T> cls);

    protected ProviderFactory() {
    }

    public static ProviderFactory getDefaultFactory() {
        HashSet hashSet = new HashSet();
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("com.sun.tracing.dtrace"));
        if ((str == null || !str.equals("disable")) && DTraceProviderFactory.isSupported()) {
            hashSet.add(new DTraceProviderFactory());
        }
        String str2 = (String) AccessController.doPrivileged(new GetPropertyAction("sun.tracing.stream"));
        if (str2 != null) {
            for (String str3 : str2.split(",")) {
                PrintStream printStreamFromSpec = getPrintStreamFromSpec(str3);
                if (printStreamFromSpec != null) {
                    hashSet.add(new PrintStreamProviderFactory(printStreamFromSpec));
                }
            }
        }
        if (hashSet.size() == 0) {
            return new NullProviderFactory();
        }
        if (hashSet.size() == 1) {
            return ((ProviderFactory[]) hashSet.toArray(new ProviderFactory[1]))[0];
        }
        return new MultiplexProviderFactory(hashSet);
    }

    private static PrintStream getPrintStreamFromSpec(final String str) {
        try {
            final int iLastIndexOf = str.lastIndexOf(46);
            final Class<?> cls = Class.forName(str.substring(0, iLastIndexOf));
            return (PrintStream) ((Field) AccessController.doPrivileged(new PrivilegedExceptionAction<Field>() { // from class: com.sun.tracing.ProviderFactory.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Field run() throws NoSuchFieldException {
                    return cls.getField(str.substring(iLastIndexOf + 1));
                }
            })).get(null);
        } catch (ClassNotFoundException e2) {
            throw new AssertionError(e2);
        } catch (IllegalAccessException e3) {
            throw new AssertionError(e3);
        } catch (PrivilegedActionException e4) {
            throw new AssertionError(e4);
        }
    }
}
