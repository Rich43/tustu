package java.util.prefs;

import java.io.ByteArrayOutputStream;
import java.util.StringTokenizer;
import javafx.fxml.FXMLLoader;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:java/util/prefs/WindowsPreferences.class */
class WindowsPreferences extends AbstractPreferences {
    private static PlatformLogger logger;
    private static final int HKEY_CURRENT_USER = -2147483647;
    private static final int HKEY_LOCAL_MACHINE = -2147483646;
    private static final int USER_ROOT_NATIVE_HANDLE = -2147483647;
    private static final int SYSTEM_ROOT_NATIVE_HANDLE = -2147483646;
    private static final int MAX_WINDOWS_PATH_LENGTH = 256;
    private static volatile Preferences userRoot;
    private static volatile Preferences systemRoot;
    private static final int ERROR_SUCCESS = 0;
    private static final int ERROR_FILE_NOT_FOUND = 2;
    private static final int ERROR_ACCESS_DENIED = 5;
    private static final int NATIVE_HANDLE = 0;
    private static final int ERROR_CODE = 1;
    private static final int SUBKEYS_NUMBER = 0;
    private static final int VALUES_NUMBER = 2;
    private static final int MAX_KEY_LENGTH = 3;
    private static final int MAX_VALUE_NAME_LENGTH = 4;
    private static final int DISPOSITION = 2;
    private static final int REG_CREATED_NEW_KEY = 1;
    private static final int REG_OPENED_EXISTING_KEY = 2;
    private static final int NULL_NATIVE_HANDLE = 0;
    private static final int DELETE = 65536;
    private static final int KEY_QUERY_VALUE = 1;
    private static final int KEY_SET_VALUE = 2;
    private static final int KEY_CREATE_SUB_KEY = 4;
    private static final int KEY_ENUMERATE_SUB_KEYS = 8;
    private static final int KEY_READ = 131097;
    private static final int KEY_WRITE = 131078;
    private static final int KEY_ALL_ACCESS = 983103;
    private boolean isBackingStoreAvailable;
    private static final byte[] WINDOWS_ROOT_PATH = stringToByteArray("Software\\JavaSoft\\Prefs");
    private static int INIT_SLEEP_TIME = 50;
    private static int MAX_ATTEMPTS = 5;

    private static native int[] WindowsRegOpenKey(int i2, byte[] bArr, int i3);

    private static native int WindowsRegCloseKey(int i2);

    private static native int[] WindowsRegCreateKeyEx(int i2, byte[] bArr);

    private static native int WindowsRegDeleteKey(int i2, byte[] bArr);

    private static native int WindowsRegFlushKey(int i2);

    private static native byte[] WindowsRegQueryValueEx(int i2, byte[] bArr);

    private static native int WindowsRegSetValueEx(int i2, byte[] bArr, byte[] bArr2);

    private static native int WindowsRegDeleteValue(int i2, byte[] bArr);

    private static native int[] WindowsRegQueryInfoKey(int i2);

    private static native byte[] WindowsRegEnumKeyEx(int i2, int i3, int i4);

    private static native byte[] WindowsRegEnumValue(int i2, int i3, int i4);

    static Preferences getUserRoot() {
        Preferences windowsPreferences = userRoot;
        if (windowsPreferences == null) {
            synchronized (WindowsPreferences.class) {
                windowsPreferences = userRoot;
                if (windowsPreferences == null) {
                    windowsPreferences = new WindowsPreferences(-2147483647, WINDOWS_ROOT_PATH);
                    userRoot = windowsPreferences;
                }
            }
        }
        return windowsPreferences;
    }

    static Preferences getSystemRoot() {
        Preferences windowsPreferences = systemRoot;
        if (windowsPreferences == null) {
            synchronized (WindowsPreferences.class) {
                windowsPreferences = systemRoot;
                if (windowsPreferences == null) {
                    windowsPreferences = new WindowsPreferences(-2147483646, WINDOWS_ROOT_PATH);
                    systemRoot = windowsPreferences;
                }
            }
        }
        return windowsPreferences;
    }

    private static int[] WindowsRegOpenKey1(int i2, byte[] bArr, int i3) {
        int[] iArrWindowsRegOpenKey = WindowsRegOpenKey(i2, bArr, i3);
        if (iArrWindowsRegOpenKey[1] == 0) {
            return iArrWindowsRegOpenKey;
        }
        if (iArrWindowsRegOpenKey[1] == 2) {
            logger().warning("Trying to recreate Windows registry node " + byteArrayToString(bArr) + " at root 0x" + Integer.toHexString(i2) + ".");
            WindowsRegCloseKey(WindowsRegCreateKeyEx(i2, bArr)[0]);
            return WindowsRegOpenKey(i2, bArr, i3);
        }
        if (iArrWindowsRegOpenKey[1] != 5) {
            long j2 = INIT_SLEEP_TIME;
            for (int i4 = 0; i4 < MAX_ATTEMPTS; i4++) {
                try {
                    Thread.sleep(j2);
                    j2 *= 2;
                    iArrWindowsRegOpenKey = WindowsRegOpenKey(i2, bArr, i3);
                    if (iArrWindowsRegOpenKey[1] == 0) {
                        return iArrWindowsRegOpenKey;
                    }
                } catch (InterruptedException e2) {
                    return iArrWindowsRegOpenKey;
                }
            }
        }
        return iArrWindowsRegOpenKey;
    }

    private static int[] WindowsRegCreateKeyEx1(int i2, byte[] bArr) {
        int[] iArrWindowsRegCreateKeyEx = WindowsRegCreateKeyEx(i2, bArr);
        if (iArrWindowsRegCreateKeyEx[1] == 0) {
            return iArrWindowsRegCreateKeyEx;
        }
        long j2 = INIT_SLEEP_TIME;
        for (int i3 = 0; i3 < MAX_ATTEMPTS; i3++) {
            try {
                Thread.sleep(j2);
                j2 *= 2;
                iArrWindowsRegCreateKeyEx = WindowsRegCreateKeyEx(i2, bArr);
                if (iArrWindowsRegCreateKeyEx[1] == 0) {
                    return iArrWindowsRegCreateKeyEx;
                }
            } catch (InterruptedException e2) {
                return iArrWindowsRegCreateKeyEx;
            }
        }
        return iArrWindowsRegCreateKeyEx;
    }

    private static int WindowsRegFlushKey1(int i2) {
        int iWindowsRegFlushKey = WindowsRegFlushKey(i2);
        if (iWindowsRegFlushKey == 0) {
            return iWindowsRegFlushKey;
        }
        long j2 = INIT_SLEEP_TIME;
        for (int i3 = 0; i3 < MAX_ATTEMPTS; i3++) {
            try {
                Thread.sleep(j2);
                j2 *= 2;
                iWindowsRegFlushKey = WindowsRegFlushKey(i2);
                if (iWindowsRegFlushKey == 0) {
                    return iWindowsRegFlushKey;
                }
            } catch (InterruptedException e2) {
                return iWindowsRegFlushKey;
            }
        }
        return iWindowsRegFlushKey;
    }

    private static int WindowsRegSetValueEx1(int i2, byte[] bArr, byte[] bArr2) {
        int iWindowsRegSetValueEx = WindowsRegSetValueEx(i2, bArr, bArr2);
        if (iWindowsRegSetValueEx == 0) {
            return iWindowsRegSetValueEx;
        }
        long j2 = INIT_SLEEP_TIME;
        for (int i3 = 0; i3 < MAX_ATTEMPTS; i3++) {
            try {
                Thread.sleep(j2);
                j2 *= 2;
                iWindowsRegSetValueEx = WindowsRegSetValueEx(i2, bArr, bArr2);
                if (iWindowsRegSetValueEx == 0) {
                    return iWindowsRegSetValueEx;
                }
            } catch (InterruptedException e2) {
                return iWindowsRegSetValueEx;
            }
        }
        return iWindowsRegSetValueEx;
    }

    private static int[] WindowsRegQueryInfoKey1(int i2) {
        int[] iArrWindowsRegQueryInfoKey = WindowsRegQueryInfoKey(i2);
        if (iArrWindowsRegQueryInfoKey[1] == 0) {
            return iArrWindowsRegQueryInfoKey;
        }
        long j2 = INIT_SLEEP_TIME;
        for (int i3 = 0; i3 < MAX_ATTEMPTS; i3++) {
            try {
                Thread.sleep(j2);
                j2 *= 2;
                iArrWindowsRegQueryInfoKey = WindowsRegQueryInfoKey(i2);
                if (iArrWindowsRegQueryInfoKey[1] == 0) {
                    return iArrWindowsRegQueryInfoKey;
                }
            } catch (InterruptedException e2) {
                return iArrWindowsRegQueryInfoKey;
            }
        }
        return iArrWindowsRegQueryInfoKey;
    }

    private static byte[] WindowsRegEnumKeyEx1(int i2, int i3, int i4) {
        byte[] bArrWindowsRegEnumKeyEx = WindowsRegEnumKeyEx(i2, i3, i4);
        if (bArrWindowsRegEnumKeyEx != null) {
            return bArrWindowsRegEnumKeyEx;
        }
        long j2 = INIT_SLEEP_TIME;
        for (int i5 = 0; i5 < MAX_ATTEMPTS; i5++) {
            try {
                Thread.sleep(j2);
                j2 *= 2;
                bArrWindowsRegEnumKeyEx = WindowsRegEnumKeyEx(i2, i3, i4);
                if (bArrWindowsRegEnumKeyEx != null) {
                    return bArrWindowsRegEnumKeyEx;
                }
            } catch (InterruptedException e2) {
                return bArrWindowsRegEnumKeyEx;
            }
        }
        return bArrWindowsRegEnumKeyEx;
    }

    private static byte[] WindowsRegEnumValue1(int i2, int i3, int i4) {
        byte[] bArrWindowsRegEnumValue = WindowsRegEnumValue(i2, i3, i4);
        if (bArrWindowsRegEnumValue != null) {
            return bArrWindowsRegEnumValue;
        }
        long j2 = INIT_SLEEP_TIME;
        for (int i5 = 0; i5 < MAX_ATTEMPTS; i5++) {
            try {
                Thread.sleep(j2);
                j2 *= 2;
                bArrWindowsRegEnumValue = WindowsRegEnumValue(i2, i3, i4);
                if (bArrWindowsRegEnumValue != null) {
                    return bArrWindowsRegEnumValue;
                }
            } catch (InterruptedException e2) {
                return bArrWindowsRegEnumValue;
            }
        }
        return bArrWindowsRegEnumValue;
    }

    private WindowsPreferences(WindowsPreferences windowsPreferences, String str) {
        super(windowsPreferences, str);
        this.isBackingStoreAvailable = true;
        int iOpenKey = windowsPreferences.openKey(4, KEY_READ);
        if (iOpenKey == 0) {
            this.isBackingStoreAvailable = false;
            return;
        }
        int[] iArrWindowsRegCreateKeyEx1 = WindowsRegCreateKeyEx1(iOpenKey, toWindowsName(str));
        if (iArrWindowsRegCreateKeyEx1[1] != 0) {
            logger().warning("Could not create windows registry node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ". Windows RegCreateKeyEx(...) returned error code " + iArrWindowsRegCreateKeyEx1[1] + ".");
            this.isBackingStoreAvailable = false;
        } else {
            this.newNode = iArrWindowsRegCreateKeyEx1[2] == 1;
            closeKey(iOpenKey);
            closeKey(iArrWindowsRegCreateKeyEx1[0]);
        }
    }

    private WindowsPreferences(int i2, byte[] bArr) {
        super(null, "");
        this.isBackingStoreAvailable = true;
        int[] iArrWindowsRegCreateKeyEx1 = WindowsRegCreateKeyEx1(i2, bArr);
        if (iArrWindowsRegCreateKeyEx1[1] != 0) {
            logger().warning("Could not open/create prefs root node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ". Windows RegCreateKeyEx(...) returned error code " + iArrWindowsRegCreateKeyEx1[1] + ".");
            this.isBackingStoreAvailable = false;
        } else {
            this.newNode = iArrWindowsRegCreateKeyEx1[2] == 1;
            closeKey(iArrWindowsRegCreateKeyEx1[0]);
        }
    }

    private byte[] windowsAbsolutePath() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(WINDOWS_ROOT_PATH, 0, WINDOWS_ROOT_PATH.length - 1);
        StringTokenizer stringTokenizer = new StringTokenizer(absolutePath(), "/");
        while (stringTokenizer.hasMoreTokens()) {
            byteArrayOutputStream.write(92);
            byte[] windowsName = toWindowsName(stringTokenizer.nextToken());
            byteArrayOutputStream.write(windowsName, 0, windowsName.length - 1);
        }
        byteArrayOutputStream.write(0);
        return byteArrayOutputStream.toByteArray();
    }

    private int openKey(int i2) {
        return openKey(i2, i2);
    }

    private int openKey(int i2, int i3) {
        return openKey(windowsAbsolutePath(), i2, i3);
    }

    private int openKey(byte[] bArr, int i2, int i3) {
        if (bArr.length <= 257) {
            int[] iArrWindowsRegOpenKey1 = WindowsRegOpenKey1(rootNativeHandle(), bArr, i2);
            if (iArrWindowsRegOpenKey1[1] == 5 && i3 != i2) {
                iArrWindowsRegOpenKey1 = WindowsRegOpenKey1(rootNativeHandle(), bArr, i3);
            }
            if (iArrWindowsRegOpenKey1[1] != 0) {
                logger().warning("Could not open windows registry node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ". Windows RegOpenKey(...) returned error code " + iArrWindowsRegOpenKey1[1] + ".");
                iArrWindowsRegOpenKey1[0] = 0;
                if (iArrWindowsRegOpenKey1[1] == 5) {
                    throw new SecurityException("Could not open windows registry node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ": Access denied");
                }
            }
            return iArrWindowsRegOpenKey1[0];
        }
        return openKey(rootNativeHandle(), bArr, i2, i3);
    }

    private int openKey(int i2, byte[] bArr, int i3, int i4) {
        if (bArr.length <= 257) {
            int[] iArrWindowsRegOpenKey1 = WindowsRegOpenKey1(i2, bArr, i3);
            if (iArrWindowsRegOpenKey1[1] == 5 && i4 != i3) {
                iArrWindowsRegOpenKey1 = WindowsRegOpenKey1(i2, bArr, i4);
            }
            if (iArrWindowsRegOpenKey1[1] != 0) {
                logger().warning("Could not open windows registry node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(i2) + ". Windows RegOpenKey(...) returned error code " + iArrWindowsRegOpenKey1[1] + ".");
                iArrWindowsRegOpenKey1[0] = 0;
            }
            return iArrWindowsRegOpenKey1[0];
        }
        int i5 = -1;
        int i6 = 256;
        while (true) {
            if (i6 <= 0) {
                break;
            }
            if (bArr[i6] != 92) {
                i6--;
            } else {
                i5 = i6;
                break;
            }
        }
        byte[] bArr2 = new byte[i5 + 1];
        System.arraycopy(bArr, 0, bArr2, 0, i5);
        bArr2[i5] = 0;
        byte[] bArr3 = new byte[(bArr.length - i5) - 1];
        System.arraycopy(bArr, i5 + 1, bArr3, 0, bArr3.length);
        int iOpenKey = openKey(i2, bArr2, i3, i4);
        if (iOpenKey == 0) {
            return 0;
        }
        int iOpenKey2 = openKey(iOpenKey, bArr3, i3, i4);
        closeKey(iOpenKey);
        return iOpenKey2;
    }

    private void closeKey(int i2) {
        int iWindowsRegCloseKey = WindowsRegCloseKey(i2);
        if (iWindowsRegCloseKey != 0) {
            logger().warning("Could not close windows registry node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ". Windows RegCloseKey(...) returned error code " + iWindowsRegCloseKey + ".");
        }
    }

    @Override // java.util.prefs.AbstractPreferences
    protected void putSpi(String str, String str2) {
        int iOpenKey = openKey(2);
        if (iOpenKey == 0) {
            this.isBackingStoreAvailable = false;
            return;
        }
        int iWindowsRegSetValueEx1 = WindowsRegSetValueEx1(iOpenKey, toWindowsName(str), toWindowsValueString(str2));
        if (iWindowsRegSetValueEx1 != 0) {
            logger().warning("Could not assign value to key " + byteArrayToString(toWindowsName(str)) + " at Windows registry node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ". Windows RegSetValueEx(...) returned error code " + iWindowsRegSetValueEx1 + ".");
            this.isBackingStoreAvailable = false;
        }
        closeKey(iOpenKey);
    }

    @Override // java.util.prefs.AbstractPreferences
    protected String getSpi(String str) {
        int iOpenKey = openKey(1);
        if (iOpenKey == 0) {
            return null;
        }
        byte[] bArrWindowsRegQueryValueEx = WindowsRegQueryValueEx(iOpenKey, toWindowsName(str));
        if (bArrWindowsRegQueryValueEx == null) {
            closeKey(iOpenKey);
            return null;
        }
        closeKey(iOpenKey);
        return toJavaValueString(bArrWindowsRegQueryValueEx);
    }

    @Override // java.util.prefs.AbstractPreferences
    protected void removeSpi(String str) {
        int iOpenKey = openKey(2);
        if (iOpenKey == 0) {
            return;
        }
        int iWindowsRegDeleteValue = WindowsRegDeleteValue(iOpenKey, toWindowsName(str));
        if (iWindowsRegDeleteValue != 0 && iWindowsRegDeleteValue != 2) {
            logger().warning("Could not delete windows registry value " + byteArrayToString(windowsAbsolutePath()) + FXMLLoader.ESCAPE_PREFIX + ((Object) toWindowsName(str)) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ". Windows RegDeleteValue(...) returned error code " + iWindowsRegDeleteValue + ".");
            this.isBackingStoreAvailable = false;
        }
        closeKey(iOpenKey);
    }

    @Override // java.util.prefs.AbstractPreferences
    protected String[] keysSpi() throws BackingStoreException {
        int iOpenKey = openKey(1);
        if (iOpenKey == 0) {
            throw new BackingStoreException("Could not open windows registry node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ".");
        }
        int[] iArrWindowsRegQueryInfoKey1 = WindowsRegQueryInfoKey1(iOpenKey);
        if (iArrWindowsRegQueryInfoKey1[1] != 0) {
            String str = "Could not query windows registry node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ". Windows RegQueryInfoKeyEx(...) returned error code " + iArrWindowsRegQueryInfoKey1[1] + ".";
            logger().warning(str);
            throw new BackingStoreException(str);
        }
        int i2 = iArrWindowsRegQueryInfoKey1[4];
        int i3 = iArrWindowsRegQueryInfoKey1[2];
        if (i3 == 0) {
            closeKey(iOpenKey);
            return new String[0];
        }
        String[] strArr = new String[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            byte[] bArrWindowsRegEnumValue1 = WindowsRegEnumValue1(iOpenKey, i4, i2 + 1);
            if (bArrWindowsRegEnumValue1 == null) {
                String str2 = "Could not enumerate value #" + i4 + "  of windows node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ".";
                logger().warning(str2);
                throw new BackingStoreException(str2);
            }
            strArr[i4] = toJavaName(bArrWindowsRegEnumValue1);
        }
        closeKey(iOpenKey);
        return strArr;
    }

    @Override // java.util.prefs.AbstractPreferences
    protected String[] childrenNamesSpi() throws BackingStoreException {
        int iOpenKey = openKey(9);
        if (iOpenKey == 0) {
            throw new BackingStoreException("Could not open windows registry node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ".");
        }
        int[] iArrWindowsRegQueryInfoKey1 = WindowsRegQueryInfoKey1(iOpenKey);
        if (iArrWindowsRegQueryInfoKey1[1] != 0) {
            String str = "Could not query windows registry node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ". Windows RegQueryInfoKeyEx(...) returned error code " + iArrWindowsRegQueryInfoKey1[1] + ".";
            logger().warning(str);
            throw new BackingStoreException(str);
        }
        int i2 = iArrWindowsRegQueryInfoKey1[3];
        int i3 = iArrWindowsRegQueryInfoKey1[0];
        if (i3 == 0) {
            closeKey(iOpenKey);
            return new String[0];
        }
        String[] strArr = new String[i3];
        String[] strArr2 = new String[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            byte[] bArrWindowsRegEnumKeyEx1 = WindowsRegEnumKeyEx1(iOpenKey, i4, i2 + 1);
            if (bArrWindowsRegEnumKeyEx1 == null) {
                String str2 = "Could not enumerate key #" + i4 + "  of windows node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ". ";
                logger().warning(str2);
                throw new BackingStoreException(str2);
            }
            strArr2[i4] = toJavaName(bArrWindowsRegEnumKeyEx1);
        }
        closeKey(iOpenKey);
        return strArr2;
    }

    @Override // java.util.prefs.AbstractPreferences, java.util.prefs.Preferences
    public void flush() throws BackingStoreException {
        if (isRemoved()) {
            this.parent.flush();
            return;
        }
        if (!this.isBackingStoreAvailable) {
            throw new BackingStoreException("flush(): Backing store not available.");
        }
        int iOpenKey = openKey(KEY_READ);
        if (iOpenKey == 0) {
            throw new BackingStoreException("Could not open windows registry node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ".");
        }
        int iWindowsRegFlushKey1 = WindowsRegFlushKey1(iOpenKey);
        if (iWindowsRegFlushKey1 != 0) {
            String str = "Could not flush windows registry node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ". Windows RegFlushKey(...) returned error code " + iWindowsRegFlushKey1 + ".";
            logger().warning(str);
            throw new BackingStoreException(str);
        }
        closeKey(iOpenKey);
    }

    @Override // java.util.prefs.AbstractPreferences, java.util.prefs.Preferences
    public void sync() throws BackingStoreException {
        if (isRemoved()) {
            throw new IllegalStateException("Node has been removed");
        }
        flush();
    }

    @Override // java.util.prefs.AbstractPreferences
    protected AbstractPreferences childSpi(String str) {
        return new WindowsPreferences(this, str);
    }

    @Override // java.util.prefs.AbstractPreferences
    public void removeNodeSpi() throws BackingStoreException {
        int iOpenKey = ((WindowsPreferences) parent()).openKey(65536);
        if (iOpenKey == 0) {
            throw new BackingStoreException("Could not open parent windows registry node of " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ".");
        }
        int iWindowsRegDeleteKey = WindowsRegDeleteKey(iOpenKey, toWindowsName(name()));
        if (iWindowsRegDeleteKey != 0) {
            String str = "Could not delete windows registry node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ". Windows RegDeleteKeyEx(...) returned error code " + iWindowsRegDeleteKey + ".";
            logger().warning(str);
            throw new BackingStoreException(str);
        }
        closeKey(iOpenKey);
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0070 A[PHI: r9
  0x0070: PHI (r9v1 char) = (r9v0 char), (r9v2 char), (r9v2 char) binds: [B:14:0x004d, B:16:0x005d, B:18:0x0064] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.lang.String toJavaName(byte[] r4) {
        /*
            r0 = r4
            java.lang.String r0 = byteArrayToString(r0)
            r5 = r0
            r0 = r5
            int r0 = r0.length()
            r1 = 1
            if (r0 <= r1) goto L20
            r0 = r5
            r1 = 0
            r2 = 2
            java.lang.String r0 = r0.substring(r1, r2)
            java.lang.String r1 = "/!"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L20
            r0 = r5
            java.lang.String r0 = toJavaAlt64Name(r0)
            return r0
        L20:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            r6 = r0
            r0 = 0
            r8 = r0
        L2b:
            r0 = r8
            r1 = r5
            int r1 = r1.length()
            if (r0 >= r1) goto La0
            r0 = r5
            r1 = r8
            char r0 = r0.charAt(r1)
            r1 = r0
            r7 = r1
            r1 = 47
            if (r0 != r1) goto L8b
            r0 = 32
            r9 = r0
            r0 = r5
            int r0 = r0.length()
            r1 = r8
            r2 = 1
            int r1 = r1 + r2
            if (r0 <= r1) goto L70
            r0 = r5
            r1 = r8
            r2 = 1
            int r1 = r1 + r2
            char r0 = r0.charAt(r1)
            r1 = r0
            r9 = r1
            r1 = 65
            if (r0 < r1) goto L70
            r0 = r9
            r1 = 90
            if (r0 > r1) goto L70
            r0 = r9
            r7 = r0
            int r8 = r8 + 1
            goto L88
        L70:
            r0 = r5
            int r0 = r0.length()
            r1 = r8
            r2 = 1
            int r1 = r1 + r2
            if (r0 <= r1) goto L88
            r0 = r9
            r1 = 47
            if (r0 != r1) goto L88
            r0 = 92
            r7 = r0
            int r8 = r8 + 1
        L88:
            goto L94
        L8b:
            r0 = r7
            r1 = 92
            if (r0 != r1) goto L94
            r0 = 47
            r7 = r0
        L94:
            r0 = r6
            r1 = r7
            java.lang.StringBuilder r0 = r0.append(r1)
            int r8 = r8 + 1
            goto L2b
        La0:
            r0 = r6
            java.lang.String r0 = r0.toString()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.prefs.WindowsPreferences.toJavaName(byte[]):java.lang.String");
    }

    private static String toJavaAlt64Name(String str) {
        byte[] bArrAltBase64ToByteArray = Base64.altBase64ToByteArray(str.substring(2));
        StringBuilder sb = new StringBuilder();
        int i2 = 0;
        while (i2 < bArrAltBase64ToByteArray.length) {
            int i3 = i2;
            int i4 = i2 + 1;
            sb.append((char) (((bArrAltBase64ToByteArray[i3] & 255) << 8) + (bArrAltBase64ToByteArray[i4] & 255)));
            i2 = i4 + 1;
        }
        return sb.toString();
    }

    private static byte[] toWindowsName(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < str.length(); i2++) {
            char cCharAt = str.charAt(i2);
            if (cCharAt < ' ' || cCharAt > 127) {
                return toWindowsAlt64Name(str);
            }
            if (cCharAt == '\\') {
                sb.append("//");
            } else if (cCharAt == '/') {
                sb.append('\\');
            } else if (cCharAt >= 'A' && cCharAt <= 'Z') {
                sb.append('/').append(cCharAt);
            } else {
                sb.append(cCharAt);
            }
        }
        return stringToByteArray(sb.toString());
    }

    private static byte[] toWindowsAlt64Name(String str) {
        byte[] bArr = new byte[2 * str.length()];
        int i2 = 0;
        for (int i3 = 0; i3 < str.length(); i3++) {
            char cCharAt = str.charAt(i3);
            int i4 = i2;
            int i5 = i2 + 1;
            bArr[i4] = (byte) (cCharAt >>> '\b');
            i2 = i5 + 1;
            bArr[i5] = (byte) cCharAt;
        }
        return stringToByteArray("/!" + Base64.byteArrayToAltBase64(bArr));
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x006e A[PHI: r10
  0x006e: PHI (r10v1 char) = (r10v0 char), (r10v2 char) binds: [B:8:0x0032, B:10:0x0042] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.lang.String toJavaValueString(byte[] r5) {
        /*
            Method dump skipped, instructions count: 203
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.prefs.WindowsPreferences.toJavaValueString(byte[]):java.lang.String");
    }

    private static byte[] toWindowsValueString(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < str.length(); i2++) {
            char cCharAt = str.charAt(i2);
            if (cCharAt < ' ' || cCharAt > 127) {
                sb.append("/u");
                StringBuilder sb2 = new StringBuilder(Integer.toHexString(str.charAt(i2)));
                sb2.reverse();
                int length = 4 - sb2.length();
                for (int i3 = 0; i3 < length; i3++) {
                    sb2.append('0');
                }
                for (int i4 = 0; i4 < 4; i4++) {
                    sb.append(sb2.charAt(3 - i4));
                }
            } else if (cCharAt == '\\') {
                sb.append("//");
            } else if (cCharAt == '/') {
                sb.append('\\');
            } else if (cCharAt >= 'A' && cCharAt <= 'Z') {
                sb.append('/').append(cCharAt);
            } else {
                sb.append(cCharAt);
            }
        }
        return stringToByteArray(sb.toString());
    }

    private int rootNativeHandle() {
        return isUserNode() ? -2147483647 : -2147483646;
    }

    private static byte[] stringToByteArray(String str) {
        byte[] bArr = new byte[str.length() + 1];
        for (int i2 = 0; i2 < str.length(); i2++) {
            bArr[i2] = (byte) str.charAt(i2);
        }
        bArr[str.length()] = 0;
        return bArr;
    }

    private static String byteArrayToString(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < bArr.length - 1; i2++) {
            sb.append((char) bArr[i2]);
        }
        return sb.toString();
    }

    @Override // java.util.prefs.AbstractPreferences
    protected void flushSpi() throws BackingStoreException {
    }

    @Override // java.util.prefs.AbstractPreferences
    protected void syncSpi() throws BackingStoreException {
    }

    private static synchronized PlatformLogger logger() {
        if (logger == null) {
            logger = PlatformLogger.getLogger("java.util.prefs");
        }
        return logger;
    }
}
