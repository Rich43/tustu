package com.sun.beans.editors;

import java.beans.PropertyEditorSupport;
import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:com/sun/beans/editors/BooleanEditor.class */
public class BooleanEditor extends PropertyEditorSupport {
    @Override // java.beans.PropertyEditorSupport, java.beans.PropertyEditor
    public String getJavaInitializationString() {
        Object value = getValue();
        return value != null ? value.toString() : FXMLLoader.NULL_KEYWORD;
    }

    @Override // java.beans.PropertyEditorSupport, java.beans.PropertyEditor
    public String getAsText() {
        Object value = getValue();
        if (value instanceof Boolean) {
            return getValidName(((Boolean) value).booleanValue());
        }
        return null;
    }

    @Override // java.beans.PropertyEditorSupport, java.beans.PropertyEditor
    public void setAsText(String str) throws IllegalArgumentException {
        if (str == null) {
            setValue(null);
        } else if (isValidName(true, str)) {
            setValue(Boolean.TRUE);
        } else {
            if (isValidName(false, str)) {
                setValue(Boolean.FALSE);
                return;
            }
            throw new IllegalArgumentException(str);
        }
    }

    @Override // java.beans.PropertyEditorSupport, java.beans.PropertyEditor
    public String[] getTags() {
        return new String[]{getValidName(true), getValidName(false)};
    }

    private String getValidName(boolean z2) {
        return z2 ? "True" : "False";
    }

    private boolean isValidName(boolean z2, String str) {
        return getValidName(z2).equalsIgnoreCase(str);
    }
}
