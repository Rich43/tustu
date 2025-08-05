package com.sun.beans.editors;

/* loaded from: rt.jar:com/sun/beans/editors/IntegerEditor.class */
public class IntegerEditor extends NumberEditor {
    @Override // java.beans.PropertyEditorSupport, java.beans.PropertyEditor
    public void setAsText(String str) throws IllegalArgumentException {
        setValue(str == null ? null : Integer.decode(str));
    }
}
