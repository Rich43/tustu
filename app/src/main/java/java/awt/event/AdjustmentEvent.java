package java.awt.event;

import java.awt.AWTEvent;
import java.awt.Adjustable;

/* loaded from: rt.jar:java/awt/event/AdjustmentEvent.class */
public class AdjustmentEvent extends AWTEvent {
    public static final int ADJUSTMENT_FIRST = 601;
    public static final int ADJUSTMENT_LAST = 601;
    public static final int ADJUSTMENT_VALUE_CHANGED = 601;
    public static final int UNIT_INCREMENT = 1;
    public static final int UNIT_DECREMENT = 2;
    public static final int BLOCK_DECREMENT = 3;
    public static final int BLOCK_INCREMENT = 4;
    public static final int TRACK = 5;
    Adjustable adjustable;
    int value;
    int adjustmentType;
    boolean isAdjusting;
    private static final long serialVersionUID = 5700290645205279921L;

    public AdjustmentEvent(Adjustable adjustable, int i2, int i3, int i4) {
        this(adjustable, i2, i3, i4, false);
    }

    public AdjustmentEvent(Adjustable adjustable, int i2, int i3, int i4, boolean z2) {
        super(adjustable, i2);
        this.adjustable = adjustable;
        this.adjustmentType = i3;
        this.value = i4;
        this.isAdjusting = z2;
    }

    public Adjustable getAdjustable() {
        return this.adjustable;
    }

    public int getValue() {
        return this.value;
    }

    public int getAdjustmentType() {
        return this.adjustmentType;
    }

    public boolean getValueIsAdjusting() {
        return this.isAdjusting;
    }

    @Override // java.awt.AWTEvent
    public String paramString() {
        String str;
        String str2;
        switch (this.id) {
            case 601:
                str = "ADJUSTMENT_VALUE_CHANGED";
                break;
            default:
                str = "unknown type";
                break;
        }
        switch (this.adjustmentType) {
            case 1:
                str2 = "UNIT_INCREMENT";
                break;
            case 2:
                str2 = "UNIT_DECREMENT";
                break;
            case 3:
                str2 = "BLOCK_DECREMENT";
                break;
            case 4:
                str2 = "BLOCK_INCREMENT";
                break;
            case 5:
                str2 = "TRACK";
                break;
            default:
                str2 = "unknown type";
                break;
        }
        return str + ",adjType=" + str2 + ",value=" + this.value + ",isAdjusting=" + this.isAdjusting;
    }
}
