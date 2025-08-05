package javax.swing.text;

import java.io.Serializable;
import javax.swing.JFormattedTextField;

/* loaded from: rt.jar:javax/swing/text/DefaultFormatterFactory.class */
public class DefaultFormatterFactory extends JFormattedTextField.AbstractFormatterFactory implements Serializable {
    private JFormattedTextField.AbstractFormatter defaultFormat;
    private JFormattedTextField.AbstractFormatter displayFormat;
    private JFormattedTextField.AbstractFormatter editFormat;
    private JFormattedTextField.AbstractFormatter nullFormat;

    public DefaultFormatterFactory() {
    }

    public DefaultFormatterFactory(JFormattedTextField.AbstractFormatter abstractFormatter) {
        this(abstractFormatter, null);
    }

    public DefaultFormatterFactory(JFormattedTextField.AbstractFormatter abstractFormatter, JFormattedTextField.AbstractFormatter abstractFormatter2) {
        this(abstractFormatter, abstractFormatter2, null);
    }

    public DefaultFormatterFactory(JFormattedTextField.AbstractFormatter abstractFormatter, JFormattedTextField.AbstractFormatter abstractFormatter2, JFormattedTextField.AbstractFormatter abstractFormatter3) {
        this(abstractFormatter, abstractFormatter2, abstractFormatter3, null);
    }

    public DefaultFormatterFactory(JFormattedTextField.AbstractFormatter abstractFormatter, JFormattedTextField.AbstractFormatter abstractFormatter2, JFormattedTextField.AbstractFormatter abstractFormatter3, JFormattedTextField.AbstractFormatter abstractFormatter4) {
        this.defaultFormat = abstractFormatter;
        this.displayFormat = abstractFormatter2;
        this.editFormat = abstractFormatter3;
        this.nullFormat = abstractFormatter4;
    }

    public void setDefaultFormatter(JFormattedTextField.AbstractFormatter abstractFormatter) {
        this.defaultFormat = abstractFormatter;
    }

    public JFormattedTextField.AbstractFormatter getDefaultFormatter() {
        return this.defaultFormat;
    }

    public void setDisplayFormatter(JFormattedTextField.AbstractFormatter abstractFormatter) {
        this.displayFormat = abstractFormatter;
    }

    public JFormattedTextField.AbstractFormatter getDisplayFormatter() {
        return this.displayFormat;
    }

    public void setEditFormatter(JFormattedTextField.AbstractFormatter abstractFormatter) {
        this.editFormat = abstractFormatter;
    }

    public JFormattedTextField.AbstractFormatter getEditFormatter() {
        return this.editFormat;
    }

    public void setNullFormatter(JFormattedTextField.AbstractFormatter abstractFormatter) {
        this.nullFormat = abstractFormatter;
    }

    public JFormattedTextField.AbstractFormatter getNullFormatter() {
        return this.nullFormat;
    }

    @Override // javax.swing.JFormattedTextField.AbstractFormatterFactory
    public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField jFormattedTextField) {
        JFormattedTextField.AbstractFormatter displayFormatter = null;
        if (jFormattedTextField == null) {
            return null;
        }
        if (jFormattedTextField.getValue() == null) {
            displayFormatter = getNullFormatter();
        }
        if (displayFormatter == null) {
            if (jFormattedTextField.hasFocus()) {
                displayFormatter = getEditFormatter();
            } else {
                displayFormatter = getDisplayFormatter();
            }
            if (displayFormatter == null) {
                displayFormatter = getDefaultFormatter();
            }
        }
        return displayFormatter;
    }
}
