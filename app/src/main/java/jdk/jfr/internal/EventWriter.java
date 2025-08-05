package jdk.jfr.internal;

import java.util.function.Supplier;
import sun.misc.Unsafe;

/* loaded from: jfr.jar:jdk/jfr/internal/EventWriter.class */
public final class EventWriter {
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final JVM jvm = JVM.getJVM();
    private long startPosition;
    private long startPositionAddress;
    private long currentPosition;
    private long maxPosition;
    private final long threadID;
    private PlatformEventType eventType;
    private boolean valid;
    private boolean started = false;
    private boolean flushOnEnd = false;
    boolean notified = false;
    private int maxEventSize = 268435455;

    public static EventWriter getEventWriter() {
        EventWriter eventWriter = (EventWriter) JVM.getEventWriter();
        return eventWriter != null ? eventWriter : JVM.newEventWriter();
    }

    public void putBoolean(boolean z2) {
        if (isValidForSize(1)) {
            this.currentPosition += Bits.putBoolean(this.currentPosition, z2);
        }
    }

    public void putByte(byte b2) {
        if (isValidForSize(1)) {
            unsafe.putByte(this.currentPosition, b2);
            this.currentPosition++;
        }
    }

    public void putChar(char c2) {
        if (isValidForSize(3)) {
            putUncheckedLong(c2);
        }
    }

    private void putUncheckedChar(char c2) {
        putUncheckedLong(c2);
    }

    public void putShort(short s2) {
        if (isValidForSize(3)) {
            putUncheckedLong(s2 & 65535);
        }
    }

    public void putInt(int i2) {
        if (isValidForSize(5)) {
            putUncheckedLong(i2 & 4294967295L);
        }
    }

    private void putUncheckedInt(int i2) {
        putUncheckedLong(i2 & 4294967295L);
    }

    public void putFloat(float f2) {
        if (isValidForSize(4)) {
            this.currentPosition += Bits.putFloat(this.currentPosition, f2);
        }
    }

    public void putLong(long j2) {
        if (isValidForSize(9)) {
            putUncheckedLong(j2);
        }
    }

    public void putDouble(double d2) {
        if (isValidForSize(8)) {
            this.currentPosition += Bits.putDouble(this.currentPosition, d2);
        }
    }

    public void putString(String str, StringPool stringPool) {
        if (str == null) {
            putByte((byte) 0);
            return;
        }
        int length = str.length();
        if (length == 0) {
            putByte((byte) 1);
            return;
        }
        if (length > 16 && length < 128) {
            long jAddString = StringPool.addString(str);
            if (jAddString > 0) {
                putByte((byte) 2);
                putLong(jAddString);
                return;
            }
        }
        putStringValue(str);
    }

    private void putStringValue(String str) {
        int length = str.length();
        if (isValidForSize(6 + (3 * length))) {
            putUncheckedByte((byte) 4);
            putUncheckedInt(length);
            for (int i2 = 0; i2 < length; i2++) {
                putUncheckedChar(str.charAt(i2));
            }
        }
    }

    public void putEventThread() {
        putLong(this.threadID);
    }

    public void putThread(Thread thread) {
        if (thread == null) {
            putLong(0L);
        } else {
            putLong(jvm.getThreadId(thread));
        }
    }

    public void putClass(Class<?> cls) {
        if (cls == null) {
            putLong(0L);
        } else {
            putLong(JVM.getClassIdNonIntrinsic(cls));
        }
    }

    public void putStackTrace() {
        if (this.eventType.getStackTraceEnabled()) {
            putLong(jvm.getStackTraceId(this.eventType.getStackTraceOffset()));
        } else {
            putLong(0L);
        }
    }

    private void reserveEventSizeField() {
        if (isValidForSize(4)) {
            this.currentPosition += 4;
        }
    }

    private void reset() {
        this.currentPosition = this.startPosition;
        if (this.flushOnEnd) {
            this.flushOnEnd = flush();
        }
        this.valid = true;
        this.started = false;
    }

    private boolean isValidForSize(int i2) {
        if (!this.valid) {
            return false;
        }
        if (this.currentPosition + i2 > this.maxPosition) {
            this.flushOnEnd = flush(usedSize(), i2);
            if (this.currentPosition + i2 > this.maxPosition) {
                Logger.log(LogTag.JFR_SYSTEM, LogLevel.WARN, (Supplier<String>) () -> {
                    return "Unable to commit. Requested size " + i2 + " too large";
                });
                this.valid = false;
                return false;
            }
            return true;
        }
        return true;
    }

    private boolean isNotified() {
        return this.notified;
    }

    private void resetNotified() {
        this.notified = false;
    }

    private int usedSize() {
        return (int) (this.currentPosition - this.startPosition);
    }

    private boolean flush() {
        return flush(usedSize(), 0);
    }

    private boolean flush(int i2, int i3) {
        return JVM.flush(this, i2, i3);
    }

    public boolean beginEvent(PlatformEventType platformEventType) {
        if (this.started) {
            return false;
        }
        this.started = true;
        this.eventType = platformEventType;
        reserveEventSizeField();
        putLong(platformEventType.getId());
        return true;
    }

    public boolean endEvent() {
        if (!this.valid) {
            reset();
            return true;
        }
        int iUsedSize = usedSize();
        if (iUsedSize > this.maxEventSize) {
            reset();
            return true;
        }
        Bits.putInt(this.startPosition, makePaddedInt(iUsedSize));
        if (isNotified()) {
            resetNotified();
            reset();
            return false;
        }
        this.startPosition = this.currentPosition;
        unsafe.putAddress(this.startPositionAddress, this.startPosition);
        if (this.flushOnEnd) {
            this.flushOnEnd = flush();
        }
        this.started = false;
        return true;
    }

    private EventWriter(long j2, long j3, long j4, long j5, boolean z2) {
        this.currentPosition = j2;
        this.startPosition = j2;
        this.maxPosition = j3;
        this.startPositionAddress = j4;
        this.threadID = j5;
        this.valid = z2;
    }

    private static int makePaddedInt(int i2) {
        return (int) (((((i2 >>> 0) & 127) | 128) << 24) + ((((i2 >>> 7) & 127) | 128) << 16) + ((((i2 >>> 14) & 127) | 128) << 8) + (((i2 >>> 21) & 127) << 0));
    }

    private void putUncheckedLong(long j2) {
        if ((j2 & (-128)) == 0) {
            putUncheckedByte((byte) j2);
            return;
        }
        putUncheckedByte((byte) (j2 | 128));
        long j3 = j2 >>> 7;
        if ((j3 & (-128)) == 0) {
            putUncheckedByte((byte) j3);
            return;
        }
        putUncheckedByte((byte) (j3 | 128));
        long j4 = j3 >>> 7;
        if ((j4 & (-128)) == 0) {
            putUncheckedByte((byte) j4);
            return;
        }
        putUncheckedByte((byte) (j4 | 128));
        long j5 = j4 >>> 7;
        if ((j5 & (-128)) == 0) {
            putUncheckedByte((byte) j5);
            return;
        }
        putUncheckedByte((byte) (j5 | 128));
        long j6 = j5 >>> 7;
        if ((j6 & (-128)) == 0) {
            putUncheckedByte((byte) j6);
            return;
        }
        putUncheckedByte((byte) (j6 | 128));
        long j7 = j6 >>> 7;
        if ((j7 & (-128)) == 0) {
            putUncheckedByte((byte) j7);
            return;
        }
        putUncheckedByte((byte) (j7 | 128));
        long j8 = j7 >>> 7;
        if ((j8 & (-128)) == 0) {
            putUncheckedByte((byte) j8);
            return;
        }
        putUncheckedByte((byte) (j8 | 128));
        long j9 = j8 >>> 7;
        if ((j9 & (-128)) == 0) {
            putUncheckedByte((byte) j9);
        } else {
            putUncheckedByte((byte) (j9 | 128));
            putUncheckedByte((byte) (j9 >>> 7));
        }
    }

    private void putUncheckedByte(byte b2) {
        unsafe.putByte(this.currentPosition, b2);
        this.currentPosition++;
    }
}
