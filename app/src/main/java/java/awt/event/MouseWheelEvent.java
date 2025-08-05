package java.awt.event;

import java.awt.Component;

/* loaded from: rt.jar:java/awt/event/MouseWheelEvent.class */
public class MouseWheelEvent extends MouseEvent {
    public static final int WHEEL_UNIT_SCROLL = 0;
    public static final int WHEEL_BLOCK_SCROLL = 1;
    int scrollType;
    int scrollAmount;
    int wheelRotation;
    double preciseWheelRotation;
    private static final long serialVersionUID = 6459879390515399677L;

    public MouseWheelEvent(Component component, int i2, long j2, int i3, int i4, int i5, int i6, boolean z2, int i7, int i8, int i9) {
        this(component, i2, j2, i3, i4, i5, 0, 0, i6, z2, i7, i8, i9);
    }

    public MouseWheelEvent(Component component, int i2, long j2, int i3, int i4, int i5, int i6, int i7, int i8, boolean z2, int i9, int i10, int i11) {
        this(component, i2, j2, i3, i4, i5, i6, i7, i8, z2, i9, i10, i11, i11);
    }

    public MouseWheelEvent(Component component, int i2, long j2, int i3, int i4, int i5, int i6, int i7, int i8, boolean z2, int i9, int i10, int i11, double d2) {
        super(component, i2, j2, i3, i4, i5, i6, i7, i8, z2, 0);
        this.scrollType = i9;
        this.scrollAmount = i10;
        this.wheelRotation = i11;
        this.preciseWheelRotation = d2;
    }

    public int getScrollType() {
        return this.scrollType;
    }

    public int getScrollAmount() {
        return this.scrollAmount;
    }

    public int getWheelRotation() {
        return this.wheelRotation;
    }

    public double getPreciseWheelRotation() {
        return this.preciseWheelRotation;
    }

    public int getUnitsToScroll() {
        return this.scrollAmount * this.wheelRotation;
    }

    @Override // java.awt.event.MouseEvent, java.awt.event.ComponentEvent, java.awt.AWTEvent
    public String paramString() {
        String str;
        if (getScrollType() == 0) {
            str = "WHEEL_UNIT_SCROLL";
        } else if (getScrollType() == 1) {
            str = "WHEEL_BLOCK_SCROLL";
        } else {
            str = "unknown scroll type";
        }
        return super.paramString() + ",scrollType=" + str + ",scrollAmount=" + getScrollAmount() + ",wheelRotation=" + getWheelRotation() + ",preciseWheelRotation=" + getPreciseWheelRotation();
    }
}
