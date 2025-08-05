package com.sun.xml.internal.txw2.output;

import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import java.io.IOException;
import java.io.Writer;

/* loaded from: rt.jar:com/sun/xml/internal/txw2/output/DumbEscapeHandler.class */
public class DumbEscapeHandler implements CharacterEscapeHandler {
    public static final CharacterEscapeHandler theInstance = new DumbEscapeHandler();

    private DumbEscapeHandler() {
    }

    @Override // com.sun.xml.internal.txw2.output.CharacterEscapeHandler
    public void escape(char[] ch, int start, int length, boolean isAttVal, Writer out) throws IOException {
        int limit = start + length;
        for (int i2 = start; i2 < limit; i2++) {
            switch (ch[i2]) {
                case '\"':
                    if (isAttVal) {
                        out.write(SerializerConstants.ENTITY_QUOT);
                        break;
                    } else {
                        out.write(34);
                        break;
                    }
                case '&':
                    out.write(SerializerConstants.ENTITY_AMP);
                    break;
                case '<':
                    out.write(SerializerConstants.ENTITY_LT);
                    break;
                case '>':
                    out.write(SerializerConstants.ENTITY_GT);
                    break;
                default:
                    if (ch[i2] > 127) {
                        out.write("&#");
                        out.write(Integer.toString(ch[i2]));
                        out.write(59);
                        break;
                    } else {
                        out.write(ch[i2]);
                        break;
                    }
            }
        }
    }
}
