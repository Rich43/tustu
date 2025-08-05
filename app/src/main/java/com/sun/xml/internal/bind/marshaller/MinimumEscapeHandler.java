package com.sun.xml.internal.bind.marshaller;

import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import java.io.IOException;
import java.io.Writer;

/* loaded from: rt.jar:com/sun/xml/internal/bind/marshaller/MinimumEscapeHandler.class */
public class MinimumEscapeHandler implements CharacterEscapeHandler {
    public static final CharacterEscapeHandler theInstance = new MinimumEscapeHandler();

    private MinimumEscapeHandler() {
    }

    @Override // com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler
    public void escape(char[] ch, int start, int length, boolean isAttVal, Writer out) throws IOException {
        int limit = start + length;
        for (int i2 = start; i2 < limit; i2++) {
            char c2 = ch[i2];
            if (c2 == '&' || c2 == '<' || c2 == '>' || c2 == '\r' || (c2 == '\"' && isAttVal)) {
                if (i2 != start) {
                    out.write(ch, start, i2 - start);
                }
                start = i2 + 1;
                switch (ch[i2]) {
                    case '\"':
                        out.write(SerializerConstants.ENTITY_QUOT);
                        break;
                    case '&':
                        out.write(SerializerConstants.ENTITY_AMP);
                        break;
                    case '<':
                        out.write(SerializerConstants.ENTITY_LT);
                        break;
                    case '>':
                        out.write(SerializerConstants.ENTITY_GT);
                        break;
                }
            }
        }
        if (start != limit) {
            out.write(ch, start, limit - start);
        }
    }
}
