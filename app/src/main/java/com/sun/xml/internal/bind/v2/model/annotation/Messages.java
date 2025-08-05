package com.sun.xml.internal.bind.v2.model.annotation;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/annotation/Messages.class */
enum Messages {
    DUPLICATE_ANNOTATIONS,
    CLASS_NOT_FOUND;

    private static final ResourceBundle rb = ResourceBundle.getBundle(Messages.class.getName());

    @Override // java.lang.Enum
    public String toString() {
        return format(new Object[0]);
    }

    public String format(Object... args) {
        return MessageFormat.format(rb.getString(name()), args);
    }
}
