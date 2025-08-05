package javax.swing;

import javax.swing.event.ChangeListener;

/* loaded from: rt.jar:javax/swing/BoundedRangeModel.class */
public interface BoundedRangeModel {
    int getMinimum();

    void setMinimum(int i2);

    int getMaximum();

    void setMaximum(int i2);

    int getValue();

    void setValue(int i2);

    void setValueIsAdjusting(boolean z2);

    boolean getValueIsAdjusting();

    int getExtent();

    void setExtent(int i2);

    void setRangeProperties(int i2, int i3, int i4, int i5, boolean z2);

    void addChangeListener(ChangeListener changeListener);

    void removeChangeListener(ChangeListener changeListener);
}
