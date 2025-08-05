package com.sun.nio.sctp;

import java.net.SocketAddress;
import jdk.Exported;
import sun.nio.ch.sctp.SctpStdSocketOption;

@Exported
/* loaded from: rt.jar:com/sun/nio/sctp/SctpStandardSocketOptions.class */
public class SctpStandardSocketOptions {
    public static final SctpSocketOption<Boolean> SCTP_DISABLE_FRAGMENTS = new SctpStdSocketOption("SCTP_DISABLE_FRAGMENTS", Boolean.class, 1);
    public static final SctpSocketOption<Boolean> SCTP_EXPLICIT_COMPLETE = new SctpStdSocketOption("SCTP_EXPLICIT_COMPLETE", Boolean.class, 2);
    public static final SctpSocketOption<Integer> SCTP_FRAGMENT_INTERLEAVE = new SctpStdSocketOption("SCTP_FRAGMENT_INTERLEAVE", Integer.class, 3);
    public static final SctpSocketOption<InitMaxStreams> SCTP_INIT_MAXSTREAMS = new SctpStdSocketOption("SCTP_INIT_MAXSTREAMS", InitMaxStreams.class);
    public static final SctpSocketOption<Boolean> SCTP_NODELAY = new SctpStdSocketOption("SCTP_NODELAY", Boolean.class, 4);
    public static final SctpSocketOption<SocketAddress> SCTP_PRIMARY_ADDR = new SctpStdSocketOption("SCTP_PRIMARY_ADDR", SocketAddress.class);
    public static final SctpSocketOption<SocketAddress> SCTP_SET_PEER_PRIMARY_ADDR = new SctpStdSocketOption("SCTP_SET_PEER_PRIMARY_ADDR", SocketAddress.class);
    public static final SctpSocketOption<Integer> SO_SNDBUF = new SctpStdSocketOption("SO_SNDBUF", Integer.class, 5);
    public static final SctpSocketOption<Integer> SO_RCVBUF = new SctpStdSocketOption("SO_RCVBUF", Integer.class, 6);
    public static final SctpSocketOption<Integer> SO_LINGER = new SctpStdSocketOption("SO_LINGER", Integer.class, 7);

    private SctpStandardSocketOptions() {
    }

    @Exported
    /* loaded from: rt.jar:com/sun/nio/sctp/SctpStandardSocketOptions$InitMaxStreams.class */
    public static class InitMaxStreams {
        private int maxInStreams;
        private int maxOutStreams;

        private InitMaxStreams(int i2, int i3) {
            this.maxInStreams = i2;
            this.maxOutStreams = i3;
        }

        public static InitMaxStreams create(int i2, int i3) {
            if (i3 < 0 || i3 > 65535) {
                throw new IllegalArgumentException("Invalid maxOutStreams value");
            }
            if (i2 < 0 || i2 > 65535) {
                throw new IllegalArgumentException("Invalid maxInStreams value");
            }
            return new InitMaxStreams(i2, i3);
        }

        public int maxInStreams() {
            return this.maxInStreams;
        }

        public int maxOutStreams() {
            return this.maxOutStreams;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(super.toString()).append(" [");
            sb.append("maxInStreams:").append(this.maxInStreams);
            sb.append("maxOutStreams:").append(this.maxOutStreams).append("]");
            return sb.toString();
        }

        public boolean equals(Object obj) {
            if (obj != null && (obj instanceof InitMaxStreams)) {
                InitMaxStreams initMaxStreams = (InitMaxStreams) obj;
                if (this.maxInStreams == initMaxStreams.maxInStreams && this.maxOutStreams == initMaxStreams.maxOutStreams) {
                    return true;
                }
                return false;
            }
            return false;
        }

        public int hashCode() {
            return (7 ^ this.maxInStreams) ^ this.maxOutStreams;
        }
    }
}
