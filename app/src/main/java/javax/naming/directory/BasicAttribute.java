package javax.naming.directory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;

/* loaded from: rt.jar:javax/naming/directory/BasicAttribute.class */
public class BasicAttribute implements Attribute {
    protected String attrID;
    protected transient Vector<Object> values;
    protected boolean ordered;
    private static final long serialVersionUID = 6743528196119291326L;

    @Override // javax.naming.directory.Attribute
    public Object clone() {
        BasicAttribute basicAttribute;
        try {
            basicAttribute = (BasicAttribute) super.clone();
        } catch (CloneNotSupportedException e2) {
            basicAttribute = new BasicAttribute(this.attrID, this.ordered);
        }
        basicAttribute.values = (Vector) this.values.clone();
        return basicAttribute;
    }

    public boolean equals(Object obj) {
        int size;
        if (obj != null && (obj instanceof Attribute)) {
            Attribute attribute = (Attribute) obj;
            if (isOrdered() == attribute.isOrdered() && this.attrID.equals(attribute.getID()) && (size = size()) == attribute.size()) {
                try {
                    if (isOrdered()) {
                        for (int i2 = 0; i2 < size; i2++) {
                            if (!valueEquals(get(i2), attribute.get(i2))) {
                                return false;
                            }
                        }
                        return true;
                    }
                    NamingEnumeration<?> all = attribute.getAll();
                    while (all.hasMoreElements()) {
                        if (find(all.nextElement2()) < 0) {
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

    public int hashCode() throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        int iHashCode = this.attrID.hashCode();
        int size = this.values.size();
        for (int i2 = 0; i2 < size; i2++) {
            Object objElementAt = this.values.elementAt(i2);
            if (objElementAt != null) {
                if (objElementAt.getClass().isArray()) {
                    int length = Array.getLength(objElementAt);
                    for (int i3 = 0; i3 < length; i3++) {
                        Object obj = Array.get(objElementAt, i3);
                        if (obj != null) {
                            iHashCode += obj.hashCode();
                        }
                    }
                } else {
                    iHashCode += objElementAt.hashCode();
                }
            }
        }
        return iHashCode;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer(this.attrID + ": ");
        if (this.values.size() == 0) {
            stringBuffer.append("No values");
        } else {
            boolean z2 = true;
            Enumeration<Object> enumerationElements = this.values.elements();
            while (enumerationElements.hasMoreElements()) {
                if (!z2) {
                    stringBuffer.append(", ");
                }
                stringBuffer.append(enumerationElements.nextElement2());
                z2 = false;
            }
        }
        return stringBuffer.toString();
    }

    public BasicAttribute(String str) {
        this(str, false);
    }

    public BasicAttribute(String str, Object obj) {
        this(str, obj, false);
    }

    public BasicAttribute(String str, boolean z2) {
        this.ordered = false;
        this.attrID = str;
        this.values = new Vector<>();
        this.ordered = z2;
    }

    public BasicAttribute(String str, Object obj, boolean z2) {
        this(str, z2);
        this.values.addElement(obj);
    }

    @Override // javax.naming.directory.Attribute
    public NamingEnumeration<?> getAll() throws NamingException {
        return new ValuesEnumImpl();
    }

    @Override // javax.naming.directory.Attribute
    public Object get() throws NamingException {
        if (this.values.size() == 0) {
            throw new NoSuchElementException("Attribute " + getID() + " has no value");
        }
        return this.values.elementAt(0);
    }

    @Override // javax.naming.directory.Attribute
    public int size() {
        return this.values.size();
    }

    @Override // javax.naming.directory.Attribute
    public String getID() {
        return this.attrID;
    }

    @Override // javax.naming.directory.Attribute
    public boolean contains(Object obj) {
        return find(obj) >= 0;
    }

    private int find(Object obj) {
        if (obj == null) {
            int size = this.values.size();
            for (int i2 = 0; i2 < size; i2++) {
                if (this.values.elementAt(i2) == null) {
                    return i2;
                }
            }
            return -1;
        }
        Class<?> cls = obj.getClass();
        if (cls.isArray()) {
            int size2 = this.values.size();
            for (int i3 = 0; i3 < size2; i3++) {
                Object objElementAt = this.values.elementAt(i3);
                if (objElementAt != null && cls == objElementAt.getClass() && arrayEquals(obj, objElementAt)) {
                    return i3;
                }
            }
            return -1;
        }
        return this.values.indexOf(obj, 0);
    }

    private static boolean valueEquals(Object obj, Object obj2) {
        if (obj == obj2) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj.getClass().isArray() && obj2.getClass().isArray()) {
            return arrayEquals(obj, obj2);
        }
        return obj.equals(obj2);
    }

    private static boolean arrayEquals(Object obj, Object obj2) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        int length = Array.getLength(obj);
        if (length != Array.getLength(obj2)) {
            return false;
        }
        for (int i2 = 0; i2 < length; i2++) {
            Object obj3 = Array.get(obj, i2);
            Object obj4 = Array.get(obj2, i2);
            if (obj3 == null || obj4 == null) {
                if (obj3 != obj4) {
                    return false;
                }
            } else if (!obj3.equals(obj4)) {
                return false;
            }
        }
        return true;
    }

    @Override // javax.naming.directory.Attribute
    public boolean add(Object obj) {
        if (isOrdered() || find(obj) < 0) {
            this.values.addElement(obj);
            return true;
        }
        return false;
    }

    @Override // javax.naming.directory.Attribute
    public boolean remove(Object obj) {
        int iFind = find(obj);
        if (iFind >= 0) {
            this.values.removeElementAt(iFind);
            return true;
        }
        return false;
    }

    @Override // javax.naming.directory.Attribute
    public void clear() {
        this.values.setSize(0);
    }

    @Override // javax.naming.directory.Attribute
    public boolean isOrdered() {
        return this.ordered;
    }

    @Override // javax.naming.directory.Attribute
    public Object get(int i2) throws NamingException {
        return this.values.elementAt(i2);
    }

    @Override // javax.naming.directory.Attribute
    public Object remove(int i2) {
        Object objElementAt = this.values.elementAt(i2);
        this.values.removeElementAt(i2);
        return objElementAt;
    }

    @Override // javax.naming.directory.Attribute
    public void add(int i2, Object obj) {
        if (!isOrdered() && contains(obj)) {
            throw new IllegalStateException("Cannot add duplicate to unordered attribute");
        }
        this.values.insertElementAt(obj, i2);
    }

    @Override // javax.naming.directory.Attribute
    public Object set(int i2, Object obj) {
        if (!isOrdered() && contains(obj)) {
            throw new IllegalStateException("Cannot add duplicate to unordered attribute");
        }
        Object objElementAt = this.values.elementAt(i2);
        this.values.setElementAt(obj, i2);
        return objElementAt;
    }

    @Override // javax.naming.directory.Attribute
    public DirContext getAttributeSyntaxDefinition() throws NamingException {
        throw new OperationNotSupportedException("attribute syntax");
    }

    @Override // javax.naming.directory.Attribute
    public DirContext getAttributeDefinition() throws NamingException {
        throw new OperationNotSupportedException("attribute definition");
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(this.values.size());
        for (int i2 = 0; i2 < this.values.size(); i2++) {
            objectOutputStream.writeObject(this.values.elementAt(i2));
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        int i2 = objectInputStream.readInt();
        this.values = new Vector<>(Math.min(1024, i2));
        while (true) {
            i2--;
            if (i2 >= 0) {
                this.values.addElement(objectInputStream.readObject());
            } else {
                return;
            }
        }
    }

    /* loaded from: rt.jar:javax/naming/directory/BasicAttribute$ValuesEnumImpl.class */
    class ValuesEnumImpl implements NamingEnumeration<Object> {
        Enumeration<Object> list;

        ValuesEnumImpl() {
            this.list = BasicAttribute.this.values.elements();
        }

        @Override // java.util.Enumeration
        public boolean hasMoreElements() {
            return this.list.hasMoreElements();
        }

        @Override // java.util.Enumeration
        /* renamed from: nextElement */
        public Object nextElement2() {
            return this.list.nextElement2();
        }

        @Override // javax.naming.NamingEnumeration
        public Object next() throws NamingException {
            return this.list.nextElement2();
        }

        @Override // javax.naming.NamingEnumeration
        public boolean hasMore() throws NamingException {
            return this.list.hasMoreElements();
        }

        @Override // javax.naming.NamingEnumeration
        public void close() throws NamingException {
            this.list = null;
        }
    }
}
