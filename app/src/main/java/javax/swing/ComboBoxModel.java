package javax.swing;

/* loaded from: rt.jar:javax/swing/ComboBoxModel.class */
public interface ComboBoxModel<E> extends ListModel<E> {
    void setSelectedItem(Object obj);

    Object getSelectedItem();
}
