package org.icepdf.core.pobjects.filters;

import java.io.InputStream;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/filters/ASCII85Decode.class */
public class ASCII85Decode extends ChunkingInputStream {
    private boolean eof = false;

    public ASCII85Decode(InputStream input) {
        setInputStream(input);
        setBufferSize(4);
    }

    /* JADX WARN: Code restructure failed: missing block: B:35:0x00f4, code lost:
    
        if (r10 != 2) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00f7, code lost:
    
        r8 = (r8 * 614125) + 16777215;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0106, code lost:
    
        if (r10 != 3) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0109, code lost:
    
        r8 = (r8 * 7225) + 65535;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0118, code lost:
    
        if (r10 != 4) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x011b, code lost:
    
        r8 = (r8 * 85) + 255;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0127, code lost:
    
        if (r10 < 2) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x012a, code lost:
    
        r7.buffer[0] = (byte) ((r8 >> 24) & 255);
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x013c, code lost:
    
        if (r10 < 3) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x013f, code lost:
    
        r7.buffer[1] = (byte) ((r8 >> 16) & 255);
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0151, code lost:
    
        if (r10 < 4) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0154, code lost:
    
        r7.buffer[2] = (byte) ((r8 >> 8) & 255);
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0167, code lost:
    
        return r10 - 1;
     */
    @Override // org.icepdf.core.pobjects.filters.ChunkingInputStream
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected int fillInternalBuffer() throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 360
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.icepdf.core.pobjects.filters.ASCII85Decode.fillInternalBuffer():int");
    }
}
