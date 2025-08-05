package javax.swing.text.html;

import java.io.Serializable;
import java.util.BitSet;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/* loaded from: rt.jar:javax/swing/text/html/OptionListModel.class */
class OptionListModel<E> extends DefaultListModel<E> implements ListSelectionModel, Serializable {
    private static final int MIN = -1;
    private static final int MAX = Integer.MAX_VALUE;
    private int selectionMode = 0;
    private int minIndex = Integer.MAX_VALUE;
    private int maxIndex = -1;
    private int anchorIndex = -1;
    private int leadIndex = -1;
    private int firstChangedIndex = Integer.MAX_VALUE;
    private int lastChangedIndex = -1;
    private boolean isAdjusting = false;
    private BitSet value = new BitSet(32);
    private BitSet initialValue = new BitSet(32);
    protected EventListenerList listenerList = new EventListenerList();
    protected boolean leadAnchorNotificationEnabled = true;

    OptionListModel() {
    }

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
        fireValueChanged(getMinSelectionIndex(), getMaxSelectionIndex(), z2);
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
        if (this.lastChangedIndex == -1) {
            return;
        }
        int i2 = this.firstChangedIndex;
        int i3 = this.lastChangedIndex;
        this.firstChangedIndex = Integer.MAX_VALUE;
        this.lastChangedIndex = -1;
        fireValueChanged(i2, i3);
    }

    private void markAsDirty(int i2) {
        this.firstChangedIndex = Math.min(this.firstChangedIndex, i2);
        this.lastChangedIndex = Math.max(this.lastChangedIndex, i2);
    }

    private void set(int i2) {
        if (this.value.get(i2)) {
            return;
        }
        this.value.set(i2);
        ((Option) get(i2)).setSelection(true);
        markAsDirty(i2);
        this.minIndex = Math.min(this.minIndex, i2);
        this.maxIndex = Math.max(this.maxIndex, i2);
    }

    private void clear(int i2) {
        if (!this.value.get(i2)) {
            return;
        }
        this.value.clear(i2);
        ((Option) get(i2)).setSelection(false);
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
                if (this.anchorIndex != -1) {
                    markAsDirty(this.anchorIndex);
                }
                markAsDirty(i2);
            }
            if (this.leadIndex != i3) {
                if (this.leadIndex != -1) {
                    markAsDirty(this.leadIndex);
                }
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
        removeSelectionInterval(this.minIndex, this.maxIndex);
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
        if (getSelectionMode() != 2) {
            setSelectionInterval(i2, i3);
        } else {
            updateLeadAnchorIndices(i2, i3);
            changeSelection(Integer.MAX_VALUE, -1, Math.min(i2, i3), Math.max(i2, i3));
        }
    }

    @Override // javax.swing.ListSelectionModel
    public void removeSelectionInterval(int i2, int i3) {
        if (i2 == -1 || i3 == -1) {
            return;
        }
        updateLeadAnchorIndices(i2, i3);
        changeSelection(Math.min(i2, i3), Math.max(i2, i3), Integer.MAX_VALUE, -1);
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
        boolean z3 = this.value.get(i2);
        for (int i7 = i4; i7 <= i5; i7++) {
            setState(i7, z3);
        }
    }

    @Override // javax.swing.ListSelectionModel
    public void removeIndexInterval(int i2, int i3) {
        int iMin = Math.min(i2, i3);
        int iMax = (Math.max(i2, i3) - iMin) + 1;
        for (int i4 = iMin; i4 <= this.maxIndex; i4++) {
            setState(i4, this.value.get(i4 + iMax));
        }
    }

    @Override // javax.swing.ListSelectionModel
    public void setValueIsAdjusting(boolean z2) {
        if (z2 != this.isAdjusting) {
            this.isAdjusting = z2;
            fireValueChanged(z2);
        }
    }

    @Override // javax.swing.DefaultListModel
    public String toString() {
        return getClass().getName() + " " + Integer.toString(hashCode()) + " " + ((getValueIsAdjusting() ? "~" : "=") + this.value.toString());
    }

    public Object clone() throws CloneNotSupportedException {
        OptionListModel optionListModel = (OptionListModel) super.clone();
        optionListModel.value = (BitSet) this.value.clone();
        optionListModel.listenerList = new EventListenerList();
        return optionListModel;
    }

    @Override // javax.swing.ListSelectionModel
    public int getAnchorSelectionIndex() {
        return this.anchorIndex;
    }

    @Override // javax.swing.ListSelectionModel
    public int getLeadSelectionIndex() {
        return this.leadIndex;
    }

    @Override // javax.swing.ListSelectionModel
    public void setAnchorSelectionIndex(int i2) {
        this.anchorIndex = i2;
    }

    @Override // javax.swing.ListSelectionModel
    public void setLeadSelectionIndex(int i2) {
        int i3 = this.anchorIndex;
        if (getSelectionMode() == 0) {
            i3 = i2;
        }
        int iMin = Math.min(this.anchorIndex, this.leadIndex);
        int iMax = Math.max(this.anchorIndex, this.leadIndex);
        int iMin2 = Math.min(i3, i2);
        int iMax2 = Math.max(i3, i2);
        if (this.value.get(this.anchorIndex)) {
            changeSelection(iMin, iMax, iMin2, iMax2);
        } else {
            changeSelection(iMin2, iMax2, iMin, iMax, false);
        }
        this.anchorIndex = i3;
        this.leadIndex = i2;
    }

    public void setInitialSelection(int i2) {
        if (this.initialValue.get(i2)) {
            return;
        }
        if (this.selectionMode == 0) {
            this.initialValue.and(new BitSet());
        }
        this.initialValue.set(i2);
    }

    public BitSet getInitialSelection() {
        return this.initialValue;
    }
}
