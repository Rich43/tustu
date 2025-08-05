package com.sun.org.apache.bcel.internal.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/util/ByteSequence.class */
public final class ByteSequence extends DataInputStream {
    private ByteArrayStream byte_stream;

    public ByteSequence(byte[] bytes) {
        super(new ByteArrayStream(bytes));
        this.byte_stream = (ByteArrayStream) this.in;
    }

    public final int getIndex() {
        return this.byte_stream.getPosition();
    }

    final void unreadByte() {
        this.byte_stream.unreadByte();
    }

    /* loaded from: rt.jar:com/sun/org/apache/bcel/internal/util/ByteSequence$ByteArrayStream.class */
    private static final class ByteArrayStream extends ByteArrayInputStream {
        ByteArrayStream(byte[] bytes) {
            super(bytes);
        }

        final int getPosition() {
            return this.pos;
        }

        final void unreadByte() {
            if (this.pos > 0) {
                this.pos--;
            }
        }
    }
}
