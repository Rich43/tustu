package sun.security.x509;

import java.io.IOException;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/x509/EDIPartyName.class */
public class EDIPartyName implements GeneralNameInterface {
    private static final byte TAG_ASSIGNER = 0;
    private static final byte TAG_PARTYNAME = 1;
    private String assigner;
    private String party;
    private int myhash;

    public EDIPartyName(String str, String str2) {
        this.assigner = null;
        this.party = null;
        this.myhash = -1;
        this.assigner = str;
        this.party = str2;
    }

    public EDIPartyName(String str) {
        this.assigner = null;
        this.party = null;
        this.myhash = -1;
        this.party = str;
    }

    public EDIPartyName(DerValue derValue) throws IOException {
        this.assigner = null;
        this.party = null;
        this.myhash = -1;
        DerValue[] sequence = new DerInputStream(derValue.toByteArray()).getSequence(2);
        int length = sequence.length;
        if (length < 1 || length > 2) {
            throw new IOException("Invalid encoding of EDIPartyName");
        }
        for (int i2 = 0; i2 < length; i2++) {
            DerValue derValue2 = sequence[i2];
            if (derValue2.isContextSpecific((byte) 0) && !derValue2.isConstructed()) {
                if (this.assigner != null) {
                    throw new IOException("Duplicate nameAssigner found in EDIPartyName");
                }
                derValue2 = derValue2.data.getDerValue();
                this.assigner = derValue2.getAsString();
            }
            if (derValue2.isContextSpecific((byte) 1) && !derValue2.isConstructed()) {
                if (this.party != null) {
                    throw new IOException("Duplicate partyName found in EDIPartyName");
                }
                this.party = derValue2.data.getDerValue().getAsString();
            }
        }
    }

    @Override // sun.security.x509.GeneralNameInterface
    public int getType() {
        return 5;
    }

    @Override // sun.security.x509.GeneralNameInterface
    public void encode(DerOutputStream derOutputStream) throws IOException {
        DerOutputStream derOutputStream2 = new DerOutputStream();
        DerOutputStream derOutputStream3 = new DerOutputStream();
        if (this.assigner != null) {
            DerOutputStream derOutputStream4 = new DerOutputStream();
            derOutputStream4.putPrintableString(this.assigner);
            derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, false, (byte) 0), derOutputStream4);
        }
        if (this.party == null) {
            throw new IOException("Cannot have null partyName");
        }
        derOutputStream3.putPrintableString(this.party);
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, false, (byte) 1), derOutputStream3);
        derOutputStream.write((byte) 48, derOutputStream2);
    }

    public String getAssignerName() {
        return this.assigner;
    }

    public String getPartyName() {
        return this.party;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof EDIPartyName)) {
            return false;
        }
        String str = ((EDIPartyName) obj).assigner;
        if (this.assigner == null) {
            if (str != null) {
                return false;
            }
        } else if (!this.assigner.equals(str)) {
            return false;
        }
        String str2 = ((EDIPartyName) obj).party;
        if (this.party == null) {
            if (str2 != null) {
                return false;
            }
            return true;
        }
        if (!this.party.equals(str2)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        if (this.myhash == -1) {
            this.myhash = 37 + (this.party == null ? 1 : this.party.hashCode());
            if (this.assigner != null) {
                this.myhash = (37 * this.myhash) + this.assigner.hashCode();
            }
        }
        return this.myhash;
    }

    public String toString() {
        return "EDIPartyName: " + (this.assigner == null ? "" : "  nameAssigner = " + this.assigner + ",") + "  partyName = " + this.party;
    }

    @Override // sun.security.x509.GeneralNameInterface
    public int constrains(GeneralNameInterface generalNameInterface) throws UnsupportedOperationException {
        if (generalNameInterface == null || generalNameInterface.getType() != 5) {
            int i2 = -1;
            return i2;
        }
        throw new UnsupportedOperationException("Narrowing, widening, and matching of names not supported for EDIPartyName");
    }

    @Override // sun.security.x509.GeneralNameInterface
    public int subtreeDepth() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("subtreeDepth() not supported for EDIPartyName");
    }
}
