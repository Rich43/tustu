package sun.swing;

import java.beans.PropertyChangeListener;
import javax.swing.Action;

/* loaded from: rt.jar:sun/swing/UIAction.class */
public abstract class UIAction implements Action {
    private String name;

    public UIAction(String str) {
        this.name = str;
    }

    public final String getName() {
        return this.name;
    }

    @Override // javax.swing.Action
    public Object getValue(String str) {
        if (str == "Name") {
            return this.name;
        }
        return null;
    }

    @Override // javax.swing.Action
    public void putValue(String str, Object obj) {
    }

    @Override // javax.swing.Action
    public void setEnabled(boolean z2) {
    }

    @Override // javax.swing.Action
    public final boolean isEnabled() {
        return isEnabled(null);
    }

    public boolean isEnabled(Object obj) {
        return true;
    }

    @Override // javax.swing.Action
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
    }

    @Override // javax.swing.Action
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
    }
}
