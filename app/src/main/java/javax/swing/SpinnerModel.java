package javax.swing;

import javax.swing.event.ChangeListener;

/* loaded from: rt.jar:javax/swing/SpinnerModel.class */
public interface SpinnerModel {
    Object getValue();

    void setValue(Object obj);

    Object getNextValue();

    Object getPreviousValue();

    void addChangeListener(ChangeListener changeListener);

    void removeChangeListener(ChangeListener changeListener);
}
