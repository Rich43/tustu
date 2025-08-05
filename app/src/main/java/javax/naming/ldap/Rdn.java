package javax.naming.ldap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import javafx.fxml.FXMLLoader;
import javax.naming.InvalidNameException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: rt.jar:javax/naming/ldap/Rdn.class */
public class Rdn implements Serializable, Comparable<Object> {
    private transient ArrayList<RdnEntry> entries;
    private static final int DEFAULT_SIZE = 1;
    private static final long serialVersionUID = -5994465067210009656L;
    private static final String escapees = ",=+<>#;\"\\";

    public Rdn(Attributes attributes) throws InvalidNameException {
        if (attributes.size() == 0) {
            throw new InvalidNameException("Attributes cannot be empty");
        }
        this.entries = new ArrayList<>(attributes.size());
        NamingEnumeration<? extends Attribute> all = attributes.getAll();
        int i2 = 0;
        while (all.hasMore()) {
            try {
                RdnEntry rdnEntry = new RdnEntry();
                Attribute next = all.next();
                rdnEntry.type = next.getID();
                rdnEntry.value = next.get();
                this.entries.add(i2, rdnEntry);
                i2++;
            } catch (NamingException e2) {
                InvalidNameException invalidNameException = new InvalidNameException(e2.getMessage());
                invalidNameException.initCause(e2);
                throw invalidNameException;
            }
        }
        sort();
    }

    public Rdn(String str) throws InvalidNameException {
        this.entries = new ArrayList<>(1);
        new Rfc2253Parser(str).parseRdn(this);
    }

    public Rdn(Rdn rdn) {
        this.entries = new ArrayList<>(rdn.entries.size());
        this.entries.addAll(rdn.entries);
    }

    public Rdn(String str, Object obj) throws InvalidNameException {
        if (obj == null) {
            throw new NullPointerException("Cannot set value to null");
        }
        if (str.equals("") || isEmptyValue(obj)) {
            throw new InvalidNameException("type or value cannot be empty, type:" + str + " value:" + obj);
        }
        this.entries = new ArrayList<>(1);
        put(str, obj);
    }

    private boolean isEmptyValue(Object obj) {
        return ((obj instanceof String) && obj.equals("")) || ((obj instanceof byte[]) && ((byte[]) obj).length == 0);
    }

    Rdn() {
        this.entries = new ArrayList<>(1);
    }

    Rdn put(String str, Object obj) {
        RdnEntry rdnEntry = new RdnEntry();
        rdnEntry.type = str;
        if (obj instanceof byte[]) {
            rdnEntry.value = ((byte[]) obj).clone();
        } else {
            rdnEntry.value = obj;
        }
        this.entries.add(rdnEntry);
        return this;
    }

    void sort() {
        if (this.entries.size() > 1) {
            Collections.sort(this.entries);
        }
    }

    public Object getValue() {
        return this.entries.get(0).getValue();
    }

    public String getType() {
        return this.entries.get(0).getType();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        int size = this.entries.size();
        if (size > 0) {
            sb.append((Object) this.entries.get(0));
        }
        for (int i2 = 1; i2 < size; i2++) {
            sb.append('+');
            sb.append((Object) this.entries.get(i2));
        }
        return sb.toString();
    }

    @Override // java.lang.Comparable
    public int compareTo(Object obj) {
        if (!(obj instanceof Rdn)) {
            throw new ClassCastException("The obj is not a Rdn");
        }
        if (obj == this) {
            return 0;
        }
        Rdn rdn = (Rdn) obj;
        int iMin = Math.min(this.entries.size(), rdn.entries.size());
        for (int i2 = 0; i2 < iMin; i2++) {
            int iCompareTo = this.entries.get(i2).compareTo(rdn.entries.get(i2));
            if (iCompareTo != 0) {
                return iCompareTo;
            }
        }
        return this.entries.size() - rdn.entries.size();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Rdn)) {
            return false;
        }
        Rdn rdn = (Rdn) obj;
        if (this.entries.size() != rdn.size()) {
            return false;
        }
        for (int i2 = 0; i2 < this.entries.size(); i2++) {
            if (!this.entries.get(i2).equals(rdn.entries.get(i2))) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int iHashCode = 0;
        for (int i2 = 0; i2 < this.entries.size(); i2++) {
            iHashCode += this.entries.get(i2).hashCode();
        }
        return iHashCode;
    }

    public Attributes toAttributes() {
        BasicAttributes basicAttributes = new BasicAttributes(true);
        for (int i2 = 0; i2 < this.entries.size(); i2++) {
            RdnEntry rdnEntry = this.entries.get(i2);
            Attribute attributePut = basicAttributes.put(rdnEntry.getType(), rdnEntry.getValue());
            if (attributePut != null) {
                attributePut.add(rdnEntry.getValue());
                basicAttributes.put(attributePut);
            }
        }
        return basicAttributes;
    }

    /* loaded from: rt.jar:javax/naming/ldap/Rdn$RdnEntry.class */
    private static class RdnEntry implements Comparable<RdnEntry> {
        private String type;
        private Object value;
        private String comparable;

        private RdnEntry() {
            this.comparable = null;
        }

        String getType() {
            return this.type;
        }

        Object getValue() {
            return this.value;
        }

        @Override // java.lang.Comparable
        public int compareTo(RdnEntry rdnEntry) {
            int iCompareToIgnoreCase = this.type.compareToIgnoreCase(rdnEntry.type);
            if (iCompareToIgnoreCase != 0) {
                return iCompareToIgnoreCase;
            }
            if (this.value.equals(rdnEntry.value)) {
                return 0;
            }
            return getValueComparable().compareTo(rdnEntry.getValueComparable());
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof RdnEntry)) {
                return false;
            }
            RdnEntry rdnEntry = (RdnEntry) obj;
            return this.type.equalsIgnoreCase(rdnEntry.type) && getValueComparable().equals(rdnEntry.getValueComparable());
        }

        public int hashCode() {
            return this.type.toUpperCase(Locale.ENGLISH).hashCode() + getValueComparable().hashCode();
        }

        public String toString() {
            return this.type + "=" + Rdn.escapeValue(this.value);
        }

        private String getValueComparable() {
            if (this.comparable != null) {
                return this.comparable;
            }
            if (this.value instanceof byte[]) {
                this.comparable = Rdn.escapeBinaryValue((byte[]) this.value);
            } else {
                this.comparable = ((String) this.value).toUpperCase(Locale.ENGLISH);
            }
            return this.comparable;
        }
    }

    public int size() {
        return this.entries.size();
    }

    public static String escapeValue(Object obj) {
        if (obj instanceof byte[]) {
            return escapeBinaryValue((byte[]) obj);
        }
        return escapeStringValue((String) obj);
    }

    private static String escapeStringValue(String str) {
        char[] charArray = str.toCharArray();
        StringBuilder sb = new StringBuilder(2 * str.length());
        int i2 = 0;
        while (i2 < charArray.length && isWhitespace(charArray[i2])) {
            i2++;
        }
        int length = charArray.length - 1;
        while (length >= 0 && isWhitespace(charArray[length])) {
            length--;
        }
        for (int i3 = 0; i3 < charArray.length; i3++) {
            char c2 = charArray[i3];
            if (i3 < i2 || i3 > length || escapees.indexOf(c2) >= 0) {
                sb.append('\\');
            }
            sb.append(c2);
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String escapeBinaryValue(byte[] bArr) {
        StringBuilder sb = new StringBuilder(1 + (2 * bArr.length));
        sb.append(FXMLLoader.CONTROLLER_METHOD_PREFIX);
        for (byte b2 : bArr) {
            sb.append(Character.forDigit(15 & (b2 >>> 4), 16));
            sb.append(Character.forDigit(15 & b2, 16));
        }
        return sb.toString();
    }

    public static Object unescapeValue(String str) {
        char[] charArray = str.toCharArray();
        int i2 = 0;
        int length = charArray.length;
        while (i2 < length && isWhitespace(charArray[i2])) {
            i2++;
        }
        while (i2 < length && isWhitespace(charArray[length - 1])) {
            length--;
        }
        if (length != charArray.length && i2 < length && charArray[length - 1] == '\\') {
            length++;
        }
        if (i2 >= length) {
            return "";
        }
        if (charArray[i2] == '#') {
            return decodeHexPairs(charArray, i2 + 1, length);
        }
        if (charArray[i2] == '\"' && charArray[length - 1] == '\"') {
            i2++;
            length--;
        }
        StringBuilder sb = new StringBuilder(length - i2);
        int i3 = -1;
        int length2 = i2;
        while (length2 < length) {
            if (charArray[length2] == '\\' && length2 + 1 < length) {
                if (!Character.isLetterOrDigit(charArray[length2 + 1])) {
                    length2++;
                    sb.append(charArray[length2]);
                    i3 = length2;
                } else {
                    byte[] utf8Octets = getUtf8Octets(charArray, length2, length);
                    if (utf8Octets.length > 0) {
                        try {
                            sb.append(new String(utf8Octets, InternalZipConstants.CHARSET_UTF8));
                        } catch (UnsupportedEncodingException e2) {
                        }
                        length2 += (utf8Octets.length * 3) - 1;
                    } else {
                        throw new IllegalArgumentException("Not a valid attribute string value:" + str + ",improper usage of backslash");
                    }
                }
            } else {
                sb.append(charArray[length2]);
            }
            length2++;
        }
        int length3 = sb.length();
        if (isWhitespace(sb.charAt(length3 - 1)) && i3 != length - 1) {
            sb.setLength(length3 - 1);
        }
        return sb.toString();
    }

    private static byte[] decodeHexPairs(char[] cArr, int i2, int i3) {
        byte[] bArr = new byte[(i3 - i2) / 2];
        int i4 = 0;
        while (i2 + 1 < i3) {
            int iDigit = Character.digit(cArr[i2], 16);
            int iDigit2 = Character.digit(cArr[i2 + 1], 16);
            if (iDigit < 0 || iDigit2 < 0) {
                break;
            }
            bArr[i4] = (byte) ((iDigit << 4) + iDigit2);
            i2 += 2;
            i4++;
        }
        if (i2 != i3) {
            throw new IllegalArgumentException("Illegal attribute value: " + new String(cArr));
        }
        return bArr;
    }

    private static byte[] getUtf8Octets(char[] cArr, int i2, int i3) {
        byte[] bArr = new byte[(i3 - i2) / 3];
        int i4 = 0;
        while (i2 + 2 < i3) {
            int i5 = i2;
            int i6 = i2 + 1;
            if (cArr[i5] != '\\') {
                break;
            }
            int i7 = i6 + 1;
            int iDigit = Character.digit(cArr[i6], 16);
            i2 = i7 + 1;
            int iDigit2 = Character.digit(cArr[i7], 16);
            if (iDigit < 0 || iDigit2 < 0) {
                break;
            }
            int i8 = i4;
            i4++;
            bArr[i8] = (byte) ((iDigit << 4) + iDigit2);
        }
        if (i4 == bArr.length) {
            return bArr;
        }
        byte[] bArr2 = new byte[i4];
        System.arraycopy(bArr, 0, bArr2, 0, i4);
        return bArr2;
    }

    private static boolean isWhitespace(char c2) {
        return c2 == ' ' || c2 == '\r';
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(toString());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.entries = new ArrayList<>(1);
        String str = (String) objectInputStream.readObject();
        try {
            new Rfc2253Parser(str).parseRdn(this);
        } catch (InvalidNameException e2) {
            throw new StreamCorruptedException("Invalid name: " + str);
        }
    }
}
