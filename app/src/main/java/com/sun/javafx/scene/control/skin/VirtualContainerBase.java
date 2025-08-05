package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import javafx.scene.control.Control;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.ScrollToEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/VirtualContainerBase.class */
public abstract class VirtualContainerBase<C extends Control, B extends BehaviorBase<C>, I extends IndexedCell> extends BehaviorSkinBase<C, B> {
    protected boolean rowCountDirty;
    protected final VirtualFlow<I> flow;

    public abstract I createCell();

    public abstract int getItemCount();

    protected abstract void updateRowCount();

    public VirtualContainerBase(C control, B behavior) {
        super(control, behavior);
        this.flow = createVirtualFlow();
        control.addEventHandler(ScrollToEvent.scrollToTopIndex(), event -> {
            if (this.rowCountDirty) {
                updateRowCount();
                this.rowCountDirty = false;
            }
            this.flow.scrollTo(((Integer) event.getScrollTarget()).intValue());
        });
    }

    protected VirtualFlow<I> createVirtualFlow() {
        return new VirtualFlow<>();
    }

    double getMaxCellWidth(int rowsToCount) {
        return snappedLeftInset() + this.flow.getMaxCellWidth(rowsToCount) + snappedRightInset();
    }

    double getVirtualFlowPreferredHeight(int rows) {
        double height = 1.0d;
        for (int i2 = 0; i2 < rows && i2 < getItemCount(); i2++) {
            height += this.flow.getCellLength(i2);
        }
        return height + snappedTopInset() + snappedBottomInset();
    }

    @Override // javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        checkState();
    }

    protected void checkState() {
        if (this.rowCountDirty) {
            updateRowCount();
            this.rowCountDirty = false;
        }
    }
}
