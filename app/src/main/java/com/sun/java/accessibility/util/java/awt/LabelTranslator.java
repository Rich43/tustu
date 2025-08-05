package com.sun.java.accessibility.util.java.awt;

import com.sun.java.accessibility.util.Translator;
import java.awt.Label;
import javax.accessibility.AccessibleRole;

/* loaded from: jaccess.jar:com/sun/java/accessibility/util/java/awt/LabelTranslator.class */
public class LabelTranslator extends Translator {
    @Override // com.sun.java.accessibility.util.Translator, javax.accessibility.AccessibleContext
    public String getAccessibleName() {
        return ((Label) this.source).getText();
    }

    @Override // com.sun.java.accessibility.util.Translator, javax.accessibility.AccessibleContext
    public void setAccessibleName(String str) {
        ((Label) this.source).setText(str);
    }

    @Override // com.sun.java.accessibility.util.Translator, javax.accessibility.AccessibleContext
    public AccessibleRole getAccessibleRole() {
        return AccessibleRole.LABEL;
    }
}
