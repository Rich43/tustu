package com.sun.xml.internal.bind.marshaller;

import java.io.IOException;
import java.io.Writer;

/* loaded from: rt.jar:com/sun/xml/internal/bind/marshaller/NoEscapeHandler.class */
public class NoEscapeHandler implements CharacterEscapeHandler {
    public static final NoEscapeHandler theInstance = new NoEscapeHandler();

    @Override // com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler
    public void escape(char[] ch, int start, int length, boolean isAttVal, Writer out) throws IOException {
        out.write(ch, start, length);
    }
}
