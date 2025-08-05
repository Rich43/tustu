package com.efiAnalytics.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

/* renamed from: com.efiAnalytics.ui.ck, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/ck.class */
public abstract class AbstractC1600ck extends JPanel implements PropertyChangeListener {
    public abstract void a(File file);

    public abstract void b(File file);

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        String propertyName = propertyChangeEvent.getPropertyName();
        if (!JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(propertyName) && JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(propertyName)) {
            if (propertyChangeEvent.getNewValue() != null) {
                File file = new File(propertyChangeEvent.getNewValue().toString());
                if (file.isDirectory()) {
                    b(file);
                    return;
                } else {
                    a(file);
                    return;
                }
            }
            if (propertyChangeEvent.getOldValue() != null) {
                if (new File(propertyChangeEvent.getOldValue().toString()).isDirectory()) {
                    b(null);
                } else {
                    a(null);
                }
            }
        }
    }
}
