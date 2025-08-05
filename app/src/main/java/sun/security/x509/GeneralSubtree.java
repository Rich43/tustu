package sun.security.x509;

import java.io.IOException;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/x509/GeneralSubtree.class */
public class GeneralSubtree {
    private static final byte TAG_MIN = 0;
    private static final byte TAG_MAX = 1;
    private static final int MIN_DEFAULT = 0;
    private GeneralName name;
    private int minimum;
    private int maximum;
    private int myhash = -1;

    public GeneralSubtree(GeneralName generalName, int i2, int i3) {
        this.minimum = 0;
        this.maximum = -1;
        this.name = generalName;
        this.minimum = i2;
        this.maximum = i3;
    }

    public GeneralSubtree(DerValue derValue) throws IOException {
        this.minimum = 0;
        this.maximum = -1;
        if (derValue.tag != 48) {
            throw new IOException("Invalid encoding for GeneralSubtree.");
        }
        this.name = new GeneralName(derValue.data.getDerValue(), true);
        while (derValue.data.available() != 0) {
            DerValue derValue2 = derValue.data.getDerValue();
            if (derValue2.isContextSpecific((byte) 0) && !derValue2.isConstructed()) {
                derValue2.resetTag((byte) 2);
                this.minimum = derValue2.getInteger();
            } else if (derValue2.isContextSpecific((byte) 1) && !derValue2.isConstructed()) {
                derValue2.resetTag((byte) 2);
                this.maximum = derValue2.getInteger();
            } else {
                throw new IOException("Invalid encoding of GeneralSubtree.");
            }
        }
    }

    public GeneralName getName() {
        return this.name;
    }

    public int getMinimum() {
        return this.minimum;
    }

    public int getMaximum() {
        return this.maximum;
    }

    public String toString() {
        String str;
        String str2 = "\n   GeneralSubtree: [\n    GeneralName: " + (this.name == null ? "" : this.name.toString()) + "\n    Minimum: " + this.minimum;
        if (this.maximum == -1) {
            str = str2 + "\t    Maximum: undefined";
        } else {
            str = str2 + "\t    Maximum: " + this.maximum;
        }
        return str + "    ]\n";
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof GeneralSubtree)) {
            return false;
        }
        GeneralSubtree generalSubtree = (GeneralSubtree) obj;
        if (this.name == null) {
            if (generalSubtree.name != null) {
                return false;
            }
        } else if (!this.name.equals(generalSubtree.name)) {
            return false;
        }
        if (this.minimum != generalSubtree.minimum || this.maximum != generalSubtree.maximum) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        if (this.myhash == -1) {
            this.myhash = 17;
            if (this.name != null) {
                this.myhash = (37 * this.myhash) + this.name.hashCode();
            }
            if (this.minimum != 0) {
                this.myhash = (37 * this.myhash) + this.minimum;
            }
            if (this.maximum != -1) {
                this.myhash = (37 * this.myhash) + this.maximum;
            }
        }
        return this.myhash;
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        DerOutputStream derOutputStream2 = new DerOutputStream();
        this.name.encode(derOutputStream2);
        if (this.minimum != 0) {
            DerOutputStream derOutputStream3 = new DerOutputStream();
            derOutputStream3.putInteger(this.minimum);
            derOutputStream2.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, false, (byte) 0), derOutputStream3);
        }
        if (this.maximum != -1) {
            DerOutputStream derOutputStream4 = new DerOutputStream();
            derOutputStream4.putInteger(this.maximum);
            derOutputStream2.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, false, (byte) 1), derOutputStream4);
        }
        derOutputStream.write((byte) 48, derOutputStream2);
    }
}
