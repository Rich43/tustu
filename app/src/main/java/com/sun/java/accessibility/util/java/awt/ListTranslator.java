package com.sun.java.accessibility.util.java.awt;

import com.sun.java.accessibility.util.Translator;
import java.awt.List;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;

/* loaded from: jaccess.jar:com/sun/java/accessibility/util/java/awt/ListTranslator.class */
public class ListTranslator extends Translator {
    @Override // com.sun.java.accessibility.util.Translator, javax.accessibility.AccessibleContext
    public AccessibleStateSet getAccessibleStateSet() {
        AccessibleStateSet accessibleStateSet = super.getAccessibleStateSet();
        if (((List) this.source).isMultipleMode()) {
            accessibleStateSet.add(AccessibleState.MULTISELECTABLE);
        }
        if (((List) this.source).getSelectedItems().length > 0) {
            accessibleStateSet.add(AccessibleState.SELECTED);
        }
        return accessibleStateSet;
    }

    @Override // com.sun.java.accessibility.util.Translator, javax.accessibility.AccessibleContext
    public AccessibleRole getAccessibleRole() {
        return AccessibleRole.LIST;
    }
}
