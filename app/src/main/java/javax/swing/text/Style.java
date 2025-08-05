package javax.swing.text;

import javax.swing.event.ChangeListener;

/* loaded from: rt.jar:javax/swing/text/Style.class */
public interface Style extends MutableAttributeSet {
    String getName();

    void addChangeListener(ChangeListener changeListener);

    void removeChangeListener(ChangeListener changeListener);
}
