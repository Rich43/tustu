package org.icepdf.core.pobjects.filters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;
import org.icepdf.core.io.BitStream;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.PdfOps;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/filters/LZWDecode.class */
public class LZWDecode extends ChunkingInputStream {
    public static final Name DECODEPARMS_KEY = new Name(PdfOps.DP_NAME);
    public static final Name EARLYCHANGE_KEY = new Name("EarlyChange");
    private BitStream inb;
    private int earlyChange;
    private int code;
    private int old_code;
    private boolean firstTime;
    private int code_len;
    private int last_code;
    private Code[] codes;

    public LZWDecode(BitStream inb, Library library, HashMap entries) {
        Number earlyChangeNumber;
        this.inb = inb;
        this.earlyChange = 1;
        HashMap decodeParmsDictionary = library.getDictionary(entries, DECODEPARMS_KEY);
        if (decodeParmsDictionary != null && (earlyChangeNumber = library.getNumber(decodeParmsDictionary, EARLYCHANGE_KEY)) != null) {
            this.earlyChange = earlyChangeNumber.intValue();
        }
        this.code = 0;
        this.old_code = 0;
        this.firstTime = true;
        initCodeTable();
        setBufferSize(4096);
    }

    @Override // org.icepdf.core.pobjects.filters.ChunkingInputStream
    protected int fillInternalBuffer() throws IOException {
        int numRead = 0;
        if (this.firstTime) {
            this.firstTime = false;
            int bits = this.inb.getBits(this.code_len);
            this.code = bits;
            this.old_code = bits;
        } else if (this.inb.atEndOfFile()) {
            return -1;
        }
        do {
            if (this.code == 256) {
                initCodeTable();
            } else {
                if (this.code == 257) {
                    break;
                }
                if (this.codes[this.code] != null) {
                    Stack stack = new Stack();
                    this.codes[this.code].getString(stack);
                    Code c2 = (Code) stack.pop();
                    addToBuffer(c2.f13123c, numRead);
                    numRead++;
                    byte first = c2.f13123c;
                    while (!stack.empty()) {
                        addToBuffer(((Code) stack.pop()).f13123c, numRead);
                        numRead++;
                    }
                    Code[] codeArr = this.codes;
                    int i2 = this.last_code;
                    this.last_code = i2 + 1;
                    codeArr[i2] = new Code(this.codes[this.old_code], first);
                } else {
                    if (this.code != this.last_code) {
                        throw new RuntimeException("LZWDecode failure");
                    }
                    Stack stack2 = new Stack();
                    this.codes[this.old_code].getString(stack2);
                    Code c3 = (Code) stack2.pop();
                    addToBuffer(c3.f13123c, numRead);
                    int numRead2 = numRead + 1;
                    byte first2 = c3.f13123c;
                    while (!stack2.empty()) {
                        addToBuffer(((Code) stack2.pop()).f13123c, numRead2);
                        numRead2++;
                    }
                    addToBuffer(first2, numRead2);
                    numRead = numRead2 + 1;
                    this.codes[this.code] = new Code(this.codes[this.old_code], first2);
                    this.last_code++;
                }
            }
            if (this.code_len < 12 && this.last_code == (1 << this.code_len) - this.earlyChange) {
                this.code_len++;
            }
            this.old_code = this.code;
            this.code = this.inb.getBits(this.code_len);
            if (this.inb.atEndOfFile()) {
                break;
            }
        } while (numRead < this.buffer.length);
        return numRead;
    }

    private void initCodeTable() {
        this.code_len = 9;
        this.last_code = 257;
        this.codes = new Code[4096];
        for (int i2 = 0; i2 < 256; i2++) {
            this.codes[i2] = new Code(null, (byte) i2);
        }
    }

    private void addToBuffer(byte b2, int offset) {
        if (offset >= this.buffer.length) {
            byte[] bufferNew = new byte[this.buffer.length * 2];
            System.arraycopy(this.buffer, 0, bufferNew, 0, this.buffer.length);
            this.buffer = bufferNew;
        }
        this.buffer[offset] = b2;
    }

    @Override // org.icepdf.core.pobjects.filters.ChunkingInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        if (this.inb != null) {
            this.inb.close();
            this.inb = null;
        }
    }

    /* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/filters/LZWDecode$Code.class */
    private static class Code {
        Code prefix;

        /* renamed from: c, reason: collision with root package name */
        byte f13123c;

        Code(Code p2, byte cc) {
            this.prefix = p2;
            this.f13123c = cc;
        }

        void getString(Stack s2) {
            s2.push(this);
            if (this.prefix != null) {
                this.prefix.getString(s2);
            }
        }
    }
}
