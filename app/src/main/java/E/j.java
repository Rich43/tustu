package E;

import bH.C;
import bH.C0995c;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: TunerStudioMS.jar:E/j.class */
public class j {
    private j() {
    }

    public static InetAddress a(String str) {
        try {
            return InetAddress.getByAddress(b(str));
        } catch (UnknownHostException e2) {
            throw new InvalidParameterException("Unknown host!");
        }
    }

    public static byte[] b(String str) {
        String[] strArrSplit = str.split("\\.");
        if (strArrSplit.length != 4) {
            throw new InvalidParameterException("Not a IP address!");
        }
        int[] iArr = new int[4];
        for (int i2 = 0; i2 < 4; i2++) {
            iArr[i2] = Integer.parseInt(strArrSplit[i2]);
            if (iArr[i2] < 0 || iArr[i2] > 255) {
                throw new InvalidParameterException("Invalid IP address!");
            }
        }
        return new byte[]{(byte) iArr[0], (byte) iArr[1], (byte) iArr[2], (byte) iArr[3]};
    }

    public static int a(byte[] bArr) {
        if (bArr.length != 4) {
            throw new InvalidParameterException("Byte array must have 4 elements.");
        }
        return ((bArr[0] & 255) << 24) | ((bArr[1] & 255) << 16) | ((bArr[2] & 255) << 8) | (bArr[3] & 255);
    }

    public static String a(int i2) {
        int[] iArr = {(i2 & (-16777216)) >>> 24, (i2 & 16711680) >>> 16, (i2 & NormalizerImpl.CC_MASK) >>> 8, i2 & 255};
        return String.format("%d.%d.%d.%d", Integer.valueOf(iArr[0]), Integer.valueOf(iArr[1]), Integer.valueOf(iArr[2]), Integer.valueOf(iArr[3]));
    }

    public static String[] a(String str, String str2) {
        int iA = a(b(str));
        int iA2 = a(b(str2));
        ArrayList arrayList = new ArrayList();
        for (int i2 = iA; i2 <= iA2; i2++) {
            arrayList.add(a(i2));
        }
        return (String[]) arrayList.toArray(new String[0]);
    }

    public static String a() {
        String hostName = System.getenv("COMPUTERNAME");
        if (hostName == null || hostName.isEmpty()) {
            hostName = System.getenv("USERDOMAIN_ROAMINGPROFILE");
        }
        if (hostName == null || hostName.isEmpty()) {
            hostName = System.getenv("USERDOMAIN");
        }
        if (hostName == null || hostName.isEmpty()) {
            try {
                hostName = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e2) {
                Logger.getLogger(j.class.getName()).log(Level.WARNING, "Failed to get host name", (Throwable) e2);
            }
        }
        return hostName;
    }

    public static InetAddress a(NetworkInterface networkInterface) {
        InetAddress inetAddress = null;
        Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
        while (inetAddresses.hasMoreElements()) {
            InetAddress inetAddressNextElement = inetAddresses.nextElement2();
            if (inetAddressNextElement.getAddress().length == 4) {
                inetAddress = inetAddressNextElement;
            }
        }
        return inetAddress;
    }

    public static String a(InetAddress inetAddress) {
        return inetAddress.getHostAddress().contains("/") ? inetAddress.getHostAddress().substring(inetAddress.getHostAddress().lastIndexOf("/") + 1) : inetAddress.getHostAddress();
    }

    public static long b(NetworkInterface networkInterface) {
        try {
            byte[] hardwareAddress = networkInterface.getHardwareAddress();
            return C0995c.b(hardwareAddress, 0, hardwareAddress.length, true, false);
        } catch (SocketException e2) {
            C.a("unable to get MAC for: " + ((Object) networkInterface));
            return -1L;
        }
    }
}
