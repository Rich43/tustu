package javax.swing;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

/* loaded from: rt.jar:javax/swing/ButtonGroup.class */
public class ButtonGroup implements Serializable {
    protected Vector<AbstractButton> buttons = new Vector<>();
    ButtonModel selection = null;

    public void add(AbstractButton abstractButton) {
        if (abstractButton == null) {
            return;
        }
        this.buttons.addElement(abstractButton);
        if (abstractButton.isSelected()) {
            if (this.selection == null) {
                this.selection = abstractButton.getModel();
            } else {
                abstractButton.setSelected(false);
            }
        }
        abstractButton.getModel().setGroup(this);
    }

    public void remove(AbstractButton abstractButton) {
        if (abstractButton == null) {
            return;
        }
        this.buttons.removeElement(abstractButton);
        if (abstractButton.getModel() == this.selection) {
            this.selection = null;
        }
        abstractButton.getModel().setGroup(null);
    }

    public void clearSelection() {
        if (this.selection != null) {
            ButtonModel buttonModel = this.selection;
            this.selection = null;
            buttonModel.setSelected(false);
        }
    }

    public Enumeration<AbstractButton> getElements() {
        return this.buttons.elements();
    }

    public ButtonModel getSelection() {
        return this.selection;
    }

    public void setSelected(ButtonModel buttonModel, boolean z2) {
        if (z2 && buttonModel != null && buttonModel != this.selection) {
            ButtonModel buttonModel2 = this.selection;
            this.selection = buttonModel;
            if (buttonModel2 != null) {
                buttonModel2.setSelected(false);
            }
            buttonModel.setSelected(true);
        }
    }

    public boolean isSelected(ButtonModel buttonModel) {
        return buttonModel == this.selection;
    }

    public int getButtonCount() {
        if (this.buttons == null) {
            return 0;
        }
        return this.buttons.size();
    }
}
