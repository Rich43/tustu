package sun.security.x509;

import java.io.IOException;
import java.util.Locale;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/x509/DNSName.class */
public class DNSName implements GeneralNameInterface {
    private String name;
    private static final String alphaDigits = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public DNSName(DerValue derValue) throws IOException {
        this.name = derValue.getIA5String();
    }

    public DNSName(String str) throws IOException {
        if (str == null || str.length() == 0) {
            throw new IOException("DNSName must not be null or empty");
        }
        if (str.contains(" ")) {
            throw new IOException("DNSName with blank components is not permitted");
        }
        if (str.startsWith(".") || str.endsWith(".")) {
            throw new IOException("DNSName may not begin or end with a .");
        }
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 < str.length()) {
                int iIndexOf = str.indexOf(46, i3);
                iIndexOf = iIndexOf < 0 ? str.length() : iIndexOf;
                if (iIndexOf - i3 < 1) {
                    throw new IOException("DNSName with empty components are not permitted");
                }
                if (alphaDigits.indexOf(str.charAt(i3)) < 0) {
                    throw new IOException("DNSName components must begin with a letter or digit");
                }
                for (int i4 = i3 + 1; i4 < iIndexOf; i4++) {
                    char cCharAt = str.charAt(i4);
                    if (alphaDigits.indexOf(cCharAt) < 0 && cCharAt != '-') {
                        throw new IOException("DNSName components must consist of letters, digits, and hyphens");
                    }
                }
                i2 = iIndexOf + 1;
            } else {
                this.name = str;
                return;
            }
        }
    }

    @Override // sun.security.x509.GeneralNameInterface
    public int getType() {
        return 2;
    }

    public String getName() {
        return this.name;
    }

    @Override // sun.security.x509.GeneralNameInterface
    public void encode(DerOutputStream derOutputStream) throws IOException {
        derOutputStream.putIA5String(this.name);
    }

    public String toString() {
        return "DNSName: " + this.name;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DNSName)) {
            return false;
        }
        return this.name.equalsIgnoreCase(((DNSName) obj).name);
    }

    public int hashCode() {
        return this.name.toUpperCase(Locale.ENGLISH).hashCode();
    }

    @Override // sun.security.x509.GeneralNameInterface
    public int constrains(GeneralNameInterface generalNameInterface) throws UnsupportedOperationException {
        int i2;
        if (generalNameInterface == null || generalNameInterface.getType() != 2) {
            i2 = -1;
        } else {
            String lowerCase = ((DNSName) generalNameInterface).getName().toLowerCase(Locale.ENGLISH);
            String lowerCase2 = this.name.toLowerCase(Locale.ENGLISH);
            if (lowerCase.equals(lowerCase2)) {
                i2 = 0;
            } else if (lowerCase2.endsWith(lowerCase)) {
                if (lowerCase2.charAt(lowerCase2.lastIndexOf(lowerCase) - 1) == '.') {
                    i2 = 2;
                } else {
                    i2 = 3;
                }
            } else if (lowerCase.endsWith(lowerCase2) && lowerCase.charAt(lowerCase.lastIndexOf(lowerCase2) - 1) == '.') {
                i2 = 1;
            } else {
                i2 = 3;
            }
        }
        return i2;
    }

    @Override // sun.security.x509.GeneralNameInterface
    public int subtreeDepth() throws UnsupportedOperationException {
        int i2 = 1;
        int iIndexOf = this.name.indexOf(46);
        while (true) {
            int i3 = iIndexOf;
            if (i3 >= 0) {
                i2++;
                iIndexOf = this.name.indexOf(46, i3 + 1);
            } else {
                return i2;
            }
        }
    }
}
