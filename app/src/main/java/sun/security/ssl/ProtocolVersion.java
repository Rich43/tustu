package sun.security.ssl;

import java.security.CryptoPrimitive;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

/* loaded from: jsse.jar:sun/security/ssl/ProtocolVersion.class */
public enum ProtocolVersion {
    TLS13(772, "TLSv1.3"),
    TLS12(771, "TLSv1.2"),
    TLS11(770, "TLSv1.1"),
    TLS10(769, "TLSv1"),
    SSL30(768, "SSLv3"),
    SSL20Hello(2, "SSLv2Hello"),
    NONE(-1, "NONE");

    final int id;
    final String name;
    final byte major;
    final byte minor;
    final boolean isAvailable;
    static final int LIMIT_MAX_VALUE = 65535;
    static final int LIMIT_MIN_VALUE = 0;
    static final ProtocolVersion[] PROTOCOLS_TO_10 = {TLS10, SSL30};
    static final ProtocolVersion[] PROTOCOLS_TO_11 = {TLS11, TLS10, SSL30};
    static final ProtocolVersion[] PROTOCOLS_TO_12 = {TLS12, TLS11, TLS10, SSL30};
    static final ProtocolVersion[] PROTOCOLS_TO_13 = {TLS13, TLS12, TLS11, TLS10, SSL30};
    static final ProtocolVersion[] PROTOCOLS_OF_NONE = {NONE};
    static final ProtocolVersion[] PROTOCOLS_OF_30 = {SSL30};
    static final ProtocolVersion[] PROTOCOLS_OF_11 = {TLS11};
    static final ProtocolVersion[] PROTOCOLS_OF_12 = {TLS12};
    static final ProtocolVersion[] PROTOCOLS_OF_13 = {TLS13};
    static final ProtocolVersion[] PROTOCOLS_10_11 = {TLS11, TLS10};
    static final ProtocolVersion[] PROTOCOLS_11_12 = {TLS12, TLS11};
    static final ProtocolVersion[] PROTOCOLS_12_13 = {TLS13, TLS12};
    static final ProtocolVersion[] PROTOCOLS_10_12 = {TLS12, TLS11, TLS10};
    static final ProtocolVersion[] PROTOCOLS_TO_TLS12 = {TLS12, TLS11, TLS10, SSL30};
    static final ProtocolVersion[] PROTOCOLS_TO_TLS11 = {TLS11, TLS10, SSL30};
    static final ProtocolVersion[] PROTOCOLS_TO_TLS10 = {TLS10, SSL30};
    static final ProtocolVersion[] PROTOCOLS_EMPTY = new ProtocolVersion[0];

    ProtocolVersion(int i2, String str) {
        this.id = i2;
        this.name = str;
        this.major = (byte) ((i2 >>> 8) & 255);
        this.minor = (byte) (i2 & 255);
        this.isAvailable = SSLAlgorithmConstraints.DEFAULT_SSL_ONLY.permits(EnumSet.of(CryptoPrimitive.KEY_AGREEMENT), str, null);
    }

    static ProtocolVersion valueOf(byte b2, byte b3) {
        for (ProtocolVersion protocolVersion : values()) {
            if (protocolVersion.major == b2 && protocolVersion.minor == b3) {
                return protocolVersion;
            }
        }
        return null;
    }

    static ProtocolVersion valueOf(int i2) {
        for (ProtocolVersion protocolVersion : values()) {
            if (protocolVersion.id == i2) {
                return protocolVersion;
            }
        }
        return null;
    }

    static String nameOf(byte b2, byte b3) {
        for (ProtocolVersion protocolVersion : values()) {
            if (protocolVersion.major == b2 && protocolVersion.minor == b3) {
                return protocolVersion.name;
            }
        }
        return "TLS-" + ((int) b2) + "." + ((int) b3);
    }

    static String nameOf(int i2) {
        return nameOf((byte) ((i2 >>> 8) & 255), (byte) (i2 & 255));
    }

    static ProtocolVersion nameOf(String str) {
        for (ProtocolVersion protocolVersion : values()) {
            if (protocolVersion.name.equals(str)) {
                return protocolVersion;
            }
        }
        return null;
    }

    static boolean isNegotiable(byte b2, byte b3, boolean z2) {
        int i2 = ((b2 & 255) << 8) | (b3 & 255);
        if (i2 < SSL30.id) {
            if (!z2 || i2 != SSL20Hello.id) {
                return false;
            }
            return true;
        }
        return true;
    }

    static String[] toStringArray(List<ProtocolVersion> list) {
        if (list != null && !list.isEmpty()) {
            String[] strArr = new String[list.size()];
            int i2 = 0;
            Iterator<ProtocolVersion> it = list.iterator();
            while (it.hasNext()) {
                int i3 = i2;
                i2++;
                strArr[i3] = it.next().name;
            }
            return strArr;
        }
        return new String[0];
    }

    static String[] toStringArray(int[] iArr) {
        if (iArr != null && iArr.length != 0) {
            String[] strArr = new String[iArr.length];
            int i2 = 0;
            for (int i3 : iArr) {
                int i4 = i2;
                i2++;
                strArr[i4] = nameOf(i3);
            }
            return strArr;
        }
        return new String[0];
    }

    static List<ProtocolVersion> namesOf(String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList(strArr.length);
        for (String str : strArr) {
            ProtocolVersion protocolVersionNameOf = nameOf(str);
            if (protocolVersionNameOf == null) {
                throw new IllegalArgumentException("Unsupported protocol" + str);
            }
            arrayList.add(protocolVersionNameOf);
        }
        return Collections.unmodifiableList(arrayList);
    }

    static boolean useTLS12PlusSpec(String str) {
        ProtocolVersion protocolVersionNameOf = nameOf(str);
        return (protocolVersionNameOf == null || protocolVersionNameOf == NONE || protocolVersionNameOf.id < TLS12.id) ? false : true;
    }

    int compare(ProtocolVersion protocolVersion) {
        if (this == protocolVersion) {
            return 0;
        }
        if (this == NONE) {
            return -1;
        }
        if (protocolVersion == NONE) {
            return 1;
        }
        return this.id - protocolVersion.id;
    }

    boolean useTLS13PlusSpec() {
        return this.id >= TLS13.id;
    }

    boolean useTLS12PlusSpec() {
        return this.id >= TLS12.id;
    }

    boolean useTLS11PlusSpec() {
        return this.id >= TLS11.id;
    }

    boolean useTLS10PlusSpec() {
        return this.id >= TLS10.id;
    }

    static boolean useTLS10PlusSpec(int i2) {
        return i2 >= TLS10.id;
    }

    static boolean useTLS13PlusSpec(int i2) {
        return i2 >= TLS13.id;
    }

    static ProtocolVersion selectedFrom(List<ProtocolVersion> list, int i2) {
        ProtocolVersion protocolVersion = NONE;
        for (ProtocolVersion protocolVersion2 : list) {
            if (protocolVersion2.id == i2) {
                return protocolVersion2;
            }
            if (protocolVersion2.id < i2 && protocolVersion2.id > protocolVersion.id) {
                protocolVersion = protocolVersion2;
            }
        }
        return protocolVersion;
    }
}
