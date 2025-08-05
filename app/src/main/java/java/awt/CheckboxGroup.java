package java.awt;

import java.io.Serializable;

/* loaded from: rt.jar:java/awt/CheckboxGroup.class */
public class CheckboxGroup implements Serializable {
    Checkbox selectedCheckbox = null;
    private static final long serialVersionUID = 3729780091441768983L;

    public Checkbox getSelectedCheckbox() {
        return getCurrent();
    }

    @Deprecated
    public Checkbox getCurrent() {
        return this.selectedCheckbox;
    }

    public void setSelectedCheckbox(Checkbox checkbox) {
        setCurrent(checkbox);
    }

    @Deprecated
    public synchronized void setCurrent(Checkbox checkbox) {
        if (checkbox != null && checkbox.group != this) {
            return;
        }
        Checkbox checkbox2 = this.selectedCheckbox;
        this.selectedCheckbox = checkbox;
        if (checkbox2 != null && checkbox2 != checkbox && checkbox2.group == this) {
            checkbox2.setState(false);
        }
        if (checkbox != null && checkbox2 != checkbox && !checkbox.getState()) {
            checkbox.setStateInternal(true);
        }
    }

    public String toString() {
        return getClass().getName() + "[selectedCheckbox=" + ((Object) this.selectedCheckbox) + "]";
    }
}
