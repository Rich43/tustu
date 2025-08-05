package com.sun.xml.internal.bind.marshaller;

import java.io.IOException;
import java.io.Writer;

/* loaded from: rt.jar:com/sun/xml/internal/bind/marshaller/CharacterEscapeHandler.class */
public interface CharacterEscapeHandler {
    void escape(char[] cArr, int i2, int i3, boolean z2, Writer writer) throws IOException;
}
