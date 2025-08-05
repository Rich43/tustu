package java.awt;

import java.awt.event.AdjustmentListener;

/* loaded from: rt.jar:java/awt/Adjustable.class */
public interface Adjustable {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int NO_ORIENTATION = 2;

    int getOrientation();

    void setMinimum(int i2);

    int getMinimum();

    void setMaximum(int i2);

    int getMaximum();

    void setUnitIncrement(int i2);

    int getUnitIncrement();

    void setBlockIncrement(int i2);

    int getBlockIncrement();

    void setVisibleAmount(int i2);

    int getVisibleAmount();

    void setValue(int i2);

    int getValue();

    void addAdjustmentListener(AdjustmentListener adjustmentListener);

    void removeAdjustmentListener(AdjustmentListener adjustmentListener);
}
