package sun.awt.windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* loaded from: rt.jar:sun/awt/windows/ThemeReader.class */
public final class ThemeReader {
    private static final Map<String, Long> widgetToTheme = new HashMap();
    private static final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private static final Lock readLock = readWriteLock.readLock();
    private static final Lock writeLock = readWriteLock.writeLock();
    private static volatile boolean valid = false;
    private static volatile boolean isThemed;
    static volatile boolean xpStyleEnabled;

    private static native boolean initThemes();

    private static native void paintBackground(int[] iArr, long j2, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    private static native Insets getThemeMargins(long j2, int i2, int i3, int i4);

    private static native boolean isThemePartDefined(long j2, int i2, int i3);

    private static native Color getColor(long j2, int i2, int i3, int i4);

    private static native int getInt(long j2, int i2, int i3, int i4);

    private static native int getEnum(long j2, int i2, int i3, int i4);

    private static native boolean getBoolean(long j2, int i2, int i3, int i4);

    private static native boolean getSysBoolean(long j2, int i2);

    private static native Point getPoint(long j2, int i2, int i3, int i4);

    private static native Dimension getPosition(long j2, int i2, int i3, int i4);

    private static native Dimension getPartSize(long j2, int i2, int i3);

    private static native long openTheme(String str);

    private static native void closeTheme(long j2);

    private static native void setWindowTheme(String str);

    private static native long getThemeTransitionDuration(long j2, int i2, int i3, int i4, int i5);

    public static native boolean isGetThemeTransitionDurationDefined();

    private static native Insets getThemeBackgroundContentMargins(long j2, int i2, int i3, int i4, int i5);

    static void flush() {
        valid = false;
    }

    public static boolean isThemed() {
        writeLock.lock();
        try {
            isThemed = initThemes();
            boolean z2 = isThemed;
            writeLock.unlock();
            return z2;
        } catch (Throwable th) {
            writeLock.unlock();
            throw th;
        }
    }

    public static boolean isXPStyleEnabled() {
        return xpStyleEnabled;
    }

    private static Long getThemeImpl(String str) {
        Long lValueOf = widgetToTheme.get(str);
        if (lValueOf == null) {
            int iIndexOf = str.indexOf("::");
            if (iIndexOf > 0) {
                setWindowTheme(str.substring(0, iIndexOf));
                lValueOf = Long.valueOf(openTheme(str.substring(iIndexOf + 2)));
                setWindowTheme(null);
            } else {
                lValueOf = Long.valueOf(openTheme(str));
            }
            widgetToTheme.put(str, lValueOf);
        }
        return lValueOf;
    }

    private static Long getTheme(String str) {
        if (!isThemed) {
            throw new IllegalStateException("Themes are not loaded");
        }
        if (!valid) {
            readLock.unlock();
            writeLock.lock();
            try {
                if (!valid) {
                    Iterator<Long> it = widgetToTheme.values().iterator();
                    while (it.hasNext()) {
                        closeTheme(it.next().longValue());
                    }
                    widgetToTheme.clear();
                    valid = true;
                }
                readLock.lock();
                writeLock.unlock();
            } catch (Throwable th) {
                readLock.lock();
                writeLock.unlock();
                throw th;
            }
        }
        Long themeImpl = widgetToTheme.get(str);
        if (themeImpl == null) {
            readLock.unlock();
            writeLock.lock();
            try {
                themeImpl = getThemeImpl(str);
                readLock.lock();
                writeLock.unlock();
            } finally {
                readLock.lock();
                writeLock.unlock();
            }
        }
        return themeImpl;
    }

    public static void paintBackground(int[] iArr, String str, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        readLock.lock();
        try {
            paintBackground(iArr, getTheme(str).longValue(), i2, i3, i4, i5, i6, i7, i8);
            readLock.unlock();
        } catch (Throwable th) {
            readLock.unlock();
            throw th;
        }
    }

    public static Insets getThemeMargins(String str, int i2, int i3, int i4) {
        readLock.lock();
        try {
            Insets themeMargins = getThemeMargins(getTheme(str).longValue(), i2, i3, i4);
            readLock.unlock();
            return themeMargins;
        } catch (Throwable th) {
            readLock.unlock();
            throw th;
        }
    }

    public static boolean isThemePartDefined(String str, int i2, int i3) {
        readLock.lock();
        try {
            boolean zIsThemePartDefined = isThemePartDefined(getTheme(str).longValue(), i2, i3);
            readLock.unlock();
            return zIsThemePartDefined;
        } catch (Throwable th) {
            readLock.unlock();
            throw th;
        }
    }

    public static Color getColor(String str, int i2, int i3, int i4) {
        readLock.lock();
        try {
            Color color = getColor(getTheme(str).longValue(), i2, i3, i4);
            readLock.unlock();
            return color;
        } catch (Throwable th) {
            readLock.unlock();
            throw th;
        }
    }

    public static int getInt(String str, int i2, int i3, int i4) {
        readLock.lock();
        try {
            int i5 = getInt(getTheme(str).longValue(), i2, i3, i4);
            readLock.unlock();
            return i5;
        } catch (Throwable th) {
            readLock.unlock();
            throw th;
        }
    }

    public static int getEnum(String str, int i2, int i3, int i4) {
        readLock.lock();
        try {
            int i5 = getEnum(getTheme(str).longValue(), i2, i3, i4);
            readLock.unlock();
            return i5;
        } catch (Throwable th) {
            readLock.unlock();
            throw th;
        }
    }

    public static boolean getBoolean(String str, int i2, int i3, int i4) {
        readLock.lock();
        try {
            boolean z2 = getBoolean(getTheme(str).longValue(), i2, i3, i4);
            readLock.unlock();
            return z2;
        } catch (Throwable th) {
            readLock.unlock();
            throw th;
        }
    }

    public static boolean getSysBoolean(String str, int i2) {
        readLock.lock();
        try {
            boolean sysBoolean = getSysBoolean(getTheme(str).longValue(), i2);
            readLock.unlock();
            return sysBoolean;
        } catch (Throwable th) {
            readLock.unlock();
            throw th;
        }
    }

    public static Point getPoint(String str, int i2, int i3, int i4) {
        readLock.lock();
        try {
            Point point = getPoint(getTheme(str).longValue(), i2, i3, i4);
            readLock.unlock();
            return point;
        } catch (Throwable th) {
            readLock.unlock();
            throw th;
        }
    }

    public static Dimension getPosition(String str, int i2, int i3, int i4) {
        readLock.lock();
        try {
            Dimension position = getPosition(getTheme(str).longValue(), i2, i3, i4);
            readLock.unlock();
            return position;
        } catch (Throwable th) {
            readLock.unlock();
            throw th;
        }
    }

    public static Dimension getPartSize(String str, int i2, int i3) {
        readLock.lock();
        try {
            Dimension partSize = getPartSize(getTheme(str).longValue(), i2, i3);
            readLock.unlock();
            return partSize;
        } catch (Throwable th) {
            readLock.unlock();
            throw th;
        }
    }

    public static long getThemeTransitionDuration(String str, int i2, int i3, int i4, int i5) {
        readLock.lock();
        try {
            long themeTransitionDuration = getThemeTransitionDuration(getTheme(str).longValue(), i2, i3, i4, i5);
            readLock.unlock();
            return themeTransitionDuration;
        } catch (Throwable th) {
            readLock.unlock();
            throw th;
        }
    }

    public static Insets getThemeBackgroundContentMargins(String str, int i2, int i3, int i4, int i5) {
        readLock.lock();
        try {
            Insets themeBackgroundContentMargins = getThemeBackgroundContentMargins(getTheme(str).longValue(), i2, i3, i4, i5);
            readLock.unlock();
            return themeBackgroundContentMargins;
        } catch (Throwable th) {
            readLock.unlock();
            throw th;
        }
    }
}
