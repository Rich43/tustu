package com.sun.org.apache.xerces.internal.util;

import java.util.Locale;
import java.util.MissingResourceException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/MessageFormatter.class */
public interface MessageFormatter {
    String formatMessage(Locale locale, String str, Object[] objArr) throws MissingResourceException;
}
