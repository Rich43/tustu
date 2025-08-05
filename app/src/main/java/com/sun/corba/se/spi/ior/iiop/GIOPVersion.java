package com.sun.corba.se.spi.ior.iiop;

import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orb.ORBVersion;
import com.sun.corba.se.spi.orb.ORBVersionFactory;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/ior/iiop/GIOPVersion.class */
public class GIOPVersion {
    public static final GIOPVersion V1_0 = new GIOPVersion((byte) 1, (byte) 0);
    public static final GIOPVersion V1_1 = new GIOPVersion((byte) 1, (byte) 1);
    public static final GIOPVersion V1_2 = new GIOPVersion((byte) 1, (byte) 2);
    public static final GIOPVersion V1_3 = new GIOPVersion((byte) 1, (byte) 3);
    public static final GIOPVersion V13_XX = new GIOPVersion((byte) 13, (byte) 1);
    public static final GIOPVersion DEFAULT_VERSION = V1_2;
    public static final int VERSION_1_0 = 256;
    public static final int VERSION_1_1 = 257;
    public static final int VERSION_1_2 = 258;
    public static final int VERSION_1_3 = 259;
    public static final int VERSION_13_XX = 3329;
    private byte major;
    private byte minor;

    public GIOPVersion() {
        this.major = (byte) 0;
        this.minor = (byte) 0;
    }

    public GIOPVersion(byte b2, byte b3) {
        this.major = (byte) 0;
        this.minor = (byte) 0;
        this.major = b2;
        this.minor = b3;
    }

    public GIOPVersion(int i2, int i3) {
        this.major = (byte) 0;
        this.minor = (byte) 0;
        this.major = (byte) i2;
        this.minor = (byte) i3;
    }

    public byte getMajor() {
        return this.major;
    }

    public byte getMinor() {
        return this.minor;
    }

    public boolean equals(GIOPVersion gIOPVersion) {
        return gIOPVersion.major == this.major && gIOPVersion.minor == this.minor;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof GIOPVersion)) {
            return equals((GIOPVersion) obj);
        }
        return false;
    }

    public int hashCode() {
        return (37 * this.major) + this.minor;
    }

    public boolean lessThan(GIOPVersion gIOPVersion) {
        if (this.major < gIOPVersion.major) {
            return true;
        }
        if (this.major == gIOPVersion.major && this.minor < gIOPVersion.minor) {
            return true;
        }
        return false;
    }

    public int intValue() {
        return (this.major << 8) | this.minor;
    }

    public String toString() {
        return ((int) this.major) + "." + ((int) this.minor);
    }

    public static GIOPVersion getInstance(byte b2, byte b3) {
        switch ((b2 << 8) | b3) {
            case 256:
                return V1_0;
            case 257:
                return V1_1;
            case 258:
                return V1_2;
            case 259:
                return V1_3;
            case VERSION_13_XX /* 3329 */:
                return V13_XX;
            default:
                return new GIOPVersion(b2, b3);
        }
    }

    public static GIOPVersion parseVersion(String str) throws NumberFormatException {
        int iIndexOf = str.indexOf(46);
        if (iIndexOf < 1 || iIndexOf == str.length() - 1) {
            throw new NumberFormatException("GIOP major, minor, and decimal point required: " + str);
        }
        return getInstance((byte) Integer.parseInt(str.substring(0, iIndexOf)), (byte) Integer.parseInt(str.substring(iIndexOf + 1, str.length())));
    }

    public static GIOPVersion chooseRequestVersion(ORB orb, IOR ior) {
        GIOPVersion gIOPVersion = orb.getORBData().getGIOPVersion();
        IIOPProfile profile = ior.getProfile();
        GIOPVersion gIOPVersion2 = profile.getGIOPVersion();
        ORBVersion oRBVersion = profile.getORBVersion();
        if (!oRBVersion.equals(ORBVersionFactory.getFOREIGN()) && oRBVersion.lessThan(ORBVersionFactory.getNEWER())) {
            return V1_0;
        }
        byte major = gIOPVersion2.getMajor();
        byte minor = gIOPVersion2.getMinor();
        byte major2 = gIOPVersion.getMajor();
        byte minor2 = gIOPVersion.getMinor();
        if (major2 < major) {
            return gIOPVersion;
        }
        if (major2 > major) {
            return gIOPVersion2;
        }
        if (minor2 <= minor) {
            return gIOPVersion;
        }
        return gIOPVersion2;
    }

    public boolean supportsIORIIOPProfileComponents() {
        return getMinor() > 0 || getMajor() > 1;
    }

    public void read(InputStream inputStream) {
        this.major = inputStream.read_octet();
        this.minor = inputStream.read_octet();
    }

    public void write(OutputStream outputStream) {
        outputStream.write_octet(this.major);
        outputStream.write_octet(this.minor);
    }
}
