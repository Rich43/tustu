package com.sun.org.apache.xerces.internal.impl.dv.xs;

import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xerces.internal.util.URI;
import java.io.UnsupportedEncodingException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/AnyURIDV.class */
public class AnyURIDV extends TypeValidator {
    private static final URI BASE_URI;
    private static boolean[] gNeedEscaping;
    private static char[] gAfterEscaping1;
    private static char[] gAfterEscaping2;
    private static char[] gHexChs;

    static {
        URI uri = null;
        try {
            uri = new URI("abc://def.ghi.jkl");
        } catch (URI.MalformedURIException e2) {
        }
        BASE_URI = uri;
        gNeedEscaping = new boolean[128];
        gAfterEscaping1 = new char[128];
        gAfterEscaping2 = new char[128];
        gHexChs = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        for (int i2 = 0; i2 <= 31; i2++) {
            gNeedEscaping[i2] = true;
            gAfterEscaping1[i2] = gHexChs[i2 >> 4];
            gAfterEscaping2[i2] = gHexChs[i2 & 15];
        }
        gNeedEscaping[127] = true;
        gAfterEscaping1[127] = '7';
        gAfterEscaping2[127] = 'F';
        char[] escChs = {' ', '<', '>', '\"', '{', '}', '|', '\\', '^', '~', '`'};
        for (char ch : escChs) {
            gNeedEscaping[ch] = true;
            gAfterEscaping1[ch] = gHexChs[ch >> 4];
            gAfterEscaping2[ch] = gHexChs[ch & 15];
        }
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public short getAllowedFacets() {
        return (short) 2079;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public Object getActualValue(String content, ValidationContext context) throws InvalidDatatypeValueException {
        try {
            if (content.length() != 0) {
                String encoded = encode(content);
                new URI(BASE_URI, encoded);
            }
            return content;
        } catch (URI.MalformedURIException e2) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{content, SchemaSymbols.ATTVAL_ANYURI});
        }
    }

    private static String encode(String anyURI) {
        int ch;
        int len = anyURI.length();
        StringBuffer buffer = new StringBuffer(len * 3);
        int i2 = 0;
        while (i2 < len && (ch = anyURI.charAt(i2)) < 128) {
            if (gNeedEscaping[ch]) {
                buffer.append('%');
                buffer.append(gAfterEscaping1[ch]);
                buffer.append(gAfterEscaping2[ch]);
            } else {
                buffer.append((char) ch);
            }
            i2++;
        }
        if (i2 < len) {
            try {
                byte[] bytes = anyURI.substring(i2).getBytes("UTF-8");
                for (byte b2 : bytes) {
                    if (b2 < 0) {
                        int ch2 = b2 + 256;
                        buffer.append('%');
                        buffer.append(gHexChs[ch2 >> 4]);
                        buffer.append(gHexChs[ch2 & 15]);
                    } else if (gNeedEscaping[b2]) {
                        buffer.append('%');
                        buffer.append(gAfterEscaping1[b2]);
                        buffer.append(gAfterEscaping2[b2]);
                    } else {
                        buffer.append((char) b2);
                    }
                }
            } catch (UnsupportedEncodingException e2) {
                return anyURI;
            }
        }
        if (buffer.length() != len) {
            return buffer.toString();
        }
        return anyURI;
    }
}
