package sun.security.x509;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.security.AccessController;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.lingala.zip4j.util.InternalZipConstants;
import org.icepdf.core.util.PdfOps;
import sun.security.action.GetBooleanAction;
import sun.security.pkcs.PKCS9Attribute;
import sun.security.util.Debug;
import sun.security.util.DerEncoder;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

/* loaded from: rt.jar:sun/security/x509/AVA.class */
public class AVA implements DerEncoder {
    private static final Debug debug = Debug.getInstance(X509CertImpl.NAME, "\t[AVA]");
    private static final boolean PRESERVE_OLD_DC_ENCODING = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("com.sun.security.preserveOldDCEncoding"))).booleanValue();
    static final int DEFAULT = 1;
    static final int RFC1779 = 2;
    static final int RFC2253 = 3;
    final ObjectIdentifier oid;
    final DerValue value;
    private static final String specialChars1779 = ",=\n+<>#;\\\"";
    private static final String specialChars2253 = ",=+<>#;\\\"";
    private static final String specialCharsDefault = ",=\n+<>#;\\\" ";
    private static final String escapedDefault = ",+<>;\"";
    private static final String hexDigits = "0123456789ABCDEF";

    public AVA(ObjectIdentifier objectIdentifier, DerValue derValue) {
        if (objectIdentifier == null || derValue == null) {
            throw new NullPointerException();
        }
        this.oid = objectIdentifier;
        this.value = derValue;
    }

    AVA(Reader reader) throws IOException {
        this(reader, 1);
    }

    AVA(Reader reader, Map<String, String> map) throws IOException {
        this(reader, 1, map);
    }

    AVA(Reader reader, int i2) throws IOException {
        this(reader, i2, Collections.emptyMap());
    }

    AVA(Reader reader, int i2, Map<String, String> map) throws IOException {
        int i3;
        StringBuilder sb = new StringBuilder();
        while (true) {
            int i4 = readChar(reader, "Incorrect AVA format");
            if (i4 == 61) {
                break;
            } else {
                sb.append((char) i4);
            }
        }
        this.oid = AVAKeyword.getOID(sb.toString(), i2, map);
        sb.setLength(0);
        if (i2 != 3) {
            while (true) {
                i3 = reader.read();
                if (i3 != 32 && i3 != 10) {
                    break;
                }
            }
        } else {
            i3 = reader.read();
            if (i3 == 32) {
                throw new IOException("Incorrect AVA RFC2253 format - leading space must be escaped");
            }
        }
        if (i3 == -1) {
            this.value = new DerValue("");
            return;
        }
        if (i3 == 35) {
            this.value = parseHexString(reader, i2);
        } else if (i3 == 34 && i2 != 3) {
            this.value = parseQuotedString(reader, sb);
        } else {
            this.value = parseString(reader, i3, i2, sb);
        }
    }

    public ObjectIdentifier getObjectIdentifier() {
        return this.oid;
    }

    public DerValue getDerValue() {
        return this.value;
    }

    public String getValueString() {
        try {
            String asString = this.value.getAsString();
            if (asString == null) {
                throw new RuntimeException("AVA string is null");
            }
            return asString;
        } catch (IOException e2) {
            throw new RuntimeException("AVA error: " + ((Object) e2), e2);
        }
    }

    private static DerValue parseHexString(Reader reader, int i2) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte b2 = 0;
        int i3 = 0;
        while (true) {
            int i4 = reader.read();
            if (!isTerminator(i4, i2)) {
                int iIndexOf = "0123456789ABCDEF".indexOf(Character.toUpperCase((char) i4));
                if (iIndexOf == -1) {
                    throw new IOException("AVA parse, invalid hex digit: " + ((char) i4));
                }
                if (i3 % 2 == 1) {
                    b2 = (byte) ((b2 * 16) + ((byte) iIndexOf));
                    byteArrayOutputStream.write(b2);
                } else {
                    b2 = (byte) iIndexOf;
                }
                i3++;
            } else {
                if (i3 == 0) {
                    throw new IOException("AVA parse, zero hex digits");
                }
                if (i3 % 2 == 1) {
                    throw new IOException("AVA parse, odd number of hex digits");
                }
                return new DerValue(byteArrayOutputStream.toByteArray());
            }
        }
    }

    private DerValue parseQuotedString(Reader reader, StringBuilder sb) throws IOException {
        int i2;
        int i3 = readChar(reader, "Quoted string did not end in quote");
        ArrayList arrayList = new ArrayList();
        boolean zIsPrintableStringChar = true;
        while (i3 != 34) {
            if (i3 == 92) {
                i3 = readChar(reader, "Quoted string did not end in quote");
                Byte embeddedHexPair = getEmbeddedHexPair(i3, reader);
                if (embeddedHexPair != null) {
                    zIsPrintableStringChar = false;
                    arrayList.add(embeddedHexPair);
                    i3 = reader.read();
                } else if (specialChars1779.indexOf((char) i3) < 0) {
                    throw new IOException("Invalid escaped character in AVA: " + ((char) i3));
                }
            }
            if (arrayList.size() > 0) {
                sb.append(getEmbeddedHexString(arrayList));
                arrayList.clear();
            }
            zIsPrintableStringChar &= DerValue.isPrintableStringChar((char) i3);
            sb.append((char) i3);
            i3 = readChar(reader, "Quoted string did not end in quote");
        }
        if (arrayList.size() > 0) {
            sb.append(getEmbeddedHexString(arrayList));
            arrayList.clear();
        }
        while (true) {
            i2 = reader.read();
            if (i2 != 10 && i2 != 32) {
                break;
            }
        }
        if (i2 != -1) {
            throw new IOException("AVA had characters other than whitespace after terminating quote");
        }
        if (this.oid.equals((Object) PKCS9Attribute.EMAIL_ADDRESS_OID) || (this.oid.equals((Object) X500Name.DOMAIN_COMPONENT_OID) && !PRESERVE_OLD_DC_ENCODING)) {
            return new DerValue((byte) 22, sb.toString().trim());
        }
        if (zIsPrintableStringChar) {
            return new DerValue(sb.toString().trim());
        }
        return new DerValue((byte) 12, sb.toString().trim());
    }

    private DerValue parseString(Reader reader, int i2, int i3, StringBuilder sb) throws IOException {
        ArrayList arrayList = new ArrayList();
        boolean zIsPrintableStringChar = true;
        boolean z2 = true;
        int i4 = 0;
        do {
            boolean z3 = false;
            if (i2 == 92) {
                z3 = true;
                i2 = readChar(reader, "Invalid trailing backslash");
                Byte embeddedHexPair = getEmbeddedHexPair(i2, reader);
                if (embeddedHexPair != null) {
                    zIsPrintableStringChar = false;
                    arrayList.add(embeddedHexPair);
                    i2 = reader.read();
                    z2 = false;
                } else {
                    if (i3 == 1 && specialCharsDefault.indexOf((char) i2) == -1) {
                        throw new IOException("Invalid escaped character in AVA: '" + ((char) i2) + PdfOps.SINGLE_QUOTE_TOKEN);
                    }
                    if (i3 == 3) {
                        if (i2 == 32) {
                            if (!z2 && !trailingSpace(reader)) {
                                throw new IOException("Invalid escaped space character in AVA.  Only a leading or trailing space character can be escaped.");
                            }
                        } else if (i2 == 35) {
                            if (!z2) {
                                throw new IOException("Invalid escaped '#' character in AVA.  Only a leading '#' can be escaped.");
                            }
                        } else if (specialChars2253.indexOf((char) i2) == -1) {
                            throw new IOException("Invalid escaped character in AVA: '" + ((char) i2) + PdfOps.SINGLE_QUOTE_TOKEN);
                        }
                    }
                }
            } else if (i3 == 3) {
                if (specialChars2253.indexOf((char) i2) != -1) {
                    throw new IOException("Character '" + ((char) i2) + "' in AVA appears without escape");
                }
            } else if (escapedDefault.indexOf((char) i2) != -1) {
                throw new IOException("Character '" + ((char) i2) + "' in AVA appears without escape");
            }
            if (arrayList.size() > 0) {
                for (int i5 = 0; i5 < i4; i5++) {
                    sb.append(" ");
                }
                i4 = 0;
                sb.append(getEmbeddedHexString(arrayList));
                arrayList.clear();
            }
            zIsPrintableStringChar &= DerValue.isPrintableStringChar((char) i2);
            if (i2 == 32 && !z3) {
                i4++;
            } else {
                for (int i6 = 0; i6 < i4; i6++) {
                    sb.append(" ");
                }
                i4 = 0;
                sb.append((char) i2);
            }
            i2 = reader.read();
            z2 = false;
        } while (!isTerminator(i2, i3));
        if (i3 == 3 && i4 > 0) {
            throw new IOException("Incorrect AVA RFC2253 format - trailing space must be escaped");
        }
        if (arrayList.size() > 0) {
            sb.append(getEmbeddedHexString(arrayList));
            arrayList.clear();
        }
        if (this.oid.equals((Object) PKCS9Attribute.EMAIL_ADDRESS_OID) || (this.oid.equals((Object) X500Name.DOMAIN_COMPONENT_OID) && !PRESERVE_OLD_DC_ENCODING)) {
            return new DerValue((byte) 22, sb.toString());
        }
        if (zIsPrintableStringChar) {
            return new DerValue(sb.toString());
        }
        return new DerValue((byte) 12, sb.toString());
    }

    private static Byte getEmbeddedHexPair(int i2, Reader reader) throws IOException {
        if ("0123456789ABCDEF".indexOf(Character.toUpperCase((char) i2)) >= 0) {
            int i3 = readChar(reader, "unexpected EOF - escaped hex value must include two valid digits");
            if ("0123456789ABCDEF".indexOf(Character.toUpperCase((char) i3)) >= 0) {
                return new Byte((byte) ((Character.digit((char) i2, 16) << 4) + Character.digit((char) i3, 16)));
            }
            throw new IOException("escaped hex value must include two valid digits");
        }
        return null;
    }

    private static String getEmbeddedHexString(List<Byte> list) throws IOException {
        int size = list.size();
        byte[] bArr = new byte[size];
        for (int i2 = 0; i2 < size; i2++) {
            bArr[i2] = list.get(i2).byteValue();
        }
        return new String(bArr, InternalZipConstants.CHARSET_UTF8);
    }

    private static boolean isTerminator(int i2, int i3) {
        switch (i2) {
            case -1:
            case 43:
            case 44:
                return true;
            case 59:
                return i3 != 3;
            default:
                return false;
        }
    }

    private static int readChar(Reader reader, String str) throws IOException {
        int i2 = reader.read();
        if (i2 == -1) {
            throw new IOException(str);
        }
        return i2;
    }

    private static boolean trailingSpace(Reader reader) throws IOException {
        boolean z2;
        if (!reader.markSupported()) {
            return true;
        }
        reader.mark(9999);
        while (true) {
            int i2 = reader.read();
            if (i2 == -1) {
                z2 = true;
                break;
            }
            if (i2 != 32) {
                if (i2 == 92) {
                    if (reader.read() != 32) {
                        z2 = false;
                        break;
                    }
                } else {
                    z2 = false;
                    break;
                }
            }
        }
        reader.reset();
        return z2;
    }

    AVA(DerValue derValue) throws IOException {
        if (derValue.tag != 48) {
            throw new IOException("AVA not a sequence");
        }
        this.oid = derValue.data.getOID();
        this.value = derValue.data.getDerValue();
        if (derValue.data.available() != 0) {
            throw new IOException("AVA, extra bytes = " + derValue.data.available());
        }
    }

    AVA(DerInputStream derInputStream) throws IOException {
        this(derInputStream.getDerValue());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AVA)) {
            return false;
        }
        return toRFC2253CanonicalString().equals(((AVA) obj).toRFC2253CanonicalString());
    }

    public int hashCode() {
        return toRFC2253CanonicalString().hashCode();
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        derEncode(derOutputStream);
    }

    @Override // sun.security.util.DerEncoder
    public void derEncode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream.putOID(this.oid);
        this.value.encode(derOutputStream);
        derOutputStream2.write((byte) 48, derOutputStream);
        outputStream.write(derOutputStream2.toByteArray());
    }

    private String toKeyword(int i2, Map<String, String> map) {
        return AVAKeyword.getKeyword(this.oid, i2, map);
    }

    public String toString() {
        return toKeywordValueString(toKeyword(1, Collections.emptyMap()));
    }

    public String toRFC1779String() {
        return toRFC1779String(Collections.emptyMap());
    }

    public String toRFC1779String(Map<String, String> map) {
        return toKeywordValueString(toKeyword(2, map));
    }

    public String toRFC2253String() {
        return toRFC2253String(Collections.emptyMap());
    }

    public String toRFC2253String(Map<String, String> map) {
        StringBuilder sb = new StringBuilder(100);
        sb.append(toKeyword(3, map));
        sb.append('=');
        if ((sb.charAt(0) >= '0' && sb.charAt(0) <= '9') || !isDerString(this.value, false)) {
            try {
                byte[] byteArray = this.value.toByteArray();
                sb.append('#');
                for (byte b2 : byteArray) {
                    sb.append(Character.forDigit(15 & (b2 >>> 4), 16));
                    sb.append(Character.forDigit(15 & b2, 16));
                }
            } catch (IOException e2) {
                throw new IllegalArgumentException("DER Value conversion");
            }
        } else {
            try {
                String str = new String(this.value.getDataBytes(), InternalZipConstants.CHARSET_UTF8);
                StringBuilder sb2 = new StringBuilder();
                for (int i2 = 0; i2 < str.length(); i2++) {
                    char cCharAt = str.charAt(i2);
                    if (DerValue.isPrintableStringChar(cCharAt) || ",=+<>#;\"\\".indexOf(cCharAt) >= 0) {
                        if (",=+<>#;\"\\".indexOf(cCharAt) >= 0) {
                            sb2.append('\\');
                        }
                        sb2.append(cCharAt);
                    } else if (cCharAt == 0) {
                        sb2.append("\\00");
                    } else if (debug != null && Debug.isOn("ava")) {
                        try {
                            byte[] bytes = Character.toString(cCharAt).getBytes(InternalZipConstants.CHARSET_UTF8);
                            for (int i3 = 0; i3 < bytes.length; i3++) {
                                sb2.append('\\');
                                sb2.append(Character.toUpperCase(Character.forDigit(15 & (bytes[i3] >>> 4), 16)));
                                sb2.append(Character.toUpperCase(Character.forDigit(15 & bytes[i3], 16)));
                            }
                        } catch (IOException e3) {
                            throw new IllegalArgumentException("DER Value conversion");
                        }
                    } else {
                        sb2.append(cCharAt);
                    }
                }
                char[] charArray = sb2.toString().toCharArray();
                StringBuilder sb3 = new StringBuilder();
                int i4 = 0;
                while (i4 < charArray.length && (charArray[i4] == ' ' || charArray[i4] == '\r')) {
                    i4++;
                }
                int length = charArray.length - 1;
                while (length >= 0 && (charArray[length] == ' ' || charArray[length] == '\r')) {
                    length--;
                }
                for (int i5 = 0; i5 < charArray.length; i5++) {
                    char c2 = charArray[i5];
                    if (i5 < i4 || i5 > length) {
                        sb3.append('\\');
                    }
                    sb3.append(c2);
                }
                sb.append(sb3.toString());
            } catch (IOException e4) {
                throw new IllegalArgumentException("DER Value conversion");
            }
        }
        return sb.toString();
    }

    public String toRFC2253CanonicalString() {
        StringBuilder sb = new StringBuilder(40);
        sb.append(toKeyword(3, Collections.emptyMap()));
        sb.append('=');
        if ((sb.charAt(0) >= '0' && sb.charAt(0) <= '9') || !isDerString(this.value, true)) {
            try {
                byte[] byteArray = this.value.toByteArray();
                sb.append('#');
                for (byte b2 : byteArray) {
                    sb.append(Character.forDigit(15 & (b2 >>> 4), 16));
                    sb.append(Character.forDigit(15 & b2, 16));
                }
            } catch (IOException e2) {
                throw new IllegalArgumentException("DER Value conversion");
            }
        } else {
            try {
                String str = new String(this.value.getDataBytes(), InternalZipConstants.CHARSET_UTF8);
                StringBuilder sb2 = new StringBuilder();
                boolean z2 = false;
                for (int i2 = 0; i2 < str.length(); i2++) {
                    char cCharAt = str.charAt(i2);
                    if (DerValue.isPrintableStringChar(cCharAt) || ",+<>;\"\\".indexOf(cCharAt) >= 0 || (i2 == 0 && cCharAt == '#')) {
                        if ((i2 == 0 && cCharAt == '#') || ",+<>;\"\\".indexOf(cCharAt) >= 0) {
                            sb2.append('\\');
                        }
                        if (!Character.isWhitespace(cCharAt)) {
                            z2 = false;
                            sb2.append(cCharAt);
                        } else if (!z2) {
                            z2 = true;
                            sb2.append(cCharAt);
                        }
                    } else if (debug != null && Debug.isOn("ava")) {
                        z2 = false;
                        try {
                            byte[] bytes = Character.toString(cCharAt).getBytes(InternalZipConstants.CHARSET_UTF8);
                            for (int i3 = 0; i3 < bytes.length; i3++) {
                                sb2.append('\\');
                                sb2.append(Character.forDigit(15 & (bytes[i3] >>> 4), 16));
                                sb2.append(Character.forDigit(15 & bytes[i3], 16));
                            }
                        } catch (IOException e3) {
                            throw new IllegalArgumentException("DER Value conversion");
                        }
                    } else {
                        z2 = false;
                        sb2.append(cCharAt);
                    }
                }
                sb.append(sb2.toString().trim());
            } catch (IOException e4) {
                throw new IllegalArgumentException("DER Value conversion");
            }
        }
        return Normalizer.normalize(sb.toString().toUpperCase(Locale.US).toLowerCase(Locale.US), Normalizer.Form.NFKD);
    }

    private static boolean isDerString(DerValue derValue, boolean z2) {
        if (z2) {
            switch (derValue.tag) {
                case 12:
                case 19:
                    return true;
                default:
                    return false;
            }
        }
        switch (derValue.tag) {
            case 12:
            case 19:
            case 20:
            case 22:
            case 27:
            case 30:
                return true;
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 21:
            case 23:
            case 24:
            case 25:
            case 26:
            case 28:
            case 29:
            default:
                return false;
        }
    }

    boolean hasRFC2253Keyword() {
        return AVAKeyword.hasKeyword(this.oid, 3);
    }

    private String toKeywordValueString(String str) {
        char cCharAt;
        StringBuilder sb = new StringBuilder(40);
        sb.append(str);
        sb.append("=");
        try {
            String asString = this.value.getAsString();
            if (asString == null) {
                byte[] byteArray = this.value.toByteArray();
                sb.append('#');
                for (int i2 = 0; i2 < byteArray.length; i2++) {
                    sb.append("0123456789ABCDEF".charAt((byteArray[i2] >> 4) & 15));
                    sb.append("0123456789ABCDEF".charAt(byteArray[i2] & 15));
                }
            } else {
                boolean z2 = false;
                StringBuilder sb2 = new StringBuilder();
                boolean z3 = false;
                int length = asString.length();
                boolean z4 = length > 1 && asString.charAt(0) == '\"' && asString.charAt(length - 1) == '\"';
                for (int i3 = 0; i3 < length; i3++) {
                    char cCharAt2 = asString.charAt(i3);
                    if (z4 && (i3 == 0 || i3 == length - 1)) {
                        sb2.append(cCharAt2);
                    } else if (DerValue.isPrintableStringChar(cCharAt2) || ",+=\n<>#;\\\"".indexOf(cCharAt2) >= 0) {
                        if (!z2 && ((i3 == 0 && (cCharAt2 == ' ' || cCharAt2 == '\n')) || ",+=\n<>#;\\\"".indexOf(cCharAt2) >= 0)) {
                            z2 = true;
                        }
                        if (cCharAt2 != ' ' && cCharAt2 != '\n') {
                            if (cCharAt2 == '\"' || cCharAt2 == '\\') {
                                sb2.append('\\');
                            }
                            z3 = false;
                        } else {
                            if (!z2 && z3) {
                                z2 = true;
                            }
                            z3 = true;
                        }
                        sb2.append(cCharAt2);
                    } else if (debug != null && Debug.isOn("ava")) {
                        z3 = false;
                        byte[] bytes = Character.toString(cCharAt2).getBytes(InternalZipConstants.CHARSET_UTF8);
                        for (int i4 = 0; i4 < bytes.length; i4++) {
                            sb2.append('\\');
                            sb2.append(Character.toUpperCase(Character.forDigit(15 & (bytes[i4] >>> 4), 16)));
                            sb2.append(Character.toUpperCase(Character.forDigit(15 & bytes[i4], 16)));
                        }
                    } else {
                        z3 = false;
                        sb2.append(cCharAt2);
                    }
                }
                if (sb2.length() > 0 && ((cCharAt = sb2.charAt(sb2.length() - 1)) == ' ' || cCharAt == '\n')) {
                    z2 = true;
                }
                if (!z4 && z2) {
                    sb.append(PdfOps.DOUBLE_QUOTE__TOKEN + sb2.toString() + PdfOps.DOUBLE_QUOTE__TOKEN);
                } else {
                    sb.append(sb2.toString());
                }
            }
            return sb.toString();
        } catch (IOException e2) {
            throw new IllegalArgumentException("DER Value conversion");
        }
    }
}
