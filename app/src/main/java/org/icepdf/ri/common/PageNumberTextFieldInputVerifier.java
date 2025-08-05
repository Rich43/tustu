package org.icepdf.ri.common;

import java.awt.Toolkit;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/PageNumberTextFieldInputVerifier.class */
public class PageNumberTextFieldInputVerifier extends InputVerifier {
    static final int maxLength = 4;

    @Override // javax.swing.InputVerifier
    public boolean verify(JComponent comp) {
        boolean returnValue = true;
        JTextField textField = (JTextField) comp;
        String content = textField.getText();
        if (content.length() != 0 && content.length() < 4) {
            try {
                Integer.parseInt(textField.getText());
            } catch (NumberFormatException e2) {
                returnValue = false;
            }
        } else if (content.length() > 0) {
            textField.setText(content.substring(0, 4));
        } else {
            textField.setText("");
        }
        return returnValue;
    }

    @Override // javax.swing.InputVerifier
    public boolean shouldYieldFocus(JComponent input) {
        boolean valid = super.shouldYieldFocus(input);
        if (!valid) {
            Toolkit.getDefaultToolkit().beep();
        }
        return valid;
    }
}
