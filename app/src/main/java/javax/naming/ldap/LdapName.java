package javax.naming.ldap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.naming.InvalidNameException;
import javax.naming.Name;

/* loaded from: rt.jar:javax/naming/ldap/LdapName.class */
public class LdapName implements Name {
    private transient List<Rdn> rdns;
    private transient String unparsed;
    private static final long serialVersionUID = -1595520034788997356L;

    public LdapName(String str) throws InvalidNameException {
        this.unparsed = str;
        parse();
    }

    public LdapName(List<Rdn> list) {
        this.rdns = new ArrayList(list.size());
        for (int i2 = 0; i2 < list.size(); i2++) {
            Rdn rdn = list.get(i2);
            if (!(rdn instanceof Rdn)) {
                throw new IllegalArgumentException("Entry:" + ((Object) rdn) + "  not a valid type;list entries must be of type Rdn");
            }
            this.rdns.add(rdn);
        }
    }

    private LdapName(String str, List<Rdn> list, int i2, int i3) {
        this.unparsed = str;
        this.rdns = new ArrayList(list.subList(i2, i3));
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
        final Iterator<Rdn> it = this.rdns.iterator();
        return new Enumeration<String>() { // from class: javax.naming.ldap.LdapName.1
            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return it.hasNext();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Enumeration
            public String nextElement() {
                return ((Rdn) it.next()).toString();
            }
        };
    }

    @Override // javax.naming.Name
    public String get(int i2) {
        return this.rdns.get(i2).toString();
    }

    public Rdn getRdn(int i2) {
        return this.rdns.get(i2);
    }

    @Override // javax.naming.Name
    public Name getPrefix(int i2) {
        try {
            return new LdapName(null, this.rdns, 0, i2);
        } catch (IllegalArgumentException e2) {
            throw new IndexOutOfBoundsException("Posn: " + i2 + ", Size: " + this.rdns.size());
        }
    }

    @Override // javax.naming.Name
    public Name getSuffix(int i2) {
        try {
            return new LdapName(null, this.rdns, i2, this.rdns.size());
        } catch (IllegalArgumentException e2) {
            throw new IndexOutOfBoundsException("Posn: " + i2 + ", Size: " + this.rdns.size());
        }
    }

    @Override // javax.naming.Name
    public boolean startsWith(Name name) {
        if (name == null) {
            return false;
        }
        int size = this.rdns.size();
        int size2 = name.size();
        return size >= size2 && matches(0, size2, name);
    }

    public boolean startsWith(List<Rdn> list) {
        if (list == null) {
            return false;
        }
        int size = this.rdns.size();
        int size2 = list.size();
        return size >= size2 && doesListMatch(0, size2, list);
    }

    @Override // javax.naming.Name
    public boolean endsWith(Name name) {
        int size;
        int size2;
        return name != null && (size = this.rdns.size()) >= (size2 = name.size()) && matches(size - size2, size, name);
    }

    public boolean endsWith(List<Rdn> list) {
        int size;
        int size2;
        return list != null && (size = this.rdns.size()) >= (size2 = list.size()) && doesListMatch(size - size2, size, list);
    }

    private boolean doesListMatch(int i2, int i3, List<Rdn> list) {
        for (int i4 = i2; i4 < i3; i4++) {
            if (!this.rdns.get(i4).equals(list.get(i4 - i2))) {
                return false;
            }
        }
        return true;
    }

    private boolean matches(int i2, int i3, Name name) {
        if (name instanceof LdapName) {
            return doesListMatch(i2, i3, ((LdapName) name).rdns);
        }
        for (int i4 = i2; i4 < i3; i4++) {
            try {
                if (!new Rfc2253Parser(name.get(i4 - i2)).parseRdn().equals(this.rdns.get(i4))) {
                    return false;
                }
            } catch (InvalidNameException e2) {
                return false;
            }
        }
        return true;
    }

    @Override // javax.naming.Name
    public Name addAll(Name name) throws InvalidNameException {
        return addAll(size(), name);
    }

    public Name addAll(List<Rdn> list) {
        return addAll(size(), list);
    }

    @Override // javax.naming.Name
    public Name addAll(int i2, Name name) throws InvalidNameException {
        this.unparsed = null;
        if (name instanceof LdapName) {
            this.rdns.addAll(i2, ((LdapName) name).rdns);
        } else {
            Enumeration<String> all = name.getAll();
            while (all.hasMoreElements()) {
                int i3 = i2;
                i2++;
                this.rdns.add(i3, new Rfc2253Parser(all.nextElement()).parseRdn());
            }
        }
        return this;
    }

    public Name addAll(int i2, List<Rdn> list) {
        this.unparsed = null;
        for (int i3 = 0; i3 < list.size(); i3++) {
            Rdn rdn = list.get(i3);
            if (!(rdn instanceof Rdn)) {
                throw new IllegalArgumentException("Entry:" + ((Object) rdn) + "  not a valid type;suffix list entries must be of type Rdn");
            }
            this.rdns.add(i3 + i2, rdn);
        }
        return this;
    }

    @Override // javax.naming.Name
    public Name add(String str) throws InvalidNameException {
        return add(size(), str);
    }

    public Name add(Rdn rdn) {
        return add(size(), rdn);
    }

    @Override // javax.naming.Name
    public Name add(int i2, String str) throws InvalidNameException {
        this.rdns.add(i2, new Rfc2253Parser(str).parseRdn());
        this.unparsed = null;
        return this;
    }

    public Name add(int i2, Rdn rdn) {
        if (rdn == null) {
            throw new NullPointerException("Cannot set comp to null");
        }
        this.rdns.add(i2, rdn);
        this.unparsed = null;
        return this;
    }

    @Override // javax.naming.Name
    public Object remove(int i2) throws InvalidNameException {
        this.unparsed = null;
        return this.rdns.remove(i2).toString();
    }

    public List<Rdn> getRdns() {
        return Collections.unmodifiableList(this.rdns);
    }

    @Override // javax.naming.Name
    public Object clone() {
        return new LdapName(this.unparsed, this.rdns, 0, this.rdns.size());
    }

    public String toString() {
        if (this.unparsed != null) {
            return this.unparsed;
        }
        StringBuilder sb = new StringBuilder();
        int size = this.rdns.size();
        if (size - 1 >= 0) {
            sb.append((Object) this.rdns.get(size - 1));
        }
        for (int i2 = size - 2; i2 >= 0; i2--) {
            sb.append(',');
            sb.append((Object) this.rdns.get(i2));
        }
        this.unparsed = sb.toString();
        return this.unparsed;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof LdapName)) {
            return false;
        }
        LdapName ldapName = (LdapName) obj;
        if (this.rdns.size() != ldapName.rdns.size()) {
            return false;
        }
        if (this.unparsed != null && this.unparsed.equalsIgnoreCase(ldapName.unparsed)) {
            return true;
        }
        for (int i2 = 0; i2 < this.rdns.size(); i2++) {
            if (!this.rdns.get(i2).equals(ldapName.rdns.get(i2))) {
                return false;
            }
        }
        return true;
    }

    @Override // javax.naming.Name, java.lang.Comparable
    public int compareTo(Object obj) {
        if (!(obj instanceof LdapName)) {
            throw new ClassCastException("The obj is not a LdapName");
        }
        if (obj == this) {
            return 0;
        }
        LdapName ldapName = (LdapName) obj;
        if (this.unparsed != null && this.unparsed.equalsIgnoreCase(ldapName.unparsed)) {
            return 0;
        }
        int iMin = Math.min(this.rdns.size(), ldapName.rdns.size());
        for (int i2 = 0; i2 < iMin; i2++) {
            int iCompareTo = this.rdns.get(i2).compareTo(ldapName.rdns.get(i2));
            if (iCompareTo != 0) {
                return iCompareTo;
            }
        }
        return this.rdns.size() - ldapName.rdns.size();
    }

    public int hashCode() {
        int iHashCode = 0;
        for (int i2 = 0; i2 < this.rdns.size(); i2++) {
            iHashCode += this.rdns.get(i2).hashCode();
        }
        return iHashCode;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(toString());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.unparsed = (String) objectInputStream.readObject();
        try {
            parse();
        } catch (InvalidNameException e2) {
            throw new StreamCorruptedException("Invalid name: " + this.unparsed);
        }
    }

    private void parse() throws InvalidNameException {
        this.rdns = new Rfc2253Parser(this.unparsed).parseDn();
    }
}
