package javax.swing.colorchooser;

import java.awt.Color;
import java.io.Serializable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/* loaded from: rt.jar:javax/swing/colorchooser/DefaultColorSelectionModel.class */
public class DefaultColorSelectionModel implements ColorSelectionModel, Serializable {
    protected transient ChangeEvent changeEvent;
    protected EventListenerList listenerList;
    private Color selectedColor;

    public DefaultColorSelectionModel() {
        this.changeEvent = null;
        this.listenerList = new EventListenerList();
        this.selectedColor = Color.white;
    }

    public DefaultColorSelectionModel(Color color) {
        this.changeEvent = null;
        this.listenerList = new EventListenerList();
        this.selectedColor = color;
    }

    @Override // javax.swing.colorchooser.ColorSelectionModel
    public Color getSelectedColor() {
        return this.selectedColor;
    }

    @Override // javax.swing.colorchooser.ColorSelectionModel
    public void setSelectedColor(Color color) {
        if (color != null && !this.selectedColor.equals(color)) {
            this.selectedColor = color;
            fireStateChanged();
        }
    }

    @Override // javax.swing.colorchooser.ColorSelectionModel
    public void addChangeListener(ChangeListener changeListener) {
        this.listenerList.add(ChangeListener.class, changeListener);
    }

    @Override // javax.swing.colorchooser.ColorSelectionModel
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
}
