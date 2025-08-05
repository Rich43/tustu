package javax.swing;

import javax.swing.event.ListDataListener;

/* loaded from: rt.jar:javax/swing/ListModel.class */
public interface ListModel<E> {
    int getSize();

    E getElementAt(int i2);

    void addListDataListener(ListDataListener listDataListener);

    void removeListDataListener(ListDataListener listDataListener);
}
