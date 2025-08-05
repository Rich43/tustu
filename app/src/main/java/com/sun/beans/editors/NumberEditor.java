package com.sun.beans.editors;

import java.beans.PropertyEditorSupport;
import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:com/sun/beans/editors/NumberEditor.class */
public abstract class NumberEditor extends PropertyEditorSupport {
    @Override // java.beans.PropertyEditorSupport, java.beans.PropertyEditor
    public String getJavaInitializationString() {
        Object value = getValue();
        return value != null ? value.toString() : FXMLLoader.NULL_KEYWORD;
    }
}
