package sun.awt.windows;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.peer.ListPeer;

/* loaded from: rt.jar:sun/awt/windows/WListPeer.class */
final class WListPeer extends WComponentPeer implements ListPeer {
    private FontMetrics fm;

    native void addItems(String[] strArr, int i2, int i3);

    @Override // java.awt.peer.ListPeer
    public native void delItems(int i2, int i3);

    @Override // java.awt.peer.ListPeer
    public native void select(int i2);

    @Override // java.awt.peer.ListPeer
    public native void deselect(int i2);

    @Override // java.awt.peer.ListPeer
    public native void makeVisible(int i2);

    public native void setMultipleSelections(boolean z2);

    public native int getMaxWidth();

    @Override // sun.awt.windows.WComponentPeer
    native void create(WComponentPeer wComponentPeer);

    private native void updateMaxItemWidth();

    native boolean isSelected(int i2);

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public boolean isFocusable() {
        return true;
    }

    @Override // java.awt.peer.ListPeer
    public int[] getSelectedIndexes() {
        int iCountItems = ((List) this.target).countItems();
        int[] iArr = new int[iCountItems];
        int i2 = 0;
        for (int i3 = 0; i3 < iCountItems; i3++) {
            if (isSelected(i3)) {
                int i4 = i2;
                i2++;
                iArr[i4] = i3;
            }
        }
        int[] iArr2 = new int[i2];
        System.arraycopy(iArr, 0, iArr2, 0, i2);
        return iArr2;
    }

    @Override // java.awt.peer.ListPeer
    public void add(String str, int i2) {
        addItem(str, i2);
    }

    @Override // java.awt.peer.ListPeer
    public void removeAll() {
        clear();
    }

    @Override // java.awt.peer.ListPeer
    public void setMultipleMode(boolean z2) {
        setMultipleSelections(z2);
    }

    @Override // java.awt.peer.ListPeer
    public Dimension getPreferredSize(int i2) {
        return preferredSize(i2);
    }

    @Override // java.awt.peer.ListPeer
    public Dimension getMinimumSize(int i2) {
        return minimumSize(i2);
    }

    public void addItem(String str, int i2) {
        addItems(new String[]{str}, i2, this.fm.stringWidth(str));
    }

    public void clear() {
        delItems(0, ((List) this.target).countItems());
    }

    public Dimension preferredSize(int i2) {
        if (this.fm == null) {
            this.fm = getFontMetrics(((List) this.target).getFont());
        }
        Dimension dimensionMinimumSize = minimumSize(i2);
        dimensionMinimumSize.width = Math.max(dimensionMinimumSize.width, getMaxWidth() + 20);
        return dimensionMinimumSize;
    }

    public Dimension minimumSize(int i2) {
        return new Dimension(20 + this.fm.stringWidth("0123456789abcde"), (this.fm.getHeight() * i2) + 4);
    }

    WListPeer(List list) {
        super(list);
    }

    @Override // sun.awt.windows.WComponentPeer
    void initialize() {
        List list = (List) this.target;
        this.fm = getFontMetrics(list.getFont());
        Font font = list.getFont();
        if (font != null) {
            setFont(font);
        }
        int iCountItems = list.countItems();
        if (iCountItems > 0) {
            String[] strArr = new String[iCountItems];
            int i2 = 0;
            for (int i3 = 0; i3 < iCountItems; i3++) {
                strArr[i3] = list.getItem(i3);
                int iStringWidth = this.fm.stringWidth(strArr[i3]);
                if (iStringWidth > i2) {
                    i2 = iStringWidth;
                }
            }
            addItems(strArr, 0, i2);
        }
        setMultipleSelections(list.allowsMultipleSelections());
        int[] selectedIndexes = list.getSelectedIndexes();
        for (int i4 : selectedIndexes) {
            select(i4);
        }
        int visibleIndex = list.getVisibleIndex();
        if (visibleIndex < 0 && selectedIndexes.length > 0) {
            visibleIndex = selectedIndexes[0];
        }
        if (visibleIndex >= 0) {
            makeVisible(visibleIndex);
        }
        super.initialize();
    }

    @Override // sun.awt.windows.WComponentPeer
    public boolean shouldClearRectBeforePaint() {
        return false;
    }

    @Override // sun.awt.windows.WComponentPeer
    synchronized void _setFont(Font font) {
        super._setFont(font);
        this.fm = getFontMetrics(((List) this.target).getFont());
        updateMaxItemWidth();
    }

    void handleAction(final int i2, final long j2, final int i3) {
        final List list = (List) this.target;
        WToolkit.executeOnEventHandlerThread(list, new Runnable() { // from class: sun.awt.windows.WListPeer.1
            @Override // java.lang.Runnable
            public void run() {
                list.select(i2);
                WListPeer.this.postEvent(new ActionEvent(WListPeer.this.target, 1001, list.getItem(i2), j2, i3));
            }
        });
    }

    void handleListChanged(final int i2) {
        final List list = (List) this.target;
        WToolkit.executeOnEventHandlerThread(list, new Runnable() { // from class: sun.awt.windows.WListPeer.2
            @Override // java.lang.Runnable
            public void run() {
                WListPeer.this.postEvent(new ItemEvent(list, 701, Integer.valueOf(i2), WListPeer.this.isSelected(i2) ? 1 : 2));
            }
        });
    }
}
