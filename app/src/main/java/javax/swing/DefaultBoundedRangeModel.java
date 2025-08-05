package javax.swing;

import java.io.Serializable;
import java.util.EventListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/* loaded from: rt.jar:javax/swing/DefaultBoundedRangeModel.class */
public class DefaultBoundedRangeModel implements BoundedRangeModel, Serializable {
    protected transient ChangeEvent changeEvent;
    protected EventListenerList listenerList;
    private int value;
    private int extent;
    private int min;
    private int max;
    private boolean isAdjusting;

    public DefaultBoundedRangeModel() {
        this.changeEvent = null;
        this.listenerList = new EventListenerList();
        this.value = 0;
        this.extent = 0;
        this.min = 0;
        this.max = 100;
        this.isAdjusting = false;
    }

    public DefaultBoundedRangeModel(int i2, int i3, int i4, int i5) {
        this.changeEvent = null;
        this.listenerList = new EventListenerList();
        this.value = 0;
        this.extent = 0;
        this.min = 0;
        this.max = 100;
        this.isAdjusting = false;
        if (i5 >= i4 && i2 >= i4 && i2 + i3 >= i2 && i2 + i3 <= i5) {
            this.value = i2;
            this.extent = i3;
            this.min = i4;
            this.max = i5;
            return;
        }
        throw new IllegalArgumentException("invalid range properties");
    }

    @Override // javax.swing.BoundedRangeModel
    public int getValue() {
        return this.value;
    }

    @Override // javax.swing.BoundedRangeModel
    public int getExtent() {
        return this.extent;
    }

    @Override // javax.swing.BoundedRangeModel
    public int getMinimum() {
        return this.min;
    }

    @Override // javax.swing.BoundedRangeModel
    public int getMaximum() {
        return this.max;
    }

    @Override // javax.swing.BoundedRangeModel
    public void setValue(int i2) {
        int iMax = Math.max(Math.min(i2, Integer.MAX_VALUE - this.extent), this.min);
        if (iMax + this.extent > this.max) {
            iMax = this.max - this.extent;
        }
        setRangeProperties(iMax, this.extent, this.min, this.max, this.isAdjusting);
    }

    @Override // javax.swing.BoundedRangeModel
    public void setExtent(int i2) {
        int iMax = Math.max(0, i2);
        if (this.value + iMax > this.max) {
            iMax = this.max - this.value;
        }
        setRangeProperties(this.value, iMax, this.min, this.max, this.isAdjusting);
    }

    @Override // javax.swing.BoundedRangeModel
    public void setMinimum(int i2) {
        int iMax = Math.max(i2, this.max);
        int iMax2 = Math.max(i2, this.value);
        setRangeProperties(iMax2, Math.min(iMax - iMax2, this.extent), i2, iMax, this.isAdjusting);
    }

    @Override // javax.swing.BoundedRangeModel
    public void setMaximum(int i2) {
        int iMin = Math.min(i2, this.min);
        int iMin2 = Math.min(i2 - iMin, this.extent);
        setRangeProperties(Math.min(i2 - iMin2, this.value), iMin2, iMin, i2, this.isAdjusting);
    }

    @Override // javax.swing.BoundedRangeModel
    public void setValueIsAdjusting(boolean z2) {
        setRangeProperties(this.value, this.extent, this.min, this.max, z2);
    }

    @Override // javax.swing.BoundedRangeModel
    public boolean getValueIsAdjusting() {
        return this.isAdjusting;
    }

    @Override // javax.swing.BoundedRangeModel
    public void setRangeProperties(int i2, int i3, int i4, int i5, boolean z2) {
        if (i4 > i5) {
            i4 = i5;
        }
        if (i2 > i5) {
            i5 = i2;
        }
        if (i2 < i4) {
            i4 = i2;
        }
        if (i3 + i2 > i5) {
            i3 = i5 - i2;
        }
        if (i3 < 0) {
            i3 = 0;
        }
        if ((i2 == this.value && i3 == this.extent && i4 == this.min && i5 == this.max && z2 == this.isAdjusting) ? false : true) {
            this.value = i2;
            this.extent = i3;
            this.min = i4;
            this.max = i5;
            this.isAdjusting = z2;
            fireStateChanged();
        }
    }

    @Override // javax.swing.BoundedRangeModel
    public void addChangeListener(ChangeListener changeListener) {
        this.listenerList.add(ChangeListener.class, changeListener);
    }

    @Override // javax.swing.BoundedRangeModel
    public void removeChangeListener(ChangeListener changeListener) {
        this.listenerList.remove(ChangeListener.class, changeListener);
    }

    public ChangeListener[] getChangeListeners() {
        return (ChangeListener[]) this.listenerList.getListeners(ChangeListener.class);
    }

    protected void fireStateChanged() {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == ChangeListener.class) {
                if (this.changeEvent == null) {
                    this.changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listenerList[length + 1]).stateChanged(this.changeEvent);
            }
        }
    }

    public String toString() {
        return getClass().getName() + "[" + ("value=" + getValue() + ", extent=" + getExtent() + ", min=" + getMinimum() + ", max=" + getMaximum() + ", adj=" + getValueIsAdjusting()) + "]";
    }

    public <T extends EventListener> T[] getListeners(Class<T> cls) {
        return (T[]) this.listenerList.getListeners(cls);
    }
}
