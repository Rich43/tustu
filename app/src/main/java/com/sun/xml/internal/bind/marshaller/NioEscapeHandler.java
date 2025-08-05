package com.sun.xml.internal.bind.marshaller;

import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/* loaded from: rt.jar:com/sun/xml/internal/bind/marshaller/NioEscapeHandler.class */
public class NioEscapeHandler implements CharacterEscapeHandler {
    private final CharsetEncoder encoder;

    public NioEscapeHandler(String charsetName) {
        this.encoder = Charset.forName(charsetName).newEncoder();
    }

    @Override // com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler
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
                    if (this.encoder.canEncode(ch[i2])) {
                        out.write(ch[i2]);
                        break;
                    } else {
                        out.write("&#");
                        out.write(Integer.toString(ch[i2]));
                        out.write(59);
                        break;
                    }
            }
        }
    }
}
