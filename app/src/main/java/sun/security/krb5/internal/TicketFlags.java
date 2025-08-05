package sun.security.krb5.internal;

import java.io.IOException;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.internal.util.KerberosFlags;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/TicketFlags.class */
public class TicketFlags extends KerberosFlags {
    public TicketFlags() {
        super(32);
    }

    public TicketFlags(boolean[] zArr) throws Asn1Exception {
        super(zArr);
        if (zArr.length > 32) {
            throw new Asn1Exception(502);
        }
    }

    public TicketFlags(int i2, byte[] bArr) throws Asn1Exception {
        super(i2, bArr);
        if (i2 > bArr.length * 8 || i2 > 32) {
            throw new Asn1Exception(502);
        }
    }

    public TicketFlags(DerValue derValue) throws Asn1Exception, IOException {
        this(derValue.getUnalignedBitString(true).toBooleanArray());
    }

    public static TicketFlags parse(DerInputStream derInputStream, byte b2, boolean z2) throws Asn1Exception, IOException {
        if (z2 && (((byte) derInputStream.peekByte()) & 31) != b2) {
            return null;
        }
        DerValue derValue = derInputStream.getDerValue();
        if (b2 != (derValue.getTag() & 31)) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        return new TicketFlags(derValue.getData().getDerValue());
    }

    public Object clone() {
        try {
            return new TicketFlags(toBooleanArray());
        } catch (Exception e2) {
            return null;
        }
    }

    public boolean match(LoginOptions loginOptions) {
        boolean z2 = false;
        if (get(1) == loginOptions.get(1) && get(3) == loginOptions.get(3) && get(8) == loginOptions.get(8)) {
            z2 = true;
        }
        return z2;
    }

    public boolean match(TicketFlags ticketFlags) {
        for (int i2 = 0; i2 <= 31; i2++) {
            if (get(i2) != ticketFlags.get(i2)) {
                return false;
            }
        }
        return true;
    }

    @Override // sun.security.krb5.internal.util.KerberosFlags
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        boolean[] booleanArray = toBooleanArray();
        for (int i2 = 0; i2 < booleanArray.length; i2++) {
            if (booleanArray[i2]) {
                switch (i2) {
                    case 0:
                        stringBuffer.append("RESERVED;");
                        break;
                    case 1:
                        stringBuffer.append("FORWARDABLE;");
                        break;
                    case 2:
                        stringBuffer.append("FORWARDED;");
                        break;
                    case 3:
                        stringBuffer.append("PROXIABLE;");
                        break;
                    case 4:
                        stringBuffer.append("PROXY;");
                        break;
                    case 5:
                        stringBuffer.append("MAY-POSTDATE;");
                        break;
                    case 6:
                        stringBuffer.append("POSTDATED;");
                        break;
                    case 7:
                        stringBuffer.append("INVALID;");
                        break;
                    case 8:
                        stringBuffer.append("RENEWABLE;");
                        break;
                    case 9:
                        stringBuffer.append("INITIAL;");
                        break;
                    case 10:
                        stringBuffer.append("PRE-AUTHENT;");
                        break;
                    case 11:
                        stringBuffer.append("HW-AUTHENT;");
                        break;
                    case 15:
                        stringBuffer.append("ENC-PA-REP;");
                        break;
                }
            }
        }
        String string = stringBuffer.toString();
        if (string.length() > 0) {
            string = string.substring(0, string.length() - 1);
        }
        return string;
    }
}
