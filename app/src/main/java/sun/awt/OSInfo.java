package sun.awt;

import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

/* loaded from: rt.jar:sun/awt/OSInfo.class */
public class OSInfo {
    private static final String OS_NAME = "os.name";
    private static final String OS_VERSION = "os.version";
    private static final PrivilegedAction<OSType> osTypeAction;
    public static final WindowsVersion WINDOWS_UNKNOWN = new WindowsVersion(-1, -1);
    public static final WindowsVersion WINDOWS_95 = new WindowsVersion(4, 0);
    public static final WindowsVersion WINDOWS_98 = new WindowsVersion(4, 10);
    public static final WindowsVersion WINDOWS_ME = new WindowsVersion(4, 90);
    public static final WindowsVersion WINDOWS_2000 = new WindowsVersion(5, 0);
    public static final WindowsVersion WINDOWS_XP = new WindowsVersion(5, 1);
    public static final WindowsVersion WINDOWS_2003 = new WindowsVersion(5, 2);
    public static final WindowsVersion WINDOWS_VISTA = new WindowsVersion(6, 0);
    private static final Map<String, WindowsVersion> windowsVersionMap = new HashMap();

    /* loaded from: rt.jar:sun/awt/OSInfo$OSType.class */
    public enum OSType {
        WINDOWS,
        LINUX,
        SOLARIS,
        MACOSX,
        UNKNOWN
    }

    static {
        windowsVersionMap.put(WINDOWS_95.toString(), WINDOWS_95);
        windowsVersionMap.put(WINDOWS_98.toString(), WINDOWS_98);
        windowsVersionMap.put(WINDOWS_ME.toString(), WINDOWS_ME);
        windowsVersionMap.put(WINDOWS_2000.toString(), WINDOWS_2000);
        windowsVersionMap.put(WINDOWS_XP.toString(), WINDOWS_XP);
        windowsVersionMap.put(WINDOWS_2003.toString(), WINDOWS_2003);
        windowsVersionMap.put(WINDOWS_VISTA.toString(), WINDOWS_VISTA);
        osTypeAction = new PrivilegedAction<OSType>() { // from class: sun.awt.OSInfo.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public OSType run2() {
                return OSInfo.getOSType();
            }
        };
    }

    private OSInfo() {
    }

    public static OSType getOSType() throws SecurityException {
        String property = System.getProperty(OS_NAME);
        if (property != null) {
            if (property.contains("Windows")) {
                return OSType.WINDOWS;
            }
            if (property.contains("Linux")) {
                return OSType.LINUX;
            }
            if (property.contains("Solaris") || property.contains("SunOS")) {
                return OSType.SOLARIS;
            }
            if (property.contains("OS X")) {
                return OSType.MACOSX;
            }
        }
        return OSType.UNKNOWN;
    }

    public static PrivilegedAction<OSType> getOSTypeAction() {
        return osTypeAction;
    }

    public static WindowsVersion getWindowsVersion() throws SecurityException {
        String property = System.getProperty(OS_VERSION);
        if (property == null) {
            return WINDOWS_UNKNOWN;
        }
        synchronized (windowsVersionMap) {
            WindowsVersion windowsVersion = windowsVersionMap.get(property);
            if (windowsVersion == null) {
                String[] strArrSplit = property.split("\\.");
                if (strArrSplit.length == 2) {
                    try {
                        windowsVersion = new WindowsVersion(Integer.parseInt(strArrSplit[0]), Integer.parseInt(strArrSplit[1]));
                        windowsVersionMap.put(property, windowsVersion);
                    } catch (NumberFormatException e2) {
                        return WINDOWS_UNKNOWN;
                    }
                } else {
                    return WINDOWS_UNKNOWN;
                }
            }
            return windowsVersion;
        }
    }

    /* loaded from: rt.jar:sun/awt/OSInfo$WindowsVersion.class */
    public static class WindowsVersion implements Comparable<WindowsVersion> {
        private final int major;
        private final int minor;

        private WindowsVersion(int i2, int i3) {
            this.major = i2;
            this.minor = i3;
        }

        public int getMajor() {
            return this.major;
        }

        public int getMinor() {
            return this.minor;
        }

        @Override // java.lang.Comparable
        public int compareTo(WindowsVersion windowsVersion) {
            int major = this.major - windowsVersion.getMajor();
            if (major == 0) {
                major = this.minor - windowsVersion.getMinor();
            }
            return major;
        }

        public boolean equals(Object obj) {
            return (obj instanceof WindowsVersion) && compareTo((WindowsVersion) obj) == 0;
        }

        public int hashCode() {
            return (31 * this.major) + this.minor;
        }

        public String toString() {
            return this.major + "." + this.minor;
        }
    }
}
