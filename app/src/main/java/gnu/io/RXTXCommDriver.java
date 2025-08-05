package gnu.io;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: RXTXcomm.jar:gnu/io/RXTXCommDriver.class */
public class RXTXCommDriver implements CommDriver {
    private static final boolean debug = false;
    private static final boolean devel = false;
    private static final boolean noVersionOutput = "true".equals(System.getProperty("gnu.io.rxtx.NoVersionOutput"));
    private String deviceDirectory;
    private String osName;

    private native boolean registerKnownPorts(int i2);

    private native boolean isPortPrefixValid(String str);

    private native boolean testRead(String str, int i2);

    private native String getDeviceDirectory();

    public static native String nativeGetVersion();

    static {
        String strNativeGetVersion;
        System.loadLibrary("rxtxSerial");
        String version = RXTXVersion.getVersion();
        try {
            strNativeGetVersion = RXTXVersion.nativeGetVersion();
        } catch (Error e2) {
            strNativeGetVersion = nativeGetVersion();
        }
        if (!version.equals(strNativeGetVersion)) {
            System.out.println(new StringBuffer().append("WARNING:  RXTX Version mismatch\n\tJar version = ").append(version).append("\n\tnative lib Version = ").append(strNativeGetVersion).toString());
        }
    }

    private final String[] getValidPortPrefixes(String[] strArr) {
        String[] strArr2 = new String[256];
        if (strArr == null) {
        }
        int i2 = 0;
        for (int i3 = 0; i3 < strArr.length; i3++) {
            if (isPortPrefixValid(strArr[i3])) {
                int i4 = i2;
                i2++;
                strArr2[i4] = strArr[i3];
            }
        }
        String[] strArr3 = new String[i2];
        System.arraycopy(strArr2, 0, strArr3, 0, i2);
        if (strArr2[0] == null) {
        }
        return strArr3;
    }

    private void checkSolaris(String str, int i2) {
        char[] cArr = {'['};
        cArr[0] = 'a';
        while (cArr[0] < '{') {
            if (testRead(str.concat(new String(cArr)), i2)) {
                CommPortIdentifier.addPortName(str.concat(new String(cArr)), i2, this);
            }
            cArr[0] = (char) (cArr[0] + 1);
        }
        cArr[0] = '0';
        while (cArr[0] <= '9') {
            if (testRead(str.concat(new String(cArr)), i2)) {
                CommPortIdentifier.addPortName(str.concat(new String(cArr)), i2, this);
            }
            cArr[0] = (char) (cArr[0] + 1);
        }
    }

    private void registerValidPorts(String[] strArr, String[] strArr2, int i2) {
        String string;
        if (strArr != null && strArr2 != null) {
            for (String str : strArr) {
                for (String str2 : strArr2) {
                    int length = str2.length();
                    if (str.length() >= length) {
                        String upperCase = str.substring(length).toUpperCase();
                        String lowerCase = str.substring(length).toLowerCase();
                        if (str.regionMatches(0, str2, 0, length) && upperCase.equals(lowerCase)) {
                            if (this.osName.toLowerCase().indexOf("windows") == -1) {
                                string = new StringBuffer().append(this.deviceDirectory).append(str).toString();
                            } else {
                                string = str;
                            }
                            if (this.osName.equals("Solaris") || this.osName.equals("SunOS")) {
                                checkSolaris(string, i2);
                            } else if (testRead(string, i2)) {
                                CommPortIdentifier.addPortName(string, i2, this);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override // gnu.io.CommDriver
    public void initialize() {
        this.osName = System.getProperty("os.name");
        this.deviceDirectory = getDeviceDirectory();
        for (int i2 = 1; i2 <= 2; i2++) {
            if (!registerSpecifiedPorts(i2) && !registerKnownPorts(i2)) {
                registerScannedPorts(i2);
            }
        }
    }

    private void addSpecifiedPorts(String str, int i2) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, System.getProperty("path.separator", CallSiteDescriptor.TOKEN_DELIMITER));
        while (stringTokenizer.hasMoreElements()) {
            String strNextToken = stringTokenizer.nextToken();
            if (testRead(strNextToken, i2)) {
                CommPortIdentifier.addPortName(strNextToken, i2, this);
            }
        }
    }

    private boolean registerSpecifiedPorts(int i2) {
        String property = null;
        Properties properties = System.getProperties();
        try {
            FileInputStream fileInputStream = new FileInputStream(new StringBuffer().append(new StringBuffer().append(System.getProperty("java.ext.dirs")).append(System.getProperty("file.separator")).toString()).append("gnu.io.rxtx.properties").toString());
            Properties properties2 = new Properties();
            properties2.load(fileInputStream);
            System.setProperties(properties2);
            Iterator<Object> it = properties2.keySet().iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                System.setProperty(str, properties2.getProperty(str));
            }
        } catch (Exception e2) {
        }
        switch (i2) {
            case 1:
                String property2 = System.getProperty("gnu.io.rxtx.SerialPorts");
                property = property2;
                if (property2 == null) {
                    property = System.getProperty("gnu.io.SerialPorts");
                    break;
                }
                break;
            case 2:
                String property3 = System.getProperty("gnu.io.rxtx.ParallelPorts");
                property = property3;
                if (property3 == null) {
                    property = System.getProperty("gnu.io.ParallelPorts");
                    break;
                }
                break;
        }
        System.setProperties(properties);
        if (property != null) {
            addSpecifiedPorts(property, i2);
            return true;
        }
        return false;
    }

    private void registerScannedPorts(int i2) {
        String[] list;
        if (this.osName.equals("Windows CE")) {
            list = new String[]{"COM1:", "COM2:", "COM3:", "COM4:", "COM5:", "COM6:", "COM7:", "COM8:"};
        } else if (this.osName.toLowerCase().indexOf("windows") != -1) {
            String[] strArr = new String[259];
            for (int i3 = 1; i3 <= 256; i3++) {
                strArr[i3 - 1] = new StringBuffer().append("COM").append(i3).toString();
            }
            for (int i4 = 1; i4 <= 3; i4++) {
                strArr[i4 + 255] = new StringBuffer().append("LPT").append(i4).toString();
            }
            list = strArr;
        } else if (this.osName.equals("Solaris") || this.osName.equals("SunOS")) {
            String[] strArr2 = new String[2];
            int i5 = 0;
            if (new File("/dev/term").list().length > 0) {
                i5 = 0 + 1;
                strArr2[0] = "term/";
            }
            String[] strArr3 = new String[i5];
            while (true) {
                i5--;
                if (i5 < 0) {
                    break;
                } else {
                    strArr3[i5] = strArr2[i5];
                }
            }
            list = strArr3;
        } else {
            list = new File(this.deviceDirectory).list();
        }
        if (list == null) {
            return;
        }
        String[] strArr4 = new String[0];
        switch (i2) {
            case 1:
                if (this.osName.equals("Linux")) {
                    strArr4 = new String[]{"ttyS", "ttySA", "ttyUSB", "rfcomm", "ttyircomm"};
                    break;
                } else if (this.osName.equals("Linux-all-ports")) {
                    strArr4 = new String[]{"comx", "holter", "modem", "rfcomm", "ttyircomm", "ttycosa0c", "ttycosa1c", "ttyACM", "ttyC", "ttyCH", "ttyD", "ttyE", "ttyF", "ttyH", "ttyI", "ttyL", "ttyM", "ttyMX", "ttyP", "ttyR", "ttyS", "ttySI", "ttySR", "ttyT", "ttyUSB", "ttyV", "ttyW", "ttyX"};
                    break;
                } else if (this.osName.toLowerCase().indexOf("qnx") != -1) {
                    strArr4 = new String[]{"ser"};
                    break;
                } else if (this.osName.equals("Irix")) {
                    strArr4 = new String[]{"ttyc", "ttyd", "ttyf", "ttym", "ttyq", "tty4d", "tty4f", "midi", "us"};
                    break;
                } else if (this.osName.equals("FreeBSD")) {
                    strArr4 = new String[]{"ttyd", "cuaa", "ttyA", "cuaA", "ttyD", "cuaD", "ttyE", "cuaE", "ttyF", "cuaF", "ttyR", "cuaR", "stl"};
                    break;
                } else if (this.osName.equals("NetBSD")) {
                    strArr4 = new String[]{"tty0"};
                    break;
                } else if (this.osName.equals("Solaris") || this.osName.equals("SunOS")) {
                    strArr4 = new String[]{"term/", "cua/"};
                    break;
                } else if (this.osName.equals("HP-UX")) {
                    strArr4 = new String[]{"tty0p", "tty1p"};
                    break;
                } else if (this.osName.equals("UnixWare") || this.osName.equals("OpenUNIX")) {
                    strArr4 = new String[]{"tty00s", "tty01s", "tty02s", "tty03s"};
                    break;
                } else if (this.osName.equals("OpenServer")) {
                    strArr4 = new String[]{"tty1A", "tty2A", "tty3A", "tty4A", "tty5A", "tty6A", "tty7A", "tty8A", "tty9A", "tty10A", "tty11A", "tty12A", "tty13A", "tty14A", "tty15A", "tty16A", "ttyu1A", "ttyu2A", "ttyu3A", "ttyu4A", "ttyu5A", "ttyu6A", "ttyu7A", "ttyu8A", "ttyu9A", "ttyu10A", "ttyu11A", "ttyu12A", "ttyu13A", "ttyu14A", "ttyu15A", "ttyu16A"};
                    break;
                } else if (this.osName.equals("Compaq's Digital UNIX") || this.osName.equals("OSF1")) {
                    strArr4 = new String[]{"tty0"};
                    break;
                } else if (this.osName.equals("BeOS")) {
                    strArr4 = new String[]{"serial"};
                    break;
                } else if (this.osName.equals("Mac OS X")) {
                    strArr4 = new String[]{"cu.KeyUSA28X191.", "tty.KeyUSA28X191.", "cu.KeyUSA28X181.", "tty.KeyUSA28X181.", "cu.KeyUSA19181.", "tty.KeyUSA19181."};
                    break;
                } else if (this.osName.toLowerCase().indexOf("windows") != -1) {
                    strArr4 = new String[]{"COM"};
                    break;
                }
                break;
            case 2:
                if (this.osName.equals("Linux")) {
                    strArr4 = new String[]{"lp"};
                    break;
                } else if (this.osName.equals("FreeBSD")) {
                    strArr4 = new String[]{"lpt"};
                    break;
                } else if (this.osName.toLowerCase().indexOf("windows") != -1) {
                    strArr4 = new String[]{"LPT"};
                    break;
                } else {
                    strArr4 = new String[0];
                    break;
                }
        }
        registerValidPorts(list, strArr4, i2);
    }

    @Override // gnu.io.CommDriver
    public CommPort getCommPort(String str, int i2) {
        try {
            switch (i2) {
                case 1:
                    if (this.osName.toLowerCase().indexOf("windows") == -1) {
                        return new RXTXPort(str);
                    }
                    return new RXTXPort(new StringBuffer().append(this.deviceDirectory).append(str).toString());
                case 2:
                    return new LPRPort(str);
                default:
                    return null;
            }
        } catch (PortInUseException e2) {
            return null;
        }
    }

    public void Report(String str) {
        System.out.println(str);
    }
}
