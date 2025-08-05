package com.sun.xml.internal.bind.v2.runtime.reflect;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/Messages.class */
enum Messages {
    UNABLE_TO_ACCESS_NON_PUBLIC_FIELD,
    UNASSIGNABLE_TYPE,
    NO_SETTER,
    NO_GETTER;

    private static final ResourceBundle rb = ResourceBundle.getBundle(Messages.class.getName());

    @Override // java.lang.Enum
    public String toString() {
        return format(new Object[0]);
    }

    public String format(Object... args) {
        return MessageFormat.format(rb.getString(name()), args);
    }
}
