package sun.net;

import java.net.SocketOption;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import jdk.net.ExtendedSocketOptions;

/* loaded from: rt.jar:sun/net/ExtendedOptionsHelper.class */
public class ExtendedOptionsHelper {
    private static final boolean keepAliveOptSupported = ExtendedOptionsImpl.keepAliveOptionsSupported();
    private static final Set<SocketOption<?>> extendedOptions = options();

    private static Set<SocketOption<?>> options() {
        HashSet hashSet = new HashSet();
        if (keepAliveOptSupported) {
            hashSet.add(ExtendedSocketOptions.TCP_KEEPCOUNT);
            hashSet.add(ExtendedSocketOptions.TCP_KEEPIDLE);
            hashSet.add(ExtendedSocketOptions.TCP_KEEPINTERVAL);
        }
        return Collections.unmodifiableSet(hashSet);
    }

    public static Set<SocketOption<?>> keepAliveOptions() {
        return extendedOptions;
    }
}
