package sun.security.x509;

import java.io.IOException;
import java.util.Locale;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/x509/RFC822Name.class */
public class RFC822Name implements GeneralNameInterface {
    private String name;

    public RFC822Name(DerValue derValue) throws IOException {
        this.name = derValue.getIA5String();
        parseName(this.name);
    }

    public RFC822Name(String str) throws IOException {
        parseName(str);
        this.name = str;
    }

    public void parseName(String str) throws IOException {
        if (str == null || str.length() == 0) {
            throw new IOException("RFC822Name may not be null or empty");
        }
        String strSubstring = str.substring(str.indexOf(64) + 1);
        if (strSubstring.length() == 0) {
            throw new IOException("RFC822Name may not end with @");
        }
        if (strSubstring.startsWith(".") && strSubstring.length() == 1) {
            throw new IOException("RFC822Name domain may not be just .");
        }
    }

    @Override // sun.security.x509.GeneralNameInterface
    public int getType() {
        return 1;
    }

    public String getName() {
        return this.name;
    }

    @Override // sun.security.x509.GeneralNameInterface
    public void encode(DerOutputStream derOutputStream) throws IOException {
        derOutputStream.putIA5String(this.name);
    }

    public String toString() {
        return "RFC822Name: " + this.name;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RFC822Name)) {
            return false;
        }
        return this.name.equalsIgnoreCase(((RFC822Name) obj).name);
    }

    public int hashCode() {
        return this.name.toUpperCase(Locale.ENGLISH).hashCode();
    }

    @Override // sun.security.x509.GeneralNameInterface
    public int constrains(GeneralNameInterface generalNameInterface) throws UnsupportedOperationException {
        int i2;
        if (generalNameInterface == null || generalNameInterface.getType() != 1) {
            i2 = -1;
        } else {
            String lowerCase = ((RFC822Name) generalNameInterface).getName().toLowerCase(Locale.ENGLISH);
            String lowerCase2 = this.name.toLowerCase(Locale.ENGLISH);
            if (lowerCase.equals(lowerCase2)) {
                i2 = 0;
            } else if (lowerCase2.endsWith(lowerCase)) {
                if (lowerCase.indexOf(64) != -1) {
                    i2 = 3;
                } else if (lowerCase.startsWith(".") || lowerCase2.charAt(lowerCase2.lastIndexOf(lowerCase) - 1) == '@') {
                    i2 = 2;
                } else {
                    i2 = 3;
                }
            } else if (!lowerCase.endsWith(lowerCase2) || lowerCase2.indexOf(64) != -1) {
                i2 = 3;
            } else if (lowerCase2.startsWith(".") || lowerCase.charAt(lowerCase.lastIndexOf(lowerCase2) - 1) == '@') {
                i2 = 1;
            } else {
                i2 = 3;
            }
        }
        return i2;
    }

    @Override // sun.security.x509.GeneralNameInterface
    public int subtreeDepth() throws UnsupportedOperationException {
        String strSubstring = this.name;
        int i2 = 1;
        int iLastIndexOf = strSubstring.lastIndexOf(64);
        if (iLastIndexOf >= 0) {
            i2 = 1 + 1;
            strSubstring = strSubstring.substring(iLastIndexOf + 1);
        }
        while (strSubstring.lastIndexOf(46) >= 0) {
            strSubstring = strSubstring.substring(0, strSubstring.lastIndexOf(46));
            i2++;
        }
        return i2;
    }
}
