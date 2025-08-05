package sun.nio.ch;

import java.net.ProtocolFamily;
import java.net.SocketOption;
import java.net.SocketOptions;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.util.HashMap;
import java.util.Map;

/* loaded from: rt.jar:sun/nio/ch/SocketOptionRegistry.class */
class SocketOptionRegistry {
    private SocketOptionRegistry() {
    }

    /* loaded from: rt.jar:sun/nio/ch/SocketOptionRegistry$RegistryKey.class */
    private static class RegistryKey {
        private final SocketOption<?> name;
        private final ProtocolFamily family;

        RegistryKey(SocketOption<?> socketOption, ProtocolFamily protocolFamily) {
            this.name = socketOption;
            this.family = protocolFamily;
        }

        public int hashCode() {
            return this.name.hashCode() + this.family.hashCode();
        }

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof RegistryKey)) {
                return false;
            }
            RegistryKey registryKey = (RegistryKey) obj;
            return this.name == registryKey.name && this.family == registryKey.family;
        }
    }

    /* loaded from: rt.jar:sun/nio/ch/SocketOptionRegistry$LazyInitialization.class */
    private static class LazyInitialization {
        static final Map<RegistryKey, OptionKey> options = options();

        private LazyInitialization() {
        }

        private static Map<RegistryKey, OptionKey> options() {
            HashMap map = new HashMap();
            map.put(new RegistryKey(StandardSocketOptions.SO_BROADCAST, Net.UNSPEC), new OptionKey(65535, 32));
            map.put(new RegistryKey(StandardSocketOptions.SO_KEEPALIVE, Net.UNSPEC), new OptionKey(65535, 8));
            map.put(new RegistryKey(StandardSocketOptions.SO_LINGER, Net.UNSPEC), new OptionKey(65535, 128));
            map.put(new RegistryKey(StandardSocketOptions.SO_SNDBUF, Net.UNSPEC), new OptionKey(65535, 4097));
            map.put(new RegistryKey(StandardSocketOptions.SO_RCVBUF, Net.UNSPEC), new OptionKey(65535, SocketOptions.SO_RCVBUF));
            map.put(new RegistryKey(StandardSocketOptions.SO_REUSEADDR, Net.UNSPEC), new OptionKey(65535, 4));
            map.put(new RegistryKey(StandardSocketOptions.TCP_NODELAY, Net.UNSPEC), new OptionKey(6, 1));
            map.put(new RegistryKey(StandardSocketOptions.IP_TOS, StandardProtocolFamily.INET), new OptionKey(0, 3));
            map.put(new RegistryKey(StandardSocketOptions.IP_MULTICAST_IF, StandardProtocolFamily.INET), new OptionKey(0, 9));
            map.put(new RegistryKey(StandardSocketOptions.IP_MULTICAST_TTL, StandardProtocolFamily.INET), new OptionKey(0, 10));
            map.put(new RegistryKey(StandardSocketOptions.IP_MULTICAST_LOOP, StandardProtocolFamily.INET), new OptionKey(0, 11));
            map.put(new RegistryKey(StandardSocketOptions.IP_TOS, StandardProtocolFamily.INET6), new OptionKey(41, 39));
            map.put(new RegistryKey(StandardSocketOptions.IP_MULTICAST_IF, StandardProtocolFamily.INET6), new OptionKey(41, 9));
            map.put(new RegistryKey(StandardSocketOptions.IP_MULTICAST_TTL, StandardProtocolFamily.INET6), new OptionKey(41, 10));
            map.put(new RegistryKey(StandardSocketOptions.IP_MULTICAST_LOOP, StandardProtocolFamily.INET6), new OptionKey(41, 11));
            map.put(new RegistryKey(ExtendedSocketOption.SO_OOBINLINE, Net.UNSPEC), new OptionKey(65535, 256));
            return map;
        }
    }

    public static OptionKey findOption(SocketOption<?> socketOption, ProtocolFamily protocolFamily) {
        return LazyInitialization.options.get(new RegistryKey(socketOption, protocolFamily));
    }
}
