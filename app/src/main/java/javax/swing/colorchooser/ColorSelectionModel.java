package javax.swing.colorchooser;

import java.awt.Color;
import javax.swing.event.ChangeListener;

/* loaded from: rt.jar:javax/swing/colorchooser/ColorSelectionModel.class */
public interface ColorSelectionModel {
    Color getSelectedColor();

    void setSelectedColor(Color color);

    void addChangeListener(ChangeListener changeListener);

    void removeChangeListener(ChangeListener changeListener);
}
