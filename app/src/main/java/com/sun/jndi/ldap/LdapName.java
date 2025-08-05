package com.sun.jndi.ldap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Vector;
import javafx.fxml.FXMLLoader;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: rt.jar:com/sun/jndi/ldap/LdapName.class */
public final class LdapName implements Name {
    private transient String unparsed;
    private transient Vector<Rdn> rdns;
    private transient boolean valuesCaseSensitive;
    static final long serialVersionUID = -1595520034788997356L;

    public LdapName(String str) throws InvalidNameException {
        this.valuesCaseSensitive = false;
        this.unparsed = str;
        parse();
    }

    private LdapName(String str, Vector<Rdn> vector) {
        this.valuesCaseSensitive = false;
        this.unparsed = str;
        this.rdns = (Vector) vector.clone();
    }

    private LdapName(String str, Vector<Rdn> vector, int i2, int i3) {
        this.valuesCaseSensitive = false;
        this.unparsed = str;
        this.rdns = new Vector<>();
        for (int i4 = i2; i4 < i3; i4++) {
            this.rdns.addElement(vector.elementAt(i4));
        }
    }

    @Override // javax.naming.Name
    public Object clone() {
        return new LdapName(this.unparsed, this.rdns);
    }

    public String toString() {
        if (this.unparsed != null) {
            return this.unparsed;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int size = this.rdns.size() - 1; size >= 0; size--) {
            if (size < this.rdns.size() - 1) {
                stringBuffer.append(',');
            }
            stringBuffer.append((Object) this.rdns.elementAt(size));
        }
        this.unparsed = new String(stringBuffer);
        return this.unparsed;
    }

    public boolean equals(Object obj) {
        return (obj instanceof LdapName) && compareTo(obj) == 0;
    }

    @Override // javax.naming.Name, java.lang.Comparable
    public int compareTo(Object obj) {
        LdapName ldapName = (LdapName) obj;
        if (obj != this) {
            if (this.unparsed != null && this.unparsed.equals(ldapName.unparsed)) {
                return 0;
            }
            int iMin = Math.min(this.rdns.size(), ldapName.rdns.size());
            for (int i2 = 0; i2 < iMin; i2++) {
                int iCompareTo = this.rdns.elementAt(i2).compareTo(ldapName.rdns.elementAt(i2));
                if (iCompareTo != 0) {
                    return iCompareTo;
                }
            }
            return this.rdns.size() - ldapName.rdns.size();
        }
        return 0;
    }

    public int hashCode() {
        int iHashCode = 0;
        for (int i2 = 0; i2 < this.rdns.size(); i2++) {
            iHashCode += this.rdns.elementAt(i2).hashCode();
        }
        return iHashCode;
    }

    @Override // javax.naming.Name
    public int size() {
        return this.rdns.size();
    }

    @Override // javax.naming.Name
    public boolean isEmpty() {
        return this.rdns.isEmpty();
    }

    @Override // javax.naming.Name
    public Enumeration<String> getAll() {
        final Enumeration<Rdn> enumerationElements = this.rdns.elements();
        return new Enumeration<String>() { // from class: com.sun.jndi.ldap.LdapName.1
            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return enumerationElements.hasMoreElements();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Enumeration
            /* renamed from: nextElement */
            public String nextElement2() {
                return ((Rdn) enumerationElements.nextElement2()).toString();
            }
        };
    }

    @Override // javax.naming.Name
    public String get(int i2) {
        return this.rdns.elementAt(i2).toString();
    }

    @Override // javax.naming.Name
    public Name getPrefix(int i2) {
        return new LdapName(null, this.rdns, 0, i2);
    }

    @Override // javax.naming.Name
    public Name getSuffix(int i2) {
        return new LdapName(null, this.rdns, i2, this.rdns.size());
    }

    @Override // javax.naming.Name
    public boolean startsWith(Name name) {
        int size = this.rdns.size();
        int size2 = name.size();
        return size >= size2 && matches(0, size2, name);
    }

    @Override // javax.naming.Name
    public boolean endsWith(Name name) {
        int size = this.rdns.size();
        int size2 = name.size();
        return size >= size2 && matches(size - size2, size, name);
    }

    public void setValuesCaseSensitive(boolean z2) {
        toString();
        this.rdns = null;
        try {
            parse();
            this.valuesCaseSensitive = z2;
        } catch (InvalidNameException e2) {
            throw new IllegalStateException("Cannot parse name: " + this.unparsed);
        }
    }

    private boolean matches(int i2, int i3, Name name) {
        Rdn rdn;
        for (int i4 = i2; i4 < i3; i4++) {
            if (name instanceof LdapName) {
                rdn = ((LdapName) name).rdns.elementAt(i4 - i2);
            } else {
                try {
                    rdn = new DnParser(name.get(i4 - i2), this.valuesCaseSensitive).getRdn();
                } catch (InvalidNameException e2) {
                    return false;
                }
            }
            if (!rdn.equals(this.rdns.elementAt(i4))) {
                return false;
            }
        }
        return true;
    }

    @Override // javax.naming.Name
    public Name addAll(Name name) throws InvalidNameException {
        return addAll(size(), name);
    }

    @Override // javax.naming.Name
    public Name addAll(int i2, Name name) throws InvalidNameException {
        if (name instanceof LdapName) {
            LdapName ldapName = (LdapName) name;
            for (int i3 = 0; i3 < ldapName.rdns.size(); i3++) {
                int i4 = i2;
                i2++;
                this.rdns.insertElementAt(ldapName.rdns.elementAt(i3), i4);
            }
        } else {
            Enumeration<String> all = name.getAll();
            while (all.hasMoreElements()) {
                int i5 = i2;
                i2++;
                this.rdns.insertElementAt(new DnParser(all.nextElement2(), this.valuesCaseSensitive).getRdn(), i5);
            }
        }
        this.unparsed = null;
        return this;
    }

    @Override // javax.naming.Name
    public Name add(String str) throws InvalidNameException {
        return add(size(), str);
    }

    @Override // javax.naming.Name
    public Name add(int i2, String str) throws InvalidNameException {
        this.rdns.insertElementAt(new DnParser(str, this.valuesCaseSensitive).getRdn(), i2);
        this.unparsed = null;
        return this;
    }

    @Override // javax.naming.Name
    public Object remove(int i2) throws InvalidNameException {
        String str = get(i2);
        this.rdns.removeElementAt(i2);
        this.unparsed = null;
        return str;
    }

    private void parse() throws InvalidNameException {
        this.rdns = new DnParser(this.unparsed, this.valuesCaseSensitive).getDn();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isWhitespace(char c2) {
        return c2 == ' ' || c2 == '\r';
    }

    public static String escapeAttributeValue(Object obj) {
        return TypeAndValue.escapeValue(obj);
    }

    public static Object unescapeAttributeValue(String str) {
        return TypeAndValue.unescapeValue(str);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(toString());
        objectOutputStream.writeBoolean(this.valuesCaseSensitive);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.unparsed = (String) objectInputStream.readObject();
        this.valuesCaseSensitive = objectInputStream.readBoolean();
        try {
            parse();
        } catch (InvalidNameException e2) {
            throw new StreamCorruptedException("Invalid name: " + this.unparsed);
        }
    }

    /* loaded from: rt.jar:com/sun/jndi/ldap/LdapName$DnParser.class */
    static class DnParser {
        private final String name;
        private final char[] chars;
        private final int len;
        private int cur = 0;
        private boolean valuesCaseSensitive;

        DnParser(String str, boolean z2) throws InvalidNameException {
            this.name = str;
            this.len = str.length();
            this.chars = str.toCharArray();
            this.valuesCaseSensitive = z2;
        }

        Vector<Rdn> getDn() throws InvalidNameException {
            this.cur = 0;
            Vector<Rdn> vector = new Vector<>((this.len / 3) + 10);
            if (this.len == 0) {
                return vector;
            }
            vector.addElement(parseRdn());
            while (this.cur < this.len) {
                if (this.chars[this.cur] == ',' || this.chars[this.cur] == ';') {
                    this.cur++;
                    vector.insertElementAt(parseRdn(), 0);
                } else {
                    throw new InvalidNameException("Invalid name: " + this.name);
                }
            }
            return vector;
        }

        Rdn getRdn() throws InvalidNameException {
            Rdn rdn = parseRdn();
            if (this.cur < this.len) {
                throw new InvalidNameException("Invalid RDN: " + this.name);
            }
            return rdn;
        }

        private Rdn parseRdn() throws InvalidNameException {
            Rdn rdn = new Rdn();
            while (this.cur < this.len) {
                consumeWhitespace();
                String attrType = parseAttrType();
                consumeWhitespace();
                if (this.cur >= this.len || this.chars[this.cur] != '=') {
                    throw new InvalidNameException("Invalid name: " + this.name);
                }
                this.cur++;
                consumeWhitespace();
                String attrValue = parseAttrValue();
                consumeWhitespace();
                rdn.add(new TypeAndValue(attrType, attrValue, this.valuesCaseSensitive));
                if (this.cur >= this.len || this.chars[this.cur] != '+') {
                    break;
                }
                this.cur++;
            }
            return rdn;
        }

        private String parseAttrType() throws InvalidNameException {
            int i2 = this.cur;
            while (this.cur < this.len) {
                char c2 = this.chars[this.cur];
                if (!Character.isLetterOrDigit(c2) && c2 != '.' && c2 != '-' && c2 != ' ') {
                    break;
                }
                this.cur++;
            }
            while (this.cur > i2 && this.chars[this.cur - 1] == ' ') {
                this.cur--;
            }
            if (i2 == this.cur) {
                throw new InvalidNameException("Invalid name: " + this.name);
            }
            return new String(this.chars, i2, this.cur - i2);
        }

        private String parseAttrValue() throws InvalidNameException {
            if (this.cur < this.len && this.chars[this.cur] == '#') {
                return parseBinaryAttrValue();
            }
            if (this.cur < this.len && this.chars[this.cur] == '\"') {
                return parseQuotedAttrValue();
            }
            return parseStringAttrValue();
        }

        private String parseBinaryAttrValue() throws InvalidNameException {
            int i2 = this.cur;
            this.cur++;
            while (this.cur < this.len && Character.isLetterOrDigit(this.chars[this.cur])) {
                this.cur++;
            }
            return new String(this.chars, i2, this.cur - i2);
        }

        private String parseQuotedAttrValue() throws InvalidNameException {
            int i2 = this.cur;
            this.cur++;
            while (this.cur < this.len && this.chars[this.cur] != '\"') {
                if (this.chars[this.cur] == '\\') {
                    this.cur++;
                }
                this.cur++;
            }
            if (this.cur >= this.len) {
                throw new InvalidNameException("Invalid name: " + this.name);
            }
            this.cur++;
            return new String(this.chars, i2, this.cur - i2);
        }

        private String parseStringAttrValue() throws InvalidNameException {
            int i2 = this.cur;
            int i3 = -1;
            while (this.cur < this.len && !atTerminator()) {
                if (this.chars[this.cur] == '\\') {
                    this.cur++;
                    i3 = this.cur;
                }
                this.cur++;
            }
            if (this.cur > this.len) {
                throw new InvalidNameException("Invalid name: " + this.name);
            }
            int i4 = this.cur;
            while (i4 > i2 && LdapName.isWhitespace(this.chars[i4 - 1]) && i3 != i4 - 1) {
                i4--;
            }
            return new String(this.chars, i2, i4 - i2);
        }

        private void consumeWhitespace() {
            while (this.cur < this.len && LdapName.isWhitespace(this.chars[this.cur])) {
                this.cur++;
            }
        }

        private boolean atTerminator() {
            return this.cur < this.len && (this.chars[this.cur] == ',' || this.chars[this.cur] == ';' || this.chars[this.cur] == '+');
        }
    }

    /* loaded from: rt.jar:com/sun/jndi/ldap/LdapName$Rdn.class */
    static class Rdn {
        private final Vector<TypeAndValue> tvs = new Vector<>();

        Rdn() {
        }

        void add(TypeAndValue typeAndValue) {
            int i2 = 0;
            while (i2 < this.tvs.size()) {
                int iCompareTo = typeAndValue.compareTo(this.tvs.elementAt(i2));
                if (iCompareTo == 0) {
                    return;
                }
                if (iCompareTo < 0) {
                    break;
                } else {
                    i2++;
                }
            }
            this.tvs.insertElementAt(typeAndValue, i2);
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i2 = 0; i2 < this.tvs.size(); i2++) {
                if (i2 > 0) {
                    stringBuffer.append('+');
                }
                stringBuffer.append((Object) this.tvs.elementAt(i2));
            }
            return new String(stringBuffer);
        }

        public boolean equals(Object obj) {
            return (obj instanceof Rdn) && compareTo(obj) == 0;
        }

        public int compareTo(Object obj) {
            Rdn rdn = (Rdn) obj;
            int iMin = Math.min(this.tvs.size(), rdn.tvs.size());
            for (int i2 = 0; i2 < iMin; i2++) {
                int iCompareTo = this.tvs.elementAt(i2).compareTo(rdn.tvs.elementAt(i2));
                if (iCompareTo != 0) {
                    return iCompareTo;
                }
            }
            return this.tvs.size() - rdn.tvs.size();
        }

        public int hashCode() {
            int iHashCode = 0;
            for (int i2 = 0; i2 < this.tvs.size(); i2++) {
                iHashCode += this.tvs.elementAt(i2).hashCode();
            }
            return iHashCode;
        }

        Attributes toAttributes() {
            BasicAttributes basicAttributes = new BasicAttributes(true);
            for (int i2 = 0; i2 < this.tvs.size(); i2++) {
                TypeAndValue typeAndValueElementAt = this.tvs.elementAt(i2);
                Attribute attribute = basicAttributes.get(typeAndValueElementAt.getType());
                if (attribute == null) {
                    basicAttributes.put(typeAndValueElementAt.getType(), typeAndValueElementAt.getUnescapedValue());
                } else {
                    attribute.add(typeAndValueElementAt.getUnescapedValue());
                }
            }
            return basicAttributes;
        }
    }

    /* loaded from: rt.jar:com/sun/jndi/ldap/LdapName$TypeAndValue.class */
    static class TypeAndValue {
        private final String type;
        private final String value;
        private final boolean binary;
        private final boolean valueCaseSensitive;
        private String comparable = null;

        TypeAndValue(String str, String str2, boolean z2) {
            this.type = str;
            this.value = str2;
            this.binary = str2.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX);
            this.valueCaseSensitive = z2;
        }

        public String toString() {
            return this.type + "=" + this.value;
        }

        public int compareTo(Object obj) {
            TypeAndValue typeAndValue = (TypeAndValue) obj;
            int iCompareToIgnoreCase = this.type.compareToIgnoreCase(typeAndValue.type);
            if (iCompareToIgnoreCase != 0) {
                return iCompareToIgnoreCase;
            }
            if (this.value.equals(typeAndValue.value)) {
                return 0;
            }
            return getValueComparable().compareTo(typeAndValue.getValueComparable());
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof TypeAndValue)) {
                return false;
            }
            TypeAndValue typeAndValue = (TypeAndValue) obj;
            return this.type.equalsIgnoreCase(typeAndValue.type) && (this.value.equals(typeAndValue.value) || getValueComparable().equals(typeAndValue.getValueComparable()));
        }

        public int hashCode() {
            return this.type.toUpperCase(Locale.ENGLISH).hashCode() + getValueComparable().hashCode();
        }

        String getType() {
            return this.type;
        }

        Object getUnescapedValue() {
            return unescapeValue(this.value);
        }

        private String getValueComparable() {
            if (this.comparable != null) {
                return this.comparable;
            }
            if (this.binary) {
                this.comparable = this.value.toUpperCase(Locale.ENGLISH);
            } else {
                this.comparable = (String) unescapeValue(this.value);
                if (!this.valueCaseSensitive) {
                    this.comparable = this.comparable.toUpperCase(Locale.ENGLISH);
                }
            }
            return this.comparable;
        }

        static String escapeValue(Object obj) {
            if (obj instanceof byte[]) {
                return escapeBinaryValue((byte[]) obj);
            }
            return escapeStringValue((String) obj);
        }

        private static String escapeStringValue(String str) {
            char[] charArray = str.toCharArray();
            StringBuffer stringBuffer = new StringBuffer(2 * str.length());
            int i2 = 0;
            while (i2 < charArray.length && LdapName.isWhitespace(charArray[i2])) {
                i2++;
            }
            int length = charArray.length - 1;
            while (length >= 0 && LdapName.isWhitespace(charArray[length])) {
                length--;
            }
            for (int i3 = 0; i3 < charArray.length; i3++) {
                char c2 = charArray[i3];
                if (i3 < i2 || i3 > length || ",=+<>#;\"\\".indexOf(c2) >= 0) {
                    stringBuffer.append('\\');
                }
                stringBuffer.append(c2);
            }
            return new String(stringBuffer);
        }

        private static String escapeBinaryValue(byte[] bArr) {
            StringBuffer stringBuffer = new StringBuffer(1 + (2 * bArr.length));
            stringBuffer.append(FXMLLoader.CONTROLLER_METHOD_PREFIX);
            for (byte b2 : bArr) {
                stringBuffer.append(Character.forDigit(15 & (b2 >>> 4), 16));
                stringBuffer.append(Character.forDigit(15 & b2, 16));
            }
            return new String(stringBuffer).toUpperCase(Locale.ENGLISH);
        }

        static Object unescapeValue(String str) {
            char[] charArray = str.toCharArray();
            int i2 = 0;
            int length = charArray.length;
            while (i2 < length && LdapName.isWhitespace(charArray[i2])) {
                i2++;
            }
            while (i2 < length && LdapName.isWhitespace(charArray[length - 1])) {
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
            StringBuffer stringBuffer = new StringBuffer(length - i2);
            int i3 = -1;
            int length2 = i2;
            while (length2 < length) {
                if (charArray[length2] == '\\' && length2 + 1 < length) {
                    if (!Character.isLetterOrDigit(charArray[length2 + 1])) {
                        length2++;
                        stringBuffer.append(charArray[length2]);
                        i3 = length2;
                    } else {
                        byte[] utf8Octets = getUtf8Octets(charArray, length2, length);
                        if (utf8Octets.length > 0) {
                            try {
                                stringBuffer.append(new String(utf8Octets, InternalZipConstants.CHARSET_UTF8));
                            } catch (UnsupportedEncodingException e2) {
                            }
                            length2 += (utf8Octets.length * 3) - 1;
                        } else {
                            throw new IllegalArgumentException("Not a valid attribute string value:" + str + ", improper usage of backslash");
                        }
                    }
                } else {
                    stringBuffer.append(charArray[length2]);
                }
                length2++;
            }
            int length3 = stringBuffer.length();
            if (LdapName.isWhitespace(stringBuffer.charAt(length3 - 1)) && i3 != length - 1) {
                stringBuffer.setLength(length3 - 1);
            }
            return new String(stringBuffer);
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
                throw new IllegalArgumentException("Illegal attribute value: #" + new String(cArr));
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
    }
}
