package com.sun.javafx.font;

import com.sun.glass.utils.NativeLibLoader;
import java.io.IOException;
import java.security.AccessController;

/* loaded from: jfxrt.jar:com/sun/javafx/font/DFontDecoder.class */
class DFontDecoder extends FontFileWriter {
    private static native long createCTFont(String str);

    private static native void releaseCTFont(long j2);

    private static native int getCTFontFormat(long j2);

    private static native int[] getCTFontTags(long j2);

    private static native byte[] getCTFontTable(long j2, int i2);

    static {
        AccessController.doPrivileged(() -> {
            NativeLibLoader.loadLibrary("javafx_font");
            return null;
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void decode(String fontName) throws IOException {
        if (fontName == null) {
            throw new IOException("Invalid font name");
        }
        long fontRef = 0;
        try {
            long fontRef2 = createCTFont(fontName);
            if (fontRef2 == 0) {
                throw new IOException("Failure creating CTFont");
            }
            int format = getCTFontFormat(fontRef2);
            if (format != 1953658213 && format != 65536 && format != 1330926671) {
                throw new IOException("Unsupported Dfont");
            }
            int[] tags = getCTFontTags(fontRef2);
            int length = (short) tags.length;
            int size = 12 + (16 * length);
            byte[] bArr = new byte[length];
            for (int i2 = 0; i2 < tags.length; i2++) {
                int tag = tags[i2];
                bArr[i2] = getCTFontTable(fontRef2, tag);
                int length2 = bArr[i2].length;
                size += (length2 + 3) & (-4);
            }
            releaseCTFont(fontRef2);
            fontRef = 0;
            setLength(size);
            writeHeader(format, length);
            int dataOffset = 12 + (16 * length);
            for (int i3 = 0; i3 < length; i3++) {
                int tag2 = tags[i3];
                byte[] bArr2 = bArr[i3];
                writeDirectoryEntry(i3, tag2, 0, dataOffset, bArr2.length);
                seek(dataOffset);
                writeBytes(bArr2);
                dataOffset += (bArr2.length + 3) & (-4);
            }
        } finally {
            if (fontRef != 0) {
                releaseCTFont(fontRef);
            }
        }
    }
}
