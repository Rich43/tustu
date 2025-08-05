package com.sun.jndi.dns;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ProtocolFamily;
import java.net.SocketException;
import java.nio.channels.DatagramChannel;
import java.security.AccessController;
import java.util.Objects;
import java.util.Random;
import sun.net.PortConfig;

/* loaded from: rt.jar:com/sun/jndi/dns/DNSDatagramSocketFactory.class */
class DNSDatagramSocketFactory {
    static final int DEVIATION = 3;
    static final int THRESHOLD = 6;
    static final int BIT_DEVIATION = 2;
    static final int HISTORY = 32;
    static final int MAX_RANDOM_TRIES = 5;
    int lastport;
    int lastSystemAllocated;
    int suitablePortCount;
    int unsuitablePortCount;
    final ProtocolFamily family;
    final int thresholdCount;
    final int deviation;
    final Random random;
    final PortHistory history;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DNSDatagramSocketFactory.class.desiredAssertionStatus();
    }

    /* loaded from: rt.jar:com/sun/jndi/dns/DNSDatagramSocketFactory$EphemeralPortRange.class */
    static final class EphemeralPortRange {
        static final int LOWER = PortConfig.getLower();
        static final int UPPER = PortConfig.getUpper();
        static final int RANGE = (UPPER - LOWER) + 1;

        private EphemeralPortRange() {
        }
    }

    private static int findFirstFreePort() {
        int localPort;
        DatagramSocket datagramSocket;
        Throwable th;
        try {
            datagramSocket = (DatagramSocket) AccessController.doPrivileged(() -> {
                return new DatagramSocket(0);
            });
            th = null;
        } catch (Exception e2) {
            localPort = 0;
        }
        try {
            try {
                localPort = datagramSocket.getLocalPort();
                if (datagramSocket != null) {
                    if (0 != 0) {
                        try {
                            datagramSocket.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        datagramSocket.close();
                    }
                }
                return localPort;
            } finally {
            }
        } finally {
        }
    }

    /* loaded from: rt.jar:com/sun/jndi/dns/DNSDatagramSocketFactory$PortHistory.class */
    static final class PortHistory {
        final int capacity;
        final int[] ports;
        final Random random;
        int index;

        PortHistory(int i2, Random random) {
            this.random = random;
            this.capacity = i2;
            this.ports = new int[i2];
        }

        public boolean contains(int i2) {
            int i3 = 0;
            for (int i4 = 0; i4 < this.capacity; i4++) {
                int i5 = this.ports[i4];
                i3 = i5;
                if (i5 == 0 || i3 == i2) {
                    break;
                }
            }
            return i3 == i2;
        }

        public boolean add(int i2) {
            if (this.ports[this.index] != 0) {
                int iNextInt = this.random.nextInt(this.capacity);
                if ((iNextInt + 1) % this.capacity == this.index) {
                    iNextInt = this.index;
                }
                int[] iArr = this.ports;
                int i3 = iNextInt;
                this.index = i3;
                iArr[i3] = i2;
            } else {
                this.ports[this.index] = i2;
            }
            int i4 = this.index + 1;
            this.index = i4;
            if (i4 == this.capacity) {
                this.index = 0;
                return true;
            }
            return true;
        }

        public boolean offer(int i2) {
            if (contains(i2)) {
                return false;
            }
            return add(i2);
        }
    }

    DNSDatagramSocketFactory() {
        this(new Random());
    }

    DNSDatagramSocketFactory(Random random) {
        this((Random) Objects.requireNonNull(random), null, 3, 6);
    }

    DNSDatagramSocketFactory(Random random, ProtocolFamily protocolFamily, int i2, int i3) {
        this.lastport = findFirstFreePort();
        this.lastSystemAllocated = this.lastport;
        this.random = (Random) Objects.requireNonNull(random);
        this.history = new PortHistory(32, random);
        this.family = protocolFamily;
        this.deviation = Math.max(1, i2);
        this.thresholdCount = Math.max(2, i3);
    }

    public synchronized DatagramSocket open() throws SocketException {
        int i2 = this.lastport;
        if (this.unsuitablePortCount > this.thresholdCount) {
            DatagramSocket datagramSocketOpenRandom = openRandom();
            if (datagramSocketOpenRandom != null) {
                return datagramSocketOpenRandom;
            }
            this.unsuitablePortCount = 0;
            this.suitablePortCount = 0;
            i2 = 0;
        }
        DatagramSocket datagramSocketOpenDefault = openDefault();
        this.lastport = datagramSocketOpenDefault.getLocalPort();
        if (i2 == 0) {
            this.lastSystemAllocated = this.lastport;
            this.history.offer(this.lastport);
            return datagramSocketOpenDefault;
        }
        boolean z2 = this.suitablePortCount > this.thresholdCount;
        boolean zFarEnough = farEnough(i2);
        if (zFarEnough && this.lastSystemAllocated > 0) {
            zFarEnough = farEnough(this.lastSystemAllocated);
        }
        boolean zContains = this.history.contains(this.lastport);
        boolean z3 = z2 || (zFarEnough && !zContains);
        if (z3 && !zContains) {
            this.history.add(this.lastport);
        }
        if (z3) {
            if (!z2) {
                this.suitablePortCount++;
            } else if (!zFarEnough || zContains) {
                this.unsuitablePortCount = 1;
                this.suitablePortCount = this.thresholdCount / 2;
            }
            this.lastSystemAllocated = this.lastport;
            return datagramSocketOpenDefault;
        }
        if (!$assertionsDisabled && z2) {
            throw new AssertionError();
        }
        DatagramSocket datagramSocketOpenRandom2 = openRandom();
        if (datagramSocketOpenRandom2 == null) {
            return datagramSocketOpenDefault;
        }
        this.unsuitablePortCount++;
        datagramSocketOpenDefault.close();
        return datagramSocketOpenRandom2;
    }

    private DatagramSocket openDefault() throws SocketException {
        if (this.family != null) {
            try {
                try {
                    DatagramSocket datagramSocketSocket = DatagramChannel.open(this.family).socket();
                    datagramSocketSocket.bind(null);
                    return datagramSocketSocket;
                } finally {
                }
            } catch (SocketException e2) {
                throw e2;
            } catch (IOException e3) {
                SocketException socketException = new SocketException(e3.getMessage());
                socketException.initCause(e3);
                throw socketException;
            }
        }
        return new DatagramSocket();
    }

    synchronized boolean isUsingNativePortRandomization() {
        return this.unsuitablePortCount <= this.thresholdCount && this.suitablePortCount > this.thresholdCount;
    }

    synchronized boolean isUsingJavaPortRandomization() {
        return this.unsuitablePortCount > this.thresholdCount;
    }

    synchronized boolean isUndecided() {
        return (isUsingJavaPortRandomization() || isUsingNativePortRandomization()) ? false : true;
    }

    private boolean farEnough(int i2) {
        return Integer.bitCount(i2 ^ this.lastport) > 2 && Math.abs(i2 - this.lastport) > this.deviation;
    }

    private DatagramSocket openRandom() {
        int iNextInt;
        boolean zContains;
        boolean z2;
        DatagramSocket datagramSocketSocket;
        int i2 = 5;
        while (true) {
            int i3 = i2;
            i2--;
            if (i3 > 0) {
                int i4 = 5;
                do {
                    iNextInt = EphemeralPortRange.LOWER + this.random.nextInt(EphemeralPortRange.RANGE);
                    zContains = this.history.contains(iNextInt);
                    z2 = this.lastport == 0 || (farEnough(iNextInt) && !zContains);
                    int i5 = i4;
                    i4--;
                    if (i5 <= 0) {
                        break;
                    }
                } while (!z2);
                if (z2) {
                    try {
                        if (this.family != null) {
                            DatagramChannel datagramChannelOpen = DatagramChannel.open(this.family);
                            try {
                                datagramSocketSocket = datagramChannelOpen.socket();
                                datagramSocketSocket.bind(new InetSocketAddress(iNextInt));
                                this.lastport = datagramSocketSocket.getLocalPort();
                                if (!zContains) {
                                    this.history.add(iNextInt);
                                    break;
                                }
                                break;
                            } catch (Throwable th) {
                                datagramChannelOpen.close();
                                throw th;
                            }
                        }
                        DatagramSocket datagramSocket = new DatagramSocket(iNextInt);
                        this.lastport = datagramSocket.getLocalPort();
                        if (!zContains) {
                            this.history.add(iNextInt);
                        }
                        return datagramSocket;
                    } catch (IOException e2) {
                    }
                }
            } else {
                return null;
            }
        }
        return datagramSocketSocket;
    }
}
