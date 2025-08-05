package sun.security.krb5;

import java.io.IOException;
import sun.security.krb5.internal.APRep;
import sun.security.krb5.internal.EncAPRepPart;
import sun.security.krb5.internal.KRBError;
import sun.security.krb5.internal.KdcErrException;
import sun.security.krb5.internal.KerberosTime;
import sun.security.krb5.internal.KrbApErrException;
import sun.security.krb5.internal.LocalSeqNumber;
import sun.security.krb5.internal.SeqNumber;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/KrbApRep.class */
public class KrbApRep {
    private byte[] obuf;
    private byte[] ibuf;
    private EncAPRepPart encPart;
    private APRep apRepMessg;

    public KrbApRep(KrbApReq krbApReq, boolean z2, EncryptionKey encryptionKey) throws IOException, KrbException {
        init(krbApReq, encryptionKey, new LocalSeqNumber());
    }

    public KrbApRep(byte[] bArr, Credentials credentials, KrbApReq krbApReq) throws IOException, KrbException {
        this(bArr, credentials);
        authenticate(krbApReq);
    }

    private void init(KrbApReq krbApReq, EncryptionKey encryptionKey, SeqNumber seqNumber) throws IOException, KrbException {
        createMessage(krbApReq.getCreds().key, krbApReq.getCtime(), krbApReq.cusec(), encryptionKey, seqNumber);
        this.obuf = this.apRepMessg.asn1Encode();
    }

    private KrbApRep(byte[] bArr, Credentials credentials) throws IOException, KrbException {
        this(new DerValue(bArr), credentials);
    }

    private KrbApRep(DerValue derValue, Credentials credentials) throws IOException, KrbException {
        String strSubstring;
        try {
            APRep aPRep = new APRep(derValue);
            this.encPart = new EncAPRepPart(new DerValue(aPRep.encPart.reset(aPRep.encPart.decrypt(credentials.key, 12))));
        } catch (Asn1Exception e2) {
            KRBError kRBError = new KRBError(derValue);
            String errorString = kRBError.getErrorString();
            if (errorString.charAt(errorString.length() - 1) == 0) {
                strSubstring = errorString.substring(0, errorString.length() - 1);
            } else {
                strSubstring = errorString;
            }
            KrbException krbException = new KrbException(kRBError.getErrorCode(), strSubstring);
            krbException.initCause(e2);
            throw krbException;
        }
    }

    private void authenticate(KrbApReq krbApReq) throws IOException, KrbException {
        if (this.encPart.ctime.getSeconds() != krbApReq.getCtime().getSeconds() || this.encPart.cusec != krbApReq.getCtime().getMicroSeconds()) {
            throw new KrbApErrException(46);
        }
    }

    public EncryptionKey getSubKey() {
        return this.encPart.getSubKey();
    }

    public Integer getSeqNumber() {
        return this.encPart.getSeqNumber();
    }

    public byte[] getMessage() {
        return this.obuf;
    }

    private void createMessage(EncryptionKey encryptionKey, KerberosTime kerberosTime, int i2, EncryptionKey encryptionKey2, SeqNumber seqNumber) throws KrbCryptoException, KdcErrException, Asn1Exception, IOException {
        Integer num = null;
        if (seqNumber != null) {
            num = new Integer(seqNumber.current());
        }
        this.encPart = new EncAPRepPart(kerberosTime, i2, encryptionKey2, num);
        this.apRepMessg = new APRep(new EncryptedData(encryptionKey, this.encPart.asn1Encode(), 12));
    }
}
