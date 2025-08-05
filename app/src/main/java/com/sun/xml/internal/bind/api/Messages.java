package com.sun.xml.internal.bind.api;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/* loaded from: rt.jar:com/sun/xml/internal/bind/api/Messages.class */
enum Messages {
    ARGUMENT_CANT_BE_NULL;

    private static final ResourceBundle rb = ResourceBundle.getBundle(Messages.class.getName());

    @Override // java.lang.Enum
    public String toString() {
        return format(new Object[0]);
    }

    public String format(Object... args) {
        return MessageFormat.format(rb.getString(name()), args);
    }
}
