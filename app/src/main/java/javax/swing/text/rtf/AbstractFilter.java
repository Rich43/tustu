package javax.swing.text.rtf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;

/* loaded from: rt.jar:javax/swing/text/rtf/AbstractFilter.class */
abstract class AbstractFilter extends OutputStream {
    protected char[] translationTable = latin1TranslationTable;
    protected boolean[] specialsTable = noSpecialsTable;
    static final char[] latin1TranslationTable;
    static final boolean[] noSpecialsTable = new boolean[256];
    static final boolean[] allSpecialsTable;

    protected abstract void write(char c2) throws IOException;

    protected abstract void writeSpecial(int i2) throws IOException;

    static {
        for (int i2 = 0; i2 < 256; i2++) {
            noSpecialsTable[i2] = false;
        }
        allSpecialsTable = new boolean[256];
        for (int i3 = 0; i3 < 256; i3++) {
            allSpecialsTable[i3] = true;
        }
        latin1TranslationTable = new char[256];
        for (int i4 = 0; i4 < 256; i4++) {
            latin1TranslationTable[i4] = (char) i4;
        }
    }

    public void readFromStream(InputStream inputStream) throws IOException {
        byte[] bArr = new byte[16384];
        while (true) {
            int i2 = inputStream.read(bArr);
            if (i2 >= 0) {
                write(bArr, 0, i2);
            } else {
                return;
            }
        }
    }

    public void readFromReader(Reader reader) throws IOException {
        char[] cArr = new char[2048];
        while (true) {
            int i2 = reader.read(cArr);
            if (i2 >= 0) {
                for (int i3 = 0; i3 < i2; i3++) {
                    write(cArr[i3]);
                }
            } else {
                return;
            }
        }
    }

    @Override // java.io.OutputStream
    public void write(int i2) throws IOException {
        if (i2 < 0) {
            i2 += 256;
        }
        if (this.specialsTable[i2]) {
            writeSpecial(i2);
            return;
        }
        char c2 = this.translationTable[i2];
        if (c2 != 0) {
            write(c2);
        }
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        StringBuilder sb = null;
        while (i3 > 0) {
            short s2 = bArr[i2];
            if (s2 < 0) {
                s2 = (short) (s2 + 256);
            }
            if (this.specialsTable[s2]) {
                if (sb != null) {
                    write(sb.toString());
                    sb = null;
                }
                writeSpecial(s2);
            } else {
                char c2 = this.translationTable[s2];
                if (c2 != 0) {
                    if (sb == null) {
                        sb = new StringBuilder();
                    }
                    sb.append(c2);
                }
            }
            i3--;
            i2++;
        }
        if (sb != null) {
            write(sb.toString());
        }
    }

    public void write(String str) throws IOException {
        int length = str.length();
        for (int i2 = 0; i2 < length; i2++) {
            write(str.charAt(i2));
        }
    }
}
