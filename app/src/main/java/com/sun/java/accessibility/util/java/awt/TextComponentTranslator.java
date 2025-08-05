package com.sun.java.accessibility.util.java.awt;

import com.sun.java.accessibility.util.Translator;
import javax.accessibility.AccessibleRole;

/* loaded from: jaccess.jar:com/sun/java/accessibility/util/java/awt/TextComponentTranslator.class */
public class TextComponentTranslator extends Translator {
    @Override // com.sun.java.accessibility.util.Translator, javax.accessibility.AccessibleContext
    public AccessibleRole getAccessibleRole() {
        return AccessibleRole.TEXT;
    }
}
