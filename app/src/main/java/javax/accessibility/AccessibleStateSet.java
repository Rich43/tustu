package javax.accessibility;

import java.util.Vector;

/* loaded from: rt.jar:javax/accessibility/AccessibleStateSet.class */
public class AccessibleStateSet {
    protected Vector<AccessibleState> states;

    public AccessibleStateSet() {
        this.states = null;
        this.states = null;
    }

    public AccessibleStateSet(AccessibleState[] accessibleStateArr) {
        this.states = null;
        if (accessibleStateArr.length != 0) {
            this.states = new Vector<>(accessibleStateArr.length);
            for (int i2 = 0; i2 < accessibleStateArr.length; i2++) {
                if (!this.states.contains(accessibleStateArr[i2])) {
                    this.states.addElement(accessibleStateArr[i2]);
                }
            }
        }
    }

    public boolean add(AccessibleState accessibleState) {
        if (this.states == null) {
            this.states = new Vector<>();
        }
        if (!this.states.contains(accessibleState)) {
            this.states.addElement(accessibleState);
            return true;
        }
        return false;
    }

    public void addAll(AccessibleState[] accessibleStateArr) {
        if (accessibleStateArr.length != 0) {
            if (this.states == null) {
                this.states = new Vector<>(accessibleStateArr.length);
            }
            for (int i2 = 0; i2 < accessibleStateArr.length; i2++) {
                if (!this.states.contains(accessibleStateArr[i2])) {
                    this.states.addElement(accessibleStateArr[i2]);
                }
            }
        }
    }

    public boolean remove(AccessibleState accessibleState) {
        if (this.states == null) {
            return false;
        }
        return this.states.removeElement(accessibleState);
    }

    public void clear() {
        if (this.states != null) {
            this.states.removeAllElements();
        }
    }

    public boolean contains(AccessibleState accessibleState) {
        if (this.states == null) {
            return false;
        }
        return this.states.contains(accessibleState);
    }

    public AccessibleState[] toArray() {
        if (this.states == null) {
            return new AccessibleState[0];
        }
        AccessibleState[] accessibleStateArr = new AccessibleState[this.states.size()];
        for (int i2 = 0; i2 < accessibleStateArr.length; i2++) {
            accessibleStateArr[i2] = this.states.elementAt(i2);
        }
        return accessibleStateArr;
    }

    public String toString() {
        String displayString = null;
        if (this.states != null && this.states.size() > 0) {
            displayString = this.states.elementAt(0).toDisplayString();
            for (int i2 = 1; i2 < this.states.size(); i2++) {
                displayString = displayString + "," + this.states.elementAt(i2).toDisplayString();
            }
        }
        return displayString;
    }
}
