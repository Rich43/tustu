package sun.security.krb5.internal;

import java.io.IOException;
import java.math.BigInteger;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.KrbException;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/KDCReq.class */
public class KDCReq {
    public KDCReqBody reqBody;
    public PAData[] pAData;
    private int pvno;
    private int msgType;

    public KDCReq(PAData[] pADataArr, KDCReqBody kDCReqBody, int i2) throws IOException {
        this.pAData = null;
        this.pvno = 5;
        this.msgType = i2;
        if (pADataArr != null) {
            this.pAData = new PAData[pADataArr.length];
            for (int i3 = 0; i3 < pADataArr.length; i3++) {
                if (pADataArr[i3] == null) {
                    throw new IOException("Cannot create a KDCRep");
                }
                this.pAData[i3] = (PAData) pADataArr[i3].clone();
            }
        }
        this.reqBody = kDCReqBody;
    }

    public KDCReq() {
        this.pAData = null;
    }

    public KDCReq(byte[] bArr, int i2) throws IOException, KrbException {
        this.pAData = null;
        init(new DerValue(bArr), i2);
    }

    public KDCReq(DerValue derValue, int i2) throws IOException, KrbException {
        this.pAData = null;
        init(derValue, i2);
    }

    protected void init(DerValue derValue, int i2) throws IOException, KrbException {
        if ((derValue.getTag() & 31) != i2) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        DerValue derValue2 = derValue.getData().getDerValue();
        if (derValue2.getTag() != 48) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        DerValue derValue3 = derValue2.getData().getDerValue();
        if ((derValue3.getTag() & 31) == 1) {
            this.pvno = derValue3.getData().getBigInteger().intValue();
            if (this.pvno != 5) {
                throw new KrbApErrException(39);
            }
            DerValue derValue4 = derValue2.getData().getDerValue();
            if ((derValue4.getTag() & 31) == 2) {
                this.msgType = derValue4.getData().getBigInteger().intValue();
                if (this.msgType != i2) {
                    throw new KrbApErrException(40);
                }
                this.pAData = PAData.parseSequence(derValue2.getData(), (byte) 3, true);
                DerValue derValue5 = derValue2.getData().getDerValue();
                if ((derValue5.getTag() & 31) == 4) {
                    this.reqBody = new KDCReqBody(derValue5.getData().getDerValue(), this.msgType);
                    return;
                }
                throw new Asn1Exception(Krb5.ASN1_BAD_ID);
            }
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        throw new Asn1Exception(Krb5.ASN1_BAD_ID);
    }

    public byte[] asn1Encode() throws Asn1Exception, IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        derOutputStream.putInteger(BigInteger.valueOf(this.pvno));
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream);
        DerOutputStream derOutputStream3 = new DerOutputStream();
        derOutputStream3.putInteger(BigInteger.valueOf(this.msgType));
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 2), derOutputStream3);
        if (this.pAData != null && this.pAData.length > 0) {
            DerOutputStream derOutputStream4 = new DerOutputStream();
            for (int i2 = 0; i2 < this.pAData.length; i2++) {
                derOutputStream4.write(this.pAData[i2].asn1Encode());
            }
            DerOutputStream derOutputStream5 = new DerOutputStream();
            derOutputStream5.write((byte) 48, derOutputStream4);
            derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 3), derOutputStream5);
        }
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 4), this.reqBody.asn1Encode(this.msgType));
        DerOutputStream derOutputStream6 = new DerOutputStream();
        derOutputStream6.write((byte) 48, derOutputStream2);
        DerOutputStream derOutputStream7 = new DerOutputStream();
        derOutputStream7.write(DerValue.createTag((byte) 64, true, (byte) this.msgType), derOutputStream6);
        return derOutputStream7.toByteArray();
    }

    public byte[] asn1EncodeReqBody() throws Asn1Exception, IOException {
        return this.reqBody.asn1Encode(this.msgType);
    }
}
