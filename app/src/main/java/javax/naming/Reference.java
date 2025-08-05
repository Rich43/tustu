package javax.naming;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

/* loaded from: rt.jar:javax/naming/Reference.class */
public class Reference implements Cloneable, Serializable {
    protected String className;
    protected Vector<RefAddr> addrs;
    protected String classFactory;
    protected String classFactoryLocation;
    private static final long serialVersionUID = -1673475790065791735L;

    public Reference(String str) {
        this.addrs = null;
        this.classFactory = null;
        this.classFactoryLocation = null;
        this.className = str;
        this.addrs = new Vector<>();
    }

    public Reference(String str, RefAddr refAddr) {
        this.addrs = null;
        this.classFactory = null;
        this.classFactoryLocation = null;
        this.className = str;
        this.addrs = new Vector<>();
        this.addrs.addElement(refAddr);
    }

    public Reference(String str, String str2, String str3) {
        this(str);
        this.classFactory = str2;
        this.classFactoryLocation = str3;
    }

    public Reference(String str, RefAddr refAddr, String str2, String str3) {
        this(str, refAddr);
        this.classFactory = str2;
        this.classFactoryLocation = str3;
    }

    public String getClassName() {
        return this.className;
    }

    public String getFactoryClassName() {
        return this.classFactory;
    }

    public String getFactoryClassLocation() {
        return this.classFactoryLocation;
    }

    public RefAddr get(String str) {
        int size = this.addrs.size();
        for (int i2 = 0; i2 < size; i2++) {
            RefAddr refAddrElementAt = this.addrs.elementAt(i2);
            if (refAddrElementAt.getType().compareTo(str) == 0) {
                return refAddrElementAt;
            }
        }
        return null;
    }

    public RefAddr get(int i2) {
        return this.addrs.elementAt(i2);
    }

    public Enumeration<RefAddr> getAll() {
        return this.addrs.elements();
    }

    public int size() {
        return this.addrs.size();
    }

    public void add(RefAddr refAddr) {
        this.addrs.addElement(refAddr);
    }

    public void add(int i2, RefAddr refAddr) {
        this.addrs.insertElementAt(refAddr, i2);
    }

    public Object remove(int i2) {
        RefAddr refAddrElementAt = this.addrs.elementAt(i2);
        this.addrs.removeElementAt(i2);
        return refAddrElementAt;
    }

    public void clear() {
        this.addrs.setSize(0);
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof Reference)) {
            Reference reference = (Reference) obj;
            if (reference.className.equals(this.className) && reference.size() == size()) {
                Enumeration<RefAddr> all = getAll();
                Enumeration<RefAddr> all2 = reference.getAll();
                while (all.hasMoreElements()) {
                    if (!all.nextElement().equals(all2.nextElement())) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int iHashCode = this.className.hashCode();
        Enumeration<RefAddr> all = getAll();
        while (all.hasMoreElements()) {
            iHashCode += all.nextElement().hashCode();
        }
        return iHashCode;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("Reference Class Name: " + this.className + "\n");
        int size = this.addrs.size();
        for (int i2 = 0; i2 < size; i2++) {
            stringBuffer.append(get(i2).toString());
        }
        return stringBuffer.toString();
    }

    public Object clone() {
        Reference reference = new Reference(this.className, this.classFactory, this.classFactoryLocation);
        Enumeration<RefAddr> all = getAll();
        reference.addrs = new Vector<>();
        while (all.hasMoreElements()) {
            reference.addrs.addElement(all.nextElement());
        }
        return reference;
    }
}
