package com.sun.xml.internal.txw2.output;

import java.io.IOException;
import java.io.Writer;

/* loaded from: rt.jar:com/sun/xml/internal/txw2/output/CharacterEscapeHandler.class */
public interface CharacterEscapeHandler {
    void escape(char[] cArr, int i2, int i3, boolean z2, Writer writer) throws IOException;
}
