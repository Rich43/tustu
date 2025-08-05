package sun.security.krb5.internal;

import java.io.IOException;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.Config;
import sun.security.krb5.KrbException;
import sun.security.krb5.internal.util.KerberosFlags;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/KDCOptions.class */
public class KDCOptions extends KerberosFlags {
    private static final int KDC_OPT_PROXIABLE = 268435456;
    private static final int KDC_OPT_RENEWABLE_OK = 16;
    private static final int KDC_OPT_FORWARDABLE = 1073741824;
    public static final int RESERVED = 0;
    public static final int FORWARDABLE = 1;
    public static final int FORWARDED = 2;
    public static final int PROXIABLE = 3;
    public static final int PROXY = 4;
    public static final int ALLOW_POSTDATE = 5;
    public static final int POSTDATED = 6;
    public static final int UNUSED7 = 7;
    public static final int RENEWABLE = 8;
    public static final int UNUSED9 = 9;
    public static final int UNUSED10 = 10;
    public static final int UNUSED11 = 11;
    public static final int CNAME_IN_ADDL_TKT = 14;
    public static final int CANONICALIZE = 15;
    public static final int RENEWABLE_OK = 27;
    public static final int ENC_TKT_IN_SKEY = 28;
    public static final int RENEW = 30;
    public static final int VALIDATE = 31;
    private static final String[] names = {"RESERVED", "FORWARDABLE", "FORWARDED", "PROXIABLE", "PROXY", "ALLOW_POSTDATE", "POSTDATED", "UNUSED7", "RENEWABLE", "UNUSED9", "UNUSED10", "UNUSED11", null, null, "CNAME_IN_ADDL_TKT", "CANONICALIZE", null, null, null, null, null, null, null, null, null, null, null, "RENEWABLE_OK", "ENC_TKT_IN_SKEY", null, "RENEW", "VALIDATE"};
    private boolean DEBUG;

    public static KDCOptions with(int... iArr) throws ArrayIndexOutOfBoundsException {
        KDCOptions kDCOptions = new KDCOptions();
        for (int i2 : iArr) {
            kDCOptions.set(i2, true);
        }
        return kDCOptions;
    }

    public KDCOptions() throws ArrayIndexOutOfBoundsException {
        super(32);
        this.DEBUG = Krb5.DEBUG;
        setDefault();
    }

    public KDCOptions(int i2, byte[] bArr) throws Asn1Exception {
        super(i2, bArr);
        this.DEBUG = Krb5.DEBUG;
        if (i2 > bArr.length * 8 || i2 > 32) {
            throw new Asn1Exception(502);
        }
    }

    public KDCOptions(boolean[] zArr) throws Asn1Exception {
        super(zArr);
        this.DEBUG = Krb5.DEBUG;
        if (zArr.length > 32) {
            throw new Asn1Exception(502);
        }
    }

    public KDCOptions(DerValue derValue) throws Asn1Exception, IOException {
        this(derValue.getUnalignedBitString(true).toBooleanArray());
    }

    public KDCOptions(byte[] bArr) {
        super(bArr.length * 8, bArr);
        this.DEBUG = Krb5.DEBUG;
    }

    public static KDCOptions parse(DerInputStream derInputStream, byte b2, boolean z2) throws Asn1Exception, IOException {
        if (z2 && (((byte) derInputStream.peekByte()) & 31) != b2) {
            return null;
        }
        DerValue derValue = derInputStream.getDerValue();
        if (b2 != (derValue.getTag() & 31)) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        return new KDCOptions(derValue.getData().getDerValue());
    }

    @Override // sun.security.krb5.internal.util.KerberosFlags
    public void set(int i2, boolean z2) throws ArrayIndexOutOfBoundsException {
        super.set(i2, z2);
    }

    @Override // sun.security.krb5.internal.util.KerberosFlags
    public boolean get(int i2) throws ArrayIndexOutOfBoundsException {
        return super.get(i2);
    }

    @Override // sun.security.krb5.internal.util.KerberosFlags
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("KDCOptions: ");
        for (int i2 = 0; i2 < 32; i2++) {
            if (get(i2)) {
                if (names[i2] != null) {
                    sb.append(names[i2]).append(",");
                } else {
                    sb.append(i2).append(",");
                }
            }
        }
        return sb.toString();
    }

    private void setDefault() throws ArrayIndexOutOfBoundsException {
        try {
            Config config = Config.getInstance();
            int intValue = config.getIntValue("libdefaults", "kdc_default_options");
            if ((intValue & 16) == 16 || config.getBooleanValue("libdefaults", "renewable")) {
                set(27, true);
            }
            if ((intValue & 268435456) == 268435456 || config.getBooleanValue("libdefaults", "proxiable")) {
                set(3, true);
            }
            if ((intValue & 1073741824) == 1073741824 || config.getBooleanValue("libdefaults", "forwardable")) {
                set(1, true);
            }
        } catch (KrbException e2) {
            if (this.DEBUG) {
                System.out.println("Exception in getting default values for KDC Options from the configuration ");
                e2.printStackTrace();
            }
        }
    }
}
