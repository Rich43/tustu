package com.sun.java.accessibility.util.java.awt;

import com.sun.java.accessibility.util.Translator;
import java.awt.Button;
import javax.accessibility.AccessibleRole;

/* loaded from: jaccess.jar:com/sun/java/accessibility/util/java/awt/ButtonTranslator.class */
public class ButtonTranslator extends Translator {
    @Override // com.sun.java.accessibility.util.Translator, javax.accessibility.AccessibleContext
    public String getAccessibleName() {
        return ((Button) this.source).getLabel();
    }

    @Override // com.sun.java.accessibility.util.Translator, javax.accessibility.AccessibleContext
    public void setAccessibleName(String str) {
        ((Button) this.source).setLabel(str);
    }

    @Override // com.sun.java.accessibility.util.Translator, javax.accessibility.AccessibleContext
    public AccessibleRole getAccessibleRole() {
        return AccessibleRole.PUSH_BUTTON;
    }
}
