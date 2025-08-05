package javax.swing.colorchooser;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;

/* loaded from: rt.jar:javax/swing/colorchooser/ColorChooserPanel.class */
final class ColorChooserPanel extends AbstractColorChooserPanel implements PropertyChangeListener {
    private static final int MASK = -16777216;
    private final ColorModel model;
    private final ColorPanel panel;
    private final DiagramComponent slider;
    private final DiagramComponent diagram;
    private final JFormattedTextField text = new JFormattedTextField();
    private final JLabel label = new JLabel(null, null, 4);

    ColorChooserPanel(ColorModel colorModel) {
        this.model = colorModel;
        this.panel = new ColorPanel(this.model);
        this.slider = new DiagramComponent(this.panel, false);
        this.diagram = new DiagramComponent(this.panel, true);
        ValueFormatter.init(6, true, this.text);
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        setEnabled(this, z2);
    }

    private static void setEnabled(Container container, boolean z2) {
        for (Component component : container.getComponents()) {
            component.setEnabled(z2);
            if (component instanceof Container) {
                setEnabled((Container) component, z2);
            }
        }
    }

    @Override // javax.swing.colorchooser.AbstractColorChooserPanel
    public void updateChooser() {
        Color colorFromModel = getColorFromModel();
        if (colorFromModel != null) {
            this.panel.setColor(colorFromModel);
            this.text.setValue(Integer.valueOf(colorFromModel.getRGB()));
            this.slider.repaint();
            this.diagram.repaint();
        }
    }

    @Override // javax.swing.colorchooser.AbstractColorChooserPanel
    protected void buildChooser() throws IllegalArgumentException {
        if (0 == getComponentCount()) {
            setLayout(new GridBagLayout());
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 3;
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.weighty = 1.0d;
            gridBagConstraints.anchor = 11;
            gridBagConstraints.fill = 2;
            gridBagConstraints.insets.top = 10;
            gridBagConstraints.insets.right = 10;
            add(this.panel, gridBagConstraints);
            gridBagConstraints.gridwidth = 1;
            gridBagConstraints.weightx = 1.0d;
            gridBagConstraints.weighty = 0.0d;
            gridBagConstraints.anchor = 10;
            gridBagConstraints.insets.right = 5;
            gridBagConstraints.insets.bottom = 10;
            add(this.label, gridBagConstraints);
            gridBagConstraints.gridx = 4;
            gridBagConstraints.weightx = 0.0d;
            gridBagConstraints.insets.right = 10;
            add(this.text, gridBagConstraints);
            gridBagConstraints.gridx = 2;
            gridBagConstraints.gridheight = 2;
            gridBagConstraints.anchor = 11;
            gridBagConstraints.ipadx = this.text.getPreferredSize().height;
            gridBagConstraints.ipady = getPreferredSize().height;
            add(this.slider, gridBagConstraints);
            gridBagConstraints.gridx = 1;
            gridBagConstraints.insets.left = 10;
            gridBagConstraints.ipadx = gridBagConstraints.ipady;
            add(this.diagram, gridBagConstraints);
            this.label.setLabelFor(this.text);
            this.text.addPropertyChangeListener("value", this);
            this.slider.setBorder(this.text.getBorder());
            this.diagram.setBorder(this.text.getBorder());
            setInheritsPopupMenu(this, true);
        }
        String text = this.model.getText(this, "HexCode");
        boolean z2 = text != null;
        this.text.setVisible(z2);
        this.text.getAccessibleContext().setAccessibleDescription(text);
        this.label.setVisible(z2);
        if (z2) {
            this.label.setText(text);
            int integer = this.model.getInteger(this, "HexCodeMnemonic");
            if (integer > 0) {
                this.label.setDisplayedMnemonic(integer);
                int integer2 = this.model.getInteger(this, "HexCodeMnemonicIndex");
                if (integer2 >= 0) {
                    this.label.setDisplayedMnemonicIndex(integer2);
                }
            }
        }
        this.panel.buildPanel();
    }

    @Override // javax.swing.colorchooser.AbstractColorChooserPanel
    public String getDisplayName() {
        return this.model.getText(this, "Name");
    }

    @Override // javax.swing.colorchooser.AbstractColorChooserPanel
    public int getMnemonic() {
        return this.model.getInteger(this, "Mnemonic");
    }

    @Override // javax.swing.colorchooser.AbstractColorChooserPanel
    public int getDisplayedMnemonicIndex() {
        return this.model.getInteger(this, "DisplayedMnemonicIndex");
    }

    @Override // javax.swing.colorchooser.AbstractColorChooserPanel
    public Icon getSmallDisplayIcon() {
        return null;
    }

    @Override // javax.swing.colorchooser.AbstractColorChooserPanel
    public Icon getLargeDisplayIcon() {
        return null;
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        ColorSelectionModel colorSelectionModel = getColorSelectionModel();
        if (colorSelectionModel != null) {
            Object newValue = propertyChangeEvent.getNewValue();
            if (newValue instanceof Integer) {
                colorSelectionModel.setSelectedColor(new Color(((-16777216) & colorSelectionModel.getSelectedColor().getRGB()) | ((Integer) newValue).intValue(), true));
            }
        }
        this.text.selectAll();
    }

    private static void setInheritsPopupMenu(JComponent jComponent, boolean z2) {
        jComponent.setInheritsPopupMenu(z2);
        for (Component component : jComponent.getComponents()) {
            if (component instanceof JComponent) {
                setInheritsPopupMenu((JComponent) component, z2);
            }
        }
    }
}
