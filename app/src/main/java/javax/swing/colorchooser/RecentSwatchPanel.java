package javax.swing.colorchooser;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.UIManager;

/* compiled from: DefaultSwatchChooserPanel.java */
/* loaded from: rt.jar:javax/swing/colorchooser/RecentSwatchPanel.class */
class RecentSwatchPanel extends SwatchPanel {
    RecentSwatchPanel() {
    }

    @Override // javax.swing.colorchooser.SwatchPanel
    protected void initValues() {
        this.swatchSize = UIManager.getDimension("ColorChooser.swatchesRecentSwatchSize", getLocale());
        this.numSwatches = new Dimension(5, 7);
        this.gap = new Dimension(1, 1);
    }

    @Override // javax.swing.colorchooser.SwatchPanel
    protected void initColors() {
        Color color = UIManager.getColor("ColorChooser.swatchesDefaultRecentColor", getLocale());
        int i2 = this.numSwatches.width * this.numSwatches.height;
        this.colors = new Color[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            this.colors[i3] = color;
        }
    }

    public void setMostRecentColor(Color color) {
        System.arraycopy(this.colors, 0, this.colors, 1, this.colors.length - 1);
        this.colors[0] = color;
        repaint();
    }
}
