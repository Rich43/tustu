package javax.swing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

/* compiled from: JColorChooser.java */
/* loaded from: rt.jar:javax/swing/ColorTracker.class */
class ColorTracker implements ActionListener, Serializable {
    JColorChooser chooser;
    Color color;

    public ColorTracker(JColorChooser jColorChooser) {
        this.chooser = jColorChooser;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.color = this.chooser.getColor();
    }

    public Color getColor() {
        return this.color;
    }
}
