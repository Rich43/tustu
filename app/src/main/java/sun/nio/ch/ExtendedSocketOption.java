package sun.nio.ch;

import java.net.SocketOption;

/* loaded from: rt.jar:sun/nio/ch/ExtendedSocketOption.class */
class ExtendedSocketOption {
    static final SocketOption<Boolean> SO_OOBINLINE = new SocketOption<Boolean>() { // from class: sun.nio.ch.ExtendedSocketOption.1
        @Override // java.net.SocketOption
        public String name() {
            return "SO_OOBINLINE";
        }

        @Override // java.net.SocketOption
        public Class<Boolean> type() {
            return Boolean.class;
        }

        public String toString() {
            return name();
        }
    };

    private ExtendedSocketOption() {
    }
}
