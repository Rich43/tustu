package org.icepdf.core.pobjects.filters;

import java.io.IOException;
import java.io.InputStream;
import org.icepdf.core.util.Parser;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/filters/ASCIIHexDecode.class */
public class ASCIIHexDecode extends ChunkingInputStream {
    public ASCIIHexDecode(InputStream input) {
        setInputStream(input);
        setBufferSize(4096);
    }

    @Override // org.icepdf.core.pobjects.filters.ChunkingInputStream
    protected int fillInternalBuffer() throws IOException {
        int hi;
        int lo;
        int numRead = 0;
        for (int i2 = 0; i2 < this.buffer.length; i2++) {
            byte val = 0;
            do {
                hi = this.in.read();
            } while (Parser.isWhitespace((char) hi));
            if (hi < 0) {
                break;
            }
            do {
                lo = this.in.read();
            } while (Parser.isWhitespace((char) lo));
            if (hi >= 48 && hi <= 57) {
                val = (byte) (0 | ((byte) (((hi - 48) << 4) & 240)));
            } else if (hi >= 97 && hi <= 122) {
                val = (byte) (0 | ((byte) ((((hi - 97) + 10) << 4) & 240)));
            } else if (hi >= 65 && hi <= 90) {
                val = (byte) (0 | ((byte) ((((hi - 65) + 10) << 4) & 240)));
            }
            if (lo >= 0) {
                if (lo >= 48 && lo <= 57) {
                    val = (byte) (val | ((byte) ((lo - 48) & 15)));
                } else if (lo >= 97 && lo <= 122) {
                    val = (byte) (val | ((byte) (((lo - 97) + 10) & 15)));
                } else if (lo >= 65 && lo <= 90) {
                    val = (byte) (val | ((byte) (((lo - 65) + 10) & 15)));
                }
            }
            int i3 = numRead;
            numRead++;
            this.buffer[i3] = val;
        }
        if (numRead == 0) {
            return -1;
        }
        return numRead;
    }
}
