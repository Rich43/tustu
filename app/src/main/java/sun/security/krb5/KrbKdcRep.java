package sun.security.krb5;

import sun.security.krb5.internal.KDCRep;
import sun.security.krb5.internal.KDCReq;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.KrbApErrException;
import sun.security.krb5.internal.PAData;
import sun.security.util.DerInputStream;

/* loaded from: rt.jar:sun/security/krb5/KrbKdcRep.class */
abstract class KrbKdcRep {
    KrbKdcRep() {
    }

    static void check(boolean z2, KDCReq kDCReq, KDCRep kDCRep, EncryptionKey encryptionKey) throws KrbApErrException {
        if (z2 && !kDCReq.reqBody.cname.equals(kDCRep.cname) && ((!kDCReq.reqBody.kdcOptions.get(15) && kDCReq.reqBody.cname.getNameType() != 10) || !kDCRep.encKDCRepPart.flags.get(15))) {
            kDCRep.encKDCRepPart.key.destroy();
            throw new KrbApErrException(41);
        }
        if (!kDCReq.reqBody.sname.equals(kDCRep.encKDCRepPart.sname)) {
            String[] nameStrings = kDCRep.encKDCRepPart.sname.getNameStrings();
            if (z2 || !kDCReq.reqBody.kdcOptions.get(15) || nameStrings == null || nameStrings.length != 2 || !nameStrings[0].equals(PrincipalName.TGS_DEFAULT_SRV_NAME) || !kDCRep.encKDCRepPart.sname.getRealmString().equals(kDCReq.reqBody.sname.getRealmString())) {
                kDCRep.encKDCRepPart.key.destroy();
                throw new KrbApErrException(41);
            }
        }
        if (kDCReq.reqBody.getNonce() != kDCRep.encKDCRepPart.nonce) {
            kDCRep.encKDCRepPart.key.destroy();
            throw new KrbApErrException(41);
        }
        if (kDCReq.reqBody.addresses != null && kDCRep.encKDCRepPart.caddr != null && !kDCReq.reqBody.addresses.equals(kDCRep.encKDCRepPart.caddr)) {
            kDCRep.encKDCRepPart.key.destroy();
            throw new KrbApErrException(41);
        }
        for (int i2 = 2; i2 < 6; i2++) {
            if (kDCReq.reqBody.kdcOptions.get(i2) != kDCRep.encKDCRepPart.flags.get(i2)) {
                if (Krb5.DEBUG) {
                    System.out.println("> KrbKdcRep.check: at #" + i2 + ". request for " + kDCReq.reqBody.kdcOptions.get(i2) + ", received " + kDCRep.encKDCRepPart.flags.get(i2));
                }
                throw new KrbApErrException(41);
            }
        }
        if (kDCReq.reqBody.kdcOptions.get(8) && !kDCRep.encKDCRepPart.flags.get(8)) {
            throw new KrbApErrException(41);
        }
        if ((kDCReq.reqBody.from == null || kDCReq.reqBody.from.isZero()) && kDCRep.encKDCRepPart.starttime != null && !kDCRep.encKDCRepPart.starttime.inClockSkew()) {
            kDCRep.encKDCRepPart.key.destroy();
            throw new KrbApErrException(37);
        }
        if (kDCReq.reqBody.from != null && !kDCReq.reqBody.from.isZero() && kDCRep.encKDCRepPart.starttime != null && !kDCReq.reqBody.from.equals(kDCRep.encKDCRepPart.starttime)) {
            kDCRep.encKDCRepPart.key.destroy();
            throw new KrbApErrException(41);
        }
        if (!kDCReq.reqBody.till.isZero() && kDCRep.encKDCRepPart.endtime.greaterThan(kDCReq.reqBody.till)) {
            kDCRep.encKDCRepPart.key.destroy();
            throw new KrbApErrException(41);
        }
        if (kDCRep.encKDCRepPart.flags.get(15)) {
            boolean z3 = false;
            boolean zVerifyAnyChecksum = false;
            if (kDCReq.pAData != null) {
                PAData[] pADataArr = kDCReq.pAData;
                int length = pADataArr.length;
                int i3 = 0;
                while (true) {
                    if (i3 >= length) {
                        break;
                    }
                    if (pADataArr[i3].getType() != 149) {
                        i3++;
                    } else {
                        z3 = true;
                        break;
                    }
                }
            }
            if (kDCRep.encKDCRepPart.pAData != null) {
                PAData[] pADataArr2 = kDCRep.encKDCRepPart.pAData;
                int length2 = pADataArr2.length;
                int i4 = 0;
                while (true) {
                    if (i4 >= length2) {
                        break;
                    }
                    PAData pAData = pADataArr2[i4];
                    if (pAData.getType() != 149) {
                        i4++;
                    } else {
                        try {
                            zVerifyAnyChecksum = new Checksum(new DerInputStream(pAData.getValue()).getDerValue()).verifyAnyChecksum(kDCReq.asn1Encode(), encryptionKey, 56);
                            break;
                        } catch (Exception e2) {
                            if (Krb5.DEBUG) {
                                e2.printStackTrace();
                            }
                        }
                    }
                }
            }
            if (z3 && !zVerifyAnyChecksum) {
                throw new KrbApErrException(41);
            }
        }
        if (kDCReq.reqBody.kdcOptions.get(8) && kDCReq.reqBody.rtime != null && !kDCReq.reqBody.rtime.isZero()) {
            if (kDCRep.encKDCRepPart.renewTill == null || kDCRep.encKDCRepPart.renewTill.greaterThan(kDCReq.reqBody.rtime)) {
                kDCRep.encKDCRepPart.key.destroy();
                throw new KrbApErrException(41);
            }
        }
    }
}
