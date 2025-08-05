package com.intel.bluetooth;

import com.intel.bluetooth.UtilsJavaSE;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Vector;
import javax.bluetooth.UUID;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/Utils.class */
public abstract class Utils {
    private static final String blueCoveImplPackage;
    static Class class$com$intel$bluetooth$MicroeditionConnector;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$com$intel$bluetooth$MicroeditionConnector == null) {
            clsClass$ = class$("com.intel.bluetooth.MicroeditionConnector");
            class$com$intel$bluetooth$MicroeditionConnector = clsClass$;
        } else {
            clsClass$ = class$com$intel$bluetooth$MicroeditionConnector;
        }
        blueCoveImplPackage = getPackage(clsClass$.getName());
    }

    private Utils() {
    }

    private static String getPackage(String className) {
        int pStart = className.lastIndexOf(46);
        if (pStart == -1) {
            return "";
        }
        return className.substring(0, pStart);
    }

    public static byte[] UUIDToByteArray(String uuidStringValue) {
        byte[] uuidValue = new byte[16];
        if (uuidStringValue.indexOf(45) != -1) {
            throw new NumberFormatException(new StringBuffer().append("The '-' character is not allowed in UUID: ").append(uuidStringValue).toString());
        }
        for (int i2 = 0; i2 < 16; i2++) {
            uuidValue[i2] = (byte) Integer.parseInt(uuidStringValue.substring(i2 * 2, (i2 * 2) + 2), 16);
        }
        return uuidValue;
    }

    static byte[] UUIDToByteArray(UUID uuid) {
        return UUIDToByteArray(uuid.toString());
    }

    public static String UUIDByteArrayToString(byte[] uuidValue) {
        StringBuffer buf = new StringBuffer();
        for (int i2 = 0; i2 < uuidValue.length; i2++) {
            buf.append(Integer.toHexString((uuidValue[i2] >> 4) & 15));
            buf.append(Integer.toHexString(uuidValue[i2] & 15));
        }
        return buf.toString();
    }

    static long UUIDTo32Bit(UUID uuid) {
        String str;
        int shortIdx;
        if (uuid != null && (shortIdx = (str = uuid.toString().toUpperCase()).indexOf(BluetoothConsts.SHORT_UUID_BASE)) != -1 && shortIdx + BluetoothConsts.SHORT_UUID_BASE.length() == str.length()) {
            return Long.parseLong(str.substring(0, shortIdx), 16);
        }
        return -1L;
    }

    static boolean is32Bit(UUID uuid) {
        return UUIDTo32Bit(uuid) != -1;
    }

    public static int securityOpt(boolean authenticate, boolean encrypt) {
        int security = 0;
        if (authenticate) {
            if (encrypt) {
                security = 2;
            } else {
                security = 1;
            }
        } else if (encrypt) {
            throw new IllegalArgumentException("Illegal encrypt configuration");
        }
        return security;
    }

    static boolean isStringSet(String str) {
        return str != null && str.length() > 0;
    }

    static String loadString(InputStream inputstream) {
        if (inputstream == null) {
            return null;
        }
        try {
            byte[] buf = new byte[256];
            int len = inputstream.read(buf);
            String str = new String(buf, 0, len);
            try {
                inputstream.close();
            } catch (IOException e2) {
            }
            return str;
        } catch (IOException e3) {
            try {
                inputstream.close();
            } catch (IOException e4) {
            }
            return null;
        } catch (Throwable th) {
            try {
                inputstream.close();
            } catch (IOException e5) {
            }
            throw th;
        }
    }

    static String getResourceProperty(Class owner, String resourceName) {
        int cr;
        try {
            String value = loadString(owner.getResourceAsStream(new StringBuffer().append("/").append(resourceName).toString()));
            if (value != null && (cr = value.indexOf(10)) != -1) {
                value = value.substring(0, cr - 1);
            }
            return value;
        } catch (Throwable th) {
            return null;
        }
    }

    public static byte[] clone(byte[] value) {
        if (value == null) {
            return null;
        }
        int length = value.length;
        byte[] bClone = new byte[length];
        System.arraycopy(value, 0, bClone, 0, length);
        return bClone;
    }

    public static Vector clone(Enumeration en) {
        Vector copy = new Vector();
        while (en.hasMoreElements()) {
            copy.addElement(en.nextElement());
        }
        return copy;
    }

    static String newStringUTF8(byte[] bytes) {
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e2) {
            return new String(bytes);
        } catch (IllegalArgumentException e3) {
            return new String(bytes);
        }
    }

    static byte[] getUTF8Bytes(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e2) {
            return str.getBytes();
        } catch (IllegalArgumentException e3) {
            return str.getBytes();
        }
    }

    static String newStringASCII(byte[] bytes) {
        try {
            return new String(bytes, "US-ASCII");
        } catch (UnsupportedEncodingException e2) {
            return new String(bytes);
        } catch (IllegalArgumentException e3) {
            return new String(bytes);
        }
    }

    static byte[] getASCIIBytes(String str) {
        try {
            return str.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e2) {
            return str.getBytes();
        } catch (IllegalArgumentException e3) {
            return str.getBytes();
        }
    }

    static Object[] vector2toArray(Vector vector, Object[] anArray) {
        vector.copyInto(anArray);
        return anArray;
    }

    public static String toHexString(long l2) {
        StringBuffer buf = new StringBuffer();
        String lo = Integer.toHexString((int) l2);
        if (l2 > 4294967295L) {
            String hi = Integer.toHexString((int) (l2 >> 32));
            buf.append(hi);
            for (int i2 = lo.length(); i2 < 8; i2++) {
                buf.append('0');
            }
        }
        buf.append(lo);
        return buf.toString();
    }

    static void j2meUsagePatternDellay() {
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e2) {
        }
    }

    /* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/Utils$TimerThread.class */
    static class TimerThread extends Thread {
        long delay;
        Runnable run;

        public TimerThread(long delay, Runnable run) {
            this.delay = delay;
            this.run = run;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                Thread.sleep(this.delay);
                this.run.run();
            } catch (InterruptedException e2) {
            }
        }
    }

    static TimerThread schedule(long delay, Runnable run) {
        TimerThread t2 = new TimerThread(delay, run);
        UtilsJavaSE.threadSetDaemon(t2);
        t2.start();
        return t2;
    }

    public static void isLegalAPICall(Vector fqcnSet) throws Error {
        UtilsJavaSE.StackTraceLocation ste = UtilsJavaSE.getLocation(fqcnSet);
        if (ste == null || ste.className.startsWith("javax.bluetooth.") || ste.className.startsWith(new StringBuffer().append(blueCoveImplPackage).append(".").toString())) {
        } else {
            throw new Error("Illegal use of the JSR-82 API");
        }
    }
}
