package com.sun.beans.editors;

import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:com/sun/beans/editors/ShortEditor.class */
public class ShortEditor extends NumberEditor {
    @Override // com.sun.beans.editors.NumberEditor, java.beans.PropertyEditorSupport, java.beans.PropertyEditor
    public String getJavaInitializationString() {
        Object value = getValue();
        return value != null ? "((short)" + value + ")" : FXMLLoader.NULL_KEYWORD;
    }

    @Override // java.beans.PropertyEditorSupport, java.beans.PropertyEditor
    public void setAsText(String str) throws IllegalArgumentException {
        setValue(str == null ? null : Short.decode(str));
    }
}
