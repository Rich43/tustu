package javax.print.attribute;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

/* loaded from: rt.jar:javax/print/attribute/HashAttributeSet.class */
public class HashAttributeSet implements AttributeSet, Serializable {
    private static final long serialVersionUID = 5311560590283707917L;
    private Class myInterface;
    private transient HashMap attrMap;

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        Attribute[] array = toArray();
        objectOutputStream.writeInt(array.length);
        for (Attribute attribute : array) {
            objectOutputStream.writeObject(attribute);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.attrMap = new HashMap();
        int i2 = objectInputStream.readInt();
        for (int i3 = 0; i3 < i2; i3++) {
            add((Attribute) objectInputStream.readObject());
        }
    }

    public HashAttributeSet() {
        this((Class<?>) Attribute.class);
    }

    public HashAttributeSet(Attribute attribute) {
        this(attribute, (Class<?>) Attribute.class);
    }

    public HashAttributeSet(Attribute[] attributeArr) {
        this(attributeArr, (Class<?>) Attribute.class);
    }

    public HashAttributeSet(AttributeSet attributeSet) {
        this(attributeSet, (Class<?>) Attribute.class);
    }

    protected HashAttributeSet(Class<?> cls) {
        this.attrMap = new HashMap();
        if (cls == null) {
            throw new NullPointerException("null interface");
        }
        this.myInterface = cls;
    }

    protected HashAttributeSet(Attribute attribute, Class<?> cls) {
        this.attrMap = new HashMap();
        if (cls == null) {
            throw new NullPointerException("null interface");
        }
        this.myInterface = cls;
        add(attribute);
    }

    protected HashAttributeSet(Attribute[] attributeArr, Class<?> cls) {
        this.attrMap = new HashMap();
        if (cls == null) {
            throw new NullPointerException("null interface");
        }
        this.myInterface = cls;
        int length = attributeArr == null ? 0 : attributeArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            add(attributeArr[i2]);
        }
    }

    protected HashAttributeSet(AttributeSet attributeSet, Class<?> cls) {
        this.attrMap = new HashMap();
        this.myInterface = cls;
        if (attributeSet != null) {
            Attribute[] array = attributeSet.toArray();
            int length = array == null ? 0 : array.length;
            for (int i2 = 0; i2 < length; i2++) {
                add(array[i2]);
            }
        }
    }

    @Override // javax.print.attribute.AttributeSet
    public Attribute get(Class<?> cls) {
        return (Attribute) this.attrMap.get(AttributeSetUtilities.verifyAttributeCategory(cls, Attribute.class));
    }

    @Override // javax.print.attribute.AttributeSet
    public boolean add(Attribute attribute) {
        return !attribute.equals(this.attrMap.put(attribute.getCategory(), AttributeSetUtilities.verifyAttributeValue(attribute, this.myInterface)));
    }

    @Override // javax.print.attribute.AttributeSet
    public boolean remove(Class<?> cls) {
        return (cls == null || AttributeSetUtilities.verifyAttributeCategory(cls, Attribute.class) == null || this.attrMap.remove(cls) == null) ? false : true;
    }

    @Override // javax.print.attribute.AttributeSet
    public boolean remove(Attribute attribute) {
        return (attribute == null || this.attrMap.remove(attribute.getCategory()) == null) ? false : true;
    }

    @Override // javax.print.attribute.AttributeSet
    public boolean containsKey(Class<?> cls) {
        return (cls == null || AttributeSetUtilities.verifyAttributeCategory(cls, Attribute.class) == null || this.attrMap.get(cls) == null) ? false : true;
    }

    @Override // javax.print.attribute.AttributeSet
    public boolean containsValue(Attribute attribute) {
        return attribute != null && (attribute instanceof Attribute) && attribute.equals(this.attrMap.get(attribute.getCategory()));
    }

    @Override // javax.print.attribute.AttributeSet
    public boolean addAll(AttributeSet attributeSet) {
        boolean z2 = false;
        for (Attribute attribute : attributeSet.toArray()) {
            Attribute attributeVerifyAttributeValue = AttributeSetUtilities.verifyAttributeValue(attribute, this.myInterface);
            z2 = !attributeVerifyAttributeValue.equals(this.attrMap.put(attributeVerifyAttributeValue.getCategory(), attributeVerifyAttributeValue)) || z2;
        }
        return z2;
    }

    @Override // javax.print.attribute.AttributeSet
    public int size() {
        return this.attrMap.size();
    }

    @Override // javax.print.attribute.AttributeSet
    public Attribute[] toArray() {
        Attribute[] attributeArr = new Attribute[size()];
        this.attrMap.values().toArray(attributeArr);
        return attributeArr;
    }

    @Override // javax.print.attribute.AttributeSet
    public void clear() {
        this.attrMap.clear();
    }

    @Override // javax.print.attribute.AttributeSet
    public boolean isEmpty() {
        return this.attrMap.isEmpty();
    }

    @Override // javax.print.attribute.AttributeSet
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof AttributeSet)) {
            return false;
        }
        AttributeSet attributeSet = (AttributeSet) obj;
        if (attributeSet.size() != size()) {
            return false;
        }
        for (Attribute attribute : toArray()) {
            if (!attributeSet.containsValue(attribute)) {
                return false;
            }
        }
        return true;
    }

    @Override // javax.print.attribute.AttributeSet
    public int hashCode() {
        int iHashCode = 0;
        for (Attribute attribute : toArray()) {
            iHashCode += attribute.hashCode();
        }
        return iHashCode;
    }
}
