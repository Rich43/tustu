package javax.naming.directory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/* loaded from: rt.jar:javax/naming/directory/BasicAttributes.class */
public class BasicAttributes implements Attributes {
    private boolean ignoreCase;
    transient Hashtable<String, Attribute> attrs;
    private static final long serialVersionUID = 4980164073184639448L;

    public BasicAttributes() {
        this.ignoreCase = false;
        this.attrs = new Hashtable<>(11);
    }

    public BasicAttributes(boolean z2) {
        this.ignoreCase = false;
        this.attrs = new Hashtable<>(11);
        this.ignoreCase = z2;
    }

    public BasicAttributes(String str, Object obj) {
        this();
        put(new BasicAttribute(str, obj));
    }

    public BasicAttributes(String str, Object obj, boolean z2) {
        this(z2);
        put(new BasicAttribute(str, obj));
    }

    @Override // javax.naming.directory.Attributes
    public Object clone() {
        BasicAttributes basicAttributes;
        try {
            basicAttributes = (BasicAttributes) super.clone();
        } catch (CloneNotSupportedException e2) {
            basicAttributes = new BasicAttributes(this.ignoreCase);
        }
        basicAttributes.attrs = (Hashtable) this.attrs.clone();
        return basicAttributes;
    }

    @Override // javax.naming.directory.Attributes
    public boolean isCaseIgnored() {
        return this.ignoreCase;
    }

    @Override // javax.naming.directory.Attributes
    public int size() {
        return this.attrs.size();
    }

    @Override // javax.naming.directory.Attributes
    public Attribute get(String str) {
        return this.attrs.get(this.ignoreCase ? str.toLowerCase(Locale.ENGLISH) : str);
    }

    @Override // javax.naming.directory.Attributes
    public NamingEnumeration<Attribute> getAll() {
        return new AttrEnumImpl();
    }

    @Override // javax.naming.directory.Attributes
    public NamingEnumeration<String> getIDs() {
        return new IDEnumImpl();
    }

    @Override // javax.naming.directory.Attributes
    public Attribute put(String str, Object obj) {
        return put(new BasicAttribute(str, obj));
    }

    @Override // javax.naming.directory.Attributes
    public Attribute put(Attribute attribute) {
        String id = attribute.getID();
        if (this.ignoreCase) {
            id = id.toLowerCase(Locale.ENGLISH);
        }
        return this.attrs.put(id, attribute);
    }

    @Override // javax.naming.directory.Attributes
    public Attribute remove(String str) {
        return this.attrs.remove(this.ignoreCase ? str.toLowerCase(Locale.ENGLISH) : str);
    }

    public String toString() {
        if (this.attrs.size() == 0) {
            return "No attributes";
        }
        return this.attrs.toString();
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof Attributes)) {
            Attributes attributes = (Attributes) obj;
            if (this.ignoreCase == attributes.isCaseIgnored() && size() == attributes.size()) {
                try {
                    NamingEnumeration<? extends Attribute> all = attributes.getAll();
                    while (all.hasMore()) {
                        Attribute next = all.next();
                        if (!next.equals(get(next.getID()))) {
                            return false;
                        }
                    }
                    return true;
                } catch (NamingException e2) {
                    return false;
                }
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int iHashCode = this.ignoreCase ? 1 : 0;
        try {
            NamingEnumeration<Attribute> all = getAll();
            while (all.hasMore()) {
                iHashCode += all.next().hashCode();
            }
        } catch (NamingException e2) {
        }
        return iHashCode;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(this.attrs.size());
        Enumeration<Attribute> enumerationElements = this.attrs.elements();
        while (enumerationElements.hasMoreElements()) {
            objectOutputStream.writeObject(enumerationElements.nextElement2());
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        int i2 = objectInputStream.readInt();
        this.attrs = i2 >= 1 ? new Hashtable<>(1 + ((int) (Math.min(768, i2) / 0.75f))) : new Hashtable<>(2);
        while (true) {
            i2--;
            if (i2 >= 0) {
                put((Attribute) objectInputStream.readObject());
            } else {
                return;
            }
        }
    }

    /* loaded from: rt.jar:javax/naming/directory/BasicAttributes$AttrEnumImpl.class */
    class AttrEnumImpl implements NamingEnumeration<Attribute> {
        Enumeration<Attribute> elements;

        public AttrEnumImpl() {
            this.elements = BasicAttributes.this.attrs.elements();
        }

        @Override // java.util.Enumeration
        public boolean hasMoreElements() {
            return this.elements.hasMoreElements();
        }

        @Override // java.util.Enumeration
        /* renamed from: nextElement */
        public Attribute nextElement2() {
            return this.elements.nextElement2();
        }

        @Override // javax.naming.NamingEnumeration
        public boolean hasMore() throws NamingException {
            return hasMoreElements();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // javax.naming.NamingEnumeration
        public Attribute next() throws NamingException {
            return nextElement2();
        }

        @Override // javax.naming.NamingEnumeration
        public void close() throws NamingException {
            this.elements = null;
        }
    }

    /* loaded from: rt.jar:javax/naming/directory/BasicAttributes$IDEnumImpl.class */
    class IDEnumImpl implements NamingEnumeration<String> {
        Enumeration<Attribute> elements;

        public IDEnumImpl() {
            this.elements = BasicAttributes.this.attrs.elements();
        }

        @Override // java.util.Enumeration
        public boolean hasMoreElements() {
            return this.elements.hasMoreElements();
        }

        @Override // java.util.Enumeration
        /* renamed from: nextElement */
        public String nextElement2() {
            return this.elements.nextElement2().getID();
        }

        @Override // javax.naming.NamingEnumeration
        public boolean hasMore() throws NamingException {
            return hasMoreElements();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // javax.naming.NamingEnumeration
        public String next() throws NamingException {
            return nextElement2();
        }

        @Override // javax.naming.NamingEnumeration
        public void close() throws NamingException {
            this.elements = null;
        }
    }
}
