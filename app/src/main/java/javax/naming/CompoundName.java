package javax.naming;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Enumeration;
import java.util.Properties;

/* loaded from: rt.jar:javax/naming/CompoundName.class */
public class CompoundName implements Name {
    protected transient NameImpl impl;
    protected transient Properties mySyntax;
    private static final long serialVersionUID = 3513100557083972036L;

    protected CompoundName(Enumeration<String> enumeration, Properties properties) {
        if (properties == null) {
            throw new NullPointerException();
        }
        this.mySyntax = properties;
        this.impl = new NameImpl(properties, enumeration);
    }

    public CompoundName(String str, Properties properties) throws InvalidNameException {
        if (properties == null) {
            throw new NullPointerException();
        }
        this.mySyntax = properties;
        this.impl = new NameImpl(properties, str);
    }

    public String toString() {
        return this.impl.toString();
    }

    public boolean equals(Object obj) {
        return obj != null && (obj instanceof CompoundName) && this.impl.equals(((CompoundName) obj).impl);
    }

    public int hashCode() {
        return this.impl.hashCode();
    }

    @Override // javax.naming.Name
    public Object clone() {
        return new CompoundName(getAll(), this.mySyntax);
    }

    @Override // javax.naming.Name, java.lang.Comparable
    public int compareTo(Object obj) {
        if (!(obj instanceof CompoundName)) {
            throw new ClassCastException("Not a CompoundName");
        }
        return this.impl.compareTo(((CompoundName) obj).impl);
    }

    @Override // javax.naming.Name
    public int size() {
        return this.impl.size();
    }

    @Override // javax.naming.Name
    public boolean isEmpty() {
        return this.impl.isEmpty();
    }

    @Override // javax.naming.Name
    public Enumeration<String> getAll() {
        return this.impl.getAll();
    }

    @Override // javax.naming.Name
    public String get(int i2) {
        return this.impl.get(i2);
    }

    @Override // javax.naming.Name
    public Name getPrefix(int i2) {
        return new CompoundName(this.impl.getPrefix(i2), this.mySyntax);
    }

    @Override // javax.naming.Name
    public Name getSuffix(int i2) {
        return new CompoundName(this.impl.getSuffix(i2), this.mySyntax);
    }

    @Override // javax.naming.Name
    public boolean startsWith(Name name) {
        if (name instanceof CompoundName) {
            return this.impl.startsWith(name.size(), name.getAll());
        }
        return false;
    }

    @Override // javax.naming.Name
    public boolean endsWith(Name name) {
        if (name instanceof CompoundName) {
            return this.impl.endsWith(name.size(), name.getAll());
        }
        return false;
    }

    @Override // javax.naming.Name
    public Name addAll(Name name) throws InvalidNameException {
        if (name instanceof CompoundName) {
            this.impl.addAll(name.getAll());
            return this;
        }
        throw new InvalidNameException("Not a compound name: " + name.toString());
    }

    @Override // javax.naming.Name
    public Name addAll(int i2, Name name) throws InvalidNameException {
        if (name instanceof CompoundName) {
            this.impl.addAll(i2, name.getAll());
            return this;
        }
        throw new InvalidNameException("Not a compound name: " + name.toString());
    }

    @Override // javax.naming.Name
    public Name add(String str) throws InvalidNameException {
        this.impl.add(str);
        return this;
    }

    @Override // javax.naming.Name
    public Name add(int i2, String str) throws InvalidNameException {
        this.impl.add(i2, str);
        return this;
    }

    @Override // javax.naming.Name
    public Object remove(int i2) throws InvalidNameException {
        return this.impl.remove(i2);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(this.mySyntax);
        objectOutputStream.writeInt(size());
        Enumeration<String> all = getAll();
        while (all.hasMoreElements()) {
            objectOutputStream.writeObject(all.nextElement2());
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.mySyntax = (Properties) objectInputStream.readObject();
        this.impl = new NameImpl(this.mySyntax);
        int i2 = objectInputStream.readInt();
        while (true) {
            try {
                i2--;
                if (i2 >= 0) {
                    add((String) objectInputStream.readObject());
                } else {
                    return;
                }
            } catch (InvalidNameException e2) {
                throw new StreamCorruptedException("Invalid name");
            }
        }
    }
}
