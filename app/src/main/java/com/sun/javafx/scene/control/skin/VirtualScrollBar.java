package com.sun.javafx.scene.control.skin;

import javafx.scene.control.IndexedCell;
import javafx.scene.control.ScrollBar;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/VirtualScrollBar.class */
public class VirtualScrollBar extends ScrollBar {
    private final VirtualFlow flow;
    private boolean virtual;
    private boolean adjusting;

    public VirtualScrollBar(VirtualFlow flow) {
        this.flow = flow;
        super.valueProperty().addListener(valueModel -> {
            if (isVirtual() && !this.adjusting) {
                flow.setPosition(getValue());
            }
        });
    }

    public void setVirtual(boolean virtual) {
        this.virtual = virtual;
    }

    public boolean isVirtual() {
        return this.virtual;
    }

    @Override // javafx.scene.control.ScrollBar
    public void decrement() {
        if (isVirtual()) {
            this.flow.adjustPixels(-10.0d);
        } else {
            super.decrement();
        }
    }

    @Override // javafx.scene.control.ScrollBar
    public void increment() {
        if (isVirtual()) {
            this.flow.adjustPixels(10.0d);
        } else {
            super.increment();
        }
    }

    @Override // javafx.scene.control.ScrollBar
    public void adjustValue(double pos) {
        if (isVirtual()) {
            this.adjusting = true;
            double oldValue = this.flow.getPosition();
            double newValue = ((getMax() - getMin()) * com.sun.javafx.util.Utils.clamp(0.0d, pos, 1.0d)) + getMin();
            if (newValue < oldValue) {
                IndexedCell cell = this.flow.getFirstVisibleCell();
                if (cell == null) {
                    return;
                } else {
                    this.flow.showAsLast(cell);
                }
            } else if (newValue > oldValue) {
                IndexedCell cell2 = this.flow.getLastVisibleCell();
                if (cell2 == null) {
                    return;
                } else {
                    this.flow.showAsFirst(cell2);
                }
            }
            this.adjusting = false;
            return;
        }
        super.adjustValue(pos);
    }
}
