package sun.awt.windows;

import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.ItemEvent;
import java.awt.peer.CheckboxPeer;

/* loaded from: rt.jar:sun/awt/windows/WCheckboxPeer.class */
final class WCheckboxPeer extends WComponentPeer implements CheckboxPeer {
    @Override // java.awt.peer.CheckboxPeer
    public native void setState(boolean z2);

    @Override // java.awt.peer.CheckboxPeer
    public native void setCheckboxGroup(CheckboxGroup checkboxGroup);

    @Override // java.awt.peer.CheckboxPeer
    public native void setLabel(String str);

    private static native int getCheckMarkSize();

    @Override // sun.awt.windows.WComponentPeer
    native void create(WComponentPeer wComponentPeer);

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public Dimension getMinimumSize() {
        String label = ((Checkbox) this.target).getLabel();
        int checkMarkSize = getCheckMarkSize();
        if (label == null) {
            label = "";
        }
        FontMetrics fontMetrics = getFontMetrics(((Checkbox) this.target).getFont());
        return new Dimension(fontMetrics.stringWidth(label) + (checkMarkSize / 2) + checkMarkSize, Math.max(fontMetrics.getHeight() + 8, checkMarkSize));
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public boolean isFocusable() {
        return true;
    }

    WCheckboxPeer(Checkbox checkbox) {
        super(checkbox);
    }

    @Override // sun.awt.windows.WComponentPeer
    void initialize() {
        Checkbox checkbox = (Checkbox) this.target;
        setState(checkbox.getState());
        setCheckboxGroup(checkbox.getCheckboxGroup());
        Color background = ((Component) this.target).getBackground();
        if (background != null) {
            setBackground(background);
        }
        super.initialize();
    }

    @Override // sun.awt.windows.WComponentPeer
    public boolean shouldClearRectBeforePaint() {
        return false;
    }

    void handleAction(final boolean z2) {
        final Checkbox checkbox = (Checkbox) this.target;
        WToolkit.executeOnEventHandlerThread(checkbox, new Runnable() { // from class: sun.awt.windows.WCheckboxPeer.1
            @Override // java.lang.Runnable
            public void run() {
                CheckboxGroup checkboxGroup = checkbox.getCheckboxGroup();
                if (checkboxGroup != null && checkbox == checkboxGroup.getSelectedCheckbox() && checkbox.getState()) {
                    return;
                }
                checkbox.setState(z2);
                WCheckboxPeer.this.postEvent(new ItemEvent(checkbox, 701, checkbox.getLabel(), z2 ? 1 : 2));
            }
        });
    }
}
