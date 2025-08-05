package java.util.zip;

import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/* loaded from: rt.jar:java/util/zip/ZipUtils.class */
class ZipUtils {
    private static final long WINDOWS_EPOCH_IN_MICROSECONDS = -11644473600000000L;

    ZipUtils() {
    }

    public static final FileTime winTimeToFileTime(long j2) {
        return FileTime.from((j2 / 10) + WINDOWS_EPOCH_IN_MICROSECONDS, TimeUnit.MICROSECONDS);
    }

    public static final long fileTimeToWinTime(FileTime fileTime) {
        return (fileTime.to(TimeUnit.MICROSECONDS) - WINDOWS_EPOCH_IN_MICROSECONDS) * 10;
    }

    public static final FileTime unixTimeToFileTime(long j2) {
        return FileTime.from(j2, TimeUnit.SECONDS);
    }

    public static final long fileTimeToUnixTime(FileTime fileTime) {
        return fileTime.to(TimeUnit.SECONDS);
    }

    private static long dosToJavaTime(long j2) {
        return new Date((int) (((j2 >> 25) & 127) + 80), (int) (((j2 >> 21) & 15) - 1), (int) ((j2 >> 16) & 31), (int) ((j2 >> 11) & 31), (int) ((j2 >> 5) & 63), (int) ((j2 << 1) & 62)).getTime();
    }

    public static long extendedDosToJavaTime(long j2) {
        return dosToJavaTime(j2) + (j2 >> 32);
    }

    private static long javaToDosTime(long j2) {
        if (new Date(j2).getYear() + 1900 < 1980) {
            return 2162688L;
        }
        return ((r0 - 1980) << 25) | ((r0.getMonth() + 1) << 21) | (r0.getDate() << 16) | (r0.getHours() << 11) | (r0.getMinutes() << 5) | (r0.getSeconds() >> 1);
    }

    public static long javaToExtendedDosTime(long j2) {
        if (j2 < 0) {
            return 2162688L;
        }
        long jJavaToDosTime = javaToDosTime(j2);
        if (jJavaToDosTime != 2162688) {
            return jJavaToDosTime + ((j2 % 2000) << 32);
        }
        return 2162688L;
    }

    public static final int get16(byte[] bArr, int i2) {
        return Byte.toUnsignedInt(bArr[i2]) | (Byte.toUnsignedInt(bArr[i2 + 1]) << 8);
    }

    public static final long get32(byte[] bArr, int i2) {
        return (get16(bArr, i2) | (get16(bArr, i2 + 2) << 16)) & 4294967295L;
    }

    public static final long get64(byte[] bArr, int i2) {
        return get32(bArr, i2) | (get32(bArr, i2 + 4) << 32);
    }
}
