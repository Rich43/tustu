package javax.swing;

import java.util.Enumeration;
import java.util.Vector;

/* loaded from: rt.jar:javax/swing/DefaultListModel.class */
public class DefaultListModel<E> extends AbstractListModel<E> {
    private Vector<E> delegate = new Vector<>();

    @Override // javax.swing.ListModel
    public int getSize() {
        return this.delegate.size();
    }

    @Override // javax.swing.ListModel
    public E getElementAt(int i2) {
        return this.delegate.elementAt(i2);
    }

    public void copyInto(Object[] objArr) {
        this.delegate.copyInto(objArr);
    }

    public void trimToSize() {
        this.delegate.trimToSize();
    }

    public void ensureCapacity(int i2) {
        this.delegate.ensureCapacity(i2);
    }

    public void setSize(int i2) {
        int size = this.delegate.size();
        this.delegate.setSize(i2);
        if (size > i2) {
            fireIntervalRemoved(this, i2, size - 1);
        } else if (size < i2) {
            fireIntervalAdded(this, size, i2 - 1);
        }
    }

    public int capacity() {
        return this.delegate.capacity();
    }

    public int size() {
        return this.delegate.size();
    }

    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }

    public Enumeration<E> elements() {
        return this.delegate.elements();
    }

    public boolean contains(Object obj) {
        return this.delegate.contains(obj);
    }

    public int indexOf(Object obj) {
        return this.delegate.indexOf(obj);
    }

    public int indexOf(Object obj, int i2) {
        return this.delegate.indexOf(obj, i2);
    }

    public int lastIndexOf(Object obj) {
        return this.delegate.lastIndexOf(obj);
    }

    public int lastIndexOf(Object obj, int i2) {
        return this.delegate.lastIndexOf(obj, i2);
    }

    public E elementAt(int i2) {
        return this.delegate.elementAt(i2);
    }

    public E firstElement() {
        return this.delegate.firstElement();
    }

    public E lastElement() {
        return this.delegate.lastElement();
    }

    public void setElementAt(E e2, int i2) {
        this.delegate.setElementAt(e2, i2);
        fireContentsChanged(this, i2, i2);
    }

    public void removeElementAt(int i2) {
        this.delegate.removeElementAt(i2);
        fireIntervalRemoved(this, i2, i2);
    }

    public void insertElementAt(E e2, int i2) {
        this.delegate.insertElementAt(e2, i2);
        fireIntervalAdded(this, i2, i2);
    }

    public void addElement(E e2) {
        int size = this.delegate.size();
        this.delegate.addElement(e2);
        fireIntervalAdded(this, size, size);
    }

    public boolean removeElement(Object obj) {
        int iIndexOf = indexOf(obj);
        boolean zRemoveElement = this.delegate.removeElement(obj);
        if (iIndexOf >= 0) {
            fireIntervalRemoved(this, iIndexOf, iIndexOf);
        }
        return zRemoveElement;
    }

    public void removeAllElements() {
        int size = this.delegate.size() - 1;
        this.delegate.removeAllElements();
        if (size >= 0) {
            fireIntervalRemoved(this, 0, size);
        }
    }

    public String toString() {
        return this.delegate.toString();
    }

    public Object[] toArray() {
        Object[] objArr = new Object[this.delegate.size()];
        this.delegate.copyInto(objArr);
        return objArr;
    }

    public E get(int i2) {
        return this.delegate.elementAt(i2);
    }

    public E set(int i2, E e2) {
        E eElementAt = this.delegate.elementAt(i2);
        this.delegate.setElementAt(e2, i2);
        fireContentsChanged(this, i2, i2);
        return eElementAt;
    }

    public void add(int i2, E e2) {
        this.delegate.insertElementAt(e2, i2);
        fireIntervalAdded(this, i2, i2);
    }

    public E remove(int i2) {
        E eElementAt = this.delegate.elementAt(i2);
        this.delegate.removeElementAt(i2);
        fireIntervalRemoved(this, i2, i2);
        return eElementAt;
    }

    public void clear() {
        int size = this.delegate.size() - 1;
        this.delegate.removeAllElements();
        if (size >= 0) {
            fireIntervalRemoved(this, 0, size);
        }
    }

    public void removeRange(int i2, int i3) {
        if (i2 > i3) {
            throw new IllegalArgumentException("fromIndex must be <= toIndex");
        }
        for (int i4 = i3; i4 >= i2; i4--) {
            this.delegate.removeElementAt(i4);
        }
        fireIntervalRemoved(this, i2, i3);
    }
}
