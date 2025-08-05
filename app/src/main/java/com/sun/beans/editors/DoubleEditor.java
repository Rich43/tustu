package com.sun.beans.editors;

/* loaded from: rt.jar:com/sun/beans/editors/DoubleEditor.class */
public class DoubleEditor extends NumberEditor {
    @Override // java.beans.PropertyEditorSupport, java.beans.PropertyEditor
    public void setAsText(String str) throws IllegalArgumentException {
        setValue(str == null ? null : Double.valueOf(str));
    }
}
