package sun.security.krb5;

import sun.security.krb5.internal.HostAddress;
import sun.security.krb5.internal.KerberosTime;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.KrbApErrException;
import sun.security.krb5.internal.SeqNumber;

/* loaded from: rt.jar:sun/security/krb5/KrbAppMessage.class */
abstract class KrbAppMessage {
    private static boolean DEBUG = Krb5.DEBUG;

    KrbAppMessage() {
    }

    void check(KerberosTime kerberosTime, Integer num, Integer num2, HostAddress hostAddress, HostAddress hostAddress2, SeqNumber seqNumber, HostAddress hostAddress3, HostAddress hostAddress4, boolean z2, boolean z3, PrincipalName principalName) throws KrbApErrException {
        if (hostAddress3 != null && (hostAddress == null || hostAddress3 == null || !hostAddress.equals(hostAddress3))) {
            if (DEBUG && hostAddress == null) {
                System.out.println("packetSAddress is null");
            }
            if (DEBUG && hostAddress3 == null) {
                System.out.println("sAddress is null");
            }
            throw new KrbApErrException(38);
        }
        if (hostAddress4 != null && (hostAddress2 == null || hostAddress4 == null || !hostAddress2.equals(hostAddress4))) {
            throw new KrbApErrException(38);
        }
        if (kerberosTime != null) {
            if (num != null) {
                kerberosTime = kerberosTime.withMicroSeconds(num.intValue());
            }
            if (!kerberosTime.inClockSkew()) {
                throw new KrbApErrException(37);
            }
        } else if (z2) {
            throw new KrbApErrException(37);
        }
        if (seqNumber == null && z3) {
            throw new KrbApErrException(400);
        }
        if (num2 != null && seqNumber != null) {
            if (num2.intValue() != seqNumber.current()) {
                throw new KrbApErrException(42);
            }
            seqNumber.step();
        } else if (z3) {
            throw new KrbApErrException(42);
        }
        if (kerberosTime == null && num2 == null) {
            throw new KrbApErrException(41);
        }
    }
}
