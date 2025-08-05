package com.efiAnalytics.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFileChooser;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/bZ.class */
final class bZ implements PropertyChangeListener {
    bZ() {
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        String propertyName = propertyChangeEvent.getPropertyName();
        if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(propertyName)) {
            bH.C.c("Directory Changed:\n\tOld:" + propertyChangeEvent.getOldValue() + "\n\tNew:" + propertyChangeEvent.getNewValue());
        } else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(propertyName)) {
            bH.C.c("File Changed:\n\tOld:" + propertyChangeEvent.getOldValue() + "\n\tNew:" + propertyChangeEvent.getNewValue());
            if (propertyChangeEvent.getNewValue() != null) {
            }
        }
    }
}
