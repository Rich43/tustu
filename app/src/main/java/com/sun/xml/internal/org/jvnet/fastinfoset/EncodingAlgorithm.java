package com.sun.xml.internal.org.jvnet.fastinfoset;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/fastinfoset/EncodingAlgorithm.class */
public interface EncodingAlgorithm {
    Object decodeFromBytes(byte[] bArr, int i2, int i3) throws EncodingAlgorithmException;

    Object decodeFromInputStream(InputStream inputStream) throws IOException, EncodingAlgorithmException;

    void encodeToOutputStream(Object obj, OutputStream outputStream) throws IOException, EncodingAlgorithmException;

    Object convertFromCharacters(char[] cArr, int i2, int i3) throws EncodingAlgorithmException;

    void convertToCharacters(Object obj, StringBuffer stringBuffer) throws EncodingAlgorithmException;
}
