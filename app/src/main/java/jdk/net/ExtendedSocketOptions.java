package jdk.net;

import java.net.SocketOption;
import jdk.Exported;

@Exported
/* loaded from: rt.jar:jdk/net/ExtendedSocketOptions.class */
public final class ExtendedSocketOptions {
    public static final SocketOption<SocketFlow> SO_FLOW_SLA = new ExtSocketOption("SO_FLOW_SLA", SocketFlow.class);
    public static final SocketOption<Integer> TCP_KEEPIDLE = new ExtSocketOption("TCP_KEEPIDLE", Integer.class);
    public static final SocketOption<Integer> TCP_KEEPINTERVAL = new ExtSocketOption("TCP_KEEPINTERVAL", Integer.class);
    public static final SocketOption<Integer> TCP_KEEPCOUNT = new ExtSocketOption("TCP_KEEPCOUNT", Integer.class);

    /* loaded from: rt.jar:jdk/net/ExtendedSocketOptions$ExtSocketOption.class */
    private static class ExtSocketOption<T> implements SocketOption<T> {
        private final String name;
        private final Class<T> type;

        ExtSocketOption(String str, Class<T> cls) {
            this.name = str;
            this.type = cls;
        }

        @Override // java.net.SocketOption
        public String name() {
            return this.name;
        }

        @Override // java.net.SocketOption
        public Class<T> type() {
            return this.type;
        }

        public String toString() {
            return this.name;
        }
    }

    private ExtendedSocketOptions() {
    }
}
