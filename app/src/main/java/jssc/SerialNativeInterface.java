package jssc;

import com.sun.glass.ui.Platform;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/* JADX WARN: Classes with same name are omitted:
  jssc.jar:jssc/SerialNativeInterface.class
 */
/* loaded from: jssc2.8.jar:jssc/SerialNativeInterface.class */
public class SerialNativeInterface {
    private static final String libVersion = "2.8";
    private static final String libMinorSuffix = "0";
    public static final int OS_LINUX = 0;
    public static final int OS_WINDOWS = 1;
    public static final int OS_SOLARIS = 2;
    public static final int OS_MAC_OS_X = 3;
    private static int osType;
    public static final long ERR_PORT_BUSY = -1;
    public static final long ERR_PORT_NOT_FOUND = -2;
    public static final long ERR_PERMISSION_DENIED = -3;
    public static final long ERR_INCORRECT_SERIAL_PORT = -4;
    public static final String PROPERTY_JSSC_NO_TIOCEXCL = "JSSC_NO_TIOCEXCL";
    public static final String PROPERTY_JSSC_IGNPAR = "JSSC_IGNPAR";
    public static final String PROPERTY_JSSC_PARMRK = "JSSC_PARMRK";

    public static native String getNativeLibraryVersion();

    public native long openPort(String str, boolean z2);

    public native boolean setParams(long j2, int i2, int i3, int i4, int i5, boolean z2, boolean z3, int i6);

    public native boolean purgePort(long j2, int i2);

    public native boolean closePort(long j2);

    public native boolean setEventsMask(long j2, int i2);

    public native int getEventsMask(long j2);

    public native int[][] waitEvents(long j2);

    public native boolean setRTS(long j2, boolean z2);

    public native boolean setDTR(long j2, boolean z2);

    public native byte[] readBytes(long j2, int i2);

    public native boolean writeBytes(long j2, byte[] bArr);

    public native int[] getBuffersBytesCount(long j2);

    public native boolean setFlowControlMode(long j2, int i2);

    public native int getFlowControlMode(long j2);

    public native String[] getSerialPortNames();

    public native int[] getLinesStatus(long j2);

    public native boolean sendBreak(long j2, int i2);

    static {
        osType = -1;
        String osName = System.getProperty("os.name");
        String architecture = System.getProperty("os.arch");
        String userHome = System.getProperty("user.home");
        String fileSeparator = System.getProperty("file.separator");
        String tmpFolder = System.getProperty("java.io.tmpdir");
        String libRootFolder = new File(userHome).canWrite() ? userHome : tmpFolder;
        String javaLibPath = System.getProperty("java.library.path");
        if (osName.equals("Linux")) {
            osName = "linux";
            osType = 0;
        } else if (osName.startsWith(Platform.WINDOWS)) {
            osName = "windows";
            osType = 1;
        } else if (osName.equals("SunOS")) {
            osName = "solaris";
            osType = 2;
        } else if (osName.equals("Mac OS X") || osName.equals("Darwin")) {
            osName = "mac_os_x";
            osType = 3;
        }
        if (architecture.equals("i386") || architecture.equals("i686")) {
            architecture = "x86";
        } else if (architecture.equals("amd64") || architecture.equals("universal")) {
            architecture = "x86_64";
        } else if (architecture.equals("arm")) {
            String floatStr = "sf";
            if (javaLibPath.toLowerCase().contains("gnueabihf") || javaLibPath.toLowerCase().contains("armhf")) {
                floatStr = "hf";
            } else {
                try {
                    Process readelfProcess = Runtime.getRuntime().exec("readelf -A /proc/self/exe");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(readelfProcess.getInputStream()));
                    while (true) {
                        String buffer = reader.readLine();
                        if (buffer == null || buffer.isEmpty()) {
                            break;
                        } else if (buffer.toLowerCase().contains("Tag_ABI_VFP_args".toLowerCase())) {
                            floatStr = "hf";
                            break;
                        }
                    }
                    reader.close();
                } catch (Exception e2) {
                }
            }
            architecture = "arm" + floatStr;
        }
        String libFolderPath = libRootFolder + fileSeparator + ".jssc" + fileSeparator + osName;
        String libName = System.mapLibraryName("jSSC-2.8_" + architecture);
        if (libName.endsWith(".dylib")) {
            libName = libName.replace(".dylib", ".jnilib");
        }
        boolean loadLib = false;
        if (isLibFolderExist(libFolderPath)) {
            if (isLibFileExist(libFolderPath + fileSeparator + libName) || extractLib(libFolderPath + fileSeparator + libName, osName, libName)) {
                loadLib = true;
            }
        } else if (new File(libFolderPath).mkdirs() && extractLib(libFolderPath + fileSeparator + libName, osName, libName)) {
            loadLib = true;
        }
        if (loadLib) {
            System.load(libFolderPath + fileSeparator + libName);
            String versionBase = getLibraryBaseVersion();
            String versionNative = getNativeLibraryVersion();
            if (!versionBase.equals(versionNative)) {
                System.err.println("Warning! jSSC Java and Native versions mismatch (Java: " + versionBase + ", Native: " + versionNative + ")");
            }
        }
    }

    private static boolean isLibFolderExist(String libFolderPath) {
        boolean returnValue = false;
        File folder = new File(libFolderPath);
        if (folder.exists() && folder.isDirectory()) {
            returnValue = true;
        }
        return returnValue;
    }

    private static boolean isLibFileExist(String libFilePath) {
        boolean returnValue = false;
        File folder = new File(libFilePath);
        if (folder.exists() && folder.isFile()) {
            returnValue = true;
        }
        return returnValue;
    }

    private static boolean extractLib(String libFilePath, String osName, String libName) {
        boolean returnValue = false;
        File libFile = new File(libFilePath);
        FileOutputStream output = null;
        InputStream input = SerialNativeInterface.class.getResourceAsStream("/libs/" + osName + "/" + libName);
        if (input != null) {
            byte[] buffer = new byte[4096];
            try {
                output = new FileOutputStream(libFilePath);
                while (true) {
                    int read = input.read(buffer);
                    if (read == -1) {
                        break;
                    }
                    output.write(buffer, 0, read);
                }
                output.close();
                input.close();
                returnValue = true;
            } catch (Exception e2) {
                try {
                    output.close();
                    if (libFile.exists()) {
                        libFile.delete();
                    }
                } catch (Exception e3) {
                }
                try {
                    input.close();
                } catch (Exception e4) {
                }
            }
        }
        return returnValue;
    }

    public static int getOsType() {
        return osType;
    }

    public static String getLibraryVersion() {
        return "2.8.0";
    }

    public static String getLibraryBaseVersion() {
        return libVersion;
    }

    public static String getLibraryMinorSuffix() {
        return "0";
    }
}
