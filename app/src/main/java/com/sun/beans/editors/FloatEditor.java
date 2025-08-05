package com.sun.beans.editors;

import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/beans/editors/FloatEditor.class */
public class FloatEditor extends NumberEditor {
    @Override // com.sun.beans.editors.NumberEditor, java.beans.PropertyEditorSupport, java.beans.PropertyEditor
    public String getJavaInitializationString() {
        Object value = getValue();
        return value != null ? value + PdfOps.F_TOKEN : FXMLLoader.NULL_KEYWORD;
    }

    @Override // java.beans.PropertyEditorSupport, java.beans.PropertyEditor
    public void setAsText(String str) throws IllegalArgumentException {
        setValue(str == null ? null : Float.valueOf(str));
    }
}
