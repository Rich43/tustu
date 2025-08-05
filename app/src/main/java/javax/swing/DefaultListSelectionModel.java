package javax.swing;

import java.beans.Transient;
import java.io.Serializable;
import java.util.BitSet;
import java.util.EventListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/* loaded from: rt.jar:javax/swing/DefaultListSelectionModel.class */
public class DefaultListSelectionModel implements ListSelectionModel, Cloneable, Serializable {
    private static final int MIN = -1;
    private static final int MAX = Integer.MAX_VALUE;
    private int selectionMode = 2;
    private int minIndex = Integer.MAX_VALUE;
    private int maxIndex = -1;
    private int anchorIndex = -1;
    private int leadIndex = -1;
    private int firstAdjustedIndex = Integer.MAX_VALUE;
    private int lastAdjustedIndex = -1;
    private boolean isAdjusting = false;
    private int firstChangedIndex = Integer.MAX_VALUE;
    private int lastChangedIndex = -1;
    private BitSet value = new BitSet(32);
    protected EventListenerList listenerList = new EventListenerList();
    protected boolean leadAnchorNotificationEnabled = true;

    @Override // javax.swing.ListSelectionModel
    public int getMinSelectionIndex() {
        if (isSelectionEmpty()) {
            return -1;
        }
        return this.minIndex;
    }

    @Override // javax.swing.ListSelectionModel
    public int getMaxSelectionIndex() {
        return this.maxIndex;
    }

    @Override // javax.swing.ListSelectionModel
    public boolean getValueIsAdjusting() {
        return this.isAdjusting;
    }

    @Override // javax.swing.ListSelectionModel
    public int getSelectionMode() {
        return this.selectionMode;
    }

    @Override // javax.swing.ListSelectionModel
    public void setSelectionMode(int i2) {
        switch (i2) {
            case 0:
            case 1:
            case 2:
                this.selectionMode = i2;
                return;
            default:
                throw new IllegalArgumentException("invalid selectionMode");
        }
    }

    @Override // javax.swing.ListSelectionModel
    public boolean isSelectedIndex(int i2) {
        if (i2 < this.minIndex || i2 > this.maxIndex) {
            return false;
        }
        return this.value.get(i2);
    }

    @Override // javax.swing.ListSelectionModel
    public boolean isSelectionEmpty() {
        return this.minIndex > this.maxIndex;
    }

    @Override // javax.swing.ListSelectionModel
    public void addListSelectionListener(ListSelectionListener listSelectionListener) {
        this.listenerList.add(ListSelectionListener.class, listSelectionListener);
    }

    @Override // javax.swing.ListSelectionModel
    public void removeListSelectionListener(ListSelectionListener listSelectionListener) {
        this.listenerList.remove(ListSelectionListener.class, listSelectionListener);
    }

    public ListSelectionListener[] getListSelectionListeners() {
        return (ListSelectionListener[]) this.listenerList.getListeners(ListSelectionListener.class);
    }

    protected void fireValueChanged(boolean z2) {
        if (this.lastChangedIndex == -1) {
            return;
        }
        int i2 = this.firstChangedIndex;
        int i3 = this.lastChangedIndex;
        this.firstChangedIndex = Integer.MAX_VALUE;
        this.lastChangedIndex = -1;
        fireValueChanged(i2, i3, z2);
    }

    protected void fireValueChanged(int i2, int i3) {
        fireValueChanged(i2, i3, getValueIsAdjusting());
    }

    protected void fireValueChanged(int i2, int i3, boolean z2) {
        Object[] listenerList = this.listenerList.getListenerList();
        ListSelectionEvent listSelectionEvent = null;
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == ListSelectionListener.class) {
                if (listSelectionEvent == null) {
                    listSelectionEvent = new ListSelectionEvent(this, i2, i3, z2);
                }
                ((ListSelectionListener) listenerList[length + 1]).valueChanged(listSelectionEvent);
            }
        }
    }

    private void fireValueChanged() {
        if (this.lastAdjustedIndex == -1) {
            return;
        }
        if (getValueIsAdjusting()) {
            this.firstChangedIndex = Math.min(this.firstChangedIndex, this.firstAdjustedIndex);
            this.lastChangedIndex = Math.max(this.lastChangedIndex, this.lastAdjustedIndex);
        }
        int i2 = this.firstAdjustedIndex;
        int i3 = this.lastAdjustedIndex;
        this.firstAdjustedIndex = Integer.MAX_VALUE;
        this.lastAdjustedIndex = -1;
        fireValueChanged(i2, i3);
    }

    public <T extends EventListener> T[] getListeners(Class<T> cls) {
        return (T[]) this.listenerList.getListeners(cls);
    }

    private void markAsDirty(int i2) {
        if (i2 == -1) {
            return;
        }
        this.firstAdjustedIndex = Math.min(this.firstAdjustedIndex, i2);
        this.lastAdjustedIndex = Math.max(this.lastAdjustedIndex, i2);
    }

    private void set(int i2) {
        if (this.value.get(i2)) {
            return;
        }
        this.value.set(i2);
        markAsDirty(i2);
        this.minIndex = Math.min(this.minIndex, i2);
        this.maxIndex = Math.max(this.maxIndex, i2);
    }

    private void clear(int i2) {
        if (!this.value.get(i2)) {
            return;
        }
        this.value.clear(i2);
        markAsDirty(i2);
        if (i2 == this.minIndex) {
            this.minIndex++;
            while (this.minIndex <= this.maxIndex && !this.value.get(this.minIndex)) {
                this.minIndex++;
            }
        }
        if (i2 == this.maxIndex) {
            this.maxIndex--;
            while (this.minIndex <= this.maxIndex && !this.value.get(this.maxIndex)) {
                this.maxIndex--;
            }
        }
        if (isSelectionEmpty()) {
            this.minIndex = Integer.MAX_VALUE;
            this.maxIndex = -1;
        }
    }

    public void setLeadAnchorNotificationEnabled(boolean z2) {
        this.leadAnchorNotificationEnabled = z2;
    }

    public boolean isLeadAnchorNotificationEnabled() {
        return this.leadAnchorNotificationEnabled;
    }

    private void updateLeadAnchorIndices(int i2, int i3) {
        if (this.leadAnchorNotificationEnabled) {
            if (this.anchorIndex != i2) {
                markAsDirty(this.anchorIndex);
                markAsDirty(i2);
            }
            if (this.leadIndex != i3) {
                markAsDirty(this.leadIndex);
                markAsDirty(i3);
            }
        }
        this.anchorIndex = i2;
        this.leadIndex = i3;
    }

    private boolean contains(int i2, int i3, int i4) {
        return i4 >= i2 && i4 <= i3;
    }

    private void changeSelection(int i2, int i3, int i4, int i5, boolean z2) {
        for (int iMin = Math.min(i4, i2); iMin <= Math.max(i5, i3); iMin++) {
            boolean zContains = contains(i2, i3, iMin);
            boolean zContains2 = contains(i4, i5, iMin);
            if (zContains2 && zContains) {
                if (z2) {
                    zContains = false;
                } else {
                    zContains2 = false;
                }
            }
            if (zContains2) {
                set(iMin);
            }
            if (zContains) {
                clear(iMin);
            }
        }
        fireValueChanged();
    }

    private void changeSelection(int i2, int i3, int i4, int i5) {
        changeSelection(i2, i3, i4, i5, true);
    }

    @Override // javax.swing.ListSelectionModel
    public void clearSelection() {
        removeSelectionIntervalImpl(this.minIndex, this.maxIndex, false);
    }

    @Override // javax.swing.ListSelectionModel
    public void setSelectionInterval(int i2, int i3) {
        if (i2 == -1 || i3 == -1) {
            return;
        }
        if (getSelectionMode() == 0) {
            i2 = i3;
        }
        updateLeadAnchorIndices(i2, i3);
        changeSelection(this.minIndex, this.maxIndex, Math.min(i2, i3), Math.max(i2, i3));
    }

    @Override // javax.swing.ListSelectionModel
    public void addSelectionInterval(int i2, int i3) {
        if (i2 == -1 || i3 == -1) {
            return;
        }
        if (getSelectionMode() == 0) {
            setSelectionInterval(i2, i3);
            return;
        }
        updateLeadAnchorIndices(i2, i3);
        int iMin = Math.min(i2, i3);
        int iMax = Math.max(i2, i3);
        if (getSelectionMode() == 1 && (iMax < this.minIndex - 1 || iMin > this.maxIndex + 1)) {
            setSelectionInterval(i2, i3);
        } else {
            changeSelection(Integer.MAX_VALUE, -1, iMin, iMax);
        }
    }

    @Override // javax.swing.ListSelectionModel
    public void removeSelectionInterval(int i2, int i3) {
        removeSelectionIntervalImpl(i2, i3, true);
    }

    private void removeSelectionIntervalImpl(int i2, int i3, boolean z2) {
        if (i2 == -1 || i3 == -1) {
            return;
        }
        if (z2) {
            updateLeadAnchorIndices(i2, i3);
        }
        int iMin = Math.min(i2, i3);
        int iMax = Math.max(i2, i3);
        if (getSelectionMode() != 2 && iMin > this.minIndex && iMax < this.maxIndex) {
            iMax = this.maxIndex;
        }
        changeSelection(iMin, iMax, Integer.MAX_VALUE, -1);
    }

    private void setState(int i2, boolean z2) {
        if (z2) {
            set(i2);
        } else {
            clear(i2);
        }
    }

    @Override // javax.swing.ListSelectionModel
    public void insertIndexInterval(int i2, int i3, boolean z2) {
        int i4 = z2 ? i2 : i2 + 1;
        int i5 = (i4 + i3) - 1;
        for (int i6 = this.maxIndex; i6 >= i4; i6--) {
            setState(i6 + i3, this.value.get(i6));
        }
        boolean z3 = getSelectionMode() == 0 ? false : this.value.get(i2);
        for (int i7 = i4; i7 <= i5; i7++) {
            setState(i7, z3);
        }
        int i8 = this.leadIndex;
        if (i8 > i2 || (z2 && i8 == i2)) {
            i8 = this.leadIndex + i3;
        }
        int i9 = this.anchorIndex;
        if (i9 > i2 || (z2 && i9 == i2)) {
            i9 = this.anchorIndex + i3;
        }
        if (i8 != this.leadIndex || i9 != this.anchorIndex) {
            updateLeadAnchorIndices(i9, i8);
        }
        fireValueChanged();
    }

    @Override // javax.swing.ListSelectionModel
    public void removeIndexInterval(int i2, int i3) {
        int iMin = Math.min(i2, i3);
        int iMax = Math.max(i2, i3);
        int i4 = (iMax - iMin) + 1;
        for (int i5 = iMin; i5 <= this.maxIndex; i5++) {
            setState(i5, this.value.get(i5 + i4));
        }
        int i6 = this.leadIndex;
        if (i6 != 0 || iMin != 0) {
            if (i6 > iMax) {
                i6 = this.leadIndex - i4;
            } else if (i6 >= iMin) {
                i6 = iMin - 1;
            }
        }
        int i7 = this.anchorIndex;
        if (i7 != 0 || iMin != 0) {
            if (i7 > iMax) {
                i7 = this.anchorIndex - i4;
            } else if (i7 >= iMin) {
                i7 = iMin - 1;
            }
        }
        if (i6 != this.leadIndex || i7 != this.anchorIndex) {
            updateLeadAnchorIndices(i7, i6);
        }
        fireValueChanged();
    }

    @Override // javax.swing.ListSelectionModel
    public void setValueIsAdjusting(boolean z2) {
        if (z2 != this.isAdjusting) {
            this.isAdjusting = z2;
            fireValueChanged(z2);
        }
    }

    public String toString() {
        return getClass().getName() + " " + Integer.toString(hashCode()) + " " + ((getValueIsAdjusting() ? "~" : "=") + this.value.toString());
    }

    public Object clone() throws CloneNotSupportedException {
        DefaultListSelectionModel defaultListSelectionModel = (DefaultListSelectionModel) super.clone();
        defaultListSelectionModel.value = (BitSet) this.value.clone();
        defaultListSelectionModel.listenerList = new EventListenerList();
        return defaultListSelectionModel;
    }

    @Override // javax.swing.ListSelectionModel
    @Transient
    public int getAnchorSelectionIndex() {
        return this.anchorIndex;
    }

    @Override // javax.swing.ListSelectionModel
    @Transient
    public int getLeadSelectionIndex() {
        return this.leadIndex;
    }

    @Override // javax.swing.ListSelectionModel
    public void setAnchorSelectionIndex(int i2) {
        updateLeadAnchorIndices(i2, this.leadIndex);
        fireValueChanged();
    }

    public void moveLeadSelectionIndex(int i2) {
        if (i2 == -1 && this.anchorIndex != -1) {
            return;
        }
        updateLeadAnchorIndices(this.anchorIndex, i2);
        fireValueChanged();
    }

    @Override // javax.swing.ListSelectionModel
    public void setLeadSelectionIndex(int i2) {
        int i3 = this.anchorIndex;
        if (i2 == -1) {
            if (i3 == -1) {
                updateLeadAnchorIndices(i3, i2);
                fireValueChanged();
                return;
            }
            return;
        }
        if (i3 == -1) {
            return;
        }
        if (this.leadIndex == -1) {
            this.leadIndex = i2;
        }
        boolean z2 = this.value.get(this.anchorIndex);
        if (getSelectionMode() == 0) {
            i3 = i2;
            z2 = true;
        }
        int iMin = Math.min(this.anchorIndex, this.leadIndex);
        int iMax = Math.max(this.anchorIndex, this.leadIndex);
        int iMin2 = Math.min(i3, i2);
        int iMax2 = Math.max(i3, i2);
        updateLeadAnchorIndices(i3, i2);
        if (z2) {
            changeSelection(iMin, iMax, iMin2, iMax2);
        } else {
            changeSelection(iMin2, iMax2, iMin, iMax, false);
        }
    }
}
