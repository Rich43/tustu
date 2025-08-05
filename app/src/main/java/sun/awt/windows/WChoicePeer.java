package sun.awt.windows;

import java.awt.Choice;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.peer.ChoicePeer;
import sun.awt.SunToolkit;

/* loaded from: rt.jar:sun/awt/windows/WChoicePeer.class */
final class WChoicePeer extends WComponentPeer implements ChoicePeer {
    private WindowListener windowListener;

    @Override // java.awt.peer.ChoicePeer
    public native void select(int i2);

    @Override // java.awt.peer.ChoicePeer
    public native void removeAll();

    @Override // java.awt.peer.ChoicePeer
    public native void remove(int i2);

    public native void addItems(String[] strArr, int i2);

    @Override // sun.awt.windows.WComponentPeer
    public native synchronized void reshape(int i2, int i3, int i4, int i5);

    @Override // sun.awt.windows.WComponentPeer
    native void create(WComponentPeer wComponentPeer);

    native void closeList();

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public Dimension getMinimumSize() {
        FontMetrics fontMetrics = getFontMetrics(((Choice) this.target).getFont());
        Choice choice = (Choice) this.target;
        int iMax = 0;
        int itemCount = choice.getItemCount();
        while (true) {
            int i2 = itemCount;
            itemCount--;
            if (i2 > 0) {
                iMax = Math.max(fontMetrics.stringWidth(choice.getItem(itemCount)), iMax);
            } else {
                return new Dimension(28 + iMax, Math.max(fontMetrics.getHeight() + 6, 15));
            }
        }
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public boolean isFocusable() {
        return true;
    }

    @Override // java.awt.peer.ChoicePeer
    public void add(String str, int i2) {
        addItem(str, i2);
    }

    @Override // sun.awt.windows.WComponentPeer
    public boolean shouldClearRectBeforePaint() {
        return false;
    }

    public void addItem(String str, int i2) {
        addItems(new String[]{str}, i2);
    }

    WChoicePeer(Choice choice) {
        super(choice);
    }

    @Override // sun.awt.windows.WComponentPeer
    void initialize() {
        WWindowPeer wWindowPeer;
        Choice choice = (Choice) this.target;
        int itemCount = choice.getItemCount();
        if (itemCount > 0) {
            String[] strArr = new String[itemCount];
            for (int i2 = 0; i2 < itemCount; i2++) {
                strArr[i2] = choice.getItem(i2);
            }
            addItems(strArr, 0);
            if (choice.getSelectedIndex() >= 0) {
                select(choice.getSelectedIndex());
            }
        }
        Window containingWindow = SunToolkit.getContainingWindow((Component) this.target);
        if (containingWindow != null && (wWindowPeer = (WWindowPeer) containingWindow.getPeer()) != null) {
            this.windowListener = new WindowAdapter() { // from class: sun.awt.windows.WChoicePeer.1
                @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
                public void windowIconified(WindowEvent windowEvent) {
                    WChoicePeer.this.closeList();
                }

                @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
                public void windowClosing(WindowEvent windowEvent) {
                    WChoicePeer.this.closeList();
                }
            };
            wWindowPeer.addWindowListener(this.windowListener);
        }
        super.initialize();
    }

    @Override // sun.awt.windows.WComponentPeer, sun.awt.windows.WObjectPeer
    protected void disposeImpl() {
        WWindowPeer wWindowPeer;
        Window containingWindow = SunToolkit.getContainingWindow((Component) this.target);
        if (containingWindow != null && (wWindowPeer = (WWindowPeer) containingWindow.getPeer()) != null) {
            wWindowPeer.removeWindowListener(this.windowListener);
        }
        super.disposeImpl();
    }

    void handleAction(final int i2) {
        final Choice choice = (Choice) this.target;
        WToolkit.executeOnEventHandlerThread(choice, new Runnable() { // from class: sun.awt.windows.WChoicePeer.2
            @Override // java.lang.Runnable
            public void run() {
                choice.select(i2);
                WChoicePeer.this.postEvent(new ItemEvent(choice, 701, choice.getItem(i2), 1));
            }
        });
    }

    int getDropDownHeight() {
        Choice choice = (Choice) this.target;
        FontMetrics fontMetrics = getFontMetrics(choice.getFont());
        return fontMetrics.getHeight() * Math.min(choice.getItemCount(), 8);
    }
}
