package sun.nio.cs.ext;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CoderResult;

/* loaded from: charsets.jar:sun/nio/cs/ext/DelegatableDecoder.class */
interface DelegatableDecoder {
    CoderResult decodeLoop(ByteBuffer byteBuffer, CharBuffer charBuffer);

    void implReset();

    CoderResult implFlush(CharBuffer charBuffer);
}
