package javax.management;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/* loaded from: rt.jar:javax/management/AttributeList.class */
public class AttributeList extends ArrayList<Object> {
    private volatile transient boolean typeSafe;
    private volatile transient boolean tainted;
    private static final long serialVersionUID = -4077085769279709076L;

    public AttributeList() {
    }

    public AttributeList(int i2) {
        super(i2);
    }

    public AttributeList(AttributeList attributeList) {
        super(attributeList);
    }

    public AttributeList(List<Attribute> list) {
        if (list == null) {
            throw new IllegalArgumentException("Null parameter");
        }
        adding((Collection<?>) list);
        super.addAll(list);
    }

    public List<Attribute> asList() {
        this.typeSafe = true;
        if (this.tainted) {
            adding((Collection<?>) this);
        }
        return this;
    }

    public void add(Attribute attribute) {
        super.add((AttributeList) attribute);
    }

    public void add(int i2, Attribute attribute) {
        try {
            super.add(i2, (int) attribute);
        } catch (IndexOutOfBoundsException e2) {
            throw new RuntimeOperationsException(e2, "The specified index is out of range");
        }
    }

    public void set(int i2, Attribute attribute) {
        try {
            super.set(i2, (int) attribute);
        } catch (IndexOutOfBoundsException e2) {
            throw new RuntimeOperationsException(e2, "The specified index is out of range");
        }
    }

    public boolean addAll(AttributeList attributeList) {
        return super.addAll((Collection) attributeList);
    }

    public boolean addAll(int i2, AttributeList attributeList) {
        try {
            return super.addAll(i2, (Collection) attributeList);
        } catch (IndexOutOfBoundsException e2) {
            throw new RuntimeOperationsException(e2, "The specified index is out of range");
        }
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(Object obj) {
        adding(obj);
        return super.add((AttributeList) obj);
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.List
    public void add(int i2, Object obj) {
        adding(obj);
        super.add(i2, (int) obj);
    }

    @Override // java.util.ArrayList, java.util.AbstractCollection, java.util.Collection
    public boolean addAll(Collection<?> collection) {
        adding(collection);
        return super.addAll(collection);
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.List
    public boolean addAll(int i2, Collection<?> collection) {
        adding(collection);
        return super.addAll(i2, collection);
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.List
    public Object set(int i2, Object obj) {
        adding(obj);
        return super.set(i2, (int) obj);
    }

    private void adding(Object obj) {
        if (obj == null || (obj instanceof Attribute)) {
            return;
        }
        if (this.typeSafe) {
            throw new IllegalArgumentException("Not an Attribute: " + obj);
        }
        this.tainted = true;
    }

    private void adding(Collection<?> collection) {
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            adding(it.next());
        }
    }
}
