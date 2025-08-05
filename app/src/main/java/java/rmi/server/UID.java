package java.rmi.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.security.SecureRandom;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:java/rmi/server/UID.class */
public final class UID implements Serializable {
    private static int hostUnique;
    private static boolean hostUniqueSet = false;
    private static final Object lock = new Object();
    private static long lastTime = System.currentTimeMillis();
    private static short lastCount = Short.MIN_VALUE;
    private static final long serialVersionUID = 1086053664494604050L;
    private final int unique;
    private final long time;
    private final short count;

    public UID() {
        synchronized (lock) {
            if (!hostUniqueSet) {
                hostUnique = new SecureRandom().nextInt();
                hostUniqueSet = true;
            }
            this.unique = hostUnique;
            if (lastCount == Short.MAX_VALUE) {
                boolean zInterrupted = Thread.interrupted();
                boolean z2 = false;
                while (!z2) {
                    long jCurrentTimeMillis = System.currentTimeMillis();
                    if (jCurrentTimeMillis == lastTime) {
                        try {
                            Thread.sleep(1L);
                        } catch (InterruptedException e2) {
                            zInterrupted = true;
                        }
                    } else {
                        lastTime = jCurrentTimeMillis < lastTime ? lastTime + 1 : jCurrentTimeMillis;
                        lastCount = Short.MIN_VALUE;
                        z2 = true;
                    }
                }
                if (zInterrupted) {
                    Thread.currentThread().interrupt();
                }
            }
            this.time = lastTime;
            short s2 = lastCount;
            lastCount = (short) (s2 + 1);
            this.count = s2;
        }
    }

    public UID(short s2) {
        this.unique = 0;
        this.time = 0L;
        this.count = s2;
    }

    private UID(int i2, long j2, short s2) {
        this.unique = i2;
        this.time = j2;
        this.count = s2;
    }

    public int hashCode() {
        return ((int) this.time) + this.count;
    }

    public boolean equals(Object obj) {
        if (obj instanceof UID) {
            UID uid = (UID) obj;
            return this.unique == uid.unique && this.count == uid.count && this.time == uid.time;
        }
        return false;
    }

    public String toString() {
        return Integer.toString(this.unique, 16) + CallSiteDescriptor.TOKEN_DELIMITER + Long.toString(this.time, 16) + CallSiteDescriptor.TOKEN_DELIMITER + Integer.toString(this.count, 16);
    }

    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.unique);
        dataOutput.writeLong(this.time);
        dataOutput.writeShort(this.count);
    }

    public static UID read(DataInput dataInput) throws IOException {
        return new UID(dataInput.readInt(), dataInput.readLong(), dataInput.readShort());
    }
}
