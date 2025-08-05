package javax.swing;

import java.io.Serializable;
import java.util.Vector;

/* loaded from: rt.jar:javax/swing/DefaultComboBoxModel.class */
public class DefaultComboBoxModel<E> extends AbstractListModel<E> implements MutableComboBoxModel<E>, Serializable {
    Vector<E> objects;
    Object selectedObject;

    public DefaultComboBoxModel() {
        this.objects = new Vector<>();
    }

    public DefaultComboBoxModel(E[] eArr) {
        this.objects = new Vector<>(eArr.length);
        for (E e2 : eArr) {
            this.objects.addElement(e2);
        }
        if (getSize() > 0) {
            this.selectedObject = getElementAt(0);
        }
    }

    public DefaultComboBoxModel(Vector<E> vector) {
        this.objects = vector;
        if (getSize() > 0) {
            this.selectedObject = getElementAt(0);
        }
    }

    @Override // javax.swing.ComboBoxModel
    public void setSelectedItem(Object obj) {
        if ((this.selectedObject != null && !this.selectedObject.equals(obj)) || (this.selectedObject == null && obj != null)) {
            this.selectedObject = obj;
            fireContentsChanged(this, -1, -1);
        }
    }

    @Override // javax.swing.ComboBoxModel
    public Object getSelectedItem() {
        return this.selectedObject;
    }

    @Override // javax.swing.ListModel
    public int getSize() {
        return this.objects.size();
    }

    @Override // javax.swing.ListModel
    public E getElementAt(int i2) {
        if (i2 >= 0 && i2 < this.objects.size()) {
            return this.objects.elementAt(i2);
        }
        return null;
    }

    public int getIndexOf(Object obj) {
        return this.objects.indexOf(obj);
    }

    @Override // javax.swing.MutableComboBoxModel
    public void addElement(E e2) {
        this.objects.addElement(e2);
        fireIntervalAdded(this, this.objects.size() - 1, this.objects.size() - 1);
        if (this.objects.size() == 1 && this.selectedObject == null && e2 != null) {
            setSelectedItem(e2);
        }
    }

    @Override // javax.swing.MutableComboBoxModel
    public void insertElementAt(E e2, int i2) {
        this.objects.insertElementAt(e2, i2);
        fireIntervalAdded(this, i2, i2);
    }

    @Override // javax.swing.MutableComboBoxModel
    public void removeElementAt(int i2) {
        if (getElementAt(i2) == this.selectedObject) {
            if (i2 == 0) {
                setSelectedItem(getSize() == 1 ? null : getElementAt(i2 + 1));
            } else {
                setSelectedItem(getElementAt(i2 - 1));
            }
        }
        this.objects.removeElementAt(i2);
        fireIntervalRemoved(this, i2, i2);
    }

    @Override // javax.swing.MutableComboBoxModel
    public void removeElement(Object obj) {
        int iIndexOf = this.objects.indexOf(obj);
        if (iIndexOf != -1) {
            removeElementAt(iIndexOf);
        }
    }

    public void removeAllElements() {
        if (this.objects.size() > 0) {
            int size = this.objects.size() - 1;
            this.objects.removeAllElements();
            this.selectedObject = null;
            fireIntervalRemoved(this, 0, size);
            return;
        }
        this.selectedObject = null;
    }
}
