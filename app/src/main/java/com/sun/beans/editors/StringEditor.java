package com.sun.beans.editors;

import java.beans.PropertyEditorSupport;
import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:com/sun/beans/editors/StringEditor.class */
public class StringEditor extends PropertyEditorSupport {
    @Override // java.beans.PropertyEditorSupport, java.beans.PropertyEditor
    public String getJavaInitializationString() {
        Object value = getValue();
        if (value == null) {
            return FXMLLoader.NULL_KEYWORD;
        }
        String string = value.toString();
        int length = string.length();
        StringBuilder sb = new StringBuilder(length + 2);
        sb.append('\"');
        for (int i2 = 0; i2 < length; i2++) {
            char cCharAt = string.charAt(i2);
            switch (cCharAt) {
                case '\b':
                    sb.append("\\b");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                default:
                    if (cCharAt < ' ' || cCharAt > '~') {
                        sb.append("\\u");
                        String hexString = Integer.toHexString(cCharAt);
                        for (int length2 = hexString.length(); length2 < 4; length2++) {
                            sb.append('0');
                        }
                        sb.append(hexString);
                        break;
                    } else {
                        sb.append(cCharAt);
                        break;
                    }
                    break;
            }
        }
        sb.append('\"');
        return sb.toString();
    }

    @Override // java.beans.PropertyEditorSupport, java.beans.PropertyEditor
    public void setAsText(String str) {
        setValue(str);
    }
}
