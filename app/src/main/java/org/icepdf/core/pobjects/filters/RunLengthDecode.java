package org.icepdf.core.pobjects.filters;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/filters/RunLengthDecode.class */
public class RunLengthDecode extends ChunkingInputStream {
    public RunLengthDecode(InputStream input) {
        setInputStream(input);
        setBufferSize(4096);
    }

    @Override // org.icepdf.core.pobjects.filters.ChunkingInputStream
    protected int fillInternalBuffer() throws IOException {
        int i2;
        int numRead = 0;
        while (numRead < this.buffer.length - 260 && (i2 = this.in.read()) >= 0) {
            if (i2 < 128) {
                numRead += fillBufferFromInputStream(numRead, i2 + 1);
            } else {
                int count = 257 - i2;
                int j2 = this.in.read();
                byte jj = (byte) (j2 & 255);
                for (int k2 = 0; k2 < count; k2++) {
                    int i3 = numRead;
                    numRead++;
                    this.buffer[i3] = jj;
                }
            }
        }
        if (numRead == 0) {
            return -1;
        }
        return numRead;
    }
}
