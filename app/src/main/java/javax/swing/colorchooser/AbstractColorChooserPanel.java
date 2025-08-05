package javax.swing.colorchooser;

import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.UIManager;
import jdk.jfr.Enabled;

/* loaded from: rt.jar:javax/swing/colorchooser/AbstractColorChooserPanel.class */
public abstract class AbstractColorChooserPanel extends JPanel {
    private final PropertyChangeListener enabledListener = new PropertyChangeListener() { // from class: javax.swing.colorchooser.AbstractColorChooserPanel.1
        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            Object newValue = propertyChangeEvent.getNewValue();
            if (newValue instanceof Boolean) {
                AbstractColorChooserPanel.this.setEnabled(((Boolean) newValue).booleanValue());
            }
        }
    };
    private JColorChooser chooser;

    public abstract void updateChooser();

    protected abstract void buildChooser();

    public abstract String getDisplayName();

    public abstract Icon getSmallDisplayIcon();

    public abstract Icon getLargeDisplayIcon();

    public int getMnemonic() {
        return 0;
    }

    public int getDisplayedMnemonicIndex() {
        return -1;
    }

    public void installChooserPanel(JColorChooser jColorChooser) {
        if (this.chooser != null) {
            throw new RuntimeException("This chooser panel is already installed");
        }
        this.chooser = jColorChooser;
        this.chooser.addPropertyChangeListener(Enabled.NAME, this.enabledListener);
        setEnabled(this.chooser.isEnabled());
        buildChooser();
        updateChooser();
    }

    public void uninstallChooserPanel(JColorChooser jColorChooser) {
        this.chooser.removePropertyChangeListener(Enabled.NAME, this.enabledListener);
        this.chooser = null;
    }

    public ColorSelectionModel getColorSelectionModel() {
        if (this.chooser != null) {
            return this.chooser.getSelectionModel();
        }
        return null;
    }

    protected Color getColorFromModel() {
        ColorSelectionModel colorSelectionModel = getColorSelectionModel();
        if (colorSelectionModel != null) {
            return colorSelectionModel.getSelectedColor();
        }
        return null;
    }

    void setSelectedColor(Color color) {
        ColorSelectionModel colorSelectionModel = getColorSelectionModel();
        if (colorSelectionModel != null) {
            colorSelectionModel.setSelectedColor(color);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        super.paint(graphics);
    }

    int getInt(Object obj, int i2) {
        Object obj2 = UIManager.get(obj, getLocale());
        if (obj2 instanceof Integer) {
            return ((Integer) obj2).intValue();
        }
        if (obj2 instanceof String) {
            try {
                return Integer.parseInt((String) obj2);
            } catch (NumberFormatException e2) {
            }
        }
        return i2;
    }
}
