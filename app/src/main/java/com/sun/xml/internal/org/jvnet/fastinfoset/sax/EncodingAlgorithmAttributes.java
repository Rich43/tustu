package com.sun.xml.internal.org.jvnet.fastinfoset.sax;

import org.xml.sax.Attributes;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/fastinfoset/sax/EncodingAlgorithmAttributes.class */
public interface EncodingAlgorithmAttributes extends Attributes {
    String getAlgorithmURI(int i2);

    int getAlgorithmIndex(int i2);

    Object getAlgorithmData(int i2);

    String getAlpababet(int i2);

    boolean getToIndex(int i2);
}
