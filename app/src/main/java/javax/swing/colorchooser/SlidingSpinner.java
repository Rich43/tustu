package javax.swing.colorchooser;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* loaded from: rt.jar:javax/swing/colorchooser/SlidingSpinner.class */
final class SlidingSpinner implements ChangeListener {
    private final ColorPanel panel;
    private final JComponent label;
    private final SpinnerNumberModel model = new SpinnerNumberModel();
    private final JSlider slider = new JSlider();
    private final JSpinner spinner = new JSpinner(this.model);
    private float value;
    private boolean internal;

    SlidingSpinner(ColorPanel colorPanel, JComponent jComponent) {
        this.panel = colorPanel;
        this.label = jComponent;
        this.slider.addChangeListener(this);
        this.spinner.addChangeListener(this);
        JSpinner.DefaultEditor defaultEditor = (JSpinner.DefaultEditor) this.spinner.getEditor();
        ValueFormatter.init(3, false, defaultEditor.getTextField());
        defaultEditor.setFocusable(false);
        this.spinner.setFocusable(false);
    }

    JComponent getLabel() {
        return this.label;
    }

    JSlider getSlider() {
        return this.slider;
    }

    JSpinner getSpinner() {
        return this.spinner;
    }

    float getValue() {
        return this.value;
    }

    void setValue(float f2) {
        int minimum = this.slider.getMinimum();
        int maximum = this.slider.getMaximum();
        this.internal = true;
        this.slider.setValue(minimum + ((int) (f2 * (maximum - minimum))));
        this.spinner.setValue(Integer.valueOf(this.slider.getValue()));
        this.internal = false;
        this.value = f2;
    }

    void setRange(int i2, int i3) {
        this.internal = true;
        this.slider.setMinimum(i2);
        this.slider.setMaximum(i3);
        this.model.setMinimum(Integer.valueOf(i2));
        this.model.setMaximum(Integer.valueOf(i3));
        this.internal = false;
    }

    void setVisible(boolean z2) {
        this.label.setVisible(z2);
        this.slider.setVisible(z2);
        this.spinner.setVisible(z2);
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) {
        if (!this.internal) {
            if (this.spinner == changeEvent.getSource()) {
                Object value = this.spinner.getValue();
                if (value instanceof Integer) {
                    this.internal = true;
                    this.slider.setValue(((Integer) value).intValue());
                    this.internal = false;
                }
            }
            int value2 = this.slider.getValue();
            this.internal = true;
            this.spinner.setValue(Integer.valueOf(value2));
            this.internal = false;
            int minimum = this.slider.getMinimum();
            this.value = (value2 - minimum) / (this.slider.getMaximum() - minimum);
            this.panel.colorChanged();
        }
    }
}
