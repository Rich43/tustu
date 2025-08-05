package com.sun.jndi.dns;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.naming.CompositeName;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:com/sun/jndi/dns/DnsName.class */
public final class DnsName implements Name {
    private String domain;
    private ArrayList<String> labels;
    private short octets;
    private static final long serialVersionUID = 7040187611324710271L;

    public DnsName() {
        this.domain = "";
        this.labels = new ArrayList<>();
        this.octets = (short) 1;
    }

    public DnsName(String str) throws InvalidNameException {
        this.domain = "";
        this.labels = new ArrayList<>();
        this.octets = (short) 1;
        parse(str);
    }

    private DnsName(DnsName dnsName, int i2, int i3) {
        this.domain = "";
        this.labels = new ArrayList<>();
        this.octets = (short) 1;
        this.labels.addAll(dnsName.labels.subList(dnsName.size() - i3, dnsName.size() - i2));
        if (size() == dnsName.size()) {
            this.domain = dnsName.domain;
            this.octets = dnsName.octets;
            return;
        }
        Iterator<String> it = this.labels.iterator();
        while (it.hasNext()) {
            String next = it.next();
            if (next.length() > 0) {
                this.octets = (short) (this.octets + ((short) (next.length() + 1)));
            }
        }
    }

    public String toString() {
        if (this.domain == null) {
            StringBuilder sb = new StringBuilder();
            Iterator<String> it = this.labels.iterator();
            while (it.hasNext()) {
                String next = it.next();
                if (sb.length() > 0 || next.length() == 0) {
                    sb.append('.');
                }
                escape(sb, next);
            }
            this.domain = sb.toString();
        }
        return this.domain;
    }

    public boolean isHostName() {
        Iterator<String> it = this.labels.iterator();
        while (it.hasNext()) {
            if (!isHostNameLabel(it.next())) {
                return false;
            }
        }
        return true;
    }

    public short getOctets() {
        return this.octets;
    }

    @Override // javax.naming.Name
    public int size() {
        return this.labels.size();
    }

    @Override // javax.naming.Name
    public boolean isEmpty() {
        return size() == 0;
    }

    public int hashCode() {
        int iHashCode = 0;
        for (int i2 = 0; i2 < size(); i2++) {
            iHashCode = (31 * iHashCode) + getKey(i2).hashCode();
        }
        return iHashCode;
    }

    public boolean equals(Object obj) {
        return (obj instanceof Name) && !(obj instanceof CompositeName) && size() == ((Name) obj).size() && compareTo(obj) == 0;
    }

    @Override // javax.naming.Name, java.lang.Comparable
    public int compareTo(Object obj) {
        return compareRange(0, size(), (Name) obj);
    }

    @Override // javax.naming.Name
    public boolean startsWith(Name name) {
        return size() >= name.size() && compareRange(0, name.size(), name) == 0;
    }

    @Override // javax.naming.Name
    public boolean endsWith(Name name) {
        return size() >= name.size() && compareRange(size() - name.size(), size(), name) == 0;
    }

    @Override // javax.naming.Name
    public String get(int i2) {
        if (i2 < 0 || i2 >= size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return this.labels.get((size() - i2) - 1);
    }

    @Override // javax.naming.Name
    public Enumeration<String> getAll() {
        return new Enumeration<String>() { // from class: com.sun.jndi.dns.DnsName.1
            int pos = 0;

            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return this.pos < DnsName.this.size();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Enumeration
            public String nextElement() {
                if (this.pos < DnsName.this.size()) {
                    DnsName dnsName = DnsName.this;
                    int i2 = this.pos;
                    this.pos = i2 + 1;
                    return dnsName.get(i2);
                }
                throw new NoSuchElementException();
            }
        };
    }

    @Override // javax.naming.Name
    public Name getPrefix(int i2) {
        return new DnsName(this, 0, i2);
    }

    @Override // javax.naming.Name
    public Name getSuffix(int i2) {
        return new DnsName(this, i2, size());
    }

    @Override // javax.naming.Name
    public Object clone() {
        return new DnsName(this, 0, size());
    }

    @Override // javax.naming.Name
    public Object remove(int i2) {
        if (i2 < 0 || i2 >= size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        String strRemove = this.labels.remove((size() - i2) - 1);
        int length = strRemove.length();
        if (length > 0) {
            this.octets = (short) (this.octets - ((short) (length + 1)));
        }
        this.domain = null;
        return strRemove;
    }

    @Override // javax.naming.Name
    public Name add(String str) throws InvalidNameException {
        return add(size(), str);
    }

    @Override // javax.naming.Name
    public Name add(int i2, String str) throws InvalidNameException {
        if (i2 < 0 || i2 > size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int length = str.length();
        if ((i2 > 0 && length == 0) || (i2 == 0 && hasRootLabel())) {
            throw new InvalidNameException("Empty label must be the last label in a domain name");
        }
        if (length > 0) {
            if (this.octets + length + 1 >= 256) {
                throw new InvalidNameException("Name too long");
            }
            this.octets = (short) (this.octets + ((short) (length + 1)));
        }
        int size = size() - i2;
        verifyLabel(str);
        this.labels.add(size, str);
        this.domain = null;
        return this;
    }

    @Override // javax.naming.Name
    public Name addAll(Name name) throws InvalidNameException {
        return addAll(size(), name);
    }

    @Override // javax.naming.Name
    public Name addAll(int i2, Name name) throws InvalidNameException {
        if (name instanceof DnsName) {
            DnsName dnsName = (DnsName) name;
            if (dnsName.isEmpty()) {
                return this;
            }
            if ((i2 > 0 && dnsName.hasRootLabel()) || (i2 == 0 && hasRootLabel())) {
                throw new InvalidNameException("Empty label must be the last label in a domain name");
            }
            short s2 = (short) ((this.octets + dnsName.octets) - 1);
            if (s2 > 255) {
                throw new InvalidNameException("Name too long");
            }
            this.octets = s2;
            this.labels.addAll(size() - i2, dnsName.labels);
            if (isEmpty()) {
                this.domain = dnsName.domain;
            } else if (this.domain == null || dnsName.domain == null) {
                this.domain = null;
            } else if (i2 == 0) {
                this.domain += (dnsName.domain.equals(".") ? "" : ".") + dnsName.domain;
            } else if (i2 == size()) {
                this.domain = dnsName.domain + (this.domain.equals(".") ? "" : ".") + this.domain;
            } else {
                this.domain = null;
            }
        } else if (name instanceof CompositeName) {
        } else {
            for (int size = name.size() - 1; size >= 0; size--) {
                add(i2, name.get(size));
            }
        }
        return this;
    }

    boolean hasRootLabel() {
        return !isEmpty() && get(0).equals("");
    }

    private int compareRange(int i2, int i3, Name name) {
        if (name instanceof CompositeName) {
            name = (DnsName) name;
        }
        int iMin = Math.min(i3 - i2, name.size());
        for (int i4 = 0; i4 < iMin; i4++) {
            String str = get(i4 + i2);
            String str2 = name.get(i4);
            int size = (size() - (i4 + i2)) - 1;
            int iCompareLabels = compareLabels(str, str2);
            if (iCompareLabels != 0) {
                return iCompareLabels;
            }
        }
        return (i3 - i2) - name.size();
    }

    String getKey(int i2) {
        return keyForLabel(get(i2));
    }

    private void parse(String str) throws InvalidNameException {
        StringBuffer stringBuffer = new StringBuffer();
        int i2 = 0;
        while (i2 < str.length()) {
            char cCharAt = str.charAt(i2);
            if (cCharAt == '\\') {
                int i3 = i2;
                i2++;
                char escapedOctet = getEscapedOctet(str, i3);
                if (isDigit(str.charAt(i2))) {
                    i2 += 2;
                }
                stringBuffer.append(escapedOctet);
            } else if (cCharAt != '.') {
                stringBuffer.append(cCharAt);
            } else {
                add(0, stringBuffer.toString());
                stringBuffer.delete(0, i2);
            }
            i2++;
        }
        if (!str.equals("") && !str.equals(".")) {
            add(0, stringBuffer.toString());
        }
        this.domain = str;
    }

    private static char getEscapedOctet(String str, int i2) throws InvalidNameException {
        try {
            int i3 = i2 + 1;
            char cCharAt = str.charAt(i3);
            if (isDigit(cCharAt)) {
                int i4 = i3 + 1;
                char cCharAt2 = str.charAt(i4);
                char cCharAt3 = str.charAt(i4 + 1);
                if (isDigit(cCharAt2) && isDigit(cCharAt3)) {
                    return (char) (((cCharAt - '0') * 100) + ((cCharAt2 - '0') * 10) + (cCharAt3 - '0'));
                }
                throw new InvalidNameException("Invalid escape sequence in " + str);
            }
            return cCharAt;
        } catch (IndexOutOfBoundsException e2) {
            throw new InvalidNameException("Invalid escape sequence in " + str);
        }
    }

    private static void verifyLabel(String str) throws InvalidNameException {
        if (str.length() > 63) {
            throw new InvalidNameException("Label exceeds 63 octets: " + str);
        }
        for (int i2 = 0; i2 < str.length(); i2++) {
            if ((str.charAt(i2) & 65280) != 0) {
                throw new InvalidNameException("Label has two-byte char: " + str);
            }
        }
    }

    private static boolean isHostNameLabel(String str) {
        for (int i2 = 0; i2 < str.length(); i2++) {
            if (!isHostNameChar(str.charAt(i2))) {
                return false;
            }
        }
        return (str.startsWith(LanguageTag.SEP) || str.endsWith(LanguageTag.SEP)) ? false : true;
    }

    private static boolean isHostNameChar(char c2) {
        return c2 == '-' || (c2 >= 'a' && c2 <= 'z') || ((c2 >= 'A' && c2 <= 'Z') || (c2 >= '0' && c2 <= '9'));
    }

    private static boolean isDigit(char c2) {
        return c2 >= '0' && c2 <= '9';
    }

    private static void escape(StringBuilder sb, String str) {
        for (int i2 = 0; i2 < str.length(); i2++) {
            char cCharAt = str.charAt(i2);
            if (cCharAt == '.' || cCharAt == '\\') {
                sb.append('\\');
            }
            sb.append(cCharAt);
        }
    }

    private static int compareLabels(String str, String str2) {
        int iMin = Math.min(str.length(), str2.length());
        for (int i2 = 0; i2 < iMin; i2++) {
            char cCharAt = str.charAt(i2);
            char cCharAt2 = str2.charAt(i2);
            if (cCharAt >= 'A' && cCharAt <= 'Z') {
                cCharAt = (char) (cCharAt + ' ');
            }
            if (cCharAt2 >= 'A' && cCharAt2 <= 'Z') {
                cCharAt2 = (char) (cCharAt2 + ' ');
            }
            if (cCharAt != cCharAt2) {
                return cCharAt - cCharAt2;
            }
        }
        return str.length() - str2.length();
    }

    private static String keyForLabel(String str) {
        StringBuffer stringBuffer = new StringBuffer(str.length());
        for (int i2 = 0; i2 < str.length(); i2++) {
            char cCharAt = str.charAt(i2);
            if (cCharAt >= 'A' && cCharAt <= 'Z') {
                cCharAt = (char) (cCharAt + ' ');
            }
            stringBuffer.append(cCharAt);
        }
        return stringBuffer.toString();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(toString());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        try {
            parse((String) objectInputStream.readObject());
        } catch (InvalidNameException e2) {
            throw new StreamCorruptedException("Invalid name: " + this.domain);
        }
    }
}
