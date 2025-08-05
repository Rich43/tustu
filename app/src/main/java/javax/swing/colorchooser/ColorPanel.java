package javax.swing.colorchooser;

import java.awt.Color;
import java.awt.Container;
import java.awt.ContainerOrderFocusTraversalPolicy;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.border.EmptyBorder;

/* loaded from: rt.jar:javax/swing/colorchooser/ColorPanel.class */
final class ColorPanel extends JPanel implements ActionListener {
    private final SlidingSpinner[] spinners;
    private final float[] values;
    private final ColorModel model;
    private Color color;

    /* renamed from: x, reason: collision with root package name */
    private int f12814x;

    /* renamed from: y, reason: collision with root package name */
    private int f12815y;

    /* renamed from: z, reason: collision with root package name */
    private int f12816z;

    ColorPanel(ColorModel colorModel) {
        super(new GridBagLayout());
        this.spinners = new SlidingSpinner[5];
        this.values = new float[this.spinners.length];
        this.f12814x = 1;
        this.f12815y = 2;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = 2;
        gridBagConstraints.gridx = 1;
        ButtonGroup buttonGroup = new ButtonGroup();
        EmptyBorder emptyBorder = null;
        for (int i2 = 0; i2 < this.spinners.length; i2++) {
            if (i2 < 3) {
                JRadioButton jRadioButton = new JRadioButton();
                if (i2 == 0) {
                    Insets insets = jRadioButton.getInsets();
                    insets.left = jRadioButton.getPreferredSize().width;
                    emptyBorder = new EmptyBorder(insets);
                    jRadioButton.setSelected(true);
                    gridBagConstraints.insets.top = 5;
                }
                add(jRadioButton, gridBagConstraints);
                buttonGroup.add(jRadioButton);
                jRadioButton.setActionCommand(Integer.toString(i2));
                jRadioButton.addActionListener(this);
                this.spinners[i2] = new SlidingSpinner(this, jRadioButton);
            } else {
                JLabel jLabel = new JLabel();
                add(jLabel, gridBagConstraints);
                jLabel.setBorder(emptyBorder);
                jLabel.setFocusable(false);
                this.spinners[i2] = new SlidingSpinner(this, jLabel);
            }
        }
        gridBagConstraints.gridx = 2;
        gridBagConstraints.weightx = 1.0d;
        gridBagConstraints.insets.top = 0;
        gridBagConstraints.insets.left = 5;
        for (SlidingSpinner slidingSpinner : this.spinners) {
            add(slidingSpinner.getSlider(), gridBagConstraints);
            gridBagConstraints.insets.top = 5;
        }
        gridBagConstraints.gridx = 3;
        gridBagConstraints.weightx = 0.0d;
        gridBagConstraints.insets.top = 0;
        for (SlidingSpinner slidingSpinner2 : this.spinners) {
            add(slidingSpinner2.getSpinner(), gridBagConstraints);
            gridBagConstraints.insets.top = 5;
        }
        setFocusTraversalPolicy(new ContainerOrderFocusTraversalPolicy());
        setFocusTraversalPolicyProvider(true);
        setFocusable(false);
        this.model = colorModel;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            this.f12816z = Integer.parseInt(actionEvent.getActionCommand());
            this.f12815y = this.f12816z != 2 ? 2 : 1;
            this.f12814x = this.f12816z != 0 ? 0 : 1;
            getParent().repaint();
        } catch (NumberFormatException e2) {
        }
    }

    void buildPanel() throws IllegalArgumentException {
        int count = this.model.getCount();
        this.spinners[4].setVisible(count > 4);
        for (int i2 = 0; i2 < count; i2++) {
            String label = this.model.getLabel(this, i2);
            JComponent label2 = this.spinners[i2].getLabel();
            if (label2 instanceof JRadioButton) {
                JRadioButton jRadioButton = (JRadioButton) label2;
                jRadioButton.setText(label);
                jRadioButton.getAccessibleContext().setAccessibleDescription(label);
            } else if (label2 instanceof JLabel) {
                ((JLabel) label2).setText(label);
            }
            this.spinners[i2].setRange(this.model.getMinimum(i2), this.model.getMaximum(i2));
            this.spinners[i2].setValue(this.values[i2]);
            this.spinners[i2].getSlider().getAccessibleContext().setAccessibleName(label);
            this.spinners[i2].getSpinner().getAccessibleContext().setAccessibleName(label);
            JSpinner.DefaultEditor defaultEditor = (JSpinner.DefaultEditor) this.spinners[i2].getSpinner().getEditor();
            defaultEditor.getTextField().getAccessibleContext().setAccessibleName(label);
            this.spinners[i2].getSlider().getAccessibleContext().setAccessibleDescription(label);
            this.spinners[i2].getSpinner().getAccessibleContext().setAccessibleDescription(label);
            defaultEditor.getTextField().getAccessibleContext().setAccessibleDescription(label);
        }
    }

    void colorChanged() {
        this.color = new Color(getColor(0), true);
        Container parent = getParent();
        if (parent instanceof ColorChooserPanel) {
            ColorChooserPanel colorChooserPanel = (ColorChooserPanel) parent;
            colorChooserPanel.setSelectedColor(this.color);
            colorChooserPanel.repaint();
        }
    }

    float getValueX() {
        return this.spinners[this.f12814x].getValue();
    }

    float getValueY() {
        return 1.0f - this.spinners[this.f12815y].getValue();
    }

    float getValueZ() {
        return 1.0f - this.spinners[this.f12816z].getValue();
    }

    void setValue(float f2) {
        this.spinners[this.f12816z].setValue(1.0f - f2);
        colorChanged();
    }

    void setValue(float f2, float f3) {
        this.spinners[this.f12814x].setValue(f2);
        this.spinners[this.f12815y].setValue(1.0f - f3);
        colorChanged();
    }

    int getColor(float f2) {
        setDefaultValue(this.f12814x);
        setDefaultValue(this.f12815y);
        this.values[this.f12816z] = 1.0f - f2;
        return getColor(3);
    }

    int getColor(float f2, float f3) {
        this.values[this.f12814x] = f2;
        this.values[this.f12815y] = 1.0f - f3;
        setValue(this.f12816z);
        return getColor(3);
    }

    void setColor(Color color) {
        if (!color.equals(this.color)) {
            this.color = color;
            this.model.setColor(color.getRGB(), this.values);
            for (int i2 = 0; i2 < this.model.getCount(); i2++) {
                this.spinners[i2].setValue(this.values[i2]);
            }
        }
    }

    private int getColor(int i2) {
        while (i2 < this.model.getCount()) {
            int i3 = i2;
            i2++;
            setValue(i3);
        }
        return this.model.getColor(this.values);
    }

    private void setValue(int i2) {
        this.values[i2] = this.spinners[i2].getValue();
    }

    private void setDefaultValue(int i2) {
        float f2 = this.model.getDefault(i2);
        this.values[i2] = f2 < 0.0f ? this.spinners[i2].getValue() : f2;
    }
}
