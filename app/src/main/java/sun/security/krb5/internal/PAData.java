package sun.security.krb5.internal;

import java.io.IOException;
import java.util.Vector;
import net.lingala.zip4j.util.InternalZipConstants;
import sun.misc.HexDumpEncoder;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.internal.crypto.EType;
import sun.security.krb5.internal.util.KerberosString;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/PAData.class */
public class PAData {
    private int pADataType;
    private byte[] pADataValue;
    private static final byte TAG_PATYPE = 1;
    private static final byte TAG_PAVALUE = 2;

    private PAData() {
        this.pADataValue = null;
    }

    public PAData(int i2, byte[] bArr) {
        this.pADataValue = null;
        this.pADataType = i2;
        if (bArr != null) {
            this.pADataValue = (byte[]) bArr.clone();
        }
    }

    public Object clone() {
        PAData pAData = new PAData();
        pAData.pADataType = this.pADataType;
        if (this.pADataValue != null) {
            pAData.pADataValue = new byte[this.pADataValue.length];
            System.arraycopy(this.pADataValue, 0, pAData.pADataValue, 0, this.pADataValue.length);
        }
        return pAData;
    }

    public PAData(DerValue derValue) throws Asn1Exception, IOException {
        this.pADataValue = null;
        if (derValue.getTag() != 48) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        DerValue derValue2 = derValue.getData().getDerValue();
        if ((derValue2.getTag() & 31) == 1) {
            this.pADataType = derValue2.getData().getBigInteger().intValue();
            DerValue derValue3 = derValue.getData().getDerValue();
            if ((derValue3.getTag() & 31) == 2) {
                this.pADataValue = derValue3.getData().getOctetString();
            }
            if (derValue.getData().available() > 0) {
                throw new Asn1Exception(Krb5.ASN1_BAD_ID);
            }
            return;
        }
        throw new Asn1Exception(Krb5.ASN1_BAD_ID);
    }

    public byte[] asn1Encode() throws Asn1Exception, IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.putInteger(this.pADataType);
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream2);
        DerOutputStream derOutputStream3 = new DerOutputStream();
        derOutputStream3.putOctetString(this.pADataValue);
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 2), derOutputStream3);
        DerOutputStream derOutputStream4 = new DerOutputStream();
        derOutputStream4.write((byte) 48, derOutputStream);
        return derOutputStream4.toByteArray();
    }

    public int getType() {
        return this.pADataType;
    }

    public byte[] getValue() {
        if (this.pADataValue == null) {
            return null;
        }
        return (byte[]) this.pADataValue.clone();
    }

    public static PAData[] parseSequence(DerInputStream derInputStream, byte b2, boolean z2) throws Asn1Exception, IOException {
        if (z2 && (((byte) derInputStream.peekByte()) & 31) != b2) {
            return null;
        }
        DerValue derValue = derInputStream.getDerValue().getData().getDerValue();
        if (derValue.getTag() != 48) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        Vector vector = new Vector();
        while (derValue.getData().available() > 0) {
            vector.addElement(new PAData(derValue.getData().getDerValue()));
        }
        if (vector.size() > 0) {
            PAData[] pADataArr = new PAData[vector.size()];
            vector.copyInto(pADataArr);
            return pADataArr;
        }
        return null;
    }

    public static int getPreferredEType(PAData[] pADataArr, int i2) throws Asn1Exception, IOException {
        if (pADataArr == null) {
            return i2;
        }
        DerValue derValue = null;
        DerValue derValue2 = null;
        for (PAData pAData : pADataArr) {
            if (pAData.getValue() != null) {
                switch (pAData.getType()) {
                    case 11:
                        derValue = new DerValue(pAData.getValue());
                        break;
                    case 19:
                        derValue2 = new DerValue(pAData.getValue());
                        break;
                }
            }
        }
        if (derValue2 != null) {
            while (derValue2.data.available() > 0) {
                ETypeInfo2 eTypeInfo2 = new ETypeInfo2(derValue2.data.getDerValue());
                if (EType.isNewer(eTypeInfo2.getEType()) || eTypeInfo2.getParams() == null) {
                    return eTypeInfo2.getEType();
                }
            }
        }
        if (derValue != null && derValue.data.available() > 0) {
            return new ETypeInfo(derValue.data.getDerValue()).getEType();
        }
        return i2;
    }

    /* loaded from: rt.jar:sun/security/krb5/internal/PAData$SaltAndParams.class */
    public static class SaltAndParams {
        public final String salt;
        public final byte[] params;

        public SaltAndParams(String str, byte[] bArr) {
            if (str != null && str.isEmpty()) {
                str = null;
            }
            this.salt = str;
            this.params = bArr;
        }
    }

    public static SaltAndParams getSaltAndParams(int i2, PAData[] pADataArr) throws Asn1Exception, IOException {
        if (pADataArr == null) {
            return null;
        }
        DerValue derValue = null;
        DerValue derValue2 = null;
        String str = null;
        for (PAData pAData : pADataArr) {
            if (pAData.getValue() != null) {
                switch (pAData.getType()) {
                    case 3:
                        str = new String(pAData.getValue(), KerberosString.MSNAME ? InternalZipConstants.CHARSET_UTF8 : "8859_1");
                        break;
                    case 11:
                        derValue = new DerValue(pAData.getValue());
                        break;
                    case 19:
                        derValue2 = new DerValue(pAData.getValue());
                        break;
                }
            }
        }
        if (derValue2 != null) {
            while (derValue2.data.available() > 0) {
                ETypeInfo2 eTypeInfo2 = new ETypeInfo2(derValue2.data.getDerValue());
                if (eTypeInfo2.getEType() == i2 && (EType.isNewer(i2) || eTypeInfo2.getParams() == null)) {
                    return new SaltAndParams(eTypeInfo2.getSalt(), eTypeInfo2.getParams());
                }
            }
        }
        if (derValue != null) {
            while (derValue.data.available() > 0) {
                ETypeInfo eTypeInfo = new ETypeInfo(derValue.data.getDerValue());
                if (eTypeInfo.getEType() == i2) {
                    return new SaltAndParams(eTypeInfo.getSalt(), null);
                }
            }
        }
        if (str != null) {
            return new SaltAndParams(str, null);
        }
        return null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(">>>Pre-Authentication Data:\n\t PA-DATA type = ").append(this.pADataType).append('\n');
        switch (this.pADataType) {
            case 2:
                sb.append("\t PA-ENC-TIMESTAMP");
                break;
            case 11:
                if (this.pADataValue != null) {
                    try {
                        DerValue derValue = new DerValue(this.pADataValue);
                        while (derValue.data.available() > 0) {
                            ETypeInfo eTypeInfo = new ETypeInfo(derValue.data.getDerValue());
                            sb.append("\t PA-ETYPE-INFO etype = ").append(eTypeInfo.getEType()).append(", salt = ").append(eTypeInfo.getSalt()).append('\n');
                        }
                        break;
                    } catch (IOException | Asn1Exception e2) {
                        sb.append("\t <Unparseable PA-ETYPE-INFO>\n");
                        break;
                    }
                }
                break;
            case 19:
                if (this.pADataValue != null) {
                    try {
                        DerValue derValue2 = new DerValue(this.pADataValue);
                        while (derValue2.data.available() > 0) {
                            ETypeInfo2 eTypeInfo2 = new ETypeInfo2(derValue2.data.getDerValue());
                            sb.append("\t PA-ETYPE-INFO2 etype = ").append(eTypeInfo2.getEType()).append(", salt = ").append(eTypeInfo2.getSalt()).append(", s2kparams = ");
                            byte[] params = eTypeInfo2.getParams();
                            if (params == null) {
                                sb.append("null\n");
                            } else if (params.length == 0) {
                                sb.append("empty\n");
                            } else {
                                sb.append(new HexDumpEncoder().encodeBuffer(params));
                            }
                        }
                        break;
                    } catch (IOException | Asn1Exception e3) {
                        sb.append("\t <Unparseable PA-ETYPE-INFO>\n");
                        break;
                    }
                }
                break;
            case 129:
                sb.append("\t PA-FOR-USER\n");
                break;
        }
        return sb.toString();
    }
}
