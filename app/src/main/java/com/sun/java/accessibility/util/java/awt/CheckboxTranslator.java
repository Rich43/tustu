package com.sun.java.accessibility.util.java.awt;

import com.sun.java.accessibility.util.Translator;
import java.awt.Checkbox;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;

/* loaded from: jaccess.jar:com/sun/java/accessibility/util/java/awt/CheckboxTranslator.class */
public class CheckboxTranslator extends Translator {
    @Override // com.sun.java.accessibility.util.Translator, javax.accessibility.AccessibleContext
    public AccessibleStateSet getAccessibleStateSet() {
        AccessibleStateSet accessibleStateSet = super.getAccessibleStateSet();
        if (((Checkbox) this.source).getState()) {
            accessibleStateSet.add(AccessibleState.CHECKED);
        }
        return accessibleStateSet;
    }

    @Override // com.sun.java.accessibility.util.Translator, javax.accessibility.AccessibleContext
    public String getAccessibleName() {
        return ((Checkbox) this.source).getLabel();
    }

    @Override // com.sun.java.accessibility.util.Translator, javax.accessibility.AccessibleContext
    public void setAccessibleName(String str) {
        ((Checkbox) this.source).setLabel(str);
    }

    @Override // com.sun.java.accessibility.util.Translator, javax.accessibility.AccessibleContext
    public AccessibleRole getAccessibleRole() {
        return AccessibleRole.CHECK_BOX;
    }
}
