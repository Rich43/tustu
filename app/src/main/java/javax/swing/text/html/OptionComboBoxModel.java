package javax.swing.text.html;

import java.io.Serializable;
import javax.swing.DefaultComboBoxModel;

/* loaded from: rt.jar:javax/swing/text/html/OptionComboBoxModel.class */
class OptionComboBoxModel<E> extends DefaultComboBoxModel<E> implements Serializable {
    private Option selectedOption = null;

    OptionComboBoxModel() {
    }

    public void setInitialSelection(Option option) {
        this.selectedOption = option;
    }

    public Option getInitialSelection() {
        return this.selectedOption;
    }
}
