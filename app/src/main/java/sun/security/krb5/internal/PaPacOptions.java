package sun.security.krb5.internal;

import java.io.IOException;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.internal.util.KerberosFlags;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/PaPacOptions.class */
public class PaPacOptions {
    private static final int CLAIMS = 0;
    private static final int BRANCH_AWARE = 1;
    private static final int FORWARD_TO_FULL_DC = 2;
    private static final int RESOURCE_BASED_CONSTRAINED_DELEGATION = 3;
    private KerberosFlags flags;

    public PaPacOptions() {
        this.flags = new KerberosFlags(32);
    }

    public PaPacOptions(DerValue derValue) throws Asn1Exception, IOException {
        if (derValue.getTag() != 48) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        DerValue derValue2 = derValue.getData().getDerValue();
        if ((derValue2.getTag() & 31) == 0) {
            this.flags = new KDCOptions(derValue2.getData().getDerValue());
            return;
        }
        throw new Asn1Exception(Krb5.ASN1_BAD_ID);
    }

    public PaPacOptions setClaims(boolean z2) throws ArrayIndexOutOfBoundsException {
        this.flags.set(0, z2);
        return this;
    }

    public boolean getClaims() {
        return this.flags.get(0);
    }

    public PaPacOptions setBranchAware(boolean z2) throws ArrayIndexOutOfBoundsException {
        this.flags.set(1, z2);
        return this;
    }

    public boolean getBranchAware() {
        return this.flags.get(1);
    }

    public PaPacOptions setForwardToFullDC(boolean z2) throws ArrayIndexOutOfBoundsException {
        this.flags.set(2, z2);
        return this;
    }

    public boolean getForwardToFullDC() {
        return this.flags.get(2);
    }

    public PaPacOptions setResourceBasedConstrainedDelegation(boolean z2) throws ArrayIndexOutOfBoundsException {
        this.flags.set(3, z2);
        return this;
    }

    public boolean getResourceBasedConstrainedDelegation() {
        return this.flags.get(3);
    }

    public byte[] asn1Encode() throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        Throwable th = null;
        try {
            try {
                derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), this.flags.asn1Encode());
                byte[] byteArray = derOutputStream.toByteArray();
                if (derOutputStream != null) {
                    if (0 != 0) {
                        try {
                            derOutputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        derOutputStream.close();
                    }
                }
                derOutputStream = new DerOutputStream();
                Throwable th3 = null;
                try {
                    try {
                        derOutputStream.write((byte) 48, byteArray);
                        byte[] byteArray2 = derOutputStream.toByteArray();
                        if (derOutputStream != null) {
                            if (0 != 0) {
                                try {
                                    derOutputStream.close();
                                } catch (Throwable th4) {
                                    th3.addSuppressed(th4);
                                }
                            } else {
                                derOutputStream.close();
                            }
                        }
                        return byteArray2;
                    } finally {
                    }
                } finally {
                }
            } finally {
            }
        } finally {
        }
    }

    public String toString() {
        return this.flags.toString();
    }
}
