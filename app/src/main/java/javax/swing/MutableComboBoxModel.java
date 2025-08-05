package javax.swing;

/* loaded from: rt.jar:javax/swing/MutableComboBoxModel.class */
public interface MutableComboBoxModel<E> extends ComboBoxModel<E> {
    void addElement(E e2);

    void removeElement(Object obj);

    void insertElementAt(E e2, int i2);

    void removeElementAt(int i2);
}
