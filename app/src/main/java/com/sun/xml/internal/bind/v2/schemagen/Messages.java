package com.sun.xml.internal.bind.v2.schemagen;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/Messages.class */
enum Messages {
    ANONYMOUS_TYPE_CYCLE;

    private static final ResourceBundle rb = ResourceBundle.getBundle(Messages.class.getName());

    @Override // java.lang.Enum
    public String toString() {
        return format(new Object[0]);
    }

    public String format(Object... args) {
        return MessageFormat.format(rb.getString(name()), args);
    }
}
