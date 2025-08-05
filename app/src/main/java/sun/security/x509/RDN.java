package sun.security.x509;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import javax.security.auth.x500.X500Principal;
import org.icepdf.core.util.PdfOps;
import org.slf4j.Marker;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

/* loaded from: rt.jar:sun/security/x509/RDN.class */
public class RDN {
    final AVA[] assertion;
    private volatile List<AVA> avaList;
    private volatile String canonicalString;

    public RDN(String str) throws IOException {
        this(str, (Map<String, String>) Collections.emptyMap());
    }

    public RDN(String str, Map<String, String> map) throws IOException {
        int iCountQuotes = 0;
        int i2 = 0;
        int i3 = 0;
        ArrayList arrayList = new ArrayList(3);
        int iIndexOf = str.indexOf(43);
        while (true) {
            int i4 = iIndexOf;
            if (i4 >= 0) {
                iCountQuotes += X500Name.countQuotes(str, i2, i4);
                if (i4 > 0 && str.charAt(i4 - 1) != '\\' && iCountQuotes != 1) {
                    String strSubstring = str.substring(i3, i4);
                    if (strSubstring.length() == 0) {
                        throw new IOException("empty AVA in RDN \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN);
                    }
                    arrayList.add(new AVA(new StringReader(strSubstring), map));
                    i3 = i4 + 1;
                    iCountQuotes = 0;
                }
                i2 = i4 + 1;
                iIndexOf = str.indexOf(43, i2);
            } else {
                String strSubstring2 = str.substring(i3);
                if (strSubstring2.length() == 0) {
                    throw new IOException("empty AVA in RDN \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN);
                }
                arrayList.add(new AVA(new StringReader(strSubstring2), map));
                this.assertion = (AVA[]) arrayList.toArray(new AVA[arrayList.size()]);
                return;
            }
        }
    }

    RDN(String str, String str2) throws IOException {
        this(str, str2, Collections.emptyMap());
    }

    RDN(String str, String str2, Map<String, String> map) throws IOException {
        if (!str2.equalsIgnoreCase(X500Principal.RFC2253)) {
            throw new IOException("Unsupported format " + str2);
        }
        int i2 = 0;
        ArrayList arrayList = new ArrayList(3);
        int iIndexOf = str.indexOf(43);
        while (true) {
            int i3 = iIndexOf;
            if (i3 >= 0) {
                if (i3 > 0 && str.charAt(i3 - 1) != '\\') {
                    String strSubstring = str.substring(i2, i3);
                    if (strSubstring.length() == 0) {
                        throw new IOException("empty AVA in RDN \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN);
                    }
                    arrayList.add(new AVA(new StringReader(strSubstring), 3, map));
                    i2 = i3 + 1;
                }
                iIndexOf = str.indexOf(43, i3 + 1);
            } else {
                String strSubstring2 = str.substring(i2);
                if (strSubstring2.length() == 0) {
                    throw new IOException("empty AVA in RDN \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN);
                }
                arrayList.add(new AVA(new StringReader(strSubstring2), 3, map));
                this.assertion = (AVA[]) arrayList.toArray(new AVA[arrayList.size()]);
                return;
            }
        }
    }

    RDN(DerValue derValue) throws IOException {
        if (derValue.tag != 49) {
            throw new IOException("X500 RDN");
        }
        DerValue[] set = new DerInputStream(derValue.toByteArray()).getSet(5);
        this.assertion = new AVA[set.length];
        for (int i2 = 0; i2 < set.length; i2++) {
            this.assertion[i2] = new AVA(set[i2]);
        }
    }

    RDN(int i2) {
        this.assertion = new AVA[i2];
    }

    public RDN(AVA ava) {
        if (ava == null) {
            throw new NullPointerException();
        }
        this.assertion = new AVA[]{ava};
    }

    public RDN(AVA[] avaArr) {
        this.assertion = (AVA[]) avaArr.clone();
        for (int i2 = 0; i2 < this.assertion.length; i2++) {
            if (this.assertion[i2] == null) {
                throw new NullPointerException();
            }
        }
    }

    public List<AVA> avas() {
        List<AVA> listUnmodifiableList = this.avaList;
        if (listUnmodifiableList == null) {
            listUnmodifiableList = Collections.unmodifiableList(Arrays.asList(this.assertion));
            this.avaList = listUnmodifiableList;
        }
        return listUnmodifiableList;
    }

    public int size() {
        return this.assertion.length;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RDN)) {
            return false;
        }
        RDN rdn = (RDN) obj;
        if (this.assertion.length != rdn.assertion.length) {
            return false;
        }
        return toRFC2253String(true).equals(rdn.toRFC2253String(true));
    }

    public int hashCode() {
        return toRFC2253String(true).hashCode();
    }

    DerValue findAttribute(ObjectIdentifier objectIdentifier) {
        for (int i2 = 0; i2 < this.assertion.length; i2++) {
            if (this.assertion[i2].oid.equals((Object) objectIdentifier)) {
                return this.assertion[i2].value;
            }
        }
        return null;
    }

    void encode(DerOutputStream derOutputStream) throws IOException {
        derOutputStream.putOrderedSetOf((byte) 49, this.assertion);
    }

    public String toString() {
        if (this.assertion.length == 1) {
            return this.assertion[0].toString();
        }
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < this.assertion.length; i2++) {
            if (i2 != 0) {
                sb.append(" + ");
            }
            sb.append(this.assertion[i2].toString());
        }
        return sb.toString();
    }

    public String toRFC1779String() {
        return toRFC1779String(Collections.emptyMap());
    }

    public String toRFC1779String(Map<String, String> map) {
        if (this.assertion.length == 1) {
            return this.assertion[0].toRFC1779String(map);
        }
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < this.assertion.length; i2++) {
            if (i2 != 0) {
                sb.append(" + ");
            }
            sb.append(this.assertion[i2].toRFC1779String(map));
        }
        return sb.toString();
    }

    public String toRFC2253String() {
        return toRFC2253StringInternal(false, Collections.emptyMap());
    }

    public String toRFC2253String(Map<String, String> map) {
        return toRFC2253StringInternal(false, map);
    }

    public String toRFC2253String(boolean z2) {
        if (!z2) {
            return toRFC2253StringInternal(false, Collections.emptyMap());
        }
        String rFC2253StringInternal = this.canonicalString;
        if (rFC2253StringInternal == null) {
            rFC2253StringInternal = toRFC2253StringInternal(true, Collections.emptyMap());
            this.canonicalString = rFC2253StringInternal;
        }
        return rFC2253StringInternal;
    }

    private String toRFC2253StringInternal(boolean z2, Map<String, String> map) {
        if (this.assertion.length == 1) {
            return z2 ? this.assertion[0].toRFC2253CanonicalString() : this.assertion[0].toRFC2253String(map);
        }
        AVA[] avaArr = this.assertion;
        if (z2) {
            avaArr = (AVA[]) this.assertion.clone();
            Arrays.sort(avaArr, AVAComparator.getInstance());
        }
        StringJoiner stringJoiner = new StringJoiner(Marker.ANY_NON_NULL_MARKER);
        for (AVA ava : avaArr) {
            stringJoiner.add(z2 ? ava.toRFC2253CanonicalString() : ava.toRFC2253String(map));
        }
        return stringJoiner.toString();
    }
}
